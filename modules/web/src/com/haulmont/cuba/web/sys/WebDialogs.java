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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.exception.ExceptionDialog;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.haulmont.cuba.web.widgets.CubaWindow;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.setClickShortcut;
import static com.haulmont.cuba.web.gui.components.WebWrapperUtils.*;

public class WebDialogs implements Dialogs {

    protected AppUI ui;

    @Inject
    protected Messages messages;
    @Inject
    protected BackgroundWorker backgroundWorker;
    @Inject
    protected IconResolver iconResolver;
    @Inject
    protected Icons icons;
    @Inject
    protected ClientConfig clientConfig;
    protected ScreenBuilders screenBuilders;

    public WebDialogs(AppUI ui) {
        this.ui = ui;
    }

    @Inject
    public void setScreenBuilders(ScreenBuilders screenBuilders) {
        this.screenBuilders = screenBuilders;
    }

    @Override
    public OptionDialogBuilder createOptionDialog() {
        backgroundWorker.checkUIAccess();

        return new OptionDialogBuilderImpl();
    }

    @Override
    public OptionDialogBuilder createOptionDialog(MessageType messageType) {
        return createOptionDialog()
                .withType(messageType);
    }

    @Override
    public MessageDialogBuilder createMessageDialog() {
        backgroundWorker.checkUIAccess();

        return new MessageDialogBuilderImpl();
    }

    @Override
    public MessageDialogBuilder createMessageDialog(MessageType messageType) {
        return createMessageDialog()
                .withType(messageType);
    }

    @Override
    public ExceptionDialogBuilder createExceptionDialog() {
        backgroundWorker.checkUIAccess();

        return new ExceptionDialogBuilderImpl();
    }

    @Override
    public InputDialogBuilder createInputDialog(FrameOwner owner) {
        return new InputDialogBuilderImpl(owner);
    }

    public CubaButton createButton(Action action) {
        CubaButton button = new CubaButton();

        if (action instanceof DialogAction) {
            DialogAction.Type type = ((DialogAction) action).getType();

            button.setCaption(messages.getMainMessage(type.getMsgKey()));
            String iconPath = icons.get(type.getIconKey());
            button.setIcon(iconResolver.getIconResource(iconPath));
        }

        button.setEnabled(action.isEnabled());

        if (StringUtils.isNotEmpty(action.getCaption())) {
            button.setCaption(action.getCaption());
        }
        if (StringUtils.isNotEmpty(action.getDescription())) {
            button.setDescription(action.getDescription());
        }
        if (StringUtils.isNotEmpty(action.getIcon())) {
            button.setIcon(iconResolver.getIconResource(action.getIcon()));
        }

        return button;
    }

    public class OptionDialogBuilderImpl implements OptionDialogBuilder {

        protected CubaWindow window;
        protected CubaLabel messageLabel;
        protected VerticalLayout layout;
        protected HorizontalLayout buttonsContainer;

        protected MessageType type = MessageType.CONFIRMATION;

        protected Action[] actions;

        public OptionDialogBuilderImpl() {
            window = new CubaWindow();

            window.setModal(true);
            window.setClosable(false);
            window.setResizable(false);

            messageLabel = new CubaLabel();
            messageLabel.setWidth(100, Sizeable.Unit.PERCENTAGE);

            layout = new VerticalLayout();
            layout.setStyleName("c-app-option-dialog");
            layout.setMargin(false);
            layout.setSpacing(true);

            buttonsContainer = new HorizontalLayout();
            buttonsContainer.setMargin(false);
            buttonsContainer.setSpacing(true);

            layout.addComponent(messageLabel);
            layout.addComponent(buttonsContainer);

            layout.setExpandRatio(messageLabel, 1);
            layout.setComponentAlignment(buttonsContainer, Alignment.BOTTOM_RIGHT);

            window.setContent(layout);

            ThemeConstants theme = ui.getApp().getThemeConstants();
            window.setWidth(theme.get("cuba.web.WebWindowManager.optionDialog.width"));
        }

