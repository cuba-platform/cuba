/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WindowDelegate {

    protected Window window;
    protected WindowManager windowManager;
    protected Window wrapper;

    public WindowDelegate(Window window, WindowManager windowManager) {
        this.window = window;
        this.windowManager = windowManager;
    }

    public Window wrapBy(Class<Window> wrapperClass) {
        try {
            Constructor<?> constructor;
            try {
                constructor = wrapperClass.getConstructor(Window.class);
            } catch (NoSuchMethodException e) {
                constructor = wrapperClass.getConstructor(IFrame.class);
            }

            wrapper = (Window) constructor.newInstance(window);
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Window getWrapper() {
        return wrapper;
    }

    public Datasource getDatasource() {
        Datasource ds = null;
        Element element = ((Component.HasXmlDescriptor) window).getXmlDescriptor();
        String datasourceName = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasourceName)) {
            final DsContext context = window.getDsContext();
            if (context != null) {
                ds = context.get(datasourceName);
            }
        }
        if (ds == null)
            throw new IllegalStateException("Can't find main datasource");
        else
            return ds;
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openWindow(windowInfo, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openLookup(windowInfo, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openFrame(wrapper, parent, windowInfo);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openFrame(wrapper, parent, windowInfo, params);
    }
}
