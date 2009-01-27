/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 19:02:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.Layout;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Window implements com.haulmont.cuba.gui.components.Window, Component.Wrapper
{
    private String id;

    private Map<String, Component> componentByIds = new HashMap<String, Component>();
    private ResourceBundle resourceBundle;

    protected ExpandLayout layout;

    public Window() {
        layout = new ExpandLayout(ExpandLayout.ORIENTATION_VERTICAL);
        layout.setMargin(true);
        layout.setSpacing(true);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(String descriptor, WindowManager.OpenType openType, Map params) {
        return App.getInstance().getScreenManager().<T>openWindow(descriptor, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(Class aclass, WindowManager.OpenType openType, Map params) {
        return App.getInstance().getScreenManager().<T>openWindow(aclass, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(String descriptor, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openWindow(descriptor, openType);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openWindow(Class aclass, WindowManager.OpenType openType) {
        return App.getInstance().getScreenManager().<T>openWindow(aclass, openType);
    }

    public void add(Component component) {
        layout.addComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
    }

    public void remove(Component component) {
        layout.removeComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
    }

    public boolean onClose() {
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void requestFocus() {
    }

    public int getHeight() {
        return layout.getHeight();
    }

    public int getHeightUnits() {
        return layout.getHeightUnits();
    }

    public void setHeight(String height) {
        layout.setHeight(height);
    }

    public int getWidth() {
        return layout.getWidth();
    }

    public int getWidthUnits() {
        return layout.getWidthUnits();
    }

    public void setWidth(String width) {
        layout.setWidth(width);
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

    public int getVerticalAlIlignment() {
        return Layout.AlignmentHandler.ALIGNMENT_VERTICAL_CENTER;
    }

    public void setVerticalAlIlignment(int verticalAlIlignment) {}

    public int getHorizontalAlIlignment() {
        return Layout.AlignmentHandler.ALIGNMENT_HORIZONTAL_CENTER;
    }

    public void setHorizontalAlIlignment(int horizontalAlIlignment) {}

    public void expand(Component component, String height, String width) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T getComponent() {
        return (T) layout;
    }

    public boolean close() {
        App.getInstance().getScreenManager().closeScreen();
        return true;
    }
}