        @Override
        public OptionDialogBuilder withCaption(String caption) {
            window.setCaption(caption);
            return this;
        }

        @Override
        public String getCaption() {
            return window.getCaption();
        }

        @Override
        public OptionDialogBuilder withMessage(String message) {
            messageLabel.setValue(message);
            return this;
        }

        @Override
        public String getMessage() {
            return messageLabel.getValue();
        }

        @Override
        public OptionDialogBuilder withType(MessageType type) {
            this.type = type;
            return this;
        }

        @Override
        public MessageType getType() {
            return type;
        }

        @Override
        public OptionDialogBuilder withContentMode(ContentMode contentMode) {
            messageLabel.setContentMode(toVaadinContentMode(contentMode));
            return this;
        }

        @Override
        public ContentMode getContentMode() {
            return toContentMode(messageLabel.getContentMode());
        }

        @Override
        public OptionDialogBuilder withActions(Action... actions) {
            this.actions = actions;
            return this;
        }

        @Override
        public Action[] getActions() {
            return actions;
        }

        @Override
        public OptionDialogBuilder withWidth(String width) {
            window.setWidth(width);

            if (getWidth() < 0) {
                messageLabel.setWidthUndefined();
                layout.setWidthUndefined();
            } else {
                messageLabel.setWidth(100, Sizeable.Unit.PERCENTAGE);
                layout.setWidth(100, Sizeable.Unit.PERCENTAGE);
            }

            return this;
        }

        @Override
        public float getWidth() {
            return window.getWidth();
        }

        @Override
        public SizeUnit getWidthSizeUnit() {
            return toSizeUnit(window.getWidthUnits());
        }

        @Override
        public OptionDialogBuilder withHeight(String height) {
            window.setHeight(height);

            if (getHeight() < 0) {
                messageLabel.setHeightUndefined();
                layout.setExpandRatio(messageLabel, 0);
                layout.setHeightUndefined();
            } else {
                messageLabel.setHeight(100, Sizeable.Unit.PERCENTAGE);
                layout.setHeight(100, Sizeable.Unit.PERCENTAGE);
                layout.setExpandRatio(messageLabel, 1);
            }

            return this;
        }

        @Override
        public float getHeight() {
            return window.getHeight();
        }

        @Override
        public SizeUnit getHeightSizeUnit() {
            return toSizeUnit(window.getHeightUnits());
        }

        @Override
        public boolean isMaximized() {
            return window.getWindowMode() == WindowMode.MAXIMIZED;
        }

        @Override
        public OptionDialogBuilder withMaximized(boolean maximized) {
            window.setWindowMode(maximized ? WindowMode.MAXIMIZED : WindowMode.NORMAL);
            return this;
        }

        @Override
        public OptionDialogBuilder maximized() {
            withMaximized(true);
            return this;
        }

        @Override
        public OptionDialogBuilder withStyleName(String styleName) {
            window.setStyleName(styleName);
            return this;
        }

        @Override
        public String getStyleName() {
            return window.getStyleName();
        }

