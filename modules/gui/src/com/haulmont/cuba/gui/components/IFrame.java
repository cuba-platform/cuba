/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:53:35
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Represents a reusable part of a window.
 * Having its own XML descriptor, but can be instantiated only inside a {@link Window}.
 * Includes functionality for work with datasources and other windows.
 */
public interface IFrame
        extends ExpandingLayout,
                Component.BelongToFrame,
                Component.Spacing,
                Component.Margin,
                Component.ActionsHolder
{
    String NAME = "iframe";

    /**
     * @return Full component path with '.' delimeter
     */
    String getFullId();

    WindowContext getContext();

    void setContext(WindowContext ctx);

    DsContext getDsContext();

    void setDsContext(DsContext dsContext);

    /**
     * @return Message pack associated with the frame
     */
    String getMessagesPack();

    /**
     * Set message pack for this frame
     * @param name Message pack name
     */
    void setMessagesPack(String name);

    /**
     * @param key Message key
     * @return Message from message pack assigned by {@link #setMessagesPack(String)} or from XML descriptor
     */
    String getMessage(String key);

    void registerComponent(Component component);

    /** Check validity by invoking validators on all components which support them */
    boolean isValid();

    /** Check validity by invoking validators on all components which support them */
    void validate() throws ValidationException;

    /**
     * These parameters will be used for a next modal dialog.<br>
     * Parameters reset to default values after opening of each modal dialog.
     */
    DialogParams getDialogParams();

    /**
     * Open window specifying additional parameters
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param openType    how to open the screen
     * @param params      parameters which are accessible inside screen controller
     * @return created window
     */
    <T extends Window> T openWindow(
            String windowAlias, WindowManager.OpenType openType, Map<String, Object> params);

    /**
     * Open window
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openWindow(
            String windowAlias, WindowManager.OpenType openType);

    /**
     * Open editor specifying additional parameters
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters which are accessible inside screen controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    <T extends Window> T openEditor(
            String windowAlias, Entity item,
            WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs);

    /**
     * Open editor specifying additional parameters
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters which are accessible inside screen controller
     * @return created window
     */
    <T extends Window> T openEditor(
            String windowAlias, Entity item,
            WindowManager.OpenType openType, Map<String, Object> params);

    /**
     * Open editor
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    <T extends Window> T openEditor(
            String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs);

    /**
     * Open editor
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openEditor(
            String windowAlias, Entity item, WindowManager.OpenType openType);

    /**
     * Open lookup window specifying additional parameters
     *
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters which are accessible inside screen controller
     * @return created window
     */
    <T extends Window> T openLookup(
            String windowAlias, Window.Lookup.Handler handler,
            WindowManager.OpenType openType, Map<String, Object> params);

    /**
     * Open lookup window
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openLookup(
            String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType);

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @return              frame's controller instance
     */
    <T extends IFrame> T openFrame(@Nullable Component parent, String windowAlias);

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @param params        parameters to be passed into the frame's controller <code>init</code> method
     * @return              frame's controller instance
     */
    <T extends IFrame> T openFrame(@Nullable Component parent, String windowAlias, Map<String, Object> params);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum MessageType {
        CONFIRMATION,
        WARNING
    }

    void showMessageDialog(String title, String message, MessageType messageType);

    void showOptionDialog(String title, String message, MessageType messageType, Action[] actions);
    void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions);

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
