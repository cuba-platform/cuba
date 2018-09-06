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
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.StandardCloseAction;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaWindow;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Component;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.cuba.web.gui.components.WebWrapperUtils.toSizeUnit;

public class WebDialogWindow extends WebWindow implements DialogWindow, InitializingBean {
    protected CubaWindow dialogWindow;

    protected BeanLocator beanLocator;

    public WebDialogWindow() {
        this.dialogWindow = new CubaWindow();
        this.dialogWindow.setStyleName("c-app-dialog-window");

        this.dialogWindow.setContent(component);

        this.dialogWindow.center();
        this.dialogWindow.setModal(true);

        this.dialogWindow.addPreCloseListener(this::onCloseButtonClick);

        // todo default size
    }

    @Override
    public void afterPropertiesSet() {
        setupDialogShortcuts();
        setupContextMenu();
    }

    protected void setupContextMenu() {
        dialogWindow.addContextActionHandler(new DialogWindowActionHandler());
    }

    protected void setupDialogShortcuts() {
        ClientConfig clientConfig = getClientConfig();

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        ShortcutListenerDelegate exitAction = new ShortcutListenerDelegate(
                "closeShortcutAction",
                closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())
        );

        exitAction.withHandler(this::onCloseShortcutTriggered);

        dialogWindow.addActionHandler(new com.vaadin.event.Action.Handler() {
            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new ShortcutAction[]{exitAction};
            }

            @Override
            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                if (action == exitAction) {
                    exitAction.handleAction(sender, target);
                }
            }
        });
    }

    protected ClientConfig getClientConfig() {
        Configuration configuration = beanLocator.get(Configuration.NAME);
        return configuration.getConfig(ClientConfig.class);
    }

    protected void onCloseButtonClick(CubaWindow.PreCloseEvent preCloseEvent) {
        preCloseEvent.setPreventClose(true);

        BeforeCloseWithCloseButtonEvent event = new BeforeCloseWithCloseButtonEvent(this);
        fireBeforeCloseWithCloseButton(event);

        if (!event.isClosePrevented()) {
            // user has clicked on X
            getFrameOwner().close(new StandardCloseAction(Window.CLOSE_ACTION_ID));
        }
    }

    protected void onCloseShortcutTriggered(Object sender, Object target) {
        if (this.isCloseable()) {
            BeforeCloseWithShortcutEvent event = new BeforeCloseWithShortcutEvent(this);
            fireBeforeCloseWithShortcut(event);

            if (!event.isClosePrevented()) {
                getFrameOwner().close(new StandardCloseAction(Window.CLOSE_ACTION_ID));
            }
        }
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);

        if (icon == null) {
            dialogWindow.setIcon(null);
        } else {
            IconResolver iconResolver = beanLocator.get(IconResolver.NAME);
            dialogWindow.setIcon(iconResolver.getIconResource(icon));
        }
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);

        this.dialogWindow.setCaption(caption);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);

        this.dialogWindow.setDescription(description);
    }

    @Override
    public Component getComposition() {
        return dialogWindow;
    }

    @Override
    public DialogOptions getDialogOptions() {
        if (dialogOptions == null) {
            dialogOptions = new WebDialogOptions();
        }
        return dialogOptions;
    }

    @Override
    public void setDialogWidth(String dialogWidth) {
        dialogWindow.setWidth(dialogWidth);
    }

    @Override
    public float getDialogWidth() {
        return dialogWindow.getWidth();
    }

    @Override
    public SizeUnit getDialogWidthUnit() {
        return toSizeUnit(dialogWindow.getWidthUnits());
    }

    @Override
    public void setDialogHeight(String dialogHeight) {
        dialogWindow.setHeight(dialogHeight);
    }

    @Override
    public float getDialogHeight() {
        return dialogWindow.getHeight();
    }

    @Override
    public SizeUnit getDialogHeightUnit() {
        return toSizeUnit(dialogWindow.getHeightUnits());
    }

    @Override
    public void setDialogStylename(String stylename) {
        dialogWindow.setStyleName(stylename);
    }

    @Override
    public String getDialogStylename() {
        return dialogWindow.getStyleName();
    }

    @Override
    public void setResizable(boolean resizable) {
        dialogWindow.setResizable(resizable);
    }

    @Override
    public boolean isResizable() {
        return dialogWindow.isResizable();
    }

    @Override
    public void setDraggable(boolean draggable) {
        dialogWindow.setDraggable(draggable);
    }

    @Override
    public boolean isDraggable() {
        return dialogWindow.isDraggable();
    }

    @Override
    public void setCloseable(boolean closeable) {
        super.setCloseable(closeable);

        dialogWindow.setClosable(closeable);
    }

    @Override
    public void setModal(boolean modal) {
        dialogWindow.setModal(modal);
    }

    @Override
    public boolean isModal() {
        return dialogWindow.isModal();
    }

    @Override
    public void setCloseOnClickOutside(boolean closeOnClickOutside) {
        dialogWindow.setCloseOnClickOutside(closeOnClickOutside);
    }

    @Override
    public boolean isCloseOnClickOutside() {
        return dialogWindow.getCloseOnClickOutside();
    }

    @Override
    public void setWindowMode(WindowMode mode) {
        dialogWindow.setWindowMode(com.vaadin.shared.ui.window.WindowMode.valueOf(mode.name()));
    }

    @Override
    public WindowMode getWindowMode() {
        return WindowMode.valueOf(dialogWindow.getWindowMode().name());
    }

    @Override
    public void center() {
        dialogWindow.center();
    }

    @Override
    public void setPositionX(int positionX) {
        dialogWindow.setPositionX(positionX);
    }

    @Override
    public int getPositionX() {
        return dialogWindow.getPositionX();
    }

    @Override
    public void setPositionY(int positionY) {
        dialogWindow.setPositionY(positionY);
    }

    @Override
    public int getPositionY() {
        return dialogWindow.getPositionY();
    }

    protected class WebDialogOptions extends DialogOptions {
        @Override
        public Float getWidth() {
            return getDialogWidth();
        }

        @Override
        public SizeUnit getWidthUnit() {
            return getDialogWidthUnit();
        }

        @Override
        protected DialogOptions setWidth(Float width, SizeUnit sizeUnit) {
            setDialogWidth(width + sizeUnit.getSymbol());

            return this;
        }

        @Override
        public Float getHeight() {
            return getDialogHeight();
        }

        @Override
        public SizeUnit getHeightUnit() {
            return getDialogHeightUnit();
        }

        @Override
        protected DialogOptions setHeight(Float height, SizeUnit sizeUnit) {
            setDialogHeight(height + sizeUnit.getSymbol());

            return this;
        }

        @Override
        public Boolean getModal() {
            return WebDialogWindow.this.isModal();
        }

        @Override
        public DialogOptions setModal(Boolean modal) {
            WebDialogWindow.this.setModal(modal);

            return this;
        }

        @Override
        public Boolean getResizable() {
            return WebDialogWindow.this.isResizable();
        }

        @Override
        public DialogOptions setResizable(Boolean resizable) {
            WebDialogWindow.this.setResizable(resizable);

            return this;
        }

        @Override
        public Boolean getCloseable() {
            return WebDialogWindow.this.isCloseable();
        }

        @Override
        public DialogOptions setCloseable(Boolean closeable) {
            WebDialogWindow.this.setCloseable(closeable);

            return this;
        }

        @Override
        public DialogOptions center() {
            WebDialogWindow.this.center();

            return this;
        }

        @Override
        public Integer getPositionX() {
            return WebDialogWindow.this.getPositionX();
        }

        @Override
        public DialogOptions setPositionX(Integer positionX) {
            WebDialogWindow.this.setPositionX(positionX);
            return this;
        }

        @Override
        public Integer getPositionY() {
            return WebDialogWindow.this.getPositionY();
        }

        @Override
        public DialogOptions setPositionY(Integer positionY) {
            WebDialogWindow.this.setPositionY(positionY);
            return this;
        }

        @Override
        public DialogOptions setMaximized(Boolean maximized) {
            setWindowMode(maximized ? WindowMode.MAXIMIZED : WindowMode.NORMAL);
            return this;
        }

        @Override
        public Boolean getMaximized() {
            return getWindowMode() == WindowMode.MAXIMIZED;
        }
    }

    protected class DialogWindowActionHandler implements com.vaadin.event.Action.Handler {

        protected com.vaadin.event.Action saveSettingsAction;
        protected com.vaadin.event.Action restoreToDefaultsAction;

        protected com.vaadin.event.Action analyzeAction;

        protected boolean initialized = false;

        public DialogWindowActionHandler() {
        }

        @Override
        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            if (!initialized) {
                Messages messages = beanLocator.get(Messages.NAME);

                saveSettingsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.saveSettings"));
                restoreToDefaultsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.restoreToDefaults"));
                analyzeAction = new com.vaadin.event.Action(messages.getMainMessage("actions.analyzeLayout"));

                initialized = true;
            }

            List<Action> actions = new ArrayList<>(3);

            ClientConfig clientConfig = getClientConfig();
            if (clientConfig.getManualScreenSettingsSaving()) {
                actions.add(saveSettingsAction);
                actions.add(restoreToDefaultsAction);
            }
            if (clientConfig.getLayoutAnalyzerEnabled()) {
                actions.add(analyzeAction);
            }

            return actions.toArray(new com.vaadin.event.Action[0]);
        }

        @Override
        public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
            if (initialized) {
                if (saveSettingsAction == action) {
                    Screen screen = getFrameOwner();
                    UiControllerUtils.saveSettings(screen);
                } else if (restoreToDefaultsAction == action) {
                    Screen screen = getFrameOwner();
                    UiControllerUtils.deleteSettings(screen);
                } else if (analyzeAction == action) {
                    LayoutAnalyzer analyzer = new LayoutAnalyzer();
                    List<LayoutTip> tipsList = analyzer.analyze(WebDialogWindow.this);

                    if (tipsList.isEmpty()) {
                        getWindowManager().showNotification("No layout problems found", Frame.NotificationType.HUMANIZED);
                    } else {
                        WindowConfig windowConfig = beanLocator.get(WindowConfig.NAME);
                        WindowInfo windowInfo = windowConfig.getWindowInfo("layoutAnalyzer");
                        getWindowManager().openWindow(windowInfo, WindowManager.OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                    }
                }
            }
        }
    }
}