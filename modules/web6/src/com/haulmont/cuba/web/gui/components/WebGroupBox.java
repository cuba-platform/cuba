/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 17:52:31
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.toolkit.gwt.client.ui.VGroupBox;
import com.haulmont.cuba.web.toolkit.ui.HorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.OrderedActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.util.*;

@ClientWidget(VGroupBox.class)
public class WebGroupBox extends Panel implements GroupBoxLayout {

    private static final long serialVersionUID = 603031841274663159L;

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
        VerticalActionsLayout container = new VerticalActionsLayout();
        initContainer(container);
    }

    private void initContainer(OrderedActionsLayout container) {
        container.setSizeFull();
        setContent(container);
        container.addActionHandler(new com.vaadin.event.Action.Handler() {
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                final Set<com.vaadin.event.Action> keys = actions.keySet();
                return keys.toArray(new com.vaadin.event.Action[keys.size()]);
            }

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

    public boolean isExpanded() {
        return !collapsable || expanded;
    }

    public void setExpanded(boolean expanded) {
        if (collapsable) {
            this.expanded = expanded;
            getContent().setVisible(expanded);
            requestRepaint();
        }
    }

    public boolean isCollapsable() {
        return collapsable;
    }

    public void setCollapsable(boolean collapsable) {
        this.collapsable = collapsable;
        if (collapsable) {
            setExpanded(true);
        }
    }

    public void addListener(ExpandListener listener) {
        if (expandListeners == null) {
            expandListeners = new ArrayList<ExpandListener>();
        }
        expandListeners.add(listener);
    }

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

    public void addListener(CollapseListener listener) {
        if (collapseListeners == null) {
            collapseListeners = new ArrayList<CollapseListener>();
        }
        collapseListeners.add(listener);
    }

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

    public void addAction(final com.haulmont.cuba.gui.components.Action action) {
        if (action instanceof ShortcutAction) {
            actions.put(WebComponentsHelper.createShortcutAction((ShortcutAction) action), action);
        }
        actionsOrder.add(action);
    }

    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        actionsOrder.remove(action);
        actions.inverse().remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

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

    public void applySettings(Element element) {
        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            String expanded = groupBoxElement.attributeValue("expanded");
            if (expanded != null) {
                setExpanded(BooleanUtils.toBoolean(expanded));
            }
        }
    }

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
