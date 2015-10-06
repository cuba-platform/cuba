/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Base class for frame controllers.
 *
 * @author abramov
 * @version $Id$
 */
public class AbstractFrame implements Frame, Component.Wrapper, Component.OrderedContainer {

    protected Frame frame;
    private String styleName;
    private Object _companion;

    private Component parent;

    @Inject
    protected Messages messages;

    public AbstractFrame() {
    }

    /** INTERNAL. Don't call from application code. */
    public void setWrappedFrame(Frame frame) {
        this.frame = frame;
    }

    /**
     * Called by the framework after creation of all components and before showing the screen.
     * <p/> Override this method and put initialization logic here.
     * @param params parameters passed from caller's code, usually from
     * {@link #openWindow(String, com.haulmont.cuba.gui.WindowManager.OpenType)} and similar methods, or set in
     *               <code>screens.xml</code> for this registered screen
     */
    public void init(Map<String, Object> params) {
    }

    @Override
    public String getId() {
        return frame.getId();
    }

    @Override
    public void setId(String id) {
        String currentId = getId();

        if (!ObjectUtils.equals(currentId, id)) {
            if (getFrame() != null) {
                getFrame().unregisterComponent(this);
            }
        }

        frame.setId(id);

        // register this wrapper instead of underlying frame
        if (!ObjectUtils.equals(currentId, id)) {
            if (getFrame() != null) {
                getFrame().registerComponent(this);
            }
        }
    }

    @Override
    public Component getParent() {
        return parent;
    }

    @Override
    public void setParent(Component parent) {
        this.parent = parent;
    }

    @Override
    public String getDebugId() {
        return frame.getDebugId();
    }

    @Override
    public void setDebugId(String id) {
        frame.setDebugId(id);
    }

    @Override
    public boolean isEnabled() {
        return frame.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        frame.setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    @Override
    public void requestFocus() {
        frame.requestFocus();
    }

    @Override
    public float getHeight() {
        return frame.getHeight();
    }

    @Override
    public int getHeightUnits() {
        return frame.getHeightUnits();
    }

    @Override
    public void setHeight(String height) {
        frame.setHeight(height);
    }

    @Override
    public float getWidth() {
        return frame.getWidth();
    }

    @Override
    public int getWidthUnits() {
        return frame.getWidthUnits();
    }

    @Override
    public void setWidth(String width) {
        frame.setWidth(width);
    }

    @Override
    public Alignment getAlignment() {
        return frame.getAlignment();
    }

    @Override
    public void setAlignment(Alignment alignment) {
        frame.setAlignment(alignment);
    }

    @Override
    public void add(Component component) {
        frame.add(component);
    }

    @Override
    public void remove(Component component) {
        frame.remove(component);
    }

    @Override
    public void removeAll() {
        frame.removeAll();
    }

    @Override
    public Component getOwnComponent(String id) {
        return frame.getOwnComponent(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return frame.getComponent(id);
    }

    @Nonnull
    @Override
    public Component getComponentNN(String id) {
        Component component = getComponent(id);
        if (component == null)
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return frame.getOwnComponents();
    }

    @Override
    public Collection<Component> getComponents() {
        return frame.getComponents();
    }

    @Override
    public Object getComponent() {
        return frame;
    }

    @Override
    public Object getComposition() {
        return frame;
    }

    @Override
    public void expand(Component component, String height, String width) {
        frame.expand(component, height, width);
    }

    @Override
    public void expand(Component component) {
        frame.expand(component);
    }

    @Override
    public boolean isExpanded(Component component) {
        return frame.isExpanded(component);
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.VERTICAL;
    }

    @Override
    public FrameContext getContext() {
        return frame.getContext();
    }

    @Override
    public void setContext(FrameContext ctx) {
        frame.setContext(ctx);
    }

    @Override
    public DsContext getDsContext() {
        return frame.getDsContext();
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        frame.setDsContext(dsContext);
    }

    @Override
    public String getMessagesPack() {
        return frame.getMessagesPack();
    }

    @Override
    public void setMessagesPack(String name) {
        frame.setMessagesPack(name);
    }

    /**
     * Get localized message from the message pack associated with this frame or window.
     * @param key   message key
     * @return      localized message
     * @see Messages#getMessage(String, String)
     */
    protected String getMessage(String key) {
        String msgPack = getMessagesPack();
        if (StringUtils.isEmpty(msgPack)) {
            throw new DevelopmentException("MessagePack is not set");
        }

        return messages.getMessage(msgPack, key);
    }

    /**
     * Get localized message from the message pack associated with this frame or window, and use it as a format
     * string for parameters provided.
     * @param key       message key
     * @param params    parameter values
     * @return          formatted string or the key in case of IllegalFormatException
     * @see Messages#formatMessage(String, String, Object...)
     */
    protected String formatMessage(String key, Object... params) {
        String msgPack = getMessagesPack();
        if (StringUtils.isEmpty(msgPack)) {
            throw new DevelopmentException("MessagePack is not set");
        }

        return messages.formatMessage(msgPack, key, params);
    }

    @Override
    public void registerComponent(Component component) {
        frame.registerComponent(component);
    }

    @Override
    public void unregisterComponent(Component component) {
        frame.unregisterComponent(component);
    }

    @Nullable
    @Override
    public Component getRegisteredComponent(String id) {
        return frame.getRegisteredComponent(id);
    }

    @Override
    public boolean isValid() {
        return frame.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        frame.validate();
    }

    @Override
    public DialogParams getDialogParams() {
        return frame.getDialogParams();
    }

    @Override
    public void add(Component childComponent, int index) {
        frame.add(childComponent, index);
    }

    @Override
    public int indexOf(Component component) {
        return frame.indexOf(component);
    }

    /**
     * @return a companion implementation, specific for the current client type
     */
    @Nullable
    public <T> T getCompanion() {
        //noinspection unchecked
        return (T) _companion;
    }

    /** INTERNAL. Don't call from application code. */
    public void setCompanion(Object companion) {
        this._companion = companion;
    }

    @Override
    public AbstractWindow openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return (AbstractWindow) frame.openWindow(windowAlias, openType, params);
    }

    @Override
    public AbstractWindow openWindow(String windowAlias, WindowManager.OpenType openType) {
        return (AbstractWindow) frame.openWindow(windowAlias, openType);
    }

    @Override
    public AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return (AbstractEditor) frame.openEditor(windowAlias, item, openType, params, parentDs);
    }

