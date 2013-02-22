/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.toolkit.ui.HorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.OrderedActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.util.*;

//@ClientWidget(VGroupBox.class)

/**
 * @author abramov
 * @version $Id$
 */
public class WebGroupBox extends WebAbstractComponent<Panel> implements GroupBoxLayout, Component.Wrapper {

    private String id;
    private IFrame frame;
    protected List<Component> components = new ArrayList<>();
    private Alignment alignment = Alignment.TOP_LEFT;
    private Orientation orientation = Orientation.VERTICAL;

    private boolean expanded = true;
    private boolean collapsable;

    private List<ExpandListener> expandListeners = null;
    private List<CollapseListener> collapseListeners = null;

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<Action>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    public WebGroupBox() {
        component = new Panel();

        VerticalActionsLayout container = new VerticalActionsLayout();
        initContainer(container);
    }

    private void initContainer(AbstractOrderedLayout container) {
        container.setSizeFull();
        component.setContent(container);
        component.addActionHandler(new com.vaadin.event.Action.Handler() {
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
        });
    }

    @Override
    public void add(Component childComponent) {
        AbstractOrderedLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(component.getContent() instanceof VerticalActionsLayout)) {
            newContent = new VerticalActionsLayout();
        } else if (orientation == Orientation.HORIZONTAL && !(component.getContent() instanceof HorizontalActionsLayout))
            newContent = new HorizontalActionsLayout();

        if (newContent != null) {
            initContainer(newContent);
            newContent.setMargin(((OrderedActionsLayout) component.getContent()).getMargin());
            newContent.setSpacing(((OrderedActionsLayout) component.getContent()).isSpacing());
            component.setContent(newContent);
        }

        getComponentContent().addComponent(WebComponentsHelper.getComposition(childComponent));
        components.add(childComponent);
    }

    @Override
    public void remove(Component childComponent) {
        getComponentContent().removeComponent(WebComponentsHelper.getComposition(childComponent));
        components.remove(childComponent);
    }

    private AbstractOrderedLayout getComponentContent() {
        return ((AbstractOrderedLayout)component.getContent());
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        for (Component component : components) {
            if (ObjectUtils.equals(component.getId(), id))
                return (T) component;
        }
        return null;
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        for (Component component : getComponents()) {
            if (ObjectUtils.equals(component.getId(), id))
                return (T) component;
        }
        return null;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(components);
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
    public boolean isExpanded() {
        return !collapsable || expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        if (collapsable) {
            this.expanded = expanded;
            getComponentContent().setVisible(expanded);
            component.markAsDirty();
        }
    }

    @Override
    public boolean isCollapsable() {
        return collapsable;
    }

    @Override
    public void setCollapsable(boolean collapsable) {
        this.collapsable = collapsable;
        if (collapsable) {
            setExpanded(true);
        }
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
        if (action instanceof ShortcutAction) {
            actions.put(WebComponentsHelper.createShortcutAction((ShortcutAction) action), action);
        }
        actionsOrder.add(action);
    }

    @Override
    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        actionsOrder.remove(action);
        actions.inverse().remove(action);
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

//    vaadin7
//    @Override
//    public void paintContent(PaintTarget target) throws PaintException {
//        super.paintContent(target);
//        target.addAttribute("collapsable", isCollapsable());
//        if (isCollapsable()) {
//            target.addAttribute("expanded", isExpanded());
//        }
//    }
//
//    @Override
//    public void changeVariables(Object source, Map variables) {
//        super.changeVariables(source, variables);
//        if (isCollapsable()) {
//            if (variables.containsKey("expand")) {
//                setExpanded(true);
//                getContent().requestRepaintAll();
//
//                fireExpandListeners();
//
//            } else if (variables.containsKey("collapse")) {
//                setExpanded(false);
//
//                fireCollapseListeners();
//            }
//        }
//    }

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

    @Override
    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    @Override
    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
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
            if (!components.isEmpty())
                throw new IllegalStateException("Unable to change groupBox orientation after adding components to it");

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