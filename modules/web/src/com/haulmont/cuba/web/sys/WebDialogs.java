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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.AppUI;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EnumSet;

import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.setClickShortcut;
import static com.haulmont.cuba.web.gui.components.WebWrapperUtils.*;

@Component(Dialogs.NAME)
@Scope(UIScope.NAME)
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

    public WebDialogs(AppUI ui) {
        this.ui = ui;
    }

    @Override
    public OptionDialog createOptionDialog() {
        backgroundWorker.checkUIAccess();

        return new OptionDialogImpl();
    }

    @Override
    public MessageDialog createMessageDialog() {
        backgroundWorker.checkUIAccess();

        return new MessageDialogImpl();
    }

    @Override
    public ExceptionDialog createExceptionDialog() {
        backgroundWorker.checkUIAccess();

        return new ExceptionDialogImpl();
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
            button.addStyleName(WebButton.ICON_STYLE);
        }

        return button;
    }

    public class OptionDialogImpl implements OptionDialog {

        protected CubaWindow window;
        protected CubaLabel messageLabel;
        protected VerticalLayout layout;
        protected HorizontalLayout buttonsContainer;

        protected MessageType type = MessageType.CONFIRMATION;

        protected Action[] actions;

        public OptionDialogImpl() {
            this.window = new CubaWindow();

            window.setModal(true);
            window.setClosable(false);
            window.setResizable(false);

            this.messageLabel = new CubaLabel();

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
        public OptionDialog setCaption(String caption) {
            window.setCaption(caption);
            return this;
        }

        @Override
        public String getCaption() {
            return window.getCaption();
        }

        @Override
        public OptionDialog setMessage(String message) {
            messageLabel.setValue(message);
            return this;
        }

        @Override
        public String getMessage() {
            return messageLabel.getValue();
        }

        @Override
        public OptionDialog setType(MessageType type) {
            this.type = type;
            return this;
        }

        @Override
        public MessageType getType() {
            return type;
        }

        @Override
        public OptionDialog setContentMode(ContentMode contentMode) {
            messageLabel.setContentMode(toVaadinContentMode(contentMode));
            return this;
        }

        @Override
        public ContentMode getContentMode() {
            return toContentMode(messageLabel.getContentMode());
        }

        @Override
        public OptionDialog setActions(Action... actions) {
            this.actions = actions;
            return this;
        }

        @Override
        public Action[] getActions() {
            return actions;
        }

        @Override
        public OptionDialog setWidth(String width) {
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
        public OptionDialog setHeight(String height) {
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
        public OptionDialog setMaximized(boolean maximized) {
            window.setWindowMode(maximized ? WindowMode.MAXIMIZED : WindowMode.NORMAL);
            return this;
        }

        @Override
        public OptionDialog setStyleName(String styleName) {
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
                    button.addStyleName("c-primary-action");
                    button.focus();

                    hasPrimaryAction = true;
                }

                buttonsContainer.addComponent(button);

                if (ui.isTestMode()) {
                    button.setCubaId("optionDialog_" + action.getId());

                    // todo check if performance mode enabled
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
                window.setId(ui.getTestIdManager().getTestId("optionDialog"));

                messageLabel.setCubaId("optionDialogLabel");
            }

            ui.addWindow(window);
            window.center();
        }

        @Nullable
        protected DialogAction findFirstActionWithType(Action[] actions, EnumSet<DialogAction.Type> types) {
            for (DialogAction.Type type : types) {
                for (Action action : actions) {
                    if (action instanceof DialogAction && ((DialogAction) action).getType() == type) {
                        return (DialogAction) action;
                    }
                }
            }
            return null;
        }
    }

    public class MessageDialogImpl implements MessageDialog {
        protected CubaWindow window;
        protected CubaLabel messageLabel;
        protected VerticalLayout layout;
        protected CubaButton okButton;

        protected MessageType type = MessageType.CONFIRMATION;

        public MessageDialogImpl() {
            window = new CubaWindow();

            window.setModal(true);
            window.setResizable(false);

            this.messageLabel = new CubaLabel();

            layout = new VerticalLayout();
            layout.setStyleName("c-app-message-dialog");
            layout.setMargin(false);
            layout.setSpacing(true);

            DialogAction action = new DialogAction(DialogAction.Type.OK);
            okButton = createButton(action);
            okButton.setClickHandler(mouseEventDetails -> {
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
        public MessageDialog setCaption(String caption) {
            window.setCaption(caption);
            return this;
        }

        @Override
        public String getCaption() {
            return window.getCaption();
        }

        @Override
        public MessageDialog setMessage(String message) {
            messageLabel.setValue(message);
            return this;
        }

        @Override
        public String getMessage() {
            return messageLabel.getValue();
        }

        @Override
        public MessageDialog setType(MessageType type) {
            this.type = type;
            return this;
        }

        @Override
        public MessageType getType() {
            return type;
        }

        @Override
        public MessageDialog setContentMode(ContentMode contentMode) {
            messageLabel.setContentMode(toVaadinContentMode(contentMode));
            return this;
        }

        @Override
        public ContentMode getContentMode() {
            return toContentMode(messageLabel.getContentMode());
        }

        @Override
        public MessageDialog setWidth(String width) {
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
        public MessageDialog setHeight(String height) {
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
        public MessageDialog setModal(boolean modal) {
            window.setModal(modal);
            return this;
        }

        @Override
        public boolean isMaximized() {
            return window.getWindowMode() == WindowMode.MAXIMIZED;
        }

        @Override
        public MessageDialog setMaximized(boolean maximized) {
            window.setWindowMode(maximized ? WindowMode.MAXIMIZED : WindowMode.NORMAL);
            return this;
        }

        @Override
        public boolean isCloseOnClickOutside() {
            return window.getCloseOnClickOutside();
        }

        @Override
        public MessageDialog setCloseOnClickOutside(boolean closeOnClickOutside) {
            window.setCloseOnClickOutside(closeOnClickOutside);
            return this;
        }

        @Override
        public MessageDialog setStyleName(String styleName) {
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

                // todo check if performance mode enabled
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

    public class ExceptionDialogImpl implements ExceptionDialog {
        protected String message;
        protected String caption;
        protected Throwable throwable;

        @Override
        public ExceptionDialog setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        @Override
        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public ExceptionDialog setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public ExceptionDialog setMessage(String message) {
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

            com.haulmont.cuba.web.exception.ExceptionDialog dialog =
                    new com.haulmont.cuba.web.exception.ExceptionDialog(rootCause, caption, message);
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
}