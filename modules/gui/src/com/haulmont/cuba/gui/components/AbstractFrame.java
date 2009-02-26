/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 10:21:02
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

public class AbstractFrame implements IFrame, Component.Wrapper {
    protected IFrame frame;

    public AbstractFrame(IFrame frame) {
        this.frame = frame;
    }

    public String getId() {
        return frame.getId();
    }

    public void setId(String id) {
        frame.setId(id);
    }

    public void requestFocus() {
        frame.requestFocus();
    }

    public float getHeight() {
        return frame.getHeight();
    }

    public int getHeightUnits() {
        return frame.getHeightUnits();
    }

    public void setHeight(String height) {
        frame.setHeight(height);
    }

    public float getWidth() {
        return frame.getWidth();
    }

    public int getWidthUnits() {
        return frame.getWidthUnits();
    }

    public void setWidth(String width) {
        frame.setWidth(width);
    }

    public Alignment getAlignment() {
        return frame.getAlignment();
    }

    public void setAlignment(Alignment alignment) {
        frame.setAlignment(alignment);
    }

    public void add(Component component) {
        frame.add(component);
    }

    public void remove(Component component) {
        frame.remove(component);
    }

    public <T extends Component> T getOwnComponent(String id) {
        return frame.<T>getOwnComponent(id); 
    }

    public <T extends Component> T getComponent(String id) {
        return frame.<T>getComponent(id);
    }

    public <T> T getComponent() {
        return (T) frame;
    }

    public void expand(Component component, String height, String width) {
        frame.expand(component, height, width);
    }

    public DsContext getDsContext() {
        return frame.getDsContext();
    }

    public void setDsContext(DsContext dsContext) {
        frame.setDsContext(dsContext);
    }

    public ResourceBundle getResourceBundle() {
        return frame.getResourceBundle();
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        frame.setResourceBundle(resourceBundle);
    }

    public <T extends Window> T openWindow(String descriptor, WindowManager.OpenType openType, Map<String, Object> params) {
        return frame.<T>openWindow(descriptor, openType, params);
    }

    public <T extends Window> T openWindow(String screenId, WindowManager.OpenType openType) {
        return frame.<T>openWindow(screenId, openType);
    }

    public <T extends Window> T openEditor(String screenId, Object item, WindowManager.OpenType openType, Map<String, Object> params) {
        return frame.<T>openEditor(screenId, item, openType, params);
    }

    public <T extends Window> T openEditor(String descriptor, Object item, WindowManager.OpenType openType) {
        return frame.<T>openEditor(descriptor, item, openType, Collections.<String, Object>emptyMap());
    }

    public <T extends Window> T openLookup(
            String descriptor, Window.Lookup.Handler handler,
                WindowManager.OpenType openType, Map<String, Object> params)
    {
        return frame.<T>openLookup(descriptor, handler, openType, params);
    }

    public <T extends Window> T openLookup(String descriptor, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return frame.<T>openLookup(descriptor, handler, openType, Collections.<String, Object>emptyMap());
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
        frame.showMessageDialog(title, message, messageType);
    }

    public Action showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        return frame.showOptionDialog(title, message, messageType, actions);
    }

    public boolean close(String actionId) {
        if (frame instanceof Window) {
            return ((Window) frame).close(actionId);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public <A extends IFrame> A getFrame() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFrame(IFrame frame) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
