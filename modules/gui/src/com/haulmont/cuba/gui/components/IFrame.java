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
import com.haulmont.cuba.gui.config.ScreenInfo;

import java.util.Map;
import java.util.ResourceBundle;

public interface IFrame extends OrderedLayout, Component.Container, Component.BelongToFrame {
    DsContext getDsContext();
    void setDsContext(DsContext dsContext);

    ResourceBundle getResourceBundle();
    void setResourceBundle(ResourceBundle resourceBundle);

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
}
