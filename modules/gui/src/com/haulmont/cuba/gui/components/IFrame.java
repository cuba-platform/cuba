/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Represents a reusable part of a screen.
 * <p/> Has its own XML descriptor, but can be instantiated only inside a {@link Window}.
 * Includes functionality for work with datasources and other windows.
 *
 * @author abramov
 * @version $Id$
 */
public interface IFrame
        extends ExpandingLayout,
                Component.BelongToFrame,
                Component.Spacing,
                Component.Margin,
                Component.ActionsHolder {

    /** XML element name used to show a frame in an enclosing screen. */
    String NAME = "iframe";

    /**
     * @return current frame context
     */
    FrameContext getContext();

    /** For internal use only. Don't call from application code. */
    void setContext(FrameContext ctx);

    /**
     * @return {@link DsContext} of the current frame or window
     */
    DsContext getDsContext();

    /** For internal use only. Don't call from application code. */
    void setDsContext(DsContext dsContext);

    /**
     * @return the message pack associated with the frame, usually in XML descriptor
     */
    String getMessagesPack();

    /**
     * Set message pack for this frame.
     * @param name message pack name
     */
    void setMessagesPack(String name);

    /** For internal use only. Don't call from application code. */
    void registerComponent(Component component);

    /** For internal use only. Don't call from application code. */
    @Nullable
    Component getRegisteredComponent(String id);

    /**
     * Check validity by invoking validators on all components which support them.
     * @return true if all components are in valid state
     */
    boolean isValid();

    /**
     * Check validity by invoking validators on all components which support them.
     * @throws ValidationException if some components are currently in invalid state
     */
    void validate() throws ValidationException;

    /**
     * @return {@link DialogParams} that will be used for opening next window in modal mode.
     * <p/> If called in <code>init()</code>
     * method of a screen, which is being opened in {@link WindowManager.OpenType#DIALOG} mode, affects the current
     * screen itself.
     */
    DialogParams getDialogParams();

    /**
     * Open a simple screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params);

    /**
     * Open a simple screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                                    Map<String, Object> params, Datasource parentDs);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                                    Map<String, Object> params);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                                    Datasource parentDs);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType);

    /**
     * Open a lookup screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType,
                                    Map<String, Object> params);

    /**
     * Open a lookup screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType);

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this
     * frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @return              frame's controller instance
     */
    <T extends IFrame> T openFrame(@Nullable Component parent, String windowAlias);

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this
     * frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @param params        parameters to be passed into the frame's controller <code>init</code> method
     * @return              frame's controller instance
     */
    <T extends IFrame> T openFrame(@Nullable Component parent, String windowAlias, Map<String, Object> params);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Message dialog type.
     */
    enum MessageType {
        /** Confirmation with plain text message */
        CONFIRMATION,
        /** Confirmation with HTML message */
        CONFIRMATION_HTML,
        /** Warning with plain text message */
        WARNING,
        /** Warning with HTML message */
        WARNING_HTML;

        public static boolean isHTML(MessageType type) {
            return type == CONFIRMATION_HTML || type == WARNING_HTML;
        }
    }

    /**
     * Show message dialog with title and message. <br/>
     * Message supports line breaks (<code>\n</code>).
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    void showMessageDialog(String title, String message, MessageType messageType);

    /**
     * Show options dialog with title and message. <br/>
     * Message supports line breaks (<code>\n</code>).
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     * @param actions     array of actions that represent options. For standard options consider use of
     *                    {@link DialogAction} instances.
     */
    void showOptionDialog(String title, String message, MessageType messageType, Action[] actions);

    /**
     * Show options dialog with title and message. <br/>
     * Message supports line breaks (<code>\n</code>).
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     * @param actions     array of actions that represent options. For standard options consider use of
     *                    {@link DialogAction} instances.
     */
    void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Popup notification type.
     */
    enum NotificationType {
        /** Tray popup with plain text message */
        TRAY,
        /** Tray popup with HTML message */
        TRAY_HTML,
        /** Standard popup with plain text message */
        HUMANIZED,
        /** Standard popup with HTML message */
        HUMANIZED_HTML,
        /** Warning popup with plain text message */
        WARNING,
        /** Warning popup with HTML message */
        WARNING_HTML,
        /** Error popup with plain text message */
        ERROR,
        /** Error popup with HTML message */
        ERROR_HTML;

        public static boolean isHTML(NotificationType type) {
            return type == TRAY_HTML || type == HUMANIZED_HTML || type == WARNING_HTML || type == ERROR_HTML;
        }
    }

    /**
     * Show notification. <br/>
     * Supports line breaks (<code>\n</code>).
     *
     * @param caption text
     * @param type    defines how to display the notification.
     *                Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    void showNotification(String caption, NotificationType type);

    /**
     * Show notification with caption description. <br/>
     * Supports line breaks (<code>\n</code>).
     *
     * @param caption     caption
     * @param description text
     * @param type        defines how to display the notification.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    void showNotification(String caption, String description, NotificationType type);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Open a web page in browser.
     * @param url       URL of the page
     * @param params    optional parameters.
     * <p/>The following parameters are recognized by Web client:
     * <ul>
     * <li/> <code>target</code> - String value used as the target name in a
     * window.open call in the client. This means that special values such as
     * "_blank", "_self", "_top", "_parent" have special meaning. If not specified, "_blank" is used.
     * <li/> <code>width</code> - Integer value specifying the width of the browser window in pixels
     * <li/> <code>height</code> - Integer value specifying the height of the browser window in pixels
     * <li/> <code>border</code> - String value specifying the border style of the window of the browser window.
     * Possible values are "DEFAULT", "MINIMAL", "NONE".
     * </ul>
     * Desktop client doesn't support any parameters and just ignores them.
     */
    void showWebPage(String url, @Nullable Map<String, Object> params);
}