        @Override
        public void show() {
            // find OK / CANCEL shortcut actions
            DialogAction firstCommitAction = findFirstActionWithType(actions,
                    EnumSet.of(DialogAction.Type.YES, DialogAction.Type.OK)
            );
            DialogAction firstDeclineAction = findFirstActionWithType(actions,
                    EnumSet.of(DialogAction.Type.CANCEL, DialogAction.Type.CLOSE, DialogAction.Type.NO)
            );

            boolean hasPrimaryAction = false;
            for (Action action : actions) {
                CubaButton button = createButton(action);
                button.setClickHandler(mouseEventDetails -> {
                    try {
                        action.actionPerform(ui.getTopLevelWindow());
                    } finally {
                        ui.removeWindow(window);
                    }
                });

                if (action instanceof AbstractAction
                        && ((AbstractAction) action).isPrimary()) {
                    button.addStyleName(WebButton.PRIMARY_ACTION_STYLENAME);
                    button.focus();

                    hasPrimaryAction = true;
                }

                buttonsContainer.addComponent(button);

                if (ui.isTestMode()) {
                    button.setCubaId("optionDialog_" + action.getId());
                }
                if (ui.isPerformanceTestMode()) {
                    button.setId(ui.getTestIdManager().getTestId("optionDialog_" + action.getId()));
                }

                if (action == firstCommitAction) {
                    setClickShortcut(button, clientConfig.getCommitShortcut());
                } else if (action == firstDeclineAction) {
                    setClickShortcut(button, clientConfig.getCloseShortcut());
                }
            }

            if (!hasPrimaryAction && actions.length > 0) {
                ((com.vaadin.ui.Component.Focusable) buttonsContainer.getComponent(0)).focus();
            }

            if (ui.isTestMode()) {
                window.setCubaId("optionDialog");
                messageLabel.setCubaId("optionDialogLabel");
            }
            if (ui.isPerformanceTestMode()) {
                window.setId(ui.getTestIdManager().getTestId("optionDialog"));
            }

            ui.addWindow(window);
            window.center();
        }

        @Nullable
        protected DialogAction findFirstActionWithType(Action[] actions, EnumSet<DialogAction.Type> types) {
            for (Action action : actions) {
                if (action instanceof DialogAction) {
                    DialogAction.Type actionType = ((DialogAction) action).getType();
                    if (types.contains(actionType)) {
                        return (DialogAction) action;
                    }
                }
            }
            return null;
        }
    }

    public class MessageDialogBuilderImpl implements MessageDialogBuilder {
        protected CubaWindow window;
        protected CubaLabel messageLabel;
        protected VerticalLayout layout;
        protected CubaButton okButton;

        protected MessageType type = MessageType.CONFIRMATION;

        public MessageDialogBuilderImpl() {
            window = new CubaWindow();

            window.setModal(true);
            window.setResizable(false);

            layout = new VerticalLayout();
            layout.setStyleName("c-app-message-dialog");
            layout.setMargin(false);
            layout.setSpacing(true);

            messageLabel = new CubaLabel();
            messageLabel.setStyleName("c-app-message-dialog-text");
            messageLabel.setWidth(100, Sizeable.Unit.PERCENTAGE);

            DialogAction action = new DialogAction(DialogAction.Type.OK);
            okButton = createButton(action);
            okButton.setClickHandler(me -> {
                try {
                    action.actionPerform(ui.getTopLevelWindow());
                } finally {
                    ui.removeWindow(window);
                }
            });

            layout.addComponent(messageLabel);

            layout.addComponent(okButton);
            layout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);

            window.setContent(layout);

            ThemeConstants theme = ui.getApp().getThemeConstants();
            window.setWidth(theme.get("cuba.web.WebWindowManager.messageDialog.width"));
        }

        @Override
        public MessageDialogBuilder withCaption(String caption) {
            window.setCaption(caption);
            return this;
        }

        @Override
        public String getCaption() {
            return window.getCaption();
        }

        @Override
        public MessageDialogBuilder withMessage(String message) {
            messageLabel.setValue(message);
            return this;
        }

        @Override
        public String getMessage() {
            return messageLabel.getValue();
        }

        @Override
        public MessageDialogBuilder withType(MessageType type) {
            this.type = type;
            return this;
        }

        @Override
        public MessageType getType() {
            return type;
        }

        @Override
        public MessageDialogBuilder withContentMode(ContentMode contentMode) {
            messageLabel.setContentMode(toVaadinContentMode(contentMode));
            return this;
        }

        @Override
        public ContentMode getContentMode() {
            return toContentMode(messageLabel.getContentMode());
        }

        @Override
        public MessageDialogBuilder withWidth(String width) {
            window.setWidth(width);

            if (getWidth() < 0) {
                messageLabel.setWidthUndefined();
                layout.setWidthUndefined();
            } else {
                messageLabel.setWidth(100, Sizeable.Unit.PERCENTAGE);
                layout.setWidth(100, Sizeable.Unit.PERCENTAGE);
            }

            return this;
        }

