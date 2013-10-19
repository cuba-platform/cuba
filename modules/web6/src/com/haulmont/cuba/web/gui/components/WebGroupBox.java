/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.toolkit.gwt.client.ui.VGroupBox;
import com.haulmont.cuba.web.toolkit.ui.HorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.OrderedActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
@ClientWidget(VGroupBox.class)
public class WebGroupBox extends Panel implements GroupBoxLayout {

    private static final long serialVersionUID = 603031841274663159L;

    protected String id;
    protected IFrame frame;
    protected List<Component> components = new ArrayList<>();
    protected Alignment alignment = Alignment.TOP_LEFT;
    protected Orientation orientation = Orientation.VERTICAL;

    protected boolean expanded = true;
    protected boolean collapsable;

    protected List<ExpandListener> expandListeners = null;
    protected List<CollapseListener> collapseListeners = null;

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<Action>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    public WebGroupBox() {
        VerticalActionsLayout container = new VerticalActionsLayout();
        initContainer(container);
    }

    private void initContainer(OrderedActionsLayout container) {
        container.setSizeFull();
        setContent(container);
        container.addActionHandler(new com.vaadin.event.Action.Handler() {
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
    public void add(Component component) {
        OrderedActionsLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(getContent() instanceof VerticalActionsLayout)) {
            newContent = new VerticalActionsLayout();
        } else if (orientation == Orientation.HORIZONTAL && !(getContent() instanceof HorizontalActionsLayout))
            newContent = new HorizontalActionsLayout();

        if (newContent != null) {
            initContainer(newContent);
            newContent.setMargin(((OrderedActionsLayout) getContent()).getMargin());
            newContent.setSpacing(((OrderedActionsLayout) getContent()).isSpacing());
            setContent(newContent);
        }

        getContent().addComponent(WebComponentsHelper.getComposition(component));
        components.add(component);
    }

    @Override
    public void remove(Component component) {
        getContent().removeComponent(WebComponentsHelper.getComposition(component));
        components.remove(component);
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
        WebComponentsHelper.expand((AbstractOrderedLayout) getContent(), expandedComponent, height, width);
    }

    @Override
    public boolean isExpanded() {
        return !collapsable || expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        if (collapsable) {
            this.expanded = expanded;
            getContent().setVisible(expanded);
            requestRepaint();
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
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("collapsable", isCollapsable());
        if (isCollapsable()) {
            target.addAttribute("expanded", isExpanded());
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (isCollapsable()) {
            if (variables.containsKey("expand")) {
                setExpanded(true);
                getContent().requestRepaintAll();

                fireExpandListeners();

            } else if (variables.containsKey("collapse")) {
                setExpanded(false);

                fireCollapseListeners();
            }
        }
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
        if (getComponentIterator().hasNext()) {
            com.vaadin.ui.Component component = getComponentIterator().next();
            if (component instanceof Focusable) {
                ((Focusable) component).focus();
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
        final com.vaadin.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    @Override
    public void setSpacing(boolean enabled) {
        ((AbstractOrderedLayout) getContent()).setSpacing(enabled);
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
}