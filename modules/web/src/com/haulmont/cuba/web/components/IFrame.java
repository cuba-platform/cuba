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

    public <T extends Window> T openWindow(String descriptor, WindowManager.OpenType openType, Map params) {
        return App.getInstance().getScreenManager().<T>openWindow(descriptor, openType, params);
    }

    public <T extends Window> T openWindow(Class aclass, WindowManager.OpenType openType, Map params) {
        return App.getInstance().getScreenManager().<T>openWindow(aclass, openType, params);
    }

    public <T extends Window> T openWindow(String descriptor, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openWindow(descriptor, openType);
    }

    public <T extends Window> T openWindow(Class aclass, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openWindow(aclass, openType);
    }
}
