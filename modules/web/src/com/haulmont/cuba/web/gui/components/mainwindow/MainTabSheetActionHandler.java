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

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.sys.WindowBreadCrumbs;
import com.haulmont.cuba.web.widgets.HasTabSheetBehaviour;
import com.haulmont.cuba.web.widgets.TabSheetBehaviour;
import com.vaadin.event.Action;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MainTabSheetActionHandler implements Action.Handler {

    protected com.vaadin.event.Action closeAllTabs;
    protected com.vaadin.event.Action closeOtherTabs;
    protected com.vaadin.event.Action closeCurrentTab;

    protected com.vaadin.event.Action showInfo;

    protected com.vaadin.event.Action analyzeLayout;

    protected com.vaadin.event.Action saveSettings;
    protected com.vaadin.event.Action restoreToDefaults;

    protected boolean initialized = false;
    protected HasTabSheetBehaviour tabSheet;

    public MainTabSheetActionHandler(HasTabSheetBehaviour tabSheet) {
        this.tabSheet = tabSheet;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (!initialized) {
            Messages messages = AppBeans.get(Messages.NAME);

            closeAllTabs = new com.vaadin.event.Action(messages.getMainMessage("actions.closeAllTabs"));
            closeOtherTabs = new com.vaadin.event.Action(messages.getMainMessage("actions.closeOtherTabs"));
            closeCurrentTab = new com.vaadin.event.Action(messages.getMainMessage("actions.closeCurrentTab"));
            showInfo = new com.vaadin.event.Action(messages.getMainMessage("actions.showInfo"));
            analyzeLayout = new com.vaadin.event.Action(messages.getMainMessage("actions.analyzeLayout"));
            saveSettings = new com.vaadin.event.Action(messages.getMainMessage("actions.saveSettings"));
            restoreToDefaults = new com.vaadin.event.Action(messages.getMainMessage("actions.restoreToDefaults"));

            initialized = true;
        }

        List<Action> actions = new ArrayList<>(5);
        actions.add(closeCurrentTab);
        actions.add(closeOtherTabs);
        actions.add(closeAllTabs);

        if (target != null) {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
            if (clientConfig.getManualScreenSettingsSaving()) {
                actions.add(saveSettings);
                actions.add(restoreToDefaults);
            }

            UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
            UserSession userSession = sessionSource.getUserSession();
            if (userSession.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION) &&
                    findEditor((Layout) target) != null) {
                actions.add(showInfo);
            }
            if (clientConfig.getLayoutAnalyzerEnabled()) {
                actions.add(analyzeLayout);
            }
        }

        return actions.toArray(new Action[0]);
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        TabSheetBehaviour tabSheetBehaviour = tabSheet.getTabSheetBehaviour();
        if (initialized) {
            if (closeCurrentTab == action) {
                tabSheetBehaviour.closeTab((com.vaadin.ui.Component) target);
            } else if (closeOtherTabs == action) {
                tabSheetBehaviour.closeOtherTabs((com.vaadin.ui.Component) target);
            } else if (closeAllTabs == action) {
                tabSheetBehaviour.closeAllTabs();
            } else if (showInfo == action) {
                showInfo(target);
            } else if (analyzeLayout == action) {
                analyzeLayout(target);
            } else if (saveSettings == action) {
                saveSettings(target);
            } else if (restoreToDefaults == action) {
                restoreToDefaults(target);
            }
        }
    }

    protected void showInfo(Object target) {
        AbstractEditor editor = (AbstractEditor) findEditor((Layout) target);
        Entity entity = editor.getItem();

        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getSession().getClass(entity.getClass());

        new ShowInfoAction().showInfo(entity, metaClass, editor);
    }

    protected void analyzeLayout(Object target) {
        Window window = findWindow((Layout) target);
        if (window != null) {
            LayoutAnalyzer analyzer = new LayoutAnalyzer();
            List<LayoutTip> tipsList = analyzer.analyze(window);

            if (tipsList.isEmpty()) {
                Notifications notifications = ComponentsHelper.getScreenContext(window).getNotifications();

                notifications.create()
                        .setCaption("No layout problems found")
                        .setType(Notifications.NotificationType.HUMANIZED)
                        .show();
            } else {
                WindowManager wm = (WindowManager) ComponentsHelper.getScreenContext(window).getScreens();
                WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo("layoutAnalyzer");

                wm.openWindow(windowInfo, WindowManager.OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
            }
        }
    }

    @Nullable
    protected com.haulmont.cuba.gui.components.Window getWindow(Object target) {
        if (target instanceof Layout) {
            Layout layout = (Layout) target;
            for (Component component : layout) {
                if (component instanceof WindowBreadCrumbs) {
                    WindowBreadCrumbs breadCrumbs = (WindowBreadCrumbs) component;
                    return breadCrumbs.getCurrentWindow();
                }
            }
        }

        return null;
    }

    protected void restoreToDefaults(Object target) {
        com.haulmont.cuba.gui.components.Window window = getWindow(target);
        if (window != null) {
            window.deleteSettings();
        }
    }

    protected void saveSettings(Object target) {
        com.haulmont.cuba.gui.components.Window window = getWindow(target);
        if (window != null) {
            window.saveSettings();
        }
    }

    protected com.haulmont.cuba.gui.components.Window.Editor findEditor(Layout layout) {
        for (Object component : layout) {
            if (component instanceof WindowBreadCrumbs) {
                WindowBreadCrumbs breadCrumbs = (WindowBreadCrumbs) component;
                if (breadCrumbs.getCurrentWindow() instanceof Window.Editor)
                    return (Window.Editor) breadCrumbs.getCurrentWindow();
            }
        }
        return null;
    }

    protected com.haulmont.cuba.gui.components.Window findWindow(Layout layout) {
        for (Object component : layout) {
            if (component instanceof WindowBreadCrumbs) {
                WindowBreadCrumbs breadCrumbs = (WindowBreadCrumbs) component;
                if (breadCrumbs.getCurrentWindow() != null) {
                    return breadCrumbs.getCurrentWindow();
                }
            }
        }
        return null;
    }
}