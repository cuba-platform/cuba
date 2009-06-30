/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:53:35
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.Map;

public interface IFrame extends Layout, Component.Container, Component.BelongToFrame, Component.HasLayout {
    DsContext getDsContext();
    void setDsContext(DsContext dsContext);

    String getMessagesPack();
    void setMessagesPack(String name);

    String getMessage(String key);

    <T extends Window> T openWindow(
            String windowAlias, WindowManager.OpenType openType, Map<String, Object> params);

    <T extends Window> T openWindow(
            String windowAlias, WindowManager.OpenType openType);

    <T extends Window> T openEditor(
            String windowAlias, Object item,
            WindowManager.OpenType openType, Map<String, Object> params);

    <T extends Window> T openEditor(
            String windowAlias, Object item, WindowManager.OpenType openType);

    <T extends Window> T openLookup(
            String windowAlias, Window.Lookup.Handler handler,
            WindowManager.OpenType openType, Map<String, Object> params);

    <T extends Window> T openLookup(
            String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum MessageType {
        CONFIRMATION,
        WARNING
    }
    
    void showMessageDialog(String title, String message, MessageType messageType);
    void showOptionDialog(String title, String message, MessageType messageType, Action[] actions);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum NotificationType {
        TRAY,
        HUMANIZED,
        WARNING,
        ERROR
    }

    void showNotification(String caption, NotificationType type);
    void showNotification(String caption, String description, NotificationType type);

}
