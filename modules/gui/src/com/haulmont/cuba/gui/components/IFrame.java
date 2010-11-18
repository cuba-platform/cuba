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
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.WindowContext;

import java.util.Map;

/**
 * Represents a reusable part of a window.
 * Having its own XML descriptor, but can be instantiated only inside a {@link Window}.
 * Includes functionality for work with datasources and other windows.
 */
public interface IFrame extends Layout, Component.BelongToFrame, Component.HasLayout, Layout.Spacing, Layout.Margin {

    WindowContext getContext();

    void setContext(WindowContext ctx);

    DsContext getDsContext();

    void setDsContext(DsContext dsContext);

    /**
     * Get message pack associated with the frame
     */
    String getMessagesPack();

    /**
     * Set message pack for this frame
     */
    void setMessagesPack(String name);

    /**
     * Get message from message pack assigned by {@link #setMessagesPack(String)} or from XML descriptor
     */
    String getMessage(String key);

    void registerComponent(Component component);

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
     * @param windowAlias screen ID as defined in <code>screen-config.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openLookup(
            String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType);

    <T extends IFrame> T openFrame(
            Component parent,
            String windowAlias
    );

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
