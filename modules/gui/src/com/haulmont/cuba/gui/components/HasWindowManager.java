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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * The interface introduced only for compatibility with legacy code.
 */
@Deprecated
public interface HasWindowManager {

    /**
     * It is recommended to use {@link Screens} instead, it can be obtained from {@link ScreenContext}
     * of {@link FrameOwner}.
     */
    WindowManager getWindowManager();

    /**
     * Open a simple screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    default AbstractWindow openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractWindow) getWindowManager().openWindow(windowInfo, openType, params);
    }

    /**
     * Open a simple screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param openType    how to open the screen
     * @return created window
     */
    default AbstractWindow openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractWindow) getWindowManager().openWindow(windowInfo, openType);
    }

    /**
     * Open an edit screen for entity instance. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @return created window
     */
    default AbstractEditor openEditor(Entity item, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return (AbstractEditor) getWindowManager().openEditor(editorScreen, item, openType);
    }

    /**
     * Open an edit screen for entity instance. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    default AbstractEditor openEditor(Entity item, WindowManager.OpenType openType,
                                      Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return (AbstractEditor) getWindowManager().openEditor(editorScreen, item, openType, params);
    }

    /**
     * Open an edit screen for entity instance. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    default AbstractEditor openEditor(Entity item, WindowManager.OpenType openType,
                                      Map<String, Object> params, Datasource parentDs) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return (AbstractEditor) getWindowManager().openEditor(editorScreen, item, openType, params, parentDs);
    }

    /**
     * Open an edit screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    default AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                                      Map<String, Object> params, Datasource parentDs) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractEditor) getWindowManager().openEditor(windowInfo, item, openType, params, parentDs);
    }

    /**
     * Open an edit screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    default AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                                      Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractEditor) getWindowManager().openEditor(windowInfo, item, openType, params);
    }

    /**
     * Open an edit screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @param parentDs    if this parameter is not null, the editor will commit edited instance into this
     *                    datasource instead of directly to database
     * @return created window
     */
    default AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType,
                                      Datasource parentDs) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractEditor) getWindowManager().openEditor(windowInfo, item, openType, parentDs);
    }

    /**
     * Open an edit screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param item        entity to edit
     * @param openType    how to open the screen
     * @return created window
     */
    default AbstractEditor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractEditor) getWindowManager().openEditor(windowInfo, item, openType);
    }

    /**
     * Open a lookup screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param entityClass required class of entity
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    default AbstractLookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler,
                                      WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        return (AbstractLookup) getWindowManager().openLookup(lookupScreen, handler, openType);
    }

    /**
     * Open a lookup screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param entityClass required class of entity
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    default AbstractLookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler,
                                      WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        return (AbstractLookup) getWindowManager().openLookup(lookupScreen, handler, openType, params);
    }

    /**
     * Open a lookup screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @param params      parameters to pass to {@code init()} method of the screen's controller
     * @return created window
     */
    default AbstractLookup openLookup(String windowAlias, Window.Lookup.Handler handler,
                                      WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractLookup) getWindowManager().openLookup(windowInfo, handler, openType, params);
    }

    /**
     * Open a lookup screen. <br> It is recommended to use {@link ScreenBuilders} bean instead.
     *
     * @param windowAlias screen ID as defined in {@code screens.xml}
     * @param handler     is invoked when selection confirmed and the lookup screen closes
     * @param openType    how to open the screen
     * @return created window
     */
    default AbstractLookup openLookup(String windowAlias, Window.Lookup.Handler handler,
                                      WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return (AbstractLookup) getWindowManager().openLookup(windowInfo, handler, openType);
    }

    /**
     * Load a frame registered in {@code screens.xml} and optionally show it inside a parent component of this
     * frame. <br> It is recommended to use {@link Fragments} bean instead.
     *
     * @param parent        if specified, all parent's sub components will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in {@code screens.xml}
     * @return              frame's controller instance
     */
    default AbstractFrame openFrame(@Nullable Component parent, String windowAlias) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

        Frame parentFrame;
        if (this instanceof LegacyFrame) {
            parentFrame = ((LegacyFrame) this).getWrappedFrame();
        } else {
            parentFrame = ((Frame) this);
        }

        return (AbstractFrame) getWindowManager().openFrame(parentFrame, parent, windowInfo);
    }

    /**
     * Load a frame registered in {@code screens.xml} and optionally show it inside a parent component of this
     * frame. <br> It is recommended to use {@link Fragments} bean instead.
     *
     * @param parent        if specified, all parent's sub components will be removed and the frame will be added
     * @param windowAlias   frame ID as defined in {@code screens.xml}
     * @param params        parameters to be passed into the frame's controller {@code init} method
     * @return              frame's controller instance
     */
    default AbstractFrame openFrame(@Nullable Component parent, String windowAlias, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

        Frame parentFrame;
        if (this instanceof LegacyFrame) {
            parentFrame = ((LegacyFrame) this).getWrappedFrame();
        } else {
            parentFrame = ((Frame) this);
        }

        return (AbstractFrame) getWindowManager().openFrame(parentFrame, parent, windowInfo, params);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Show message dialog with title and message. <br>
     * Message supports line breaks ({@code \n}). <br>
     * It is recommended to use {@link Dialogs} bean instead.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    default void showMessageDialog(String title, String message, Frame.MessageType messageType) {
        getWindowManager().showMessageDialog(title, message, messageType);
    }

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
    default void showOptionDialog(String title, String message, Frame.MessageType messageType, Action[] actions) {
        getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    /**
     * Show options dialog with title and message. <br>
     * Message supports line breaks ({@code \n}). <br>
     * It is recommended to use {@link Dialogs} bean instead.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     * @param actions     list of actions that represent options. For standard options consider use of
     *                    {@link DialogAction} instances.
     */
    default void showOptionDialog(String title, String message, Frame.MessageType messageType, List<Action> actions) {
        getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[0]));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Show notification with {@link Frame.NotificationType#HUMANIZED}. <br>
     * Supports line breaks ({@code \n}). <br>
     * It is recommended to use {@link Notifications} bean instead.
     *
     * @param caption notification text
     */
    default void showNotification(String caption) {
        getWindowManager().showNotification(caption);
    }

    /**
     * Show notification. <br>
     * Supports line breaks ({@code \n}) for non HTML type. <br>
     * It is recommended to use {@link Notifications} bean instead.
     *
     * @param caption notification text
     * @param type    defines how to display the notification.
     *                Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    default void showNotification(String caption, Frame.NotificationType type) {
        getWindowManager().showNotification(caption, type);
    }

    /**
     * Show notification with caption and description. <br>
     * Supports line breaks ({@code \n}) for non HTML type. <br>
     * It is recommended to use {@link Notifications} bean instead.
     *
     * @param caption     notification text
     * @param description notification description
     * @param type        defines how to display the notification.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    default void showNotification(String caption, String description, Frame.NotificationType type) {
        getWindowManager().showNotification(caption, description, type);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Open a web page in browser. <br> It is recommended to use {@link WebBrowserTools} bean instead.
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
    default void showWebPage(String url, @Nullable Map<String, Object> params) {
        getWindowManager().showWebPage(url, params);
    }
}