        @Override
        public float getWidth() {
            return window.getWidth();
        }

        @Override
        public SizeUnit getWidthSizeUnit() {
            return toSizeUnit(window.getWidthUnits());
        }

        @Override
        public MessageDialogBuilder withHeight(String height) {
            window.setHeight(height);

            if (getHeight() < 0) {
                messageLabel.setHeightUndefined();
                layout.setExpandRatio(messageLabel, 0);
                layout.setHeightUndefined();
            } else {
                messageLabel.setHeight(100, Sizeable.Unit.PERCENTAGE);
                layout.setHeight(100, Sizeable.Unit.PERCENTAGE);
                layout.setExpandRatio(messageLabel, 1);
            }

            return this;
        }

        @Override
        public float getHeight() {
            return window.getHeight();
        }

        @Override
        public SizeUnit getHeightSizeUnit() {
            return toSizeUnit(window.getHeightUnits());
        }

        @Override
        public boolean isModal() {
            return window.isModal();
        }

        @Override
        public MessageDialogBuilder withModal(boolean modal) {
            window.setModal(modal);
            return this;
        }

        @Override
        public MessageDialogBuilder modal() {
            return withModal(true);
        }

        @Override
        public boolean isMaximized() {
            return window.getWindowMode() == WindowMode.MAXIMIZED;
        }

        @Override
        public MessageDialogBuilder withMaximized(boolean maximized) {
            window.setWindowMode(maximized ? WindowMode.MAXIMIZED : WindowMode.NORMAL);
            return this;
        }

        @Override
        public MessageDialogBuilder maximized() {
            return withMaximized(true);
        }

        @Override
        public boolean isCloseOnClickOutside() {
            return window.getCloseOnClickOutside();
        }

        @Override
        public MessageDialogBuilder withCloseOnClickOutside(boolean closeOnClickOutside) {
            window.setCloseOnClickOutside(closeOnClickOutside);
            return this;
        }

        @Override
        public MessageDialogBuilder closeOnClickOutside() {
            return withCloseOnClickOutside(true);
        }

        @Override
        public MessageDialogBuilder withStyleName(String styleName) {
            window.setStyleName(styleName);
            return this;
        }

        @Override
        public String getStyleName() {
            return window.getStyleName();
        }

        @Override
        public void show() {
            initShortcuts();

            if (ui.isTestMode()) {
                window.setCubaId("messageDialog");
                messageLabel.setCubaId("messageDialogLabel");
                okButton.setCubaId("messageDialogOk");
            }

            if (ui.isPerformanceTestMode()) {
                window.setId(ui.getTestIdManager().getTestId("messageDialog"));
            }

            if (!window.isModal()) {
                for (com.vaadin.ui.Window w : ui.getWindows()) {
                    if (w.isModal()) {
                        window.setModal(true);
                        break;
                    }
                }
            }

            ui.addWindow(window);
            window.center();
            window.bringToFront();

            okButton.focus();
        }

