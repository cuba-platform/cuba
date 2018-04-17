/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
 * <br> Has its own XML descriptor, but can be instantiated only inside a {@link Window}.
 * Includes functionality for work with datasources and other windows.
 */
public interface Frame
        extends ExpandingLayout,
                OrderedContainer,
                Component.BelongToFrame,
        HasSpacing,
        HasMargin,
                ActionsHolder,
                Component.HasIcon,
                Component.HasCaption {

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
     * Check validity by invoking validators on specified components which support them
     * and show validation result notification.
     * @return true if the validation was successful, false if there were any problems
     */
    boolean validate(List<Validatable> fields);

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification.
     * @return true if the validation was successful, false if there were any problems
     */
    boolean validateAll();

    /**
     * @return {@link DialogParams} that will be used for opening next window in modal mode.
     * <br> If called in {@code init()}
     * method of a screen, which is being opened in {@link WindowManager.OpenType#DIALOG} mode, affects the current
     * screen itself.
     *
     * @deprecated Use {@link WindowManager.OpenType} or {@link Window#getDialogOptions()} from screen controller
     */
    @Deprecated
    DialogParams getDialogParams();

    /**
     * Open a simple screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    Window openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params);

    /**
     * Open a simple screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
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
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    Window.Editor openEditor(Entity item, WindowManager.OpenType openType,
                             Map<String, Object> params);

    /**
     * Open an edit screen for entity instance.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    Window.Editor openEditor(Entity item, WindowManager.OpenType openType,
                             Map<String, Object> params, Datasource parentDs);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                             Map<String, Object> params, Datasource parentDs);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                             Map<String, Object> params);

    /**
     * Open an edit screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
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
     * @param windowAlias screen ID as defined in {@code screens.xml}
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
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType,
                             Map<String, Object> params);

    /**
     * Open a lookup screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType,
                                    Map<String, Object> params);

    /**
     * Open a lookup screen.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType);

    /**
     * Load a frame registered in {@code screens.xml} and optionally show it inside a parent component of this
     * frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in {@code screens.xml}
     * @return              frame's controller instance
     */
    Frame openFrame(@Nullable Component parent, String windowAlias);

    /**
     * Load a frame registered in {@code screens.xml} and optionally show it inside a parent component of this
     * frame.
     * @param parent        if specified, all parent's subcomponents will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in {@code screens.xml}
     * @param params        parameters to be passed into the frame's controller {@code init} method
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

        private Float width;
        private SizeUnit widthUnit;
        private Boolean modal;
        private Boolean closeOnClickOutside;
        private Boolean maximized;

        public MessageMode getMessageMode() {
            return messageMode;
        }

        public Float getWidth() {
            return width;
        }

        /**
         * @deprecated Use {@link #width(Float)} instead.
         */
        @Deprecated
        public MessageType width(Integer width) {
            return width(width.floatValue());
        }

        /**
         * @deprecated Use {@link #setWidth(Float)} instead.
         */
        @Deprecated
        public MessageType setWidth(Integer width) {
            return setWidth(width.floatValue());
        }

        public MessageType width(Float width) {
            MessageType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public MessageType setWidth(Float width) {
            MessageType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public MessageType width(String width) {
            return setWidth(width);
        }

        public MessageType setWidth(String width) {
            MessageType instance = getMutableInstance();

            SizeWithUnit size = SizeWithUnit.parseStringSize(width);

            instance.width = size.getSize();
            instance.widthUnit = size.getUnit();
            return instance;
        }

        public SizeUnit getWidthUnit() {
            return widthUnit;
        }

        public MessageType setWidthUnit(SizeUnit widthUnit) {
            MessageType instance = getMutableInstance();
            instance.widthUnit = widthUnit;
            return instance;
        }

        public MessageType widthAuto() {
            MessageType instance = getMutableInstance();

            instance.width = -1.0f;
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

        /**
         * @return true if a window can be closed by click on outside window area
         */
        public Boolean getCloseOnClickOutside() {
            return closeOnClickOutside;
        }

        /**
         * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
         * It works when a window has a modal mode.
         */
        public MessageType setCloseOnClickOutside(boolean closeOnClickOutside) {
            MessageType instance = getMutableInstance();

            instance.closeOnClickOutside = closeOnClickOutside;
            return instance;
        }

        /**
         * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
         * It works when a window has a modal mode.
         */
        public MessageType closeOnClickOutside(boolean closeOnClickOutside) {
            MessageType instance = getMutableInstance();

            instance.closeOnClickOutside = closeOnClickOutside;
            return instance;
        }

        /**
         * @return true if a window is maximized across the screen.
         */
        public Boolean getMaximized() {
            return maximized;
        }

        /**
         * Set maximized to true if a window should be maximized across the screen.
         */
        public MessageType maximized(Boolean maximized) {
            MessageType instance = getMutableInstance();

            instance.maximized = maximized;
            return instance;
        }

        /**
         * Set maximized to true if a window should be maximized across the screen.
         */
        public MessageType setMaximized(Boolean maximized) {
            MessageType instance = getMutableInstance();

            instance.maximized = maximized;
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
     * Show message dialog with title and message. <br>
     * Message supports line breaks ({@code \n}).
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    void showMessageDialog(String title, String message, MessageType messageType);

    /**
     * Show options dialog with title and message. <br>
     * Message supports line breaks ({@code \n}).
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     * @param actions     array of actions that represent options. For standard options consider use of
     *                    {@link DialogAction} instances.
     */
    void showOptionDialog(String title, String message, MessageType messageType, Action[] actions);

    /**
     * Show options dialog with title and message. <br>
     * Message supports line breaks ({@code \n}).
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
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
     * Show notification with {@link NotificationType#HUMANIZED}. <br>
     * Supports line breaks ({@code \n}).
     *
     * @param caption notification text
     */
    void showNotification(String caption);

    /**
     * Show notification. <br>
     * Supports line breaks ({@code \n}) for non HTML type.
     *
     * @param caption notification text
     * @param type    defines how to display the notification.
     *                Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    void showNotification(String caption, NotificationType type);

    /**
     * Show notification with caption and description. <br>
     * Supports line breaks ({@code \n}) for non HTML type.
     *
     * @param caption     notification text
     * @param description notification description
     * @param type        defines how to display the notification.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    void showNotification(String caption, String description, NotificationType type);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Open a web page in browser.
     *
     * @param url    URL of the page
     * @param params optional parameters.
     *               <br>The following parameters are recognized by Web client:
     *               - {@code target} - String value used as the target name in a
     *               window.open call in the client. This means that special values such as
     *               "_blank", "_self", "_top", "_parent" have special meaning. If not specified, "_blank" is used. <br>
     *               - {@code width} - Integer value specifying the width of the browser window in pixels<br>
     *               - {@code height} - Integer value specifying the height of the browser window in pixels<br>
     *               - {@code border} - String value specifying the border style of the window of the browser window.
     *               Possible values are "DEFAULT", "MINIMAL", "NONE".<br>
     *               <p>
     *               Desktop client doesn't support any parameters and just ignores them.
     */
    void showWebPage(String url, @Nullable Map<String, Object> params);

    interface Wrapper {
        Frame getWrappedFrame();
    }
}