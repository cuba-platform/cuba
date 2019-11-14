/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction.InputDialogActionPerformed;
import com.haulmont.cuba.gui.screen.FrameOwner;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility dialogs API.
 */
public interface Dialogs {

    /**
     * Creates option dialog builder.
     * <br>
     * Example of showing an option dialog:
     * <pre>{@code
     * dialogs.createOptionDialog()
     *         .withType(MessageType.CONFIRMATION)
     *         .withCaption("Question")
     *         .withMessage("Do you want to discard data?")
     *         .withActions(
     *                 new DialogAction(DialogAction.Type.YES).withHandler(e -> {
     *                     // YES option selected
     *                 }),
     *                 new DialogAction(DialogAction.Type.NO).withHandler(e -> {
     *                     // NO option selected
     *                 })
     *         )
     *         .show();
     * }</pre>
     *
     * @return builder
     */
    OptionDialogBuilder createOptionDialog();

    /**
     * Creates option dialog builder with the passed message type.
     * <br>
     * Example of showing an option dialog:
     * <pre>{@code
     * dialogs.createOptionDialog()
     *         .withType(MessageType.CONFIRMATION)
     *         .withCaption("Question")
     *         .withMessage("Do you want to save data?")
     *         .withActions(
     *                 new DialogAction(DialogAction.Type.YES).withHandler(e -> {
     *                     // YES option selected
     *                 }),
     *                 new DialogAction(DialogAction.Type.NO)
     *                         .withCaption("I am not sure")
     *                         .withHandler(e -> {
     *                     // NO option selected
     *                 }),
     *                 new DialogAction(DialogAction.Type.CANCEL).withHandler(e -> {
     *                     // CANCEL option selected
     *                 })
     *         )
     *         .show();
     * }</pre>
     *
     * @return builder
     */
    OptionDialogBuilder createOptionDialog(MessageType messageType);

    /**
     * Creates message dialog builder.
     * <br>
     * Example of showing a message dialog:
     * <pre>{@code
     * dialogs.createMessageDialog()
     *         .withType(MessageType.WARNING)
     *         .withCaption("Alert")
     *         .withMessage("Report has been saved")
     *         .show();
     * }</pre>
     *
     * @return builder
     */
    MessageDialogBuilder createMessageDialog();

    /**
     * Creates message dialog builder with the passed message type.
     * <br>
     * Example of showing a message dialog:
     * <pre>{@code
     * dialogs.createMessageDialog(MessageType.WARNING)
     *         .withCaption("Alert")
     *         .withMessage("Report has been saved")
     *         .show();
     * }</pre>
     *
     * @return builder
     */
    MessageDialogBuilder createMessageDialog(MessageType messageType);

    /**
     * Creates exception dialog builder.
     * <br>
     * Example of showing an exception dialog:
     * <pre>{@code
     * dialogs.createExceptionDialog()
     *         .withCaption("Alert")
     *         .withMessage("Report has been saved")
     *         .withThrowable(exception)
     *         .show();
     * }</pre>
     *
     * @return builder
     */
    ExceptionDialogBuilder createExceptionDialog();

    /**
     * Creates input dialog builder.
     * <p>
     * Example of showing an input dialog:
     * <pre>{@code
     * dialogs.createInputDialog(this)
     *         .withParameters(
     *                 stringParameter("name").withCaption("Name"),
     *                 intParameter("count").withCaption("Count"))
     *         .withActions(DialogActions.OK_CANCEL)
     *         .withCloseListener(closeEvent ->
     *                 notifications.create(Notifications.NotificationType.TRAY)
     *                         .withCaption("Dialog is closed")
     *                         .show())
     *         .withCaption("Goods")
     *         .show();
     * }</pre>
     *
     * @param owner origin screen from input dialog is invoked
     * @return builder
     */
    InputDialogBuilder createInputDialog(FrameOwner owner);