    @Override
    public AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return (AbstractEditor) frame.openEditor(windowAlias, item, openType, params);
    }

    @Override
    public AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return (AbstractEditor) frame.openEditor(windowAlias, item, openType, Collections.<String, Object>emptyMap(), parentDs);
    }

    @Override
    public AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return (AbstractEditor) frame.openEditor(windowAlias, item, openType, Collections.<String, Object>emptyMap());
    }

    @Override
    public AbstractLookup openLookup(
            String windowAlias, @Nullable Window.Lookup.Handler handler,
            WindowManager.OpenType openType, Map<String, Object> params) {
        return (AbstractLookup) frame.openLookup(windowAlias, handler, openType, params);
    }

    @Override
    public AbstractLookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return (AbstractLookup) frame.openLookup(windowAlias, handler, openType, Collections.<String, Object>emptyMap());
    }

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @return              frame's controller instance
     */
    @Override
    public AbstractFrame openFrame(@Nullable Component parent, String windowAlias) {
        return (AbstractFrame) frame.openFrame(parent, windowAlias);
    }

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @param params        parameters to be passed into the frame's controller <code>init</code> method
     * @return              frame's controller instance
     */
    @Override
    public AbstractFrame openFrame(@Nullable Component parent, String windowAlias, Map<String, Object> params) {
        return (AbstractFrame) frame.openFrame(parent, windowAlias, params);
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        frame.showMessageDialog(title, message, messageType);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        frame.showOptionDialog(title, message, messageType, actions);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
        frame.showOptionDialog(title, message, messageType, actions);
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        frame.showNotification(caption, type);
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        frame.showNotification(caption, description, type);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        frame.showWebPage(url, params);
    }

    @Override
    public Frame getFrame() {
        return this.frame.getFrame();
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame.setFrame(frame);
        // register this wrapper instead of underlying frame
        frame.registerComponent(this);
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public void setSpacing(boolean enabled) {
        frame.setSpacing(enabled);
    }

    @Override
    public void setMargin(boolean enable) {
        frame.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        frame.setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    @Override
    public void addAction(Action action) {
        frame.addAction(action);
    }

    @Override
    public void removeAction(@Nullable Action action) {
        frame.removeAction(action);
    }

    @Override
    public void removeAction(@Nullable String id) {
        frame.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        frame.removeAllActions();
    }

    @Override
    public Collection<Action> getActions() {
        return frame.getActions();
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        return frame.getAction(id);
    }

    @Nonnull
    @Override
    public Action getActionNN(String id) {
        Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }
}