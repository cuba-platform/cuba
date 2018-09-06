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

import com.haulmont.bali.events.EventHub;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.compatibility.AfterCloseListenerAdapter;
import com.haulmont.cuba.gui.components.compatibility.CloseListenerAdapter;
import com.haulmont.cuba.gui.components.compatibility.SelectHandlerAdapter;
import com.haulmont.cuba.gui.components.compatibility.SelectValidatorAdapter;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.Screen.AfterCloseEvent;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents an independent screen opened inside the main application window.
 */
public interface Window extends Frame, Component.HasCaption, Component.HasIcon {
    /**
     * Constant that should be passed to {@link #close(String)} and {@link #close(String, boolean)} methods when
     * the screen is closed after commit of changes. Propagated to {@link CloseListener#windowClosed}.
     */
    String COMMIT_ACTION_ID = "commit";

    /**
     * Constant that should be passed to {@link #close(String)} and {@link #close(String, boolean)} methods when
     * the screen is closed without commit. Propagated to {@link CloseListener#windowClosed}.
     */
    String CLOSE_ACTION_ID = "close";

    /**
     * Constant that passed to {@link #close(String)} and {@link #close(String, boolean)} methods when
     * the lookup screen is closed with selected items. Propagated to {@link CloseListener#windowClosed}.
     */
    String SELECT_ACTION_ID = "select";

    String BROWSE_WINDOW_SUFFIX = ".browse";

    String LOOKUP_WINDOW_SUFFIX = ".lookup";

    String EDITOR_WINDOW_SUFFIX = ".edit";

    String CREATE_WINDOW_SUFFIX = ".create";

    void setCloseable(boolean closeable);
    boolean isCloseable();

    @Override
    Screen getFrameOwner();

    /**
     * @return current window context
     */
    @Override
    WindowContext getContext();

    /**
     * Add a listener that will be notified when this screen is closed.
     *
     * @param listener listener instance
     * @deprecated Use {@link Screen#addAfterCloseListener(Consumer)} instead.
     */
    @Deprecated
    default void addListener(CloseListener listener) {
        getFrameOwner().addAfterCloseListener(new AfterCloseListenerAdapter(listener));
    }

    @Deprecated
    default void removeListener(CloseListener listener) {
        EventHub eventHub = UiControllerUtils.getEventHub(getFrameOwner());
        eventHub.unsubscribe(AfterCloseEvent.class, new AfterCloseListenerAdapter(listener));
    }

    /**
     * Add a listener that will be notified when this screen is closed.
     *
     * @param listener listener instance
     * @deprecated Use {@link Screen#addAfterCloseListener(Consumer)} instead.
     */
    @Deprecated
    default void addCloseListener(CloseListener listener) {
        getFrameOwner().addAfterCloseListener(new AfterCloseListenerAdapter(listener));
    }

    @Deprecated
    default void removeCloseListener(CloseListener listener) {
        EventHub eventHub = UiControllerUtils.getEventHub(getFrameOwner());
        eventHub.unsubscribe(AfterCloseEvent.class, new AfterCloseListenerAdapter(listener));
    }

    /**
     * Add a listener that will be notified when this screen is closed with actionId {@link #COMMIT_ACTION_ID}.
     *
     * @param listener listener instance
     * @deprecated Use {@link Screen#addAfterCloseListener(Consumer)} instead.
     */
    @Deprecated
    default void addCloseWithCommitListener(CloseWithCommitListener listener) {
        addCloseListener(new CloseListenerAdapter(listener));
    }

    @Deprecated
    default void removeCloseWithCommitListener(CloseWithCommitListener listener) {
        removeCloseListener(new CloseListenerAdapter(listener));
    }

    /**
     * This method is called by the framework after opening the screen to apply user settings to all components.
     */
    @Deprecated
    default void applySettings(Settings settings) {
        UiControllerUtils.applySettings(getFrameOwner(), settings);
    }

    /**
     * This method is called by the framework when closing the screen
     * to save user settings if they have been changed.
     */
    @Deprecated
    default void saveSettings() {
        UiControllerUtils.saveSettings(getFrameOwner());
    }

    /**
     * This method is called by the framework on reset to defaults action
     */
    @Deprecated
    default void deleteSettings() {
        UiControllerUtils.deleteSettings(getFrameOwner());
    }

    /**
     * Set a component to be focused after the screen is opened.
     *
     * @param componentId component's ID in XML. If null, then first focusable component will be focused
     */
    void setFocusComponent(String componentId);

