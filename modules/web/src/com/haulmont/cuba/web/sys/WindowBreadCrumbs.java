/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WindowBreadCrumbs extends HorizontalLayout {

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

    public WindowBreadCrumbs() {
        setWidth(100, Unit.PERCENTAGE);
        setHeight(-1, Unit.PIXELS); // TODO (abramov) This is a bit tricky
        setStyleName("cuba-headline-container");

        tabbedMode = AppWindow.Mode.TABBED.equals(App.getInstance().getAppWindow().getMode());

        if (tabbedMode)
            setVisible(false);

        logoLayout = new HorizontalLayout();
        logoLayout.setStyleName("cuba-breadcrumbs-logo");
        logoLayout.setMargin(true);
        logoLayout.setSpacing(true);

        linksLayout = new HorizontalLayout();
        linksLayout.setStyleName("cuba-breadcrumbs");
        linksLayout.setSizeUndefined();

        if (!tabbedMode) {
            closeBtn = new Button("", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    final Window window = getCurrentWindow();
                    window.close(Window.CLOSE_ACTION_ID);
                }
            });
            closeBtn.setIcon(new VersionedThemeResource("icons/close.png"));
            closeBtn.setStyleName("cuba-closetab-button");
        }

        AppUI ui = AppUI.getCurrent();
        if (ui.isTestMode()) {
            TestIdManager testIdManager = ui.getTestIdManager();
            linksLayout.setId(testIdManager.getTestId("breadCrumbs"));
            linksLayout.setCubaId("breadCrumbs");

            if (closeBtn != null) {
                closeBtn.setId(testIdManager.reserveId("closeBtn"));
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
            setVisible(true);
    }

    public void removeWindow() {
        if (!windows.isEmpty()) {
            windows.removeLast();
            update();
        }
        if (windows.size() <= 1 && tabbedMode)
            setVisible(false);
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

    protected void setSubTitleContents() {

    }

    public void update() {
        AppUI ui = AppUI.getCurrent();
        boolean isTestMode = ui.isTestMode();

        linksLayout.removeAllComponents();
        btn2win.clear();
        for (Iterator<Window> it = windows.iterator(); it.hasNext();) {
            Window window = it.next();
            Button button = new Button(window.getCaption().trim(), new BtnClickListener());
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
                separatorLab.setSizeUndefined();
                separatorLab.setContentMode(ContentMode.HTML);
                linksLayout.addComponent(separatorLab);
            } else {
                Label captionLabel = new Label(window.getCaption());
                captionLabel.setSizeUndefined();
                linksLayout.addComponent(captionLabel);
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