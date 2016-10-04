/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaSingleModeContainer;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.vaadin.event.Action;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.drophandlers.DefaultTabSheetDropHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WebAppWorkArea extends WebAbstractComponent<CssLayout> implements AppWorkArea {

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
    protected CubaSingleModeContainer singleContainer;

    // lazy initialized listeners list
    protected List<StateChangeListener> stateChangeListeners = null;

    public WebAppWorkArea() {
        component = new CssLayout();
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
    public void setFrame(Frame frame) {
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
        tabSheet.setDragMode(LayoutDragMode.CLONE);
        tabSheet.setDropHandler(new DefaultTabSheetDropHandler());
        tabSheet.setHeight("100%");
        tabSheet.setStyleName(TABBED_CONTAINER_STYLENAME);

        Action.Handler actionHandler = createTabSheetActionHandler(tabSheet);
        tabSheet.addActionHandler(actionHandler);

        return tabSheet;
    }

    protected Action.Handler createTabSheetActionHandler(CubaTabSheet tabSheet) {
        return new MainTabSheetActionHandler(tabSheet);
    }

    protected CubaSingleModeContainer createSingleModeContainer() {
        CubaSingleModeContainer boxLayout = new CubaSingleModeContainer();
        boxLayout.setHeight("100%");
        boxLayout.setStyleName(SINGLE_CONTAINER_STYLENAME);
        return boxLayout;
    }

    public CubaTabSheet getTabbedWindowContainer() {
        return tabbedContainer;
    }

    public CubaSingleModeContainer getSingleWindowContainer() {
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