/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.ITogglePanel;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;

import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("serial")
@ClientWidget(ITogglePanel.class)
public class TogglePanel extends Panel {

    private boolean expanded = false;
    private boolean hideToggle = false;

    private String expandText;
    private String collapseText;

    private Layout expandLayout = null;

    public TogglePanel() {
        this(null, null);
    }

    public TogglePanel(Layout collapseLayout, Layout expandLayout) {
        super(collapseLayout);
        setExpandLayout(expandLayout);
    }

    public TogglePanel(String caption) {
        this(caption, null, null);
    }

    public TogglePanel(String caption, Layout collapseLayout, Layout expandLayout) {
        super(caption, collapseLayout);
        setExpandLayout(expandLayout);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (isExpanded()) {
            getExpandLayout().paint(target);
        } else {
            getCollapseLayout().paint(target);
        }
        target.addAttribute("expanded", isExpanded());
        if (isHideToggle()) {
            target.addAttribute("hideToggle", true);
        }
        if (getExpandText() != null) {
            target.addAttribute("expandText", getExpandText());
        }
        if (getCollapseText() != null) {
            target.addAttribute("collapseText", getCollapseText());
        }

        if (actionManager != null) {
            actionManager.paintActions(null, target);
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {

        boolean needRepaint = false;

        super.changeVariables(source, variables);
        if (variables.containsKey("toggle")) {
            togglePanel(false);

            needRepaint = true;
        }

        if (needRepaint) {
            requestRepaint();
        }
    }

    public Layout getCollapseLayout() {
        return getLayout();
    }

    public void setCollapseLayout(Layout layout) {
        setLayout(layout);
    }

    @Override
    public void setLayout(Layout newLayout) {
        super.setLayout(newLayout);
    }

    public Layout getExpandLayout() {
        return expandLayout;
    }

    public void setExpandLayout(Layout layout) {
        if (layout == null) {
            layout = new VerticalLayout();
            layout.setMargin(true);
        }

        if (layout == expandLayout) {
            return;
        }

        // detach old layout if present
        if (expandLayout != null) {
            expandLayout.setParent(null);
            expandLayout.removeListener((ComponentContainer.ComponentAttachListener) this);
            expandLayout.removeListener((ComponentContainer.ComponentDetachListener) this);
        }

        // Sets the panel to be parent for the layout
        layout.setParent(this);

        // Sets the new layout
        expandLayout = layout;

        // Adds the event listeners for new layout
        expandLayout.addListener((ComponentContainer.ComponentAttachListener) this);
        expandLayout.addListener((ComponentContainer.ComponentDetachListener) this);
    }

    public void setExpanded(boolean expanded) {
        if (isExpanded() != expanded) {
            setExpanded(expanded,  true);
        }
    }

    protected void setExpanded(boolean expanded, boolean repaint) {
        this.expanded = expanded;
        if (repaint) {
            requestRepaint();
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void togglePanel() {
        togglePanel(true);
    }

    protected void togglePanel(boolean repaint) {
        setExpanded(!isExpanded(), repaint);
        fireToggleEvent();
    }

    public boolean isHideToggle() {
        return hideToggle;
    }

    public void setHideToggle(boolean hideToggle) {
        if (isHideToggle() != hideToggle) {
            this.hideToggle = hideToggle;
            requestRepaint();
        }
    }

    public String getExpandText() {
        return expandText;
    }

    public void setExpandText(String expandText) {
        this.expandText = expandText;
        requestRepaint();
    }

    public String getCollapseText() {
        return collapseText;
    }

    public void setCollapseText(String collapseText) {
        this.collapseText = collapseText;
        requestRepaint();
    }

    public void addListener(TogglePanelListener listener) {
        addListener(TogglePanelEvent.class, listener,
                TOGGLE_PANEL_METHOD);
    }

    public void removeListener(TogglePanelListener listener) {
        removeListener(TogglePanelEvent.class, listener,
                TOGGLE_PANEL_METHOD);
    }

    protected void fireToggleEvent() {
        fireEvent(new TogglePanelEvent(this));
    }

    private static final Method TOGGLE_PANEL_METHOD;
    static {
        try {
            TOGGLE_PANEL_METHOD = TogglePanelListener.class
                    .getDeclaredMethod("togglePanel",
                            new Class[] { TogglePanelEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in TogglePanel");
        }
    }

    public class TogglePanelEvent extends Component.Event {
        public TogglePanelEvent(TogglePanel source) {
            super(source);
        }

        @Override
        public TogglePanel getSource() {
            return (TogglePanel) super.getSource();
        }
    }

    public interface TogglePanelListener {

        void togglePanel(TogglePanelEvent event);

    }
}