    /**
     * Builder of dialog with option buttons.
     */
    interface OptionDialogBuilder
            extends DialogBuilder<OptionDialogBuilder>,
                    HasType<OptionDialogBuilder>,
                    HasMessage<OptionDialogBuilder>,
                    HasContentMode<OptionDialogBuilder>,
                    HasMaximized<OptionDialogBuilder>,
                    HasStyleName<OptionDialogBuilder> {
        /**
         * Sets dialog actions.
         *
         * @param actions actions
         * @return builder
         */
        OptionDialogBuilder withActions(Action... actions);
        /**
         * @return dialog actions
         */
        Action[] getActions();
        /**
         * Shows the dialog.
         */
        void show();
    }

    /**
     * Builder of information dialog.
     */
    interface MessageDialogBuilder
            extends DialogBuilder<MessageDialogBuilder>,
                    HasType<MessageDialogBuilder>,
                    HasMessage<MessageDialogBuilder>,
                    HasContentMode<MessageDialogBuilder>,
                    HasModal<MessageDialogBuilder>,
                    HasMaximized<MessageDialogBuilder>,
                    HasStyleName<MessageDialogBuilder> {

        /**
         * @return true if window can be closed by click outside of window content (by modality curtain)
         */
        boolean isCloseOnClickOutside();
        /**
         * Sets if window can be closed by click outside of window content (by modality curtain).
         *
         * @param closeOnClickOutside true if window to be closed by click outside of window content (by modality curtain)
         * @return builder
         */
        MessageDialogBuilder withCloseOnClickOutside(boolean closeOnClickOutside);
        /**
         * Enables closeOnClickOutside mode for window, so window can be closed by click outside of window content
         * (by modality curtain).
         *
         * @return builder
         */
        MessageDialogBuilder closeOnClickOutside();

        /**
         * Shows the dialog.
         */
        void show();
    }

    /**
     * Builder of unhandled exception dialog.
     */
    interface ExceptionDialogBuilder extends
            HasCaption<ExceptionDialogBuilder>,
            HasMessage<ExceptionDialogBuilder> {

        /**
         * Sets exception object.
         *
         * @param throwable throwable
         * @return builder
         */
        ExceptionDialogBuilder withThrowable(Throwable throwable);
        /**
         * @return throwable
         */
        Throwable getThrowable();

        /**
         * Shows the dialog.
         */
        void show();
    }

    /**
     * Builder for dialogs with inputs.
     */
    interface InputDialogBuilder extends DialogBuilder<InputDialogBuilder> {

        /**
         * Adds input parameter to the dialog. InputParameter describes field which will be used in the input dialog.
         * <p>
         * Example:
         * <pre>{@code
         *  dialogs.createInputDialog(this)
         *         .withParameter(
         *                 entityParameter("userField", User.class)
         *                         .withCaption("User field")
         *                         .withRequired(true)
         *         )
         *         .show();
         * } </pre>
         *
         * @param parameter input parameter that will be added to the dialog
         * @return builder
         * @see InputParameter#entityParameter(String, Class)
         */
        InputDialogBuilder withParameter(InputParameter parameter);

        /**
         * Sets input parameters. InputParameter describes field which will be used in the input dialog.
         * <p>
         * Example:
         * <pre>{@code
         *  dialogs.createInputDialog(this)
         *          .withParameters(
         *                  stringParameter("nameField")
         *                          .withCaption("Name field caption")
         *                          .withDefaultValue("default value"),
         *                  intParameter("countField")
         *                          .withCaption("Count field caption")
         *                          .withRequired(true))
         *          .show();
         *  } </pre>
         *
         * @param parameters input parameters
         * @return builder
         * @see InputParameter#stringParameter(String)
         * @see InputParameter#intParameter(String)
         */
        InputDialogBuilder withParameters(InputParameter... parameters);

        /**
         * Add close listener to the dialog. See close actions for {@link DialogActions} in {@link InputDialog}.
         *
         * @param listener close listener to add
         * @return builder
         */
        InputDialogBuilder withCloseListener(Consumer<InputDialog.InputDialogCloseEvent> listener);