        protected void initShortcuts() {
            String closeShortcut = clientConfig.getCloseShortcut();
            KeyCombination closeCombination = KeyCombination.create(closeShortcut);

            window.addAction(
                    new ShortcutListenerDelegate("Esc",
                            closeCombination.getKey().getCode(),
                            KeyCombination.Modifier.codes(closeCombination.getModifiers())
                    ).withHandler((sender, target) ->
                            window.close()
                    ));

            window.addAction(new ShortcutListenerDelegate("Enter", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null)
                    .withHandler((sender, target) ->
                            window.close()
                    ));
        }
    }

    public class ExceptionDialogBuilderImpl implements ExceptionDialogBuilder {
        protected String message;
        protected String caption;
        protected Throwable throwable;

        @Override
        public ExceptionDialogBuilder withThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        @Override
        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public ExceptionDialogBuilder withCaption(String caption) {
            this.caption = caption;
            return this;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public ExceptionDialogBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public void show() {
            if (throwable == null) {
                throw new IllegalStateException("throwable should not be null");
            }

            Throwable rootCause = ExceptionUtils.getRootCause(throwable);
            if (rootCause == null) {
                rootCause = throwable;
            }

            ExceptionDialog dialog = new ExceptionDialog(rootCause, caption, message);
            for (com.vaadin.ui.Window window : ui.getWindows()) {
                if (window.isModal()) {
                    dialog.setModal(true);
                    break;
                }
            }
            ui.addWindow(dialog);
            dialog.focus();
        }
    }

    public class InputDialogBuilderImpl implements InputDialogBuilder {

        protected InputDialog inputDialog;

        public InputDialogBuilderImpl(FrameOwner owner) {
            inputDialog = screenBuilders.screen(owner)
                    .withScreenClass(InputDialog.class)
                    .withOpenMode(OpenMode.DIALOG)
                    .build();
        }

        @Override
        public InputDialogBuilder withParameter(InputParameter parameter) {
            inputDialog.setParameter(parameter);
            return this;
        }

        @Override
        public InputDialogBuilder withParameters(InputParameter... parameters) {
            inputDialog.setParameters(parameters);
            return this;
        }

        public Collection<InputParameter> getParameters() {
            return inputDialog.getParameters();
        }

        @Override
        public InputDialogBuilder withCloseListener(Consumer<InputDialog.InputDialogCloseEvent> listener) {
            inputDialog.setCloseListener(listener);
            return this;
        }

        public Consumer<InputDialog.InputDialogCloseEvent> getCloseListener() {
            return inputDialog.getCloseListener();
        }

        @Override
        public InputDialogBuilder withActions(InputDialogAction... actions) {
            inputDialog.setActions(actions);
            return this;
        }

        public Collection<Action> getActions() {
            return inputDialog.getActions();
        }

        @Override
        public InputDialogBuilder withActions(DialogActions actions) {
            inputDialog.setDialogActions(actions);
            return this;
        }

        @Override
        public InputDialogBuilder withActions(DialogActions actions, Consumer<InputDialog.InputDialogResult> resultHandler) {
            inputDialog.setDialogActions(actions);
            inputDialog.setResultHandler(resultHandler);
            return this;
        }

        public DialogActions getDialogActions() {
            return inputDialog.getDialogActions();
        }

        @Nullable
        public Consumer<InputDialog.InputDialogResult> getResultHandler() {
            return inputDialog.getResultHandler();
        }

        @Override
        public InputDialogBuilder withValidator(Function<InputDialog.ValidationContext, ValidationErrors> validator) {
            inputDialog.setValidator(validator);
            return this;
        }

        public Function<InputDialog.ValidationContext, ValidationErrors> getValidator() {
            return inputDialog.getValidator();
        }

        @Override
        public InputDialogBuilder withCaption(String caption) {
            inputDialog.getDialogWindow().setCaption(caption);
            return this;
        }

        @Override
        public InputDialogBuilder withWidth(String width) {
            inputDialog.getDialogWindow().setDialogWidth(width);
            return this;
        }

        public float getWidth() {
            return inputDialog.getDialogWindow().getDialogWidth();
        }

        @Override
        public SizeUnit getWidthSizeUnit() {
            return inputDialog.getDialogWindow().getWidthSizeUnit();
        }

        @Override
        public InputDialogBuilder withHeight(String height) {
            inputDialog.getDialogWindow().setDialogHeight(height);
            return this;
        }

        public float getHeight() {
            return inputDialog.getDialogWindow().getDialogHeight();
        }

        @Override
        public SizeUnit getHeightSizeUnit() {
            return inputDialog.getDialogWindow().getHeightSizeUnit();
        }

        @Nullable
        public String getCaption() {
            return inputDialog.getDialogWindow().getCaption();
        }

        @Override
        public InputDialog show() {
            InputDialog dialog = build();
            dialog.show();
            return dialog;
        }

        @Override
        public InputDialog build() {
            return inputDialog;
        }
    }
}