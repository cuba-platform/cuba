/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:51:22
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.ScreenInfo;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.App;

import java.util.Map;
import java.util.ResourceBundle;

public class IFrame extends AbstractPanel implements com.haulmont.cuba.gui.components.IFrame {
    private ResourceBundle resourceBundle;

    public IFrame() {
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public <T extends Window> T openWindow(String screenId, WindowManager.OpenType openType, Map<String, Object> params) {
        ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(screenId);
        return App.getInstance().getScreenManager().<T>openWindow(screenInfo, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String screenId, Object item, WindowManager.OpenType openType, Map<String, Object> params) {
        ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(screenId);
        return App.getInstance().getScreenManager().<T>openEditor(screenInfo, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String screenId, Object item, WindowManager.OpenType openType) {
        ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(screenId);
        return App.getInstance().getScreenManager().<T>openEditor(screenInfo, item, openType);
    }

    public <T extends Window> T openWindow(String screenId, WindowManager.OpenType openType) {
        ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(screenId);
        return App.getInstance().getScreenManager().<T>openWindow(screenInfo, openType);
    }

    public <T extends Window> T openLookup(String screenId, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(screenId);
        return App.getInstance().getScreenManager().<T>openLookup(screenInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String screenId, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        ScreenInfo screenInfo = App.getInstance().getScreenConfig().getScreenInfo(screenId);
        return App.getInstance().getScreenManager().<T>openLookup(screenInfo, handler, openType);
    }
}
