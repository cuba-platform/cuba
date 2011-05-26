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
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBox;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.toolkit.gwt.client.ui.VGroupBox;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

@ClientWidget(VGroupBox.class)
public class WebGroupBox extends WebAbstractPanel implements GroupBox {

    private static final long serialVersionUID = 603031841274663159L;

    private boolean expanded = true;
    private boolean collapsable;

    private List<ExpandListener> expandListeners = null;
    private List<CollapseListener> collapseListeners = null;

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<Action>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    public WebGroupBox() {
        VerticalActionsLayout container = new VerticalActionsLayout();
        container.setSpacing(true);
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
        final com.vaadin.ui.Component comp = WebComponentsHelper.unwrap(component);
        if (comp instanceof Layout || comp instanceof com.vaadin.ui.Panel) {
            if (getContent() == null) {
                setContent((com.vaadin.ui.ComponentContainer) comp);
            } else {
                getContent().addComponent(comp);
            }
            this.component = component;
        } else {
            throw new UnsupportedOperationException("Only layout or panel component is supported inside groupBox");
        }
    }

    @Override
    public void remove(Component component) {
        final com.vaadin.ui.Component comp = WebComponentsHelper.unwrap(component);
        if (getContent() == comp) {
            setContent(null);
            this.component = null;
        } else {
            getContent().removeComponent(comp);
        }
    }

    @Override
    public void expand(Component component, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.getComposition(component);
        expandedComponent.setSizeFull();
    }

    public void expandLayout(boolean expandLayout) {
        if (expandLayout) {
            getContent().setSizeFull();
        } else {
            getContent().setWidth("100%");
            getContent().setHeight("-1px");
        }
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
}
