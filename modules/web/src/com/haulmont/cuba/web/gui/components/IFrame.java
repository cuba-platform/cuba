/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:51:22
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.core.global.MessageProvider;

import java.util.Map;

public class IFrame extends AbstractPanel implements com.haulmont.cuba.gui.components.IFrame {

    private String messagePack;
    private DsContext dsContext;
    private com.haulmont.cuba.gui.components.IFrame frame;

    public IFrame() {
    }

    public DsContext getDsContext() {
        return dsContext == null ? getFrame().getDsContext() : dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public String getMessagesPack() {
        return messagePack;
    }

    public void setMessagesPack(String name) {
        messagePack = name;
    }

    public String getMessage(String key) {
        if (messagePack == null)
            throw new IllegalStateException("MessagePack is not set");
        return MessageProvider.getMessage(messagePack, key);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Object item, WindowManager.OpenType openType, Map<String, Object> params) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Object item, WindowManager.OpenType openType) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType);
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
        App.getInstance().getWindowManager().showMessageDialog(title, message, messageType);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    public <A extends com.haulmont.cuba.gui.components.IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(com.haulmont.cuba.gui.components.IFrame frame) {
        this.frame = frame;
    }
}
