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

import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class WindowBreadCrumbs extends HorizontalLayout {

    protected boolean visibleExplicitly = true;

    public interface Listener {
        void windowClick(Window window);
    }

    protected boolean tabbedMode;

    protected LinkedList<Window> windows = new LinkedList<>();

    protected HorizontalLayout logoLayout;
    protected HorizontalLayout linksLayout;
    protected Button closeBtn;

    protected Map<Button, Window> btn2win = new HashMap<>();

    protected Set<Listener> listeners = new HashSet<>();

    public WindowBreadCrumbs(WebAppWorkArea workArea) {
        setWidth(100, Unit.PERCENTAGE);
        setHeight(-1, Unit.PIXELS);
        setStyleName("cuba-headline-container");

        tabbedMode = workArea.getMode() == AppWorkArea.Mode.TABBED;

        if (tabbedMode)
            super.setVisible(false);

        logoLayout = new HorizontalLayout();
        logoLayout.setStyleName("cuba-breadcrumbs-logo");
        logoLayout.setMargin(true);
        logoLayout.setSpacing(true);

        linksLayout = new HorizontalLayout();
        linksLayout.setStyleName("cuba-breadcrumbs");
        linksLayout.setSizeUndefined();

        if (!tabbedMode) {
            closeBtn = new CubaButton("", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    final Window window = getCurrentWindow();
                    window.close(Window.CLOSE_ACTION_ID);
                }
            });
            closeBtn.setIcon(WebComponentsHelper.getIcon("icons/close.png"));
            closeBtn.setStyleName("cuba-closetab-button");
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

        HorizontalLayout enclosingLayout = new HorizontalLayout();
        enclosingLayout.setStyleName("cuba-breadcrumbs-container");
        enclosingLayout.addComponent(linksLayout);
        enclosingLayout.setComponentAlignment(linksLayout, Alignment.MIDDLE_LEFT);

        addComponent(logoLayout);
        addComponent(enclosingLayout);

        if (closeBtn != null) {
            addComponent(closeBtn);
        }

        setExpandRatio(enclosingLayout, 1);
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
    }

    public void removeWindow() {
        if (!windows.isEmpty()) {
            windows.removeLast();
            update();
        }
        if (windows.size() <= 1 && tabbedMode)
            super.setVisible(false);
    }

    @Override
    public void setVisible(boolean visible) {
        this.visibleExplicitly = visible;

        super.setVisible(isVisible() && visibleExplicitly);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    private void fireListeners(Window window) {
        for (Listener listener : listeners) {
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
                linksLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);

                Label separatorLab = new Label("&nbsp;&gt;&nbsp;");
                separatorLab.setSizeUndefined();
                separatorLab.setContentMode(ContentMode.HTML);
                linksLayout.addComponent(separatorLab);
                linksLayout.setComponentAlignment(separatorLab, Alignment.MIDDLE_LEFT);
            } else {
                Label captionLabel = new Label(window.getCaption());
                captionLabel.setSizeUndefined();
                linksLayout.addComponent(captionLabel);
                linksLayout.setComponentAlignment(captionLabel, Alignment.MIDDLE_LEFT);
            }
        }
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