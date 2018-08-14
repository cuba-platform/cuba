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
package com.haulmont.cuba.web;

import com.google.common.collect.Lists;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.core.global.UuidSource;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ScreenHistorySupport;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component.BelongToFrame;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.Window.BeforeCloseWithCloseButtonEvent;
import com.haulmont.cuba.gui.components.Window.BeforeCloseWithShortcutEvent;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea.Mode;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.TopLevelWindowAttachListener;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.web.exception.ExceptionDialog;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebWrapperUtils;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.sys.WindowBreadCrumbs;
import com.haulmont.cuba.web.widgets.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.haulmont.cuba.gui.components.Component.AUTO_SIZE_PX;
import static com.haulmont.cuba.gui.components.Frame.MessageType;
import static com.haulmont.cuba.gui.components.Frame.NotificationType;
import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.convertNotificationType;
import static com.vaadin.server.Sizeable.Unit;
import static java.util.Collections.singletonMap;

@org.springframework.stereotype.Component(WebWindowManager.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WebWindowManager extends WindowManager {

    public static final String NAME = "cuba_WebWindowManager";

    public static final int HUMANIZED_NOTIFICATION_DELAY_MSEC = 3000;
    public static final int WARNING_NOTIFICATION_DELAY_MSEC = -1;

    private static final Logger log = LoggerFactory.getLogger(WebWindowManager.class);

    protected App app;
    protected AppUI ui;

    @Inject
    protected WebConfig webConfig;
    @Inject
    protected ClientConfig clientConfig;
    @Inject
    protected Icons icons;
    @Inject
    protected UuidSource uuidSource;
    @Inject
    protected UserSettingService userSettingService;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected IconResolver iconResolver;

    protected final Map<com.vaadin.ui.ComponentContainer, WindowBreadCrumbs> tabs = new HashMap<>();
    protected final Map<WindowBreadCrumbs, Stack<Pair<Window, Integer>>> stacks = new HashMap<>();
    protected final Map<Window, WindowOpenInfo> windowOpenMode = new LinkedHashMap<>();
    protected final Map<Window, Integer> windows = new HashMap<>();

    protected boolean disableSavingScreenHistory;
    protected ScreenHistorySupport screenHistorySupport;

    public WebWindowManager() {
        screenHistorySupport = new ScreenHistorySupport();
    }

    public void setUi(AppUI ui) {
        this.ui = ui;
        this.app = ui.getApp();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public App getApp() {
        return app;
    }

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<>(windowOpenMode.keySet());
    }

    @Override
    public void selectWindowTab(Window window) {
        WindowOpenInfo openInfo = windowOpenMode.get(window);
        if (openInfo != null) {
            OpenMode openMode = openInfo.getOpenMode();
            if (openMode == OpenMode.NEW_TAB
                    || openMode == OpenMode.NEW_WINDOW
                    || openMode == OpenMode.THIS_TAB) {
                // show in tabsheet
                Layout layout = (Layout) openInfo.getData();
                TabSheetBehaviour webTabsheet = getConfiguredWorkArea(createWorkAreaContext(window))
                        .getTabbedWindowContainer().getTabSheetBehaviour();
                webTabsheet.setSelectedTab(layout);
            }
        }
    }

    @Override
    public void setWindowCaption(Window window, String caption, String description) {
        Window webWindow = window;
        if (window instanceof Window.Wrapper) {
            webWindow = ((Window.Wrapper) window).getWrappedWindow();
        }
        window.setCaption(caption);
        window.setDescription(description);

        WindowOpenInfo openInfo = windowOpenMode.get(webWindow);
        String formattedCaption;

        if (openInfo != null
                && (openInfo.getOpenMode() == OpenMode.NEW_TAB
                || openInfo.getOpenMode() == OpenMode.NEW_WINDOW
                || openInfo.getOpenMode() == OpenMode.THIS_TAB)) {
            formattedCaption = formatTabCaption(caption, description);
        } else {
            formattedCaption = formatTabDescription(caption, description);
        }

        if (openInfo != null) {
            if (openInfo.getOpenMode() == OpenMode.DIALOG) {
                com.vaadin.ui.Window dialog = (com.vaadin.ui.Window) openInfo.getData();
                dialog.setCaption(formattedCaption);
            } else {
                if (getConfiguredWorkArea(createWorkAreaContext(window)).getMode() == Mode.SINGLE) {
                    return;
                }

                Component tabContent = (Component) openInfo.getData();
                if (tabContent == null) {
                    return;
                }

                TabSheetBehaviour tabSheet = getConfiguredWorkArea(createWorkAreaContext(window))
                        .getTabbedWindowContainer().getTabSheetBehaviour();
                String tabId = tabSheet.getTab(tabContent);
                if (tabId == null) {
                    return;
                }

                tabSheet.setTabCaption(tabId, formattedCaption);

                String formattedDescription = formatTabDescription(caption, description);
                if (!Objects.equals(formattedDescription, formattedCaption)) {
                    tabSheet.setTabDescription(tabId, formattedDescription);
                } else {
                    tabSheet.setTabDescription(tabId, null);
                }
            }
        }
    }

    protected static class WindowOpenInfo {
        protected Window window;
        protected OpenMode openMode;
        protected Object data;

        public WindowOpenInfo(Window window, OpenMode openMode) {
            this.window = window;
            this.openMode = openMode;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Window getWindow() {
            return window;
        }

        public OpenMode getOpenMode() {
            return openMode;
        }
    }

    protected com.vaadin.ui.ComponentContainer findTab(Integer hashCode) {
        Set<Map.Entry<com.vaadin.ui.ComponentContainer, WindowBreadCrumbs>> set = tabs.entrySet();
        for (Map.Entry<com.vaadin.ui.ComponentContainer, WindowBreadCrumbs> entry : set) {
            Window currentWindow = entry.getValue().getCurrentWindow();
            if (hashCode.equals(getWindowHashCode(currentWindow))) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected Stack<Pair<Window, Integer>> getStack(WindowBreadCrumbs breadCrumbs) {
        return stacks.get(breadCrumbs);
    }

    protected boolean hasModalWindow() {
        for (Map.Entry<Window, WindowOpenInfo> entry : windowOpenMode.entrySet()) {
            if (OpenMode.DIALOG == entry.getValue().getOpenMode()
                    && BooleanUtils.isTrue(entry.getKey().getDialogOptions().getModal())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void showWindow(final Window window, final String caption, OpenType type, boolean multipleOpen) {
        showWindow(window, caption, null, type, multipleOpen);
    }

    @Override
    protected void showWindow(final Window window, final String caption, final String description, OpenType type,
                              final boolean multipleOpen) {
        OpenType targetOpenType = type.copy();

        overrideOpenTypeParams(targetOpenType, window.getDialogOptions());

        boolean forciblyDialog = false;
        if (targetOpenType.getOpenMode() != OpenMode.DIALOG && hasModalWindow()) {
            targetOpenType.setOpenMode(OpenMode.DIALOG);
            forciblyDialog = true;
        }

        if (targetOpenType.getOpenMode() == OpenMode.THIS_TAB && tabs.size() == 0) {
            targetOpenType.setOpenMode(OpenMode.NEW_TAB);
        }

        final WindowOpenInfo openInfo = new WindowOpenInfo(window, targetOpenType.getOpenMode());
        Component component;

        window.setCaption(caption);
        window.setDescription(description);

        switch (targetOpenType.getOpenMode()) {
            case NEW_TAB:
            case NEW_WINDOW:
                final WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(window));
                workArea.switchTo(AppWorkArea.State.WINDOW_CONTAINER);

                if (workArea.getMode() == Mode.SINGLE) {
                    VerticalLayout mainLayout = workArea.getSingleWindowContainer();
                    if (mainLayout.iterator().hasNext()) {
                        com.vaadin.ui.ComponentContainer oldLayout = (com.vaadin.ui.ComponentContainer) mainLayout.iterator().next();
                        WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);
                        if (oldBreadCrumbs != null) {
                            Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                            oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, () ->
                                    showWindow(window, caption, description, OpenType.NEW_TAB, false)
                            );
                            return;
                        }
                    }
                } else {
                    final Integer hashCode = getWindowHashCode(window);
                    com.vaadin.ui.ComponentContainer tab = null;
                    if (hashCode != null && !multipleOpen) {
                        tab = findTab(hashCode);
                    }
                    com.vaadin.ui.ComponentContainer oldLayout = tab;
                    final WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);

                    if (oldBreadCrumbs != null
                            && windowOpenMode.containsKey(oldBreadCrumbs.getCurrentWindow().getFrame())
                            && !multipleOpen) {
                        Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                        selectWindowTab(((Window.Wrapper) oldBreadCrumbs.getCurrentWindow()).getWrappedWindow());

                        int tabPosition = -1;
                        final TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
                        String tabId = tabSheet.getTab(tab);
                        if (tabId != null) {
                            tabPosition = tabSheet.getTabPosition(tabId);
                        }

                        final int finalTabPosition = tabPosition;
                        oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, () -> {
                            showWindow(window, caption, description, OpenType.NEW_TAB, false);

                            Window wrappedWindow = window;
                            if (window instanceof Window.Wrapper) {
                                wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
                            }

                            if (finalTabPosition >= 0 && finalTabPosition < tabSheet.getComponentCount() - 1) {
                                moveWindowTab(workArea, wrappedWindow, finalTabPosition);
                            }
                        });
                        return;
                    }
                }
                component = showWindowNewTab(window, multipleOpen);
                break;

            case THIS_TAB:
                getConfiguredWorkArea(createWorkAreaContext(window)).switchTo(AppWorkArea.State.WINDOW_CONTAINER);

                component = showWindowThisTab(window, caption, description);
                break;

            case DIALOG:
                component = showWindowDialog(window, targetOpenType, forciblyDialog);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        openInfo.setData(component);

        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            windowOpenMode.put(wrappedWindow, openInfo);
        } else {
            windowOpenMode.put(window, openInfo);
        }

        afterShowWindow(window);
    }

    @Override
    public boolean isOpenAsNewTab(OpenType openType) {
        return super.isOpenAsNewTab(openType) || openType == OpenType.NEW_WINDOW;
    }

    /**
     * @param workArea Work area
     * @param window   Window implementation (WebWindow)
     * @param position new tab position
     */
    protected void moveWindowTab(WebAppWorkArea workArea, Window window, int position) {
        // move tab to
        TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

        if (position >= 0 && position < tabSheet.getComponentCount()) {
            WindowOpenInfo openInfo = windowOpenMode.get(window);
            if (openInfo != null) {
                OpenMode openMode = openInfo.getOpenMode();
                if (openMode == OpenMode.NEW_TAB
                        || openMode == OpenMode.NEW_WINDOW
                        || openMode == OpenMode.THIS_TAB) {
                    // show in tabsheet
                    Layout layout = (Layout) openInfo.getData();

                    tabSheet.moveTab(layout, position);
                    tabSheet.setSelectedTab(layout);
                }
            }
        }
    }

    protected void moveFocus(TabSheetBehaviour tabSheet, String tabId) {
        //noinspection SuspiciousMethodCalls
        Window window = tabs.get(tabSheet.getTabComponent(tabId)).getCurrentWindow();

        if (window != null) {
            boolean focused = false;
            String focusComponentId = window.getFocusComponent();
            if (focusComponentId != null) {
                com.haulmont.cuba.gui.components.Component focusComponent = window.getComponent(focusComponentId);
                if (focusComponent instanceof com.haulmont.cuba.gui.components.Component.Focusable
                        && focusComponent.isEnabledRecursive()
                        && focusComponent.isVisibleRecursive()) {
                    ((com.haulmont.cuba.gui.components.Component.Focusable) focusComponent).focus();
                    focused = true;
                }
            }

            if (!focused && window instanceof Window.Wrapper) {
                Window.Wrapper wrapper = (Window.Wrapper) window;
                focused = ((WebWindow) wrapper.getWrappedWindow()).findAndFocusChildComponent();
                if (!focused) {
                    tabSheet.focus();
                }
            }
        }
    }

    protected Component showWindowNewTab(final Window window, final boolean multipleOpen) {
        final WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs(window);
        breadCrumbs.addListener(
                new WindowBreadCrumbs.Listener() {
                    @Override
                    public void windowClick(final Window window) {
                        Runnable op = new Runnable() {
                            @Override
                            public void run() {
                                Window currentWindow = breadCrumbs.getCurrentWindow();

                                if (currentWindow != null && window != currentWindow) {
                                    if (!isCloseWithCloseButtonPrevented(currentWindow)) {
                                        currentWindow.closeAndRun(Window.CLOSE_ACTION_ID, this);
                                    }
                                }
                            }
                        };
                        op.run();
                    }
                }
        );
        breadCrumbs.addWindow(window);

        //noinspection UnnecessaryLocalVariable
        final Layout layout = createNewTabLayout(window, multipleOpen, breadCrumbs);

        return layout;
    }

    protected Layout createNewTabLayout(final Window window, final boolean multipleOpen, WindowBreadCrumbs breadCrumbs,
                                        Component... additionalComponents) {
        Layout layout = new CssLayout();
        layout.setPrimaryStyleName("c-app-window-wrap");
        layout.setSizeFull();

        layout.addComponent(breadCrumbs);
        if (additionalComponents != null) {
            for (final Component c : additionalComponents) {
                layout.addComponent(c);
            }
        }

        final Component component = WebComponentsHelper.getComposition(window);
        component.setSizeFull();
        layout.addComponent(component);

        WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(window));

        if (workArea.getMode() == Mode.TABBED) {
            layout.addStyleName("c-app-tabbed-window");
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            String tabId;
            Integer hashCode = getWindowHashCode(window);
            com.vaadin.ui.ComponentContainer tab = null;
            if (hashCode != null) {
                tab = findTab(hashCode);
            }
            if (tab != null && !multipleOpen) {
                tabSheet.replaceComponent(tab, layout);
                tabSheet.removeComponent(tab);
                tabs.put(layout, breadCrumbs);
                tabId = tabSheet.getTab(layout);
            } else {
                tabs.put(layout, breadCrumbs);

                tabId = "tab_" + uuidSource.createUuid();

                tabSheet.addTab(layout, tabId);

                String id = "tab_" + window.getId();

                if (ui.isTestMode()) {
                    tabSheet.setTabCubaId(tabId, id);
                }
                if (ui.isPerformanceTestMode()) {
                    tabSheet.setTabTestId(tabId, ui.getTestIdManager().getTestId(id));
                }
            }
            String windowContentSwitchMode = window.getContentSwitchMode().name();
            ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(windowContentSwitchMode);
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);

            String formattedCaption = formatTabCaption(window.getCaption(), window.getDescription());
            tabSheet.setTabCaption(tabId, formattedCaption);
            String formattedDescription = formatTabDescription(window.getCaption(), window.getDescription());
            if (!Objects.equals(formattedCaption, formattedDescription)) {
                tabSheet.setTabDescription(tabId, formattedDescription);
            } else {
                tabSheet.setTabDescription(tabId, null);
            }

            tabSheet.setTabIcon(tabId, iconResolver.getIconResource(window.getIcon()));
            tabSheet.setTabClosable(tabId, true);
            tabSheet.setTabCloseHandler(layout, (targetTabSheet, tabContent) -> {
                //noinspection SuspiciousMethodCalls
                WindowBreadCrumbs breadCrumbs1 = tabs.get(tabContent);

                if (!canWindowBeClosed(breadCrumbs1.getCurrentWindow())) {
                    return;
                }

                Runnable closeTask = new TabCloseTask(breadCrumbs1);
                closeTask.run();

                // it is needed to force redraw tabsheet if it has a lot of tabs and part of them are hidden
                targetTabSheet.markAsDirty();
            });
            tabSheet.setSelectedTab(layout);
        } else {
            tabs.put(layout, breadCrumbs);
            layout.addStyleName("c-app-single-window");

            VerticalLayout mainLayout = workArea.getSingleWindowContainer();
            mainLayout.removeAllComponents();
            mainLayout.addComponent(layout);
        }

        return layout;
    }

    public class TabCloseTask implements Runnable {
        private final WindowBreadCrumbs breadCrumbs;

        public TabCloseTask(WindowBreadCrumbs breadCrumbs) {
            this.breadCrumbs = breadCrumbs;
        }

        @Override
        public void run() {
            Window windowToClose = breadCrumbs.getCurrentWindow();
            if (windowToClose != null) {
                if (!isCloseWithCloseButtonPrevented(windowToClose)) {
                    windowToClose.closeAndRun(Window.CLOSE_ACTION_ID, new TabCloseTask(breadCrumbs));
                }
            }
        }
    }

    protected String formatTabCaption(final String caption, final String description) {
        String s = formatTabDescription(caption, description);
        int maxLength = webConfig.getMainTabCaptionLength();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        } else {
            return s;
        }
    }

    protected String formatTabDescription(final String caption, final String description) {
        if (!StringUtils.isEmpty(description)) {
            return String.format("%s: %s", caption, description);
        } else {
            return caption;
        }
    }

    protected Component showWindowThisTab(final Window window, final String caption, final String description) {
        WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(window));

        Layout layout;
        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            layout = (Layout) tabSheet.getSelectedTab();
        } else {
            layout = (Layout) workArea.getSingleWindowContainer().getComponent(0);
        }

        final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
        if (breadCrumbs == null) {
            throw new IllegalStateException("BreadCrumbs not found");
        }

        final Window currentWindow = breadCrumbs.getCurrentWindow();

        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        boolean pushed = false;
        for (Map.Entry<Window, Integer> entry : set) {
            if (entry.getKey().equals(currentWindow)) {
                windows.remove(currentWindow);
                getStack(breadCrumbs).push(new Pair<>(entry.getKey(), entry.getValue()));
                pushed = true;
                break;
            }
        }
        if (!pushed) {
            getStack(breadCrumbs).push(new Pair<>(currentWindow, null));
        }

        removeFromWindowMap(currentWindow);
        layout.removeComponent(WebComponentsHelper.getComposition(currentWindow));

        final Component component = WebComponentsHelper.getComposition(window);
        component.setSizeFull();
        layout.addComponent(component);

        breadCrumbs.addWindow(window);

        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            String tabId = tabSheet.getTab(layout);
            String formattedCaption = formatTabCaption(caption, description);
            tabSheet.setTabCaption(tabId, formattedCaption);
            String formattedDescription = formatTabDescription(caption, description);

            if (!Objects.equals(formattedCaption, formattedDescription)) {
                tabSheet.setTabDescription(tabId, formattedDescription);
            } else {
                tabSheet.setTabDescription(tabId, null);
            }

            tabSheet.setTabIcon(tabId, iconResolver.getIconResource(window.getIcon()));

            ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(window.getContentSwitchMode().name());
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);
        } else {
            layout.markAsDirtyRecursive();
        }

        return layout;
    }

    protected WebAppWorkArea getConfiguredWorkArea(@Nullable WorkAreaContext workAreaContext) {
        Window.TopLevelWindow topLevelWindow = ui.getTopLevelWindow();

        if (topLevelWindow instanceof Window.MainWindow) {
            AppWorkArea workArea = ((Window.MainWindow) topLevelWindow).getWorkArea();
            if (workArea != null) {
                return (WebAppWorkArea) workArea;
            }
        }

        throw new IllegalStateException("Application does not have any configured work area");
    }

    protected Component showWindowDialog(Window window, OpenType openType, boolean forciblyDialog) {
        final CubaWindow vWindow = createDialogWindow(window);
        vWindow.setStyleName("c-app-dialog-window");
        if (ui.isTestMode()) {
            vWindow.setCubaId("dialog_" + window.getId());
        }

        if (ui.isPerformanceTestMode()) {
            vWindow.setId(ui.getTestIdManager().getTestId("dialog_" + window.getId()));
        }

        Layout layout = (Layout) WebComponentsHelper.getComposition(window);
        vWindow.setContent(layout);

        vWindow.addPreCloseListener(event -> {
            event.setPreventClose(true);
            if (!isCloseWithCloseButtonPrevented(window)) {
                // user has clicked on X
                window.close(Window.CLOSE_ACTION_ID);
            }
        });

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        ShortcutAction exitAction = new ShortcutAction(
                "closeShortcutAction",
                closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())
        );

        Map<com.vaadin.event.Action, Runnable> actions = singletonMap(exitAction, () -> {
            if (openType.getOpenMode() != OpenMode.DIALOG
                    || BooleanUtils.isNotFalse(window.getDialogOptions().getCloseable())) {
                if (isCloseWithShortcutPrevented(window)) {
                    return;
                }
                window.close(Window.CLOSE_ACTION_ID);
            }
        });

        WebComponentsHelper.setActions(vWindow, actions);

        boolean dialogParamsSizeUndefined = openType.getHeight() == null && openType.getWidth() == null;

        ThemeConstants theme = app.getThemeConstants();

        if (forciblyDialog && dialogParamsSizeUndefined) {
            layout.setHeight(100, Unit.PERCENTAGE);

            vWindow.setWidth(theme.getInt("cuba.web.WebWindowManager.forciblyDialog.width"), Unit.PIXELS);
            vWindow.setHeight(theme.getInt("cuba.web.WebWindowManager.forciblyDialog.height"), Unit.PIXELS);

            // resizable by default, but may be overridden in dialog params
            vWindow.setResizable(BooleanUtils.isNotFalse(openType.getResizable()));

            window.setHeightFull();
        } else {
            if (openType.getWidth() == null) {
                vWindow.setWidth(theme.getInt("cuba.web.WebWindowManager.dialog.width"), Unit.PIXELS);
            } else if (openType.getWidth() == AUTO_SIZE_PX) {
                vWindow.setWidthUndefined();
                layout.setWidthUndefined();
                window.setWidthAuto();
            } else {
                vWindow.setWidth(openType.getWidth(),
                        openType.getWidthUnit() != null
                                ? WebWrapperUtils.toVaadinUnit(openType.getWidthUnit())
                                : Unit.PIXELS);
            }

            if (openType.getHeight() != null && openType.getHeight() != AUTO_SIZE_PX) {
                vWindow.setHeight(openType.getHeight(),
                        openType.getHeightUnit() != null
                                ? WebWrapperUtils.toVaadinUnit(openType.getHeightUnit())
                                : Unit.PIXELS);
                layout.setHeight(100, Unit.PERCENTAGE);
                window.setHeightFull();
            } else {
                window.setHeightAuto();
            }

            // non resizable by default
            vWindow.setResizable(BooleanUtils.isTrue(openType.getResizable()));
        }

        if (openType.getCloseable() != null) {
            vWindow.setClosable(openType.getCloseable());
        }

        boolean modal = true;
        if (!hasModalWindow() && openType.getModal() != null) {
            modal = openType.getModal();
        }
        vWindow.setModal(modal);

        if (vWindow.isModal()) {
            boolean informationDialog = false;
            if (openType.getCloseOnClickOutside() != null) {
                informationDialog = openType.getCloseOnClickOutside();
            }
            vWindow.setCloseOnClickOutside(informationDialog);
        }

        if (openType.getMaximized() != null) {
            if (openType.getMaximized()) {
                vWindow.setWindowMode(WindowMode.MAXIMIZED);
            } else {
                vWindow.setWindowMode(WindowMode.NORMAL);
            }
        }

        if (openType.getPositionX() == null
                && openType.getPositionY() == null) {
            vWindow.center();
        } else {
            if (openType.getPositionX() != null) {
                vWindow.setPositionX(openType.getPositionX());
            }
            if (openType.getPositionY() != null) {
                vWindow.setPositionY(openType.getPositionY());
            }
        }

        ui.addWindow(vWindow);

        return vWindow;
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs(Window window) {
        WebAppWorkArea appWorkArea = getConfiguredWorkArea(createWorkAreaContext(window));
        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs(appWorkArea);

        boolean showBreadCrumbs = webConfig.getShowBreadCrumbs() || Mode.SINGLE == appWorkArea.getMode();
        windowBreadCrumbs.setVisible(showBreadCrumbs);

        stacks.put(windowBreadCrumbs, new Stack<>());
        return windowBreadCrumbs;
    }

    protected CubaWindow createDialogWindow(Window window) {
        CubaWindow dialogWindow = new CubaWindow(window.getCaption());

        if (window.getIcon() != null) {
            dialogWindow.setIcon(iconResolver.getIconResource(window.getIcon()));
        }

        dialogWindow.setErrorHandler(ui);
        dialogWindow.addContextActionHandler(new DialogWindowActionHandler(window));
        return dialogWindow;
    }

    @Override
    public void close(Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        WindowOpenInfo openInfo = windowOpenMode.get(window);
        if (openInfo == null) {
            log.warn("Problem closing window {}: WindowOpenMode not found", window);
            return;
        }
        disableSavingScreenHistory = false;
        closeWindow(window, openInfo);
        windowOpenMode.remove(window);
        removeFromWindowMap(openInfo.getWindow());
    }

    /**
     * Check modifications and close all screens in all main windows.
     *
     * @param runIfOk a closure to run after all screens are closed
     */
    public void checkModificationsAndCloseAll(Runnable runIfOk) {
        checkModificationsAndCloseAll(runIfOk, null);
    }

    /**
     * Check modifications and close all screens in all main windows.
     *
     * @param runIfOk     a closure to run after all screens are closed
     * @param runIfCancel a closure to run if there were modifications and a user canceled the operation
     */
    public void checkModificationsAndCloseAll(Runnable runIfOk, @Nullable Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            if (!disableSavingScreenHistory) {
                screenHistorySupport.saveScreenHistory(window, windowOpenMode.get(window).getOpenMode());
            }

            if (window instanceof WrappedWindow && ((WrappedWindow) window).getWrapper() != null) {
                ((WrappedWindow) window).getWrapper().saveSettings();
            } else {
                window.saveSettings();
            }

            if (window instanceof WrappedWindow
                    && ((WrappedWindow) window).getWrapper() instanceof Window.Committable) {
                WrappedWindow wrappedWindow = (WrappedWindow) window;
                modified = ((Window.Committable) wrappedWindow.getWrapper()).isModified();
            } else if (window.getDsContext() != null
                    && window.getDsContext().isModified()) {
                modified = true;
            }
        }
        disableSavingScreenHistory = true;
        if (modified) {
            showOptionDialog(
                    messages.getMainMessage("closeUnsaved.caption"),
                    messages.getMainMessage("discardChangesOnClose"),
                    MessageType.WARNING,
                    new Action[]{
                            new BaseAction("closeApplication")
                                    .withCaption(messages.getMainMessage("closeApplication"))
                                    .withIcon(icons.get(CubaIcon.DIALOG_OK))
                                    .withHandler(event -> {

                                closeAllWindows();
                                runIfOk.run();
                            }),
                            new DialogAction(Type.CANCEL, Status.PRIMARY)
                                    .withHandler(event -> {

                                if (runIfCancel != null) {
                                    runIfCancel.run();
                                }
                            })
                    }
            );
        } else {
            closeAllWindows();
            runIfOk.run();
        }
    }

    public void closeAllTabbedWindows() {
        closeAllTabbedWindowsExcept(null);
    }

    public void closeAllTabbedWindowsExcept(@Nullable com.vaadin.ui.ComponentContainer keepOpened) {
        boolean modified = false;
        List<WebWindow> windowsToClose = new ArrayList<>();
        WindowBreadCrumbs keepOpenedCrumbs = tabs.get(keepOpened);
        Frame keepOpenedFrame = keepOpenedCrumbs != null ? keepOpenedCrumbs.getCurrentWindow().getFrame() : null;

        for (Window window : getOpenWindows()) {
            if (!canWindowBeClosed(window)) {
                continue;
            }

            OpenMode openMode = windowOpenMode.get(window).getOpenMode();
            WindowBreadCrumbs windowBreadCrumbs = tabs.get(windowOpenMode.get(window).getData());
            Window currentWindow = (windowBreadCrumbs != null && window != windowBreadCrumbs.getCurrentWindow())
                    ? windowBreadCrumbs.getCurrentWindow()
                    : window;
            if (window.getFrame() == keepOpenedFrame || openMode == OpenMode.DIALOG
                    || keepOpenedCrumbs == windowBreadCrumbs || isCloseWithCloseButtonPrevented(currentWindow))
                continue;

            if (window.getDsContext() != null && window.getDsContext().isModified()) {
                modified = true;
            }

            windowsToClose.add((WebWindow) window);
        }

        disableSavingScreenHistory = true;

        if (modified) {
            showOptionDialog(
                    messages.getMainMessage("closeUnsaved.caption"),
                    messages.getMainMessage("discardChangesInTabs"),
                    MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(messages.getMainMessage("closeTabs")) {
                                {
                                    icon = icons.get(CubaIcon.DIALOG_OK);
                                }

                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    closeTabsForce(windowsToClose);
                                }
                            },
                            new DialogAction(Type.CANCEL, Status.PRIMARY)
                    }
            );
        } else {
            closeTabsForce(windowsToClose);
        }
    }

    protected void closeTabsForce(List<WebWindow> windowsToClose) {
        windowsToClose = Lists.reverse(windowsToClose);
        for (WebWindow window : windowsToClose) {
            window.close(Window.CLOSE_ACTION_ID, true);
        }
    }

    /**
     * Close all screens in all main windows (browser tabs).
     */
    public void closeAllWindows() {
        app.cleanupBackgroundTasks();
        app.closeAllWindows();
    }

    /**
     * Close all screens in the main window (browser tab) this WindowManager belongs to.
     */
    public void closeAll() {
        List<Map.Entry<Window, WindowOpenInfo>> entries = new ArrayList<>(windowOpenMode.entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            WebWindow window = (WebWindow) entries.get(i).getKey();
            if (window instanceof WebWindow.Editor) {
                ((WebWindow.Editor) window).releaseLock();
            }
            closeWindow(window, entries.get(i).getValue());
        }
        disableSavingScreenHistory = false;
        windowOpenMode.clear();
        windows.clear();
    }

    protected void closeWindow(Window window, WindowOpenInfo openInfo) {
        if (!disableSavingScreenHistory) {
            screenHistorySupport.saveScreenHistory(window, openInfo.getOpenMode());
        }

        WebWindow webWindow = (WebWindow) window;
        webWindow.stopTimers();

        switch (openInfo.getOpenMode()) {
            case DIALOG: {
                final CubaWindow cubaDialogWindow = (CubaWindow) openInfo.getData();
                cubaDialogWindow.forceClose();
                fireListeners(window, tabs.size() != 0);
                break;
            }

            case NEW_WINDOW:
            case NEW_TAB: {
                final Layout layout = (Layout) openInfo.getData();
                layout.removeComponent(WebComponentsHelper.getComposition(window));

                WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(window));

                if (workArea.getMode() == Mode.TABBED) {
                    TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
                    tabSheet.silentCloseTabAndSelectPrevious(layout);
                    tabSheet.removeComponent(layout);
                } else {
                    VerticalLayout singleLayout = workArea.getSingleWindowContainer();
                    singleLayout.removeComponent(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = tabs.get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                tabs.remove(layout);
                stacks.remove(windowBreadCrumbs);
                fireListeners(window, !tabs.isEmpty());
                if (tabs.isEmpty() && app.getConnection().isConnected()) {
                    workArea.switchTo(AppWorkArea.State.INITIAL_LAYOUT);
                }
                break;
            }
            case THIS_TAB: {
                final Layout layout = (Layout) openInfo.getData();

                final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                if (breadCrumbs == null) {
                    throw new IllegalStateException("Unable to close screen: breadCrumbs not found");
                }

                breadCrumbs.removeWindow();
                Window currentWindow = breadCrumbs.getCurrentWindow();
                if (!getStack(breadCrumbs).empty()) {
                    Pair<Window, Integer> entry = getStack(breadCrumbs).pop();
                    putToWindowMap(entry.getFirst(), entry.getSecond());
                }

                Component component = WebComponentsHelper.getComposition(currentWindow);
                component.setSizeFull();

                WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(window));

                layout.removeComponent(WebComponentsHelper.getComposition(window));
                if (app.getConnection().isConnected()) {
                    layout.addComponent(component);

                    if (workArea.getMode() == Mode.TABBED) {
                        TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
                        String tabId = tabSheet.getTab(layout);
                        String formattedCaption = formatTabCaption(currentWindow.getCaption(), currentWindow.getDescription());
                        tabSheet.setTabCaption(tabId, formattedCaption);
                        String formattedDescription = formatTabDescription(currentWindow.getCaption(), currentWindow.getDescription());

                        if (!Objects.equals(formattedCaption, formattedDescription)) {
                            tabSheet.setTabDescription(tabId, formattedDescription);
                        } else {
                            tabSheet.setTabDescription(tabId, null);
                        }

                        tabSheet.setTabIcon(tabId, iconResolver.getIconResource(currentWindow.getIcon()));

                        ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(currentWindow.getContentSwitchMode().name());
                        tabSheet.setContentSwitchMode(tabId, contentSwitchMode);
                    }
                }
                fireListeners(window, !tabs.isEmpty());
                if (tabs.isEmpty() && app.getConnection().isConnected()) {
                    workArea.switchTo(AppWorkArea.State.INITIAL_LAYOUT);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public void showFrame(com.haulmont.cuba.gui.components.Component parent, Frame frame) {
        if (parent instanceof ComponentContainer) {
            ComponentContainer container = (ComponentContainer) parent;
            for (com.haulmont.cuba.gui.components.Component c : container.getComponents()) {
                if (c instanceof com.haulmont.cuba.gui.components.Component.Disposable) {
                    com.haulmont.cuba.gui.components.Component.Disposable disposable =
                            (com.haulmont.cuba.gui.components.Component.Disposable) c;
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
                container.remove(c);
            }
            container.add(frame);
        } else {
            throw new IllegalStateException(
                    "Parent component must be com.haulmont.cuba.gui.components.Component.Container"
            );
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void showNotification(String caption) {
        showNotification(caption, null, NotificationType.HUMANIZED);
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        showNotification(caption, null, type);
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        backgroundWorker.checkUIAccess();

        Notification notification = new Notification(caption, description, convertNotificationType(type));
        notification.setHtmlContentAllowed(NotificationType.isHTML(type));
        setNotificationDelayMsec(notification, type);
        notification.show(Page.getCurrent());
    }

    protected void setNotificationDelayMsec(Notification notification, NotificationType type) {
        switch (type) {
            case HUMANIZED:
            case HUMANIZED_HTML:
                notification.setDelayMsec(HUMANIZED_NOTIFICATION_DELAY_MSEC);
                break;
            case WARNING:
            case WARNING_HTML:
                notification.setDelayMsec(WARNING_NOTIFICATION_DELAY_MSEC);
                break;
        }
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        backgroundWorker.checkUIAccess();

        com.vaadin.ui.Window vWindow = new CubaWindow(title);

        if (ui.isTestMode()) {
            vWindow.setCubaId("messageDialog");
        }

        if (ui.isPerformanceTestMode()) {
            vWindow.setId(ui.getTestIdManager().getTestId("messageDialog"));
        }

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        vWindow.addAction(
                new ShortcutListenerDelegate("Esc",
                        closeCombination.getKey().getCode(),
                        KeyCombination.Modifier.codes(closeCombination.getModifiers())
                ).withHandler((sender, target) ->
                        vWindow.close()
                ));

        vWindow.addAction(new ShortcutListenerDelegate("Enter", ShortcutAction.KeyCode.ENTER, null)
                .withHandler((sender, target) -> {
                    vWindow.close();
                }));

        VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("c-app-message-dialog");
        layout.setMargin(false);
        layout.setSpacing(true);
        if (messageType.getWidth() != null
                && messageType.getWidth() == AUTO_SIZE_PX) {
            layout.setWidthUndefined();
        }
        vWindow.setContent(layout);

        CubaLabel messageLab = new CubaLabel();
        messageLab.setValue(message);
        if (MessageType.isHTML(messageType)) {
            messageLab.setContentMode(ContentMode.HTML);
        } else {
            messageLab.setContentMode(ContentMode.TEXT);
        }
        if (messageType.getWidth() != null && messageType.getWidth() == AUTO_SIZE_PX) {
            messageLab.setWidthUndefined();
        }
        layout.addComponent(messageLab);

        DialogAction action = new DialogAction(Type.OK);
        CubaButton button = WebComponentsHelper.createButton();

        button.setCaption(action.getCaption());
        button.setIcon(iconResolver.getIconResource(action.getIcon()));
        button.addStyleName(WebButton.ICON_STYLE);
        button.setClickHandler(event ->
                vWindow.close()
        );

        button.focus();

        layout.addComponent(button);

        layout.setComponentAlignment(button, Alignment.BOTTOM_RIGHT);

        float width;
        SizeUnit unit;
        if (messageType.getWidth() != null) {
            width = messageType.getWidth();
            unit = messageType.getWidthUnit();
        } else {
            SizeWithUnit size = SizeWithUnit.parseStringSize(
                    app.getThemeConstants().get("cuba.web.WebWindowManager.messageDialog.width"));
            width = size.getSize();
            unit = size.getUnit();
        }

        vWindow.setWidth(width, unit != null
                ? WebWrapperUtils.toVaadinUnit(unit)
                : Unit.PIXELS);
        vWindow.setResizable(false);

        boolean modal = true;
        if (!hasModalWindow()) {
            if (messageType.getModal() != null) {
                modal = messageType.getModal();
            }
        }
        vWindow.setModal(modal);

        boolean closeOnClickOutside = false;
        if (vWindow.isModal()) {
            if (messageType.getCloseOnClickOutside() != null) {
                closeOnClickOutside = messageType.getCloseOnClickOutside();
            }
        }
        ((CubaWindow) vWindow).setCloseOnClickOutside(closeOnClickOutside);

        if (messageType.getMaximized() != null) {
            if (messageType.getMaximized()) {
                vWindow.setWindowMode(WindowMode.MAXIMIZED);
            } else {
                vWindow.setWindowMode(WindowMode.NORMAL);
            }
        }

        ui.addWindow(vWindow);
        vWindow.center();
        vWindow.focus();

        if (ui.isTestMode()) {
            messageLab.setCubaId("messageDialogLabel");
            button.setCubaId("messageDialogOk");
        }
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        backgroundWorker.checkUIAccess();

        final com.vaadin.ui.Window window = new CubaWindow(title);

        if (ui.isTestMode()) {
            window.setCubaId("optionDialog");
        }

        if (ui.isPerformanceTestMode()) {
            window.setId(ui.getTestIdManager().getTestId("optionDialog"));
        }

        window.setClosable(false);

        CubaLabel messageLab = new CubaLabel();
        messageLab.setValue(message);
        if (MessageType.isHTML(messageType)) {
            messageLab.setContentMode(ContentMode.HTML);
        } else {
            messageLab.setContentMode(ContentMode.TEXT);
        }
        if (messageType.getWidth() != null && messageType.getWidth() == AUTO_SIZE_PX) {
            messageLab.setWidthUndefined();
        }

        float width;
        SizeUnit unit;
        if (messageType.getWidth() != null) {
            width = messageType.getWidth();
            unit = messageType.getWidthUnit();
        } else {
            SizeWithUnit size = SizeWithUnit.parseStringSize(
                    app.getThemeConstants().get("cuba.web.WebWindowManager.optionDialog.width"));
            width = size.getSize();
            unit = size.getUnit();
        }

        if (messageType.getModal() != null) {
            log.warn("MessageType.modal is not supported for showOptionDialog");
        }

        window.setWidth(width, unit != null
                ? WebWrapperUtils.toVaadinUnit(unit)
                : Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);

        boolean closeOnClickOutside = false;
        if (window.isModal()) {
            if (messageType.getCloseOnClickOutside() != null) {
                closeOnClickOutside = messageType.getCloseOnClickOutside();
            }
        }
        ((CubaWindow) window).setCloseOnClickOutside(closeOnClickOutside);

        if (messageType.getMaximized() != null) {
            if (messageType.getMaximized()) {
                window.setWindowMode(WindowMode.MAXIMIZED);
            } else {
                window.setWindowMode(WindowMode.NORMAL);
            }
        }

        VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("c-app-option-dialog");
        layout.setMargin(false);
        layout.setSpacing(true);
        if (messageType.getWidth() != null && messageType.getWidth() == AUTO_SIZE_PX) {
            layout.setWidthUndefined();
        }
        window.setContent(layout);

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.setMargin(false);
        buttonsContainer.setSpacing(true);

        boolean hasPrimaryAction = false;
        Map<Action, Button> buttonMap = new HashMap<>();
        for (Action action : actions) {
            CubaButton button = WebComponentsHelper.createButton();
            button.setCaption(action.getCaption());
            button.setEnabled(action.isEnabled());
            button.setClickHandler(event -> {
                try {
                    action.actionPerform(null);
                } finally {
                    ui.removeWindow(window);
                }
            });

            if (StringUtils.isNotEmpty(action.getIcon())) {
                button.setIcon(iconResolver.getIconResource(action.getIcon()));
                button.addStyleName(WebButton.ICON_STYLE);
            }

            if (action instanceof AbstractAction && ((AbstractAction) action).isPrimary()) {
                button.addStyleName("c-primary-action");
                button.focus();

                hasPrimaryAction = true;
            }

            if (ui.isTestMode()) {
                button.setCubaId("optionDialog_" + action.getId());
            }

            if (ui.isPerformanceTestMode()) {
                button.setId(ui.getTestIdManager().getTestId("optionDialog_" + action.getId()));
            }

            buttonsContainer.addComponent(button);
            buttonMap.put(action, button);
        }

        assignDialogShortcuts(buttonMap);

        if (!hasPrimaryAction && actions.length > 0) {
            ((Button) buttonsContainer.getComponent(0)).focus();
        }

        layout.addComponent(messageLab);
        layout.addComponent(buttonsContainer);

        layout.setExpandRatio(messageLab, 1);
        layout.setComponentAlignment(buttonsContainer, Alignment.BOTTOM_RIGHT);

        ui.addWindow(window);
        window.center();

        if (ui.isTestMode()) {
            messageLab.setCubaId("optionDialogLabel");
        }
    }

    @Override
    public void showExceptionDialog(Throwable throwable) {
        showExceptionDialog(throwable, null, null);
    }

    @Override
    public void showExceptionDialog(Throwable throwable, @Nullable String caption, @Nullable String message) {
        Preconditions.checkNotNullArgument(throwable);

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

    public WindowBreadCrumbs getBreadCrumbs(com.vaadin.ui.ComponentContainer container) {
        return tabs.get(container);
    }

    protected void assignDialogShortcuts(Map<Action, Button> buttonMap) {
        List<DialogAction> dialogActions = new ArrayList<>();
        for (Action action : buttonMap.keySet()) {
            if (action instanceof DialogAction) {
                dialogActions.add((DialogAction) action);
            }
        }

        // find action for commit shortcut
        Action firstOkAction = dialogActions.stream()
                .filter(action -> action.getType() == Type.OK)
                .findFirst().orElse(null);
        if (firstOkAction == null) {
            firstOkAction = dialogActions.stream()
                    .filter(action -> action.getType() == Type.YES)
                    .findFirst().orElse(null);
        }
        if (firstOkAction != null) {
            WebComponentsHelper.setClickShortcut(buttonMap.get(firstOkAction), clientConfig.getCommitShortcut());
        }

        // find action for close shortcut
        Action firstCancelAction = dialogActions.stream()
                .filter(action -> action.getType() == Type.CANCEL)
                .findFirst().orElse(null);

        if (firstCancelAction == null) {
            firstCancelAction = dialogActions.stream()
                    .filter(action -> action.getType() == Type.CLOSE)
                    .findFirst().orElse(null);

            if (firstCancelAction == null) {
                firstCancelAction = dialogActions.stream()
                        .filter(action -> action.getType() == Type.NO)
                        .findFirst().orElse(null);
            }
        }

        if (firstCancelAction != null) {
            WebComponentsHelper.setClickShortcut(buttonMap.get(firstCancelAction), clientConfig.getCloseShortcut());
        }
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        String target = null;
        Integer width = null;
        Integer height = null;
        String border = "DEFAULT";
        Boolean tryToOpenAsPopup = null;
        if (params != null) {
            target = (String) params.get("target");
            width = (Integer) params.get("width");
            height = (Integer) params.get("height");
            border = (String) params.get("border");
            tryToOpenAsPopup = (Boolean) params.get("tryToOpenAsPopup");
        }
        if (target == null) {
            target = "_blank";
        }
        if (width != null && height != null && border != null) {
            ui.getPage().open(url, target, width, height, BorderStyle.valueOf(border));
        } else if (tryToOpenAsPopup != null) {
            ui.getPage().open(url, target, tryToOpenAsPopup);
        } else {
            ui.getPage().open(url, target, false);
        }
    }

    @Override
    public void initDebugIds(final Frame frame) {
        if (ui.isPerformanceTestMode()) {
            ComponentsHelper.walkComponents(frame, (component, name) -> {
                if (component instanceof HasDebugId
                        && ((HasDebugId) component).getDebugId() == null) {
                    Frame componentFrame = null;
                    if (component instanceof BelongToFrame) {
                        componentFrame = ((BelongToFrame) component).getFrame();
                    }
                    if (componentFrame == null) {
                        log.warn("Frame for component {} is not assigned", component.getClass());
                    } /*else { todo
                        if (component instanceof WebAbstractComponent) {
                            WebAbstractComponent webComponent = (WebAbstractComponent) component;
                        }
                    }*/
                }
            });
        }
    }

    @Override
    protected void putToWindowMap(Window window, Integer hashCode) {
        if (window != null) {
            windows.put(window, hashCode);
        }
    }

    protected void removeFromWindowMap(Window window) {
        windows.remove(window);
    }

    protected Integer getWindowHashCode(Window window) {
        return windows.get(window);
    }

    @Override
    protected Window getWindow(Integer hashCode) {
        AppWorkArea workArea = getConfiguredWorkArea(null);
        if (workArea == null || workArea.getMode() == Mode.SINGLE) {
            return null;
        }

        for (Map.Entry<Window, Integer> entry : windows.entrySet()) {
            if (hashCode.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void checkCanOpenWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
        if (openType.getOpenMode() != OpenMode.NEW_TAB
                && openType.getOpenMode() != OpenMode.NEW_WINDOW) {
            return;
        }

        if (!windowInfo.getMultipleOpen() && getWindow(getHash(windowInfo, params)) != null) {
            //window is already open
            return;
        }

        int maxCount = webConfig.getMaxTabCount();
        if (maxCount > 0 && maxCount <= tabs.size()) {
            Notification notification = new Notification(
                    messages.formatMainMessage("tooManyOpenTabs.message", maxCount),
                    Notification.Type.WARNING_MESSAGE
            );
            notification.setDelayMsec(WARNING_NOTIFICATION_DELAY_MSEC);
            notification.show(ui.getPage());

            throw new SilentException();
        }
    }

    public void createTopLevelWindow(WindowInfo windowInfo) {
        ui.beforeTopLevelWindowInit();

        String template = windowInfo.getTemplate();

        Window.TopLevelWindow topLevelWindow;

        Map<String, Object> params = Collections.emptyMap();
        if (template != null) {
            //noinspection unchecked
            topLevelWindow = (Window.TopLevelWindow) createWindow(windowInfo, OpenType.NEW_TAB, params,
                    LayoutLoaderConfig.getWindowLoaders(), true);
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null) {
                //noinspection unchecked
                topLevelWindow = (Window.TopLevelWindow) createWindowByScreenClass(windowInfo, params);
            } else {
                throw new DevelopmentException("Unable to load top level window");
            }
        }

        // detect work area
        Window windowImpl = ((Window.Wrapper) topLevelWindow).getWrappedWindow();

        if (topLevelWindow instanceof AbstractMainWindow) {
            AbstractMainWindow mainWindow = (AbstractMainWindow) topLevelWindow;

            // bind system UI components to AbstractMainWindow
            ComponentsHelper.walkComponents(windowImpl, component -> {
                if (component instanceof AppWorkArea) {
                    mainWindow.setWorkArea((AppWorkArea) component);
                } else if (component instanceof UserIndicator) {
                    mainWindow.setUserIndicator((UserIndicator) component);
                } else if (component instanceof FoldersPane) {
                    mainWindow.setFoldersPane((FoldersPane) component);
                }

                return false;
            });
        }

        ui.setTopLevelWindow(topLevelWindow);

        // load menu
        ComponentsHelper.walkComponents(windowImpl, component -> {
            if (component instanceof TopLevelWindowAttachListener) {
                ((TopLevelWindowAttachListener) component).topLevelWindowAttached(topLevelWindow);
            }

            return false;
        });

        if (topLevelWindow instanceof Window.HasWorkArea) {
            AppWorkArea workArea = ((Window.HasWorkArea) topLevelWindow).getWorkArea();
            if (workArea != null) {
                workArea.addStateChangeListener(new AppWorkArea.StateChangeListener() {
                    @Override
                    public void stateChanged(AppWorkArea.State newState) {
                        if (newState == AppWorkArea.State.WINDOW_CONTAINER) {
                            initTabShortcuts();

                            // listener used only once
                            getConfiguredWorkArea(createWorkAreaContext(topLevelWindow)).removeStateChangeListener(this);
                        }
                    }
                });
            }
        }

        afterShowWindow(topLevelWindow);
    }

    protected void initTabShortcuts() {
        Window.TopLevelWindow topLevelWindow = ui.getTopLevelWindow();
        CubaOrderedActionsLayout actionsLayout = topLevelWindow.unwrap(CubaOrderedActionsLayout.class);

        if (getConfiguredWorkArea(createWorkAreaContext(topLevelWindow)).getMode() == Mode.TABBED) {
            actionsLayout.addShortcutListener(createNextWindowTabShortcut(topLevelWindow));
            actionsLayout.addShortcutListener(createPreviousWindowTabShortcut(topLevelWindow));
        }
        actionsLayout.addShortcutListener(createCloseShortcut(topLevelWindow));
    }

    protected boolean hasDialogWindows() {
        return !ui.getWindows().isEmpty();
    }

    public ShortcutListener createCloseShortcut(Window.TopLevelWindow topLevelWindow) {
        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination combination = KeyCombination.create(closeShortcut);

        return new ShortcutListenerDelegate("onClose", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers()))
                .withHandler((sender, target) ->
                        closeWindowByShortcut(topLevelWindow)
                );
    }

    protected void closeWindowByShortcut(Window.TopLevelWindow topLevelWindow) {
        WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(topLevelWindow));
        if (workArea.getState() != AppWorkArea.State.WINDOW_CONTAINER) {
            return;
        }

        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            if (tabSheet != null) {
                Layout layout = (Layout) tabSheet.getSelectedTab();
                if (layout != null) {
                    tabSheet.focus();

                    WindowBreadCrumbs breadCrumbs = tabs.get(layout);

                    if (!canWindowBeClosed(breadCrumbs.getCurrentWindow())) {
                        return;
                    }

                    if (isCloseWithShortcutPrevented(breadCrumbs.getCurrentWindow())) {
                        return;
                    }

                    if (stacks.get(breadCrumbs).empty()) {
                        final Component previousTab = tabSheet.getPreviousTab(layout);
                        if (previousTab != null) {
                            breadCrumbs.getCurrentWindow().closeAndRun(Window.CLOSE_ACTION_ID, () ->
                                    tabSheet.setSelectedTab(previousTab)
                            );
                        } else {
                            breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                        }
                    } else {
                        breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                    }
                }
            }
        } else {
            Iterator<WindowBreadCrumbs> it = tabs.values().iterator();
            if (it.hasNext()) {
                Window currentWindow = it.next().getCurrentWindow();
                if (!isCloseWithShortcutPrevented(currentWindow)) {
                    ui.focus();
                    currentWindow.close(Window.CLOSE_ACTION_ID);
                }
            }
        }
    }

    protected boolean canWindowBeClosed(Window window) {
        if (webConfig.getDefaultScreenCanBeClosed()) {
            return true;
        }

        String defaultScreenId = webConfig.getDefaultScreenId();

        if (webConfig.getUserCanChooseDefaultScreen()) {
            String userDefaultScreen = userSettingService.loadSetting(ClientType.WEB, "userDefaultScreen");
            defaultScreenId = StringUtils.isEmpty(userDefaultScreen) ? defaultScreenId : userDefaultScreen;
        }

        return !window.getId().equals(defaultScreenId);
    }

    @Override
    public void openDefaultScreen() {
        String defaultScreenId = webConfig.getDefaultScreenId();

        if (webConfig.getUserCanChooseDefaultScreen()) {
            String userDefaultScreen = userSettingService.loadSetting(ClientType.WEB, "userDefaultScreen");
            defaultScreenId = StringUtils.isEmpty(userDefaultScreen) ? defaultScreenId : userDefaultScreen;
        }

        if (StringUtils.isEmpty(defaultScreenId)) {
            return;
        }

        if (!windowConfig.hasWindow(defaultScreenId)) {
            log.info("Can't find default screen: {}", defaultScreenId);
            return;
        }

        Window window = openWindow(windowConfig.getWindowInfo(defaultScreenId), OpenType.NEW_TAB);
        // in case of window is created by Runnable instance
        if (window == null) {
            return;
        }

        if (!webConfig.getDefaultScreenCanBeClosed()) {
            WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(window));
            if (workArea.getMode() == Mode.TABBED) {
                TabSheetBehaviour tabSheetBehaviour = workArea.getTabbedWindowContainer()
                        .getTabSheetBehaviour();

                HasComponents tabLayout = WebComponentsHelper.getComposition(window).getParent();
                String tabId = tabSheetBehaviour.getTab(tabLayout);

                tabSheetBehaviour.setTabClosable(tabId, false);
            }
        }
    }

    protected boolean isCloseWithShortcutPrevented(Window currentWindow) {
        WebWindow webWindow = (WebWindow) ComponentsHelper.getWindowImplementation(currentWindow);

        if (webWindow != null) {
            BeforeCloseWithShortcutEvent event = new BeforeCloseWithShortcutEvent(webWindow);
            webWindow.fireBeforeCloseWithShortcut(event);
            return event.isClosePrevented();
        }

        return false;
    }

    protected boolean isCloseWithCloseButtonPrevented(Window currentWindow) {
        WebWindow webWindow = (WebWindow) ComponentsHelper.getWindowImplementation(currentWindow);

        if (webWindow != null) {
            BeforeCloseWithCloseButtonEvent event = new BeforeCloseWithCloseButtonEvent(webWindow);
            webWindow.fireBeforeCloseWithCloseButton(event);
            return event.isClosePrevented();
        }

        return false;
    }

    public ShortcutListener createNextWindowTabShortcut(Window.TopLevelWindow topLevelWindow) {
        String nextTabShortcut = clientConfig.getNextTabShortcut();
        KeyCombination combination = KeyCombination.create(nextTabShortcut);

        return new ShortcutListenerDelegate(
                "onNextTab", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())
        ).withHandler((sender, target) -> {
            WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(topLevelWindow));
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            if (tabSheet != null
                    && !hasDialogWindows()
                    && tabSheet.getComponentCount() > 1) {
                Component selectedTabComponent = tabSheet.getSelectedTab();
                String tabId = tabSheet.getTab(selectedTabComponent);
                int tabPosition = tabSheet.getTabPosition(tabId);
                int newTabPosition = (tabPosition + 1) % tabSheet.getComponentCount();

                String newTabId = tabSheet.getTab(newTabPosition);
                tabSheet.setSelectedTab(newTabId);

                moveFocus(tabSheet, newTabId);
            }
        });
    }

    public ShortcutListener createPreviousWindowTabShortcut(Window.TopLevelWindow topLevelWindow) {
        String previousTabShortcut = clientConfig.getPreviousTabShortcut();
        KeyCombination combination = KeyCombination.create(previousTabShortcut);

        return new ShortcutListenerDelegate("onPreviousTab", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())
        ).withHandler((sender, target) -> {
            WebAppWorkArea workArea = getConfiguredWorkArea(createWorkAreaContext(topLevelWindow));
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            if (tabSheet != null
                    && !hasDialogWindows()
                    && tabSheet.getComponentCount() > 1) {
                Component selectedTabComponent = tabSheet.getSelectedTab();
                String selectedTabId = tabSheet.getTab(selectedTabComponent);
                int tabPosition = tabSheet.getTabPosition(selectedTabId);
                int newTabPosition = (tabSheet.getComponentCount() + tabPosition - 1) % tabSheet.getComponentCount();

                String newTabId = tabSheet.getTab(newTabPosition);
                tabSheet.setSelectedTab(newTabId);

                moveFocus(tabSheet, newTabId);
            }
        });
    }

    protected class DialogWindowActionHandler implements com.vaadin.event.Action.Handler {

        protected Window window;
        protected com.vaadin.event.Action saveSettingsAction;
        protected com.vaadin.event.Action restoreToDefaultsAction;

        protected com.vaadin.event.Action analyzeAction;

        protected boolean initialized = false;

        public DialogWindowActionHandler(Window window) {
            this.window = window;
        }

        @Override
        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            if (!initialized) {
                saveSettingsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.saveSettings"));
                restoreToDefaultsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.restoreToDefaults"));
                analyzeAction = new com.vaadin.event.Action(messages.getMainMessage("actions.analyzeLayout"));

                initialized = true;
            }

            List<com.vaadin.event.Action> actions = new ArrayList<>(3);

            if (clientConfig.getManualScreenSettingsSaving()) {
                actions.add(saveSettingsAction);
                actions.add(restoreToDefaultsAction);
            }
            if (clientConfig.getLayoutAnalyzerEnabled()) {
                actions.add(analyzeAction);
            }

            return actions.toArray(new com.vaadin.event.Action[actions.size()]);
        }

        @Override
        public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
            if (initialized) {
                if (saveSettingsAction == action) {
                    window.saveSettings();
                } else if (restoreToDefaultsAction == action) {
                    window.deleteSettings();
                } else if (analyzeAction == action) {
                    LayoutAnalyzer analyzer = new LayoutAnalyzer();
                    List<LayoutTip> tipsList = analyzer.analyze(window);

                    if (tipsList.isEmpty()) {
                        window.showNotification("No layout problems found", NotificationType.HUMANIZED);
                    } else {
                        window.openWindow("layoutAnalyzer", OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                    }
                }
            }
        }
    }

    @Nullable
    protected WorkAreaContext createWorkAreaContext(WindowInfo windowInfo) {
        return null;
    }

    @Nullable
    protected WorkAreaContext createWorkAreaContext(Window window) {
        return null;
    }

    public interface WorkAreaContext {

    }
}