        /**
         * Sets dialog actions. {@link InputDialogAction} provides access to input dialog in {@link InputDialogActionPerformed}
         * where it is possible to get values form the fields and implement logic to close dialog.
         * <p>
         * Note, if there is no actions are set input dialog will use {@link DialogActions#OK_CANCEL} by default.
         * </p>
         * Example:
         * <pre>{@code
         *  dialogs.createInputDialog(this)
         *         .withCaption("Dialog caption")
         *         .withParameter(parameter("nameField").withCaption("Name"))
         *         .withActions(
         *                 action("okAction")
         *                         .withCaption("OK")
         *                         .withIcon(CubaIcon.DIALOG_OK)
         *                         .withHandler(event -> {
         *                             InputDialog inputDialog = event.getInputDialog();
         *                             String name = inputDialog.getValue("nameField");
         *                             // do logic
         *                             inputDialog.close(InputDialog.INPUT_DIALOG_OK_ACTION);
         *                         }),
         *                 action("cancelAction")
         *                         .withCaption("Cancel")
         *                         .withIcon(CubaIcon.DIALOG_CANCEL)
         *                         .withValidationRequired(false)
         *                         .withHandler(event -> {
         *                             InputDialog inputDialog = event.getInputDialog();
         *                             inputDialog.close(InputDialog.INPUT_DIALOG_CANCEL_ACTION);
         *                         }))
         *         .show();
         * }
         * </pre>
         *
         * @param actions actions
         * @return builder
         * @see InputDialogAction#action(String)
         */
        InputDialogBuilder withActions(InputDialogAction... actions);

        /**
         * Sets predefined dialog actions. "OK" and "YES" actions always check fields validation before close the dialog.
         * By default if there is no actions are set input dialog will use {@link DialogActions#OK_CANCEL}.
         *
         * @param actions actions
         * @return builder
         */
        InputDialogBuilder withActions(DialogActions actions);

        /**
         * Sets dialog actions and result handler. "OK" and "YES" actions always check fields validation before close
         * the dialog. Handler is invoked after close event and can be used instead of
         * {@link #withCloseListener(Consumer)}.
         * Example
         * <pre>{@code
         *  dialogs.createInputDialog(this)
         *         .withCaption("Dialog caption")
         *         .withParameter(parameter("nameField").withCaption("Name"))
         *         .withActions(DialogActions.OK_CANCEL, result -> {
         *             switch (result.getCloseActionType()) {
         *                 case OK:
         *                     String name = result.getValue("nameField");
         *                     // do logic
         *                     break;
         *                 case CANCEL:
         *                     // do logic
         *                     break;
         *             }
         *         })
         *         .show();
         * } </pre>
         *
         * @param actions       dialog actions
         * @param resultHandler result handler
         * @return builder
         */
        InputDialogBuilder withActions(DialogActions actions, Consumer<InputDialog.InputDialogResult> resultHandler);

        /**
         * Sets additional handler for field validation. It receives input dialog context and must return {@link ValidationErrors}
         * instance. Returned validation errors will be shown with another errors from fields.
         * Example
         * <pre>{@code
         *  dialogs.createInputDialog(this)
         *         .withParameters(
         *                 stringParameter("phoneField").withCaption("Phone"),
         *                 stringParameter("addressField").withCaption("Address"))
         *         .withValidator(context -> {
         *             String phone = context.getValue("phoneField");
         *             String address = context.getValue("addressField");
         *             if (Strings.isNullOrEmpty(phone) && Strings.isNullOrEmpty(address)) {
         *                 return ValidationErrors.of("Phone or Address should be filled");
         *             }
         *             return ValidationErrors.none();
         *         })
         *         .show();
         *  }</pre>
         *
         * @param validator validator
         * @return builder
         */
        InputDialogBuilder withValidator(Function<InputDialog.ValidationContext, ValidationErrors> validator);

        /**
         * Builds the input dialog.
         *
         * @return input dialog
         */
        InputDialog build();

        InputDialog show();
    }

