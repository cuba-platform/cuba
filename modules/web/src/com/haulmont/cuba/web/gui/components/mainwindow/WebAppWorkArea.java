/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.vaadin.event.Action;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebAppWorkArea extends WebAbstractComponent<VerticalLayout> implements AppWorkArea {

    public static final String WORKAREA_STYLENAME = "cuba-app-workarea";

    public static final String MODE_TABBED_STYLENAME = "cuba-app-workarea-tabbed";
    public static final String MODE_SINGLE_STYLENAME = "cuba-app-workarea-single";

    public static final String STATE_INITIAL_STYLENAME = "cuba-app-workarea-initial";
    public static final String STATE_WINDOWS_STYLENAME = "cuba-app-workarea-windows";

    public static final String SINGLE_CONTAINER_STYLENAME = "cuba-main-singlewindow";
    public static final String TABBED_CONTAINER_STYLENAME = "cuba-main-tabsheet";

    public static final String INITIAL_LAYOUT_STYLENAME = "cuba-initial-layout";

    protected Mode mode = Mode.TABBED;
    protected State state = State.INITIAL_LAYOUT;

    protected VBoxLayout initialLayout;
    protected CubaTabSheet tabbedContainer;
    protected VerticalLayout singleContainer;

    // lazy initialized listeners list
    protected List<StateChangeListener> stateChangeListeners = null;

    public WebAppWorkArea() {
        component = new VerticalLayout();
        component.addStyleName(WORKAREA_STYLENAME);
        component.addStyleName(MODE_TABBED_STYLENAME);
        component.addStyleName(STATE_INITIAL_STYLENAME);

        ComponentsFactory cf = AppBeans.get(ComponentsFactory.NAME);
        setInitialLayout(cf.createComponent(VBoxLayout.class));

        tabbedContainer = createTabbedModeContainer();

        UserSettingsTools userSettingsTools = AppBeans.get(UserSettingsTools.NAME);
        setMode(userSettingsTools.loadAppWindowMode());
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(WORKAREA_STYLENAME);

        if (mode == Mode.TABBED) {
            component.addStyleName(MODE_TABBED_STYLENAME);
        } else {
            component.addStyleName(MODE_SINGLE_STYLENAME);
        }

        if (state == State.INITIAL_LAYOUT) {
            component.addStyleName(STATE_INITIAL_STYLENAME);
        } else {
            component.addStyleName(STATE_WINDOWS_STYLENAME);
        }
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);

        initialLayout.setFrame(frame);
    }

    @Nonnull
    @Override
    public VBoxLayout getInitialLayout() {
        return initialLayout;
    }

    @Override
    public void setInitialLayout(VBoxLayout initialLayout) {
        if (state == State.WINDOW_CONTAINER) {
            throw new IllegalStateException("Unable to change AppWorkArea initial layout in WINDOW_CONTAINER state");
        }
        Preconditions.checkNotNullArgument(initialLayout);

        if (this.initialLayout != null) {
            component.removeComponent(WebComponentsHelper.getComposition(this.initialLayout));
        }

        this.initialLayout = initialLayout;

        initialLayout.setParent(this);
        initialLayout.setHeight("100%");
        initialLayout.setWidth("100%");

        Component vInitialLayout = WebComponentsHelper.getComposition(initialLayout);
        vInitialLayout.addStyleName(INITIAL_LAYOUT_STYLENAME);
        component.addComponent(vInitialLayout);
        component.setExpandRatio(vInitialLayout, 1);
    }

    @Override
    public void addStateChangeListener(StateChangeListener listener) {
        if (stateChangeListeners == null) {
            stateChangeListeners = new LinkedList<>();
        }
        if (!stateChangeListeners.contains(listener)) {
            stateChangeListeners.add(listener);
        }
    }

    @Override
    public void removeStateChangeListener(StateChangeListener listener) {
        if (stateChangeListeners != null) {
            stateChangeListeners.remove(listener);

            if (stateChangeListeners.isEmpty()) {
                stateChangeListeners = null;
            }
        }
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        if (state == State.WINDOW_CONTAINER) {
            throw new IllegalStateException("Unable to change AppWorkArea mode in WINDOW_CONTAINER state");
        }

        if (this.mode != mode) {
            if (mode == Mode.SINGLE) {
                tabbedContainer = null;

                singleContainer = createSingleModeContainer();
                component.addStyleName(MODE_SINGLE_STYLENAME);
                component.removeStyleName(MODE_TABBED_STYLENAME);
            } else {
                singleContainer = null;

                tabbedContainer = createTabbedModeContainer();
                component.removeStyleName(MODE_SINGLE_STYLENAME);
                component.addStyleName(MODE_TABBED_STYLENAME);
            }

            this.mode = mode;
        }
    }

    protected CubaTabSheet createTabbedModeContainer() {
        CubaTabSheet tabSheet = new CubaTabSheet();
        tabSheet.setHeight("100%");
        tabSheet.setStyleName(TABBED_CONTAINER_STYLENAME);

        Action.Handler actionHandler = createTabSheetActionHandler(tabSheet);
        tabSheet.addActionHandler(actionHandler);

        return tabSheet;
    }

    protected Action.Handler createTabSheetActionHandler(CubaTabSheet tabSheet) {
        return new MainTabSheetActionHandler(tabSheet);
    }

    protected VerticalLayout createSingleModeContainer() {
        VerticalLayout boxLayout = new VerticalLayout();
        boxLayout.setHeight("100%");
        boxLayout.setStyleName(SINGLE_CONTAINER_STYLENAME);
        return boxLayout;
    }

    public CubaTabSheet getTabbedWindowContainer() {
        return tabbedContainer;
    }

    public VerticalLayout getSingleWindowContainer() {
        return singleContainer;
    }

    /**
     * Used only by {@link com.haulmont.cuba.web.WebWindowManager}
     *
     * @param state new state
     */
    public void switchTo(State state) {
        if (this.state != state) {
            component.getUI().focus();
            component.removeAllComponents();

            if (state == State.WINDOW_CONTAINER) {
                if (mode == Mode.SINGLE) {
                    component.addComponent(singleContainer);
                } else {
                    component.addComponent(tabbedContainer);
                }
                component.addStyleName(STATE_WINDOWS_STYLENAME);
                component.removeStyleName(STATE_INITIAL_STYLENAME);
            } else {
                component.addComponent(WebComponentsHelper.getComposition(initialLayout));
                component.removeStyleName(STATE_WINDOWS_STYLENAME);
                component.addStyleName(STATE_INITIAL_STYLENAME);
            }

            this.state = state;

            if (stateChangeListeners != null) {
                for (StateChangeListener listener : new ArrayList<>(stateChangeListeners)) {
                    listener.stateChanged(state);
                }
            }
        }
    }

    @Override
    public State getState() {
        return state;
    }
}