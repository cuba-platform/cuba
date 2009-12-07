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
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collections;
import java.util.Map;
import java.util.Collection;

public class AbstractFrame implements IFrame, Component.Wrapper {
    protected IFrame frame;
    private String styleName;

    public AbstractFrame(IFrame frame) {
        this.frame = frame;
    }

    public String getId() {
        return frame.getId();
    }

    public void setId(String id) {
        frame.setId(id);
    }

    public String getDebugId() {
        return frame.getDebugId();
    }

    public void setDebugId(String id) {
        frame.setDebugId(id);
    }

    public boolean isEnabled() {
        return frame.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        frame.setEnabled(enabled);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
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

    public Collection<Component> getOwnComponents() {
        return frame.getOwnComponents();
    }

    public Collection<Component> getComponents() {
        return frame.getComponents();
    }

    public <T> T getComponent() {
        return (T) frame;
    }

    public Object getComposition() {
        return frame;
    }

    public void expand(Component component, String height, String width) {
        frame.expand(component, height, width);
    }

    public WindowContext getContext() {
        return frame.getContext();
    }

    public void setContext(WindowContext ctx) {
        frame.setContext(ctx);
    }

    public DsContext getDsContext() {
        return frame.getDsContext();
    }

    public void setDsContext(DsContext dsContext) {
        frame.setDsContext(dsContext);
    }

    public String getMessagesPack() {
        return frame.getMessagesPack();
    }

    public void setMessagesPack(String name) {
        frame.setMessagesPack(name);
    }

    public String getMessage(String key) {
        String msgPack = getMessagesPack();
        if (msgPack == null)
            throw new IllegalStateException("MessagePack is not set");
        return MessageProvider.getMessage(msgPack, key);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return frame.<T>openWindow(windowAlias, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        return frame.<T>openWindow(windowAlias, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return frame.<T>openEditor(windowAlias, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return frame.<T>openEditor(windowAlias, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return frame.<T>openEditor(windowAlias, item, openType, Collections.<String, Object>emptyMap(), parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return frame.<T>openEditor(windowAlias, item, openType, Collections.<String, Object>emptyMap());
    }

    public <T extends Window> T openLookup(
            String windowAlias, Window.Lookup.Handler handler,
                WindowManager.OpenType openType, Map<String, Object> params)
    {
        return frame.<T>openLookup(windowAlias, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return frame.<T>openLookup(windowAlias, handler, openType, Collections.<String, Object>emptyMap());
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
        frame.showMessageDialog(title, message, messageType);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        frame.showOptionDialog(title, message, messageType, actions);
    }

    public void showNotification(String caption, NotificationType type) {
        frame.showNotification(caption, type);
    }

    public void showNotification(String caption, String description, NotificationType type) {
        frame.showNotification(caption, description, type);
    }

    public boolean close(String actionId) {
        if (frame instanceof Window) {
            return ((Window) frame).close(actionId);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean close(String actionId, boolean force) {
        if (frame instanceof Window) {
            return ((Window) frame).close(actionId, force);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public <A extends IFrame> A getFrame() {
        return (A) this.frame.getFrame();
    }

    public void setFrame(IFrame frame) {
        this.frame.setFrame(frame);
    }

    public String getStyleName() {
        return styleName;  
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void expandLayout(boolean expandLayout) {
        frame.expandLayout(expandLayout);
    }
}
