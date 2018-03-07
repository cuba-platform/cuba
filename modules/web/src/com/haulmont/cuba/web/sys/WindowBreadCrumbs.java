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
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class WindowBreadCrumbs extends CssLayout {

    protected static final String BREADCRUMBS_VISIBLE_WRAP_STYLE = "c-breadcrumbs-visible";
    protected static final String C_HEADLINE_CONTAINER = "c-headline-container";

    protected boolean visibleExplicitly = true;
    protected Label label;

    public interface Listener {
        void windowClick(Window window);
    }

    protected boolean tabbedMode;

    protected LinkedList<Window> windows = new LinkedList<>();

    protected Layout logoLayout;
    protected Layout linksLayout;
    protected Button closeBtn;

    protected Map<Button, Window> btn2win = new HashMap<>();

    protected List<Listener> listeners = new ArrayList<>();

    public WindowBreadCrumbs(AppWorkArea workArea) {
        setWidth(100, Unit.PERCENTAGE);
        setHeightUndefined();
        setPrimaryStyleName(C_HEADLINE_CONTAINER);

        tabbedMode = workArea.getMode() == AppWorkArea.Mode.TABBED;

        if (tabbedMode) {
            super.setVisible(false);
        }

        addAttachListener(event ->
                adjustParentStyles()
        );

        logoLayout = createLogoLayout();

        linksLayout = createLinksLayout();
        linksLayout.setSizeUndefined();

        if (!tabbedMode) {
            closeBtn = new CubaButton("", event -> {
                Window window = getCurrentWindow();
                if (!isCloseWithCloseButtonPrevented(window)) {
                    window.close(Window.CLOSE_ACTION_ID);
                }
            });
            closeBtn.setIcon(AppBeans.get(IconResolver.class).getIconResource("icons/close.png"));
            closeBtn.setStyleName("c-closetab-button");
        }

        AppUI ui = AppUI.getCurrent();
        if (ui.isTestMode()) {
            TestIdManager testIdManager = ui.getTestIdManager();
            linksLayout.setId(testIdManager.getTestId("breadCrumbs"));
            linksLayout.setCubaId("breadCrumbs");

            if (closeBtn != null) {
                closeBtn.setId(testIdManager.getTestId("closeBtn"));
                closeBtn.setCubaId("closeBtn");
            }
        }

        Layout enclosingLayout = createEnclosingLayout();
        enclosingLayout.addComponent(linksLayout);

        addComponent(logoLayout);
        addComponent(enclosingLayout);

        boolean controlsVisible = AppBeans.get(Configuration.class)
                .getConfig(WebConfig.class)
                .getShowBreadCrumbs();

        enclosingLayout.setVisible(controlsVisible);

        if (closeBtn != null) {
            addComponent(closeBtn);
        }
    }

    protected boolean isCloseWithCloseButtonPrevented(Window currentWindow) {
        WebWindow webWindow = (WebWindow) ComponentsHelper.getWindowImplementation(currentWindow);

        if (webWindow != null) {
            Window.BeforeCloseWithCloseButtonEvent event = new Window.BeforeCloseWithCloseButtonEvent(webWindow);
            webWindow.fireBeforeCloseWithCloseButton(event);
            return event.isClosePrevented();
        }

        return false;
    }

    protected Layout createEnclosingLayout() {
        Layout enclosingLayout = new CssLayout();
        enclosingLayout.setPrimaryStyleName("c-breadcrumbs-container");
        return enclosingLayout;
    }

    protected Layout createLinksLayout() {
        CssLayout linksLayout = new CssLayout();
        linksLayout.setPrimaryStyleName("c-breadcrumbs");
        return linksLayout;
    }

    protected Layout createLogoLayout() {
        CssLayout logoLayout = new CssLayout();
        logoLayout.setPrimaryStyleName("c-breadcrumbs-logo");
        return logoLayout;
    }

    public Window getCurrentWindow() {
        if (windows.isEmpty())
            return null;
        else
            return windows.getLast();
    }

    public void addWindow(Window window) {
        windows.add(window);
        update();
        if (windows.size() > 1 && tabbedMode)
            super.setVisible(visibleExplicitly);

        if (getParent() != null) {
            adjustParentStyles();
        }
    }

    public void removeWindow() {
        if (!windows.isEmpty()) {
            windows.removeLast();
            update();
        }
        if (windows.size() <= 1 && tabbedMode)
            super.setVisible(false);

        if (getParent() != null) {
            adjustParentStyles();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visibleExplicitly = visible;

        super.setVisible(isVisible() && visibleExplicitly);

        if (getParent() != null) {
            adjustParentStyles();
        }
    }

    protected void adjustParentStyles() {
        if (isVisible()) {
            getParent().addStyleName(BREADCRUMBS_VISIBLE_WRAP_STYLE);
        } else {
            getParent().removeStyleName(BREADCRUMBS_VISIBLE_WRAP_STYLE);
        }
    }

    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    protected void fireListeners(Window window) {
        for (Listener listener : listeners.toArray(new Listener[listeners.size()])) {
            listener.windowClick(window);
        }
    }

    public void update() {
        AppUI ui = AppUI.getCurrent();
        boolean isTestMode = ui.isTestMode();

        linksLayout.removeAllComponents();
        btn2win.clear();
        for (Iterator<Window> it = windows.iterator(); it.hasNext();) {
            Window window = it.next();
            Button button = new CubaButton(StringUtils.trimToEmpty(window.getCaption()), new BtnClickListener());
            button.setSizeUndefined();
            button.setStyleName(BaseTheme.BUTTON_LINK);
            button.setTabIndex(-1);

            if (isTestMode) {
                button.setCubaId("breadCrubms_Button_" + window.getId());
                button.setId(ui.getTestIdManager().getTestId("breadCrubms_Button_" + window.getId()));
            }

            btn2win.put(button, window);

            if (it.hasNext()) {
                linksLayout.addComponent(button);

                Label separatorLab = new Label("&nbsp;&gt;&nbsp;");
                separatorLab.setStyleName("c-breadcrumbs-separator");
                separatorLab.setSizeUndefined();
                separatorLab.setContentMode(ContentMode.HTML);
                linksLayout.addComponent(separatorLab);
            } else {
                Label captionLabel = new Label(window.getCaption());
                captionLabel.setStyleName("c-breadcrumbs-win-caption");
                captionLabel.setSizeUndefined();
                linksLayout.addComponent(captionLabel);

                this.label = captionLabel;
            }
        }
    }

    public Label getLabel() {
        return label;
    }

    public class BtnClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            Window win = btn2win.get(event.getButton());
            if (win != null)
                fireListeners(win);
        }
    }
}