    /**
     * @return an ID of the component which is set to be focused after the screen is opened
     */
    String getFocusComponent();

    /**
     * @return object encapsulating user settings for the current screen
     */
    @Deprecated
    default Settings getSettings() {
        return UiControllerUtils.getSettings(getFrameOwner());
    }

    /**
     * Close the screen.
     * <br> If the screen has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * the confirmation dialog will be shown.
     * <br> Don't override this method in subclasses, use hook {@link AbstractWindow#preClose(String)}
     *
     * @param actionId action ID that will be propagated to attached {@link CloseListener}s.
     *                 Use {@link #COMMIT_ACTION_ID} if some changes have just been committed, or
     *                 {@link #CLOSE_ACTION_ID} otherwise. These constants are recognized by various mechanisms of the
     *                 framework.
     */
    @Deprecated
    default boolean close(String actionId) {
        OperationResult result = getFrameOwner().close(new StandardCloseAction(actionId));
        return result.getStatus() == OperationResult.Status.SUCCESS;
    }

    /**
     * Close the screen.
     * <br> If the window has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * and force=false, the confirmation dialog will be shown.
     *
     * @param actionId action ID that will be propagated to attached {@link CloseListener}s.
     *                 Use {@link #COMMIT_ACTION_ID} if some changes have just been committed, or
     *                 {@link #CLOSE_ACTION_ID} otherwise. These constants are recognized by various mechanisms of the
     *                 framework.
     * @param force    if true, no confirmation dialog will be shown even if the screen has uncommitted changes
     */
    @Deprecated
    default boolean close(String actionId, boolean force) {
        OperationResult result = getFrameOwner().close(new StandardCloseAction(actionId, force));
        return result.getStatus() == OperationResult.Status.SUCCESS;
    }

    /**
     * INTERNAL. Don't call from application code.
     *
     * todo deprecate and use "Promise close()" method instead.
     */
    @Deprecated
    default void closeAndRun(String actionId, Runnable runnable) {
        getFrameOwner().close(new StandardCloseAction(actionId))
                .then(runnable);
    }

    /**
     * Add a {@link Timer} component to this screen.
     * <br> This method is called when a timer is created from XML descriptor. It should also be called from an
     * application code if the timer is created programmatically by {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory#createTimer()} method.
     * <br> The timer added to the window is stopped when the window is closed.
     *
     * @param timer Timer instance
     */
    void addTimer(Timer timer);

    /**
     * Returns a {@link Timer} added to this screen.
     *
     * @param id timer ID
     * @return timer instance or null if not found
     */
    @Nullable
    Timer getTimer(String id);

    /**
     * Check validity by invoking validators on specified components which support them
     * and show validation result notification.
     *
     * @return true if the validation was successful, false if there were any problems
     */
    boolean validate(List<Validatable> fields);

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification.
     *
     * @return true if the validation was successful, false if there were any problems
     */
    boolean validateAll();

    /**
     * @return window manager instance
     */
    WindowManager getWindowManager();

    /**
     * @return dialog options of window. Options will be applied only if window opened with {@link OpenMode#DIALOG}.
     */
    @Deprecated
    DialogOptions getDialogOptions();

    /**
     * Defines how the managed main TabSheet switches a tab with the given window: hides or unloads its content.
     */
    enum ContentSwitchMode {
        /**
         * Tab switching is determined by the managed main TabSheet mode (hide or unload content of a tab).
         */
        DEFAULT,
        /**
         * Tab content should be hidden not considering the TabSheet mode.
         */
        HIDE,
        /**
         * Tab content should be unloaded not considering the TabSheet mode.
         */
        UNLOAD
    }

    /**
     * Represents a window that can be committed on close.
     * <br>
     * todo support in AbstractWindow
     * Implement this interface in controller if you want to support saving uncommitted changes on window close.
     * {@link AbstractEditor} already implements it.
     */
    interface Committable {

        /**
         * @return whether the window contains uncommitted changes
         */
        boolean isModified();

        /**
         * Commit changes and close the window.
         */
        void commitAndClose();
    }

    /**
     * Only for compatibility with old screens.
     */
    @Deprecated
    interface Editor<T extends Entity> extends EditorScreen<T>, Window.Committable {
        /**
         * Name that is used to register a client type specific screen implementation in
         * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
         */
        @Deprecated
        String NAME = "window.editor";

        /**
         * @return currently edited entity instance
         */
        T getItem();

