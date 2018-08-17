package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.gui.screen.ScreenOptions;

public class ScreenContextImpl implements ScreenContext {

    protected final ScreenOptions options;
    protected final WindowInfo windowInfo;

    protected final Screens screens;
    protected final Dialogs dialogs;
    protected final Notifications notifications;

    public ScreenContextImpl(WindowInfo windowInfo, ScreenOptions options,
                             Screens screens, Dialogs dialogs, Notifications notifications) {
        this.windowInfo = windowInfo;
        this.options = options;

        this.screens = screens;
        this.dialogs = dialogs;
        this.notifications = notifications;
    }

    @Override
    public ScreenOptions getScreenOptions() {
        return options;
    }

    @Override
    public WindowInfo getWindowInfo() {
        return windowInfo;
    }

    @Override
    public Screens getScreens() {
        return screens;
    }

    @Override
    public Dialogs getDialogs() {
        return dialogs;
    }

    @Override
    public Notifications getNotifications() {
        return notifications;
    }
}