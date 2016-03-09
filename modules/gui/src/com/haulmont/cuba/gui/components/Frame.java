/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Represents a reusable part of a screen.
 * <p/> Has its own XML descriptor, but can be instantiated only inside a {@link Window}.
 * Includes functionality for work with datasources and other windows.
 *
 * @author abramov
 */
public interface Frame
        extends ExpandingLayout,
                Component.OrderedContainer,
                Component.BelongToFrame,
                Component.Spacing,
                Component.Margin,
                Component.ActionsHolder {

    /** XML element name used to show a frame in an enclosing screen. */
    String NAME = "frame";

    /**
     * @return current frame context
     */
    FrameContext getContext();

    /** INTERNAL. Don't call from application code. */
    void setContext(FrameContext ctx);

    /**
     * @return {@link DsContext} of the current frame or window
     */
    DsContext getDsContext();

    /** INTERNAL. Don't call from application code. */
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

    /** INTERNAL. Don't call from application code. */
    void registerComponent(Component component);

    /** INTERNAL. Don't call from application code. */
    void unregisterComponent(Component component);

    /** INTERNAL. Don't call from application code. */
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
     *
     * @deprecated Use {@link WindowManager.OpenType}
     */
    @Deprecated
    DialogParams getDialogParams();

    /**
     * Open a simple screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    Window openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params);

    /**
     * Open a simple screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param openType    how to open the screen
     * @return created window
     */
    Window openWindow(String windowAlias, WindowManager.OpenType openType);

    /**
     * Open an edit screen for entity instance.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @return created window
     */
    Window.Editor openEditor(Entity item, WindowManager.OpenType openType);

    /**
     * Open an edit screen for entity instance.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    Window.Editor openEditor(Entity item, WindowManager.OpenType openType,
                             Map<String, Object> params);

    /**
     * Open an edit screen for entity instance.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    Window.Editor openEditor(Entity item, WindowManager.OpenType openType,
                             Map<String, Object> params, Datasource parentDs);

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
    Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
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
    Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
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
    Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @return created window
     */
    Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType);

    /**
     * Open a lookup screen.
     *
     * @param entityClass required class of entity
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType);

    /**
     * Open a lookup screen.
     *
     * @param entityClass required class of entity
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType,
                             Map<String, Object> params);

    /**
     * Open a lookup screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters to pass to <code>init()</code> method of the screen's controller
     * @return created window
     */
    Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType,
                                    Map<String, Object> params);

    /**
     * Open a lookup screen.
     *
     * @param windowAlias screen ID as defined in <code>screens.xml</code>
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType);

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this
     * frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @return              frame's controller instance
     */
    Frame openFrame(@Nullable Component parent, String windowAlias);

    /**
     * Load a frame registered in <code>screens.xml</code> and optionally show it inside a parent component of this
     * frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in <code>screens.xml</code>
     * @param params        parameters to be passed into the frame's controller <code>init</code> method
     * @return              frame's controller instance
     */
    Frame openFrame(@Nullable Component parent, String windowAlias, Map<String, Object> params);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Message dialog type.
     */
    final class MessageType {
        /** Confirmation with plain text message */
        public static final MessageType CONFIRMATION = new MessageType(MessageMode.CONFIRMATION, false);
        /** Confirmation with HTML message */
        public static final MessageType CONFIRMATION_HTML = new MessageType(MessageMode.CONFIRMATION_HTML, false);
        /** Warning with plain text message */
        public static final MessageType WARNING = new MessageType(MessageMode.WARNING, false);
        /** Warning with HTML message */
        public static final MessageType WARNING_HTML = new MessageType(MessageMode.WARNING_HTML, false);

        private MessageMode messageMode;
        private boolean mutable = true;

        public MessageType(MessageMode messageMode) {
            this.messageMode = messageMode;
        }

        private MessageType(MessageMode messageMode, boolean mutable) {
            this.messageMode = messageMode;
            this.mutable = mutable;
        }

        private Integer width;
        private Boolean modal;

        public MessageMode getMessageMode() {
            return messageMode;
        }

        public Integer getWidth() {
            return width;
        }

        public MessageType width(Integer width) {
            MessageType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public MessageType setWidth(Integer width) {
            MessageType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public Boolean getModal() {
            return modal;
        }

        public MessageType setModal(Boolean modal) {
            MessageType instance = getMutableInstance();

            instance.modal = modal;
            return instance;
        }

        public MessageType modal(Boolean modal) {
            MessageType instance = getMutableInstance();

            instance.modal = modal;
            return instance;
        }

        private MessageType getMutableInstance() {
            if (!mutable) {
                return copy();
            }

            return this;
        }

        public static boolean isHTML(MessageType type) {
            return MessageMode.isHTML(type.messageMode);
        }

        public MessageType copy() {
            MessageType copy = new MessageType(messageMode);

            copy.setWidth(width);

            return copy;
        }

        public static MessageType valueOf(String messageTypeString) {
            Preconditions.checkNotNullArgument(messageTypeString, "messageTypeString should not be null");

            switch (messageTypeString) {
                case "CONFIRMATION":
                    return CONFIRMATION;

                case "CONFIRMATION_HTML":
                    return CONFIRMATION_HTML;

                case "WARNING":
                    return WARNING;

                case "WARNING_HTML":
                    return WARNING_HTML;

                default:
                    throw new IllegalArgumentException("Unable to parse OpenType");
            }
        }
    }

    enum MessageMode {
        /** Confirmation with plain text message */
        CONFIRMATION,
        /** Confirmation with HTML message */
        CONFIRMATION_HTML,
        /** Warning with plain text message */
        WARNING,
        /** Warning with HTML message */
        WARNING_HTML;

        public static boolean isHTML(MessageMode type) {
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
     * @param actions     list of actions that represent options. For standard options consider use of
     *                    {@link DialogAction} instances.
     */
    void showOptionDialog(String title, String message, MessageType messageType, List<Action> actions);

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
     * Supports line breaks (<code>\n</code>) for non HTML type.
     *
     * @param caption notification text
     * @param type    defines how to display the notification.
     *                Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    void showNotification(String caption, NotificationType type);

    /**
     * Show notification with caption and description. <br/>
     * Supports line breaks (<code>\n</code>) for non HTML type.
     *
     * @param caption     notification text
     * @param description notification description
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

    interface Wrapper {
        Frame getWrappedFrame();
    }
}