    /**
     * Base class for all Dialog Builders.
     * @param <T> return type of fluent API methods
     */
    interface DialogBuilder<T extends DialogBuilder> extends HasCaption<T>, HasSize<T> {
    }

    /**
     * Marker interface for Dialog Builders that have caption.
     * @param <T> return type of fluent API methods
     */
    interface HasCaption<T> {
        /**
         * Sets caption text.
         *
         * @param caption caption text
         * @return builder
         */
        T withCaption(String caption);
        /**
         * @return caption text
         */
        String getCaption();
    }

    /**
     * Marker interface for Dialog Builders that have message.
     * @param <T> return type of fluent API methods
     */
    interface HasMessage<T> {
        /**
         * Sets message text.
         *
         * @param message message text
         * @return builder
         */
        T withMessage(String message);
        /**
         * @return message text
         */
        String getMessage();
    }

    /**
     * Marker interface for Dialog Builders that have content mode setting.
     * @param <T> return type of fluent API methods
     */
    interface HasContentMode<T> {

        /**
         * Sets content mode for message, e.g. {@link ContentMode#TEXT}, {@link ContentMode#HTML}
         * or {@link ContentMode#PREFORMATTED}.
         *
         * @param contentMode content mode
         * @return builder
         */
        T withContentMode(ContentMode contentMode);
        /**
         * @return message content mode
         */
        ContentMode getContentMode();
    }

    /**
     * Marker interface for Dialog Builders that have size setting.
     * @param <T> return type of fluent API methods
     */
    interface HasSize<T> {
        /**
         * Sets dialog width.
         *
         * @param width width
         * @return builder
         */
        T withWidth(String width);
        /**
         * @return dialog width value
         */
        float getWidth();
        /**
         * @return dialog width unit
         */
        SizeUnit getWidthSizeUnit();

        /**
         * Sets dialog height.
         *
         * @param height height
         * @return builder
         */
        T withHeight(String height);
        /**
         *
         * @return dialog height value
         */
        float getHeight();
        /**
         *
         * @return dialog height unit
         */
        SizeUnit getHeightSizeUnit();
    }

    /**
     * Marker interface for Dialog Builders that have stylename setting.
     * @param <T> return type of fluent API methods
     */
    interface HasStyleName<T> {
        /**
         * Sets custom CSS style name for dialog.
         *
         * @param styleName style name
         * @return builder
         */
        T withStyleName(String styleName);
        /**
         * @return custom style name
         */
        String getStyleName();
    }

    /**
     * Marker interface for Dialog Builders that have modal setting.
     * @param <T> return type of fluent API methods
     */
    interface HasModal<T> {
        /**
         * @return true if window is modal
         */
        boolean isModal();
        /**
         * Sets dialog modality. When a modal window is open, components outside that window cannot be accessed.
         *
         * @param modal modal flag
         * @return builder
         */
        T withModal(boolean modal);
        /**
         * Enables modal mode for dialog.
         *
         * @return builder
         */
        T modal();
    }

    /**
     * Marker interface for Dialog Builders that have maximized setting.
     * @param <T> return type of fluent API methods
     */
    interface HasMaximized<T> {
        /**
         * Sets whether dialog should be maximized.
         *
         * @param maximized maximized flag
         * @return builder
         */
        T withMaximized(boolean maximized);
        /**
         * Enables dialog maximized mode.
         *
         * @return builder
         */
        T maximized();
        /**
         * @return true if dialog will be maximized
         */
        boolean isMaximized();
    }

    /**
     * Marker interface for Dialog Builders that have message type setting.
     * @param <T> return type of fluent API methods
     */
    interface HasType<T> {
        /**
         * Sets message type, e.g. {@link MessageType#CONFIRMATION} or {@link MessageType#WARNING}.
         *
         * @param type message type
         * @return builder
         */
        T withType(MessageType type);
        /**
         * @return message type
         */
        MessageType getType();
    }

    /**
     * Message type of a dialog.
     */
    enum MessageType {
        /**
         * Confirmation message.
         */
        CONFIRMATION,
        /**
         * Warning message.
         */
        WARNING
    }
}
