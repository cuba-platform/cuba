/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.toolkit.ui.GroupBox;
import com.haulmont.cuba.web.toolkit.ui.HorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.OrderedActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Layout;
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
public class WebGroupBox extends WebAbstractComponent<GroupBox> implements GroupBoxLayout, GroupBox.ExpandChangeHandler {

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected Map<String, Component> componentByIds = new HashMap<>();

    protected Orientation orientation = Orientation.VERTICAL;

    protected List<ExpandListener> expandListeners = null;
    protected List<CollapseListener> collapseListeners = null;

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    protected com.vaadin.event.Action.Handler actionHandler;

    public WebGroupBox() {
        component = new GroupBox();
        component.setExpandChangeHandler(this);

        VerticalActionsLayout container = new VerticalActionsLayout();
        initContainer(container);
    }

    private void initContainer(AbstractOrderedLayout container) {
        component.setContent(container);
        if (actionHandler == null) {
            actionHandler = new com.vaadin.event.Action.Handler() {
                @Override
                public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                    final Set<com.vaadin.event.Action> keys = actions.keySet();
                    return keys.toArray(new com.vaadin.event.Action[keys.size()]);
                }

                @Override
                public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                    Action act = actions.get(action);
                    if (act != null && act.isEnabled()) {
                        act.actionPerform(WebGroupBox.this);
                    }
                }
            };
            component.addActionHandler(actionHandler);
        }
    }

    private AbstractOrderedLayout getComponentContent() {
        return ((AbstractOrderedLayout) component.getContent());
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
        if (orientation == Orientation.VERTICAL && !(component.getContent() instanceof VerticalActionsLayout)) {
            newContent = new VerticalActionsLayout();
        } else if (orientation == Orientation.HORIZONTAL && !(component.getContent() instanceof HorizontalActionsLayout)) {
            newContent = new HorizontalActionsLayout();
        }

        if (newContent != null) {
            initContainer(newContent);

            newContent.setMargin(((OrderedActionsLayout) component.getContent()).getMargin());
            newContent.setSpacing(((OrderedActionsLayout) component.getContent()).isSpacing());
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
    public void remove(Component childComponent) {
        getComponentContent().removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void setFrame(IFrame frame) {
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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
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
        if (expandListeners == null) {
            expandListeners = new ArrayList<>();
        }
        expandListeners.add(listener);
    }

    @Override
    public void removeListener(ExpandListener listener) {
        if (expandListeners != null) {
            expandListeners.remove(listener);
            if (expandListeners.isEmpty()) {
                expandListeners = null;
            }
        }
    }

    private void fireExpandListeners() {
        if (expandListeners != null) {
            for (final ExpandListener expandListener : expandListeners) {
                expandListener.onExpand(this);
            }
        }
    }

    @Override
    public void addListener(CollapseListener listener) {
        if (collapseListeners == null) {
            collapseListeners = new ArrayList<>();
        }
        collapseListeners.add(listener);
    }

    @Override
    public void removeListener(CollapseListener listener) {
        if (collapseListeners != null) {
            collapseListeners.remove(listener);
            if (collapseListeners.isEmpty()) {
                collapseListeners = null;
            }
        }
    }

    private void fireCollapseListeners() {
        if (collapseListeners != null) {
            for (final CollapseListener collapseListener : collapseListeners) {
                collapseListener.onCollapse(this);
            }
        }
    }

    @Override
    public void addAction(final com.haulmont.cuba.gui.components.Action action) {
        if (action.getShortcut() != null) {
            actions.put(WebComponentsHelper.createShortcutAction(action), action);
        }
        actionsOrder.add(action);
    }

    @Override
    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        actionsOrder.remove(action);
        actions.inverse().remove(action);
    }

    @Override
    public void removeAction(String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    @Override
    public void removeAllActions() {
        actionsOrder.clear();
        actions.clear();
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    @Override
    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        for (com.haulmont.cuba.gui.components.Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public void applySettings(Element element) {
        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            String expanded = groupBoxElement.attributeValue("expanded");
            if (expanded != null) {
                setExpanded(BooleanUtils.toBoolean(expanded));
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            element.remove(groupBoxElement);
        }
        groupBoxElement = element.addElement("groupBox");
        groupBoxElement.addAttribute("expanded", BooleanUtils.toStringTrueFalse(isExpanded()));
        return true;
    }

    @Override
    public boolean isBorderVisible() {
        return true;
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void requestFocus() {
        Iterator<com.vaadin.ui.Component> componentIterator = getComponentContent().getComponentIterator();
        if (componentIterator.hasNext()) {
            com.vaadin.ui.Component component = componentIterator.next();
            if (component instanceof com.vaadin.ui.Component.Focusable) {
                ((com.vaadin.ui.Component.Focusable) component).focus();
            }
        }
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component parentComponent = component.getParent();
        if (parentComponent instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) parentComponent).setComponentAlignment(component,
                    WebComponentsHelper.convertAlignment(alignment));
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
    public void expandStateChanged(boolean expanded) {
        if (expanded) {
            fireExpandListeners();
        } else {
            fireCollapseListeners();
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