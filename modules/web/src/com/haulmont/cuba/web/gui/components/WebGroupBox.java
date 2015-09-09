/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.compatibility.ComponentExpandCollapseListenerWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaOrderedActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.convertAlignment;

/**
 * @author abramov
 * @version $Id$
 */
public class WebGroupBox extends WebAbstractComponent<CubaGroupBox> implements GroupBoxLayout {

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected Map<String, Component> componentByIds = new HashMap<>();

    protected Orientation orientation = Orientation.VERTICAL;

    protected List<ExpandedStateChangeListener> expandedStateChangeListeners;

    protected boolean settingsEnabled = true;

    public WebGroupBox() {
        component = new CubaGroupBox();
        component.setExpandChangeHandler(expanded -> fireExpandStateChange(expanded));

        CubaVerticalActionsLayout container = new CubaVerticalActionsLayout();
        component.setContent(container);
    }

    @Override
    public void add(Component childComponent) {
        add(childComponent, ownComponents.size());
    }

    @Override
    public void add(Component childComponent, int index) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        AbstractOrderedLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(component.getContent() instanceof CubaVerticalActionsLayout)) {
            newContent = new CubaVerticalActionsLayout();
        } else if (orientation == Orientation.HORIZONTAL && !(component.getContent() instanceof CubaHorizontalActionsLayout)) {
            newContent = new CubaHorizontalActionsLayout();
        }

        if (newContent != null) {
            component.setContent(newContent);

            newContent.setMargin(((CubaOrderedActionsLayout) component.getContent()).getMargin());
            newContent.setSpacing(((CubaOrderedActionsLayout) component.getContent()).isSpacing());
        }

        if (ownComponents.contains(childComponent)) {
            int existingIndex = getComponentContent().getComponentIndex(WebComponentsHelper.getComposition(childComponent));
            if (index > existingIndex) {
                index--;
            }

            remove(childComponent);
        }

        com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);
        getComponentContent().addComponent(vComponent, index);
        getComponentContent().setComponentAlignment(vComponent, convertAlignment(childComponent.getAlignment()));

        if (childComponent.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
        }

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
        }

        if (index == ownComponents.size()) {
            ownComponents.add(childComponent);
        } else {
            List<Component> componentsTempList = new ArrayList<>(ownComponents);
            componentsTempList.add(index, childComponent);

            ownComponents.clear();
            ownComponents.addAll(componentsTempList);
        }

        childComponent.setParent(this);
    }

    @Override
    public int indexOf(Component component) {
        return ComponentsHelper.indexOf(ownComponents, component);
    }

    @Override
    public void remove(Component childComponent) {
        getComponentContent().removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        getComponentContent().removeAllComponents();
        componentByIds.clear();

        List<Component> components = new ArrayList<>(ownComponents);
        ownComponents.clear();

        for (Component childComponent : components) {
            childComponent.setParent(null);
        }
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : ownComponents) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    protected AbstractOrderedLayout getComponentContent() {
        return ((AbstractOrderedLayout) component.getContent());
    }

    @Override
    public Component getOwnComponent(String id) {
        return componentByIds.get(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public Component getComponentNN(String id) {
        Component component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void expand(Component component, String height, String width) {
        com.vaadin.ui.Component expandedComponent = WebComponentsHelper.getComposition(component);
        WebComponentsHelper.expand(getComponentContent(), expandedComponent, height, width);
    }

    @Override
    public boolean isExpanded(Component component) {
        return ownComponents.contains(component) && WebComponentsHelper.isComponentExpanded(component);
    }

    @Override
    public boolean isExpanded() {
        return component.isExpanded();
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return orientation == Orientation.HORIZONTAL ? ExpandDirection.HORIZONTAL : ExpandDirection.VERTICAL;
    }

    @Override
    public void setExpanded(boolean expanded) {
        component.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsable() {
        return component.isCollapsable();
    }

    @Override
    public void setCollapsable(boolean collapsable) {
        component.setCollapsable(collapsable);
    }

    @Override
    public void addListener(ExpandListener listener) {
        addExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void removeListener(ExpandListener listener) {
        removeExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void addListener(CollapseListener listener) {
        addExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void removeListener(CollapseListener listener) {
        removeExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void addExpandedStateChangeListener(ExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners == null) {
            expandedStateChangeListeners = new ArrayList<>();
        }
        if (!expandedStateChangeListeners.contains(listener)) {
            expandedStateChangeListeners.add(listener);
        }
    }

    @Override
    public void removeExpandedStateChangeListener(ExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners != null) {
            expandedStateChangeListeners.remove(listener);
        }
    }

    protected void fireExpandStateChange(boolean expanded) {
        if (expandedStateChangeListeners != null && !expandedStateChangeListeners.isEmpty()) {
            ExpandedStateChangeEvent event = new ExpandedStateChangeEvent(this, expanded);

            for (ExpandedStateChangeListener listener : expandedStateChangeListeners) {
                listener.expandedStateChanged(event);
            }
        }
    }

    @Override
    public void applySettings(Element element) {
        if (isSettingsEnabled()) {
            Element groupBoxElement = element.element("groupBox");
            if (groupBoxElement != null) {
                String expanded = groupBoxElement.attributeValue("expanded");
                if (expanded != null) {
                    setExpanded(BooleanUtils.toBoolean(expanded));
                }
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            element.remove(groupBoxElement);
        }
        groupBoxElement = element.addElement("groupBox");
        groupBoxElement.addAttribute("expanded", BooleanUtils.toStringTrueFalse(isExpanded()));
        return true;
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }

    @Override
    public boolean isBorderVisible() {
        return true;
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestFocus() {
        Iterator<com.vaadin.ui.Component> componentIterator = getComponentContent().iterator();
        if (componentIterator.hasNext()) {
            com.vaadin.ui.Component component = componentIterator.next();
            if (component instanceof com.vaadin.ui.Component.Focusable) {
                ((com.vaadin.ui.Component.Focusable) component).focus();
            }
        }
    }

    @Override
    public void setSpacing(boolean enabled) {
        getComponentContent().setSpacing(enabled);
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (!ObjectUtils.equals(orientation, this.orientation)) {
            if (!ownComponents.isEmpty()) {
                throw new IllegalStateException("Unable to change groupBox orientation after adding components to it");
            }

            this.orientation = orientation;
        }
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }
}