        /**
         * Called by the framework to set an edited entity after creation of all components and datasources, and
         * after <code>init()</code>.
         *
         * @param item entity instance
         */
        void setItem(Entity item);

        /**
         * Called by the framework to validate the screen components and commit changes.
         *
         * @return true if commit was successful
         */
        boolean commit();

        /**
         * Called by the framework to commit changes with optional validation.
         *
         * @param validate false to avoid validation
         * @return true if commit was successful
         */
        boolean commit(boolean validate);

        /**
         * @return parent datasource if it is set
         */
        @Nullable
        Datasource getParentDs();

        /**
         * This method is called by the framework to set parent datasource to commit into this datasource instead
         * of directly to the database.
         */
        void setParentDs(Datasource parentDs);

        /**
         * @return true if Editor will perform additional validation on {@link Window#validateAll()}
         * call using {@link com.haulmont.cuba.core.global.BeanValidation}.
         * @see com.haulmont.cuba.core.global.BeanValidation
         */
        boolean isCrossFieldValidate();

        /**
         * Enable/disable cross field validation on {@link Window#validateAll()} call. <br>
         * Cross field validation is triggered for item of main datasource with {@link UiCrossFieldChecks} group only
         * (without {@link javax.validation.groups.Default} group) when there are no other validation errors in UI components. <br>
         * <p>
         * Cross field validation is triggered before {@link AbstractWindow#postValidate} hook.
         *
         * @param crossFieldValidate cross field validate flag
         * @see com.haulmont.cuba.core.global.BeanValidation
         */
        void setCrossFieldValidate(boolean crossFieldValidate);
    }

    /**
     * Represents a lookup screen.
     */
    @Deprecated
    interface Lookup<T extends Entity> extends LookupScreen<T> {

        String LOOKUP_ITEM_CLICK_ACTION_ID = "lookupItemClickAction";
        String LOOKUP_ENTER_PRESSED_ACTION_ID = "lookupEnterPressed";
        String LOOKUP_SELECT_ACTION_ID = "lookupSelectAction";
        String LOOKUP_CANCEL_ACTION_ID = "lookupCancelAction";

        /**
         * Name that is used to register a client type specific screen implementation in
         * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
         */
        String NAME = "window.lookup";

        /**
         * @return component that is used to lookup entity instances
         */
        Component getLookupComponent();

        /**
         * Set component that is used to lookup entity instances.
         */
        void setLookupComponent(Component lookupComponent);

        /**
         * @return current lookup handler
         */
        @Nullable
        @Deprecated
        default Handler getLookupHandler() {
            if (getSelectHandler() == null) {
                return null;
            }

            return ((SelectHandlerAdapter) getSelectHandler()).getHandler();
        }

        /**
         * Set a lookup handler.
         *
         * @param handler handler implementation
         */
        @Deprecated
        default void setLookupHandler(Handler handler) {
            setSelectHandler(new SelectHandlerAdapter<>(handler));
        }

        /**
         * @return current lookup validator
         */
        @Deprecated
        @Nullable
        default Validator getLookupValidator() {
            if (getSelectValidator() == null) {
                return null;
            }
            return ((SelectValidatorAdapter<T>) getSelectValidator()).getValidator();
        }

        /**
         * Set a lookup validator
         *
         * @param validator validator implementation
         */
        @Deprecated
        default void setLookupValidator(Validator validator) {
            setSelectValidator(new SelectValidatorAdapter(validator));
        }

        /**
         * INTERNAL.
         * Invoked by the framework after creating the window to give it a chance to setup a specific layout.
         */
        void initLookupLayout();

        /**
         * Callback interface to receive selected entities.
         * <br> Implementations of this interface must be passed to
         * {@link LegacyFrame#openLookup} methods or set directly in
         * the screen instance via {@link #setLookupHandler}.
         */
        interface Handler {
            /**
             * Called upon selection.
             *
             * @param items selected entity instances
             */
            void handleLookup(Collection items);
        }

        /**
         * Callback interface to validate the lookup screen upon selection before calling
         * {@link Handler#handleLookup(java.util.Collection)} method.
         * <br> Implementations of this interface must be set in the screen instance via {@link #setLookupValidator}.
         */
        interface Validator {
            /**
             * Called upon selection.
             *
             * @return true to proceed with selection, false to interrupt the selection and don't close the screen
             */
            boolean validate();
        }
    }

    /**
     * Marker interface implemented by top-level windows of the application: login window and main window. Only one
     * top-level window exists at a time, depending on the connection state.
     *
     * @deprecated Is not required for screen controllers anymore
     */
    @Deprecated
    interface TopLevelWindow {
    }

    interface HasWorkArea {
        @Nullable
        AppWorkArea getWorkArea();
    }

    interface HasUserIndicator {
        @Nullable
        UserIndicator getUserIndicator();
    }

    interface HasFoldersPane {
        @Nullable
        FoldersPane getFoldersPane();
    }

    /**
     * @deprecated Use {@link MainScreen}
     */
    @Deprecated
    interface MainWindow extends MainScreen {
    }

    /**
     * Listener to be notified when a screen is closed.
     */
    interface CloseListener {
        /**
         * Called when a screen is closed.
         *
         * @param actionId ID of action caused the screen closing, passed here from {@link Window#close} methods
         */
        void windowClosed(String actionId);
    }

    /**
     * Listener to be notified when a screen is closed with actionId {@link #COMMIT_ACTION_ID}.
     */
    interface CloseWithCommitListener {
        /**
         * Called when a screen is closed with actionId {@link #CLOSE_ACTION_ID}.
         */
        void windowClosedWithCommitAction();
    }

    class BeforeCloseEvent extends EventObject {
        protected boolean closePrevented = false;

        /**
         * @param source the window to be closed
         */
        public BeforeCloseEvent(Window source) {
            super(source);
        }

        @Override
        public Window getSource() {
            return (Window) super.getSource();
        }

        public void preventWindowClose() {
            this.closePrevented = true;
        }

        public boolean isClosePrevented() {
            return closePrevented;
        }
    }

    /**
     * Listener to be notified when a screen is closed with {@link ClientConfig#getCloseShortcut()}.
     */
    @FunctionalInterface
    interface BeforeCloseWithShortcutListener {
        /**
         * Called when a screen is closed with {@link ClientConfig#getCloseShortcut()}.
         *
         * @param event an event providing more information
         */
        void beforeCloseWithShortcut(BeforeCloseWithShortcutEvent event);
    }

    /**
     * An event that is fired before a screen is closed with {@link ClientConfig#getCloseShortcut()}.
     */
    class BeforeCloseWithShortcutEvent extends BeforeCloseEvent {
        /**
         * @param source the window to be closed
         */
        public BeforeCloseWithShortcutEvent(Window source) {
            super(source);
        }
    }

    /**
     * Register a new before close with shortcut listener.
     *
     * @param listener the listener to register
     */
    void addBeforeCloseWithShortcutListener(BeforeCloseWithShortcutListener listener);

    /**
     * Removes a previously registered before close with shortcut listener.
     *
     * @param listener the listener to remove
     */
    void removeBeforeCloseWithShortcutListener(BeforeCloseWithShortcutListener listener);

    /**
     * Listener to be notified when a screen is closed with one of the following approaches:
     * screen's close button, bread crumbs, TabSheet tabs' close actions (Close, Close All, Close Others).
     */
    @FunctionalInterface
    interface BeforeCloseWithCloseButtonListener {
        /**
         * Called when a screen is closed with one of the following approaches:
         * screen's close button, bread crumbs, TabSheet tabs' close actions (Close, Close All, Close Others).
         *
         * @param event an event providing more information
         */
        void beforeCloseWithCloseButton(BeforeCloseWithCloseButtonEvent event);
    }

    /**
     * An event that is fired before a screen is closed with one of the following approaches:
     * screen's close button, bread crumbs, TabSheet tabs' close actions (Close, Close All, Close Others).
     */
    class BeforeCloseWithCloseButtonEvent extends BeforeCloseEvent {
        /**
         * @param source the window to be closed
         */
        public BeforeCloseWithCloseButtonEvent(Window source) {
            super(source);
        }
    }

    /**
     * Register a new before close with close button listener.
     *
     * @param listener the listener to register
     */
    void addBeforeCloseWithCloseButtonListener(BeforeCloseWithCloseButtonListener listener);

    /**
     * Removes a previously registered before close with close button listener.
     *
     * @param listener the listener to remove
     */
    void removeBeforeCloseWithCloseButtonListener(BeforeCloseWithCloseButtonListener listener);

    /**
     * INTERNAL.
     * Interface implemented by screen controllers which are not themselves windows,
     * but has {@link Window} interface and delegate work to wrapped real window.
     */
    @Deprecated
    interface Wrapper {
        Window getWrappedWindow();
    }
}