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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenMode;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.settings.Settings;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Represents an independent screen opened inside the main application window.
 *
 */
public interface Window extends Frame, Component.HasCaption, Component.HasIcon {

    /**
     * Name that is used to register a client type specific screen implementation in
     * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
     */
    String NAME = "window";

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

    /**
     * @return current window context
     */
    @Override
    WindowContext getContext();

    /**
     * Add a listener that will be notified when this screen is closed.
     *
     * @deprecated Use {@link #addCloseListener(CloseListener)}
     * @param listener listener instance
     */
    @Deprecated
    void addListener(CloseListener listener);
    @Deprecated
    void removeListener(CloseListener listener);

    /**
     * Add a listener that will be notified when this screen is closed.
     * @param listener listener instance
     */
    void addCloseListener(CloseListener listener);
    void removeCloseListener(CloseListener listener);

    /**
     * Add a listener that will be notified when this screen is closed with actionId {@link #COMMIT_ACTION_ID}.
     * @param listener listener instance
     */
    void addCloseWithCommitListener(CloseWithCommitListener listener);
    void removeCloseWithCommitListener(CloseWithCommitListener listener);

    /** This method is called by the framework after opening the screen to apply user settings to all components. */
    void applySettings(Settings settings);

    /** This method is called by the framework when closing the screen
     * to save user settings if they have been changed. */
    void saveSettings();

    /** This method is called by the framework on reset to defaults action */
    void deleteSettings();

    /**
     * Set a component to be focused after the screen is opened.
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
    Settings getSettings();

    /**
     * Close the screen.
     * <p/> If the screen has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * the confirmation dialog will be shown.
     *
     * @param actionId action ID that will be propagated to attached {@link CloseListener}s.
     *                 Use {@link #COMMIT_ACTION_ID} if some changes have just been committed, or
     *                 {@link #CLOSE_ACTION_ID} otherwise. These constants are recognized by various mechanisms of the
     *                 framework.
     */
    boolean close(String actionId);

    /** Close the screen.
     * <p/> If the window has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * and force=false, the confirmation dialog will be shown.
     *
     * @param actionId action ID that will be propagated to attached {@link CloseListener}s.
     *                 Use {@link #COMMIT_ACTION_ID} if some changes have just been committed, or
     *                 {@link #CLOSE_ACTION_ID} otherwise. These constants are recognized by various mechanisms of the
     *                 framework.
     * @param force    if true, no confirmation dialog will be shown even if the screen has uncommitted changes
     */
    boolean close(String actionId, boolean force);

    /** INTERNAL. Don't call from application code. */
    void closeAndRun(String actionId, Runnable runnable);

    /**
     * Add a {@link Timer} component to this screen.
     * <p/> This method is called when a timer is created from XML descriptor. It should also be called from an
     * application code if the timer is created programmatically by {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory#createTimer()} method.
     * <p/> The timer added to the window is stopped when the window is closed.
     *
     * @param timer Timer instance
     */
    void addTimer(Timer timer);

    /**
     * Returns a {@link Timer} added to this screen.
     * @param id timer ID
     * @return timer instance or null if not found
     */
    @Nullable
    Timer getTimer(String id);

    /**
     * Check validity by invoking validators on specified components which support them
     * and show validation result notification.
     * @return true if the validation was succesful, false if there were any problems
     */
    boolean validate(List<Validatable> fields);

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification.
     * @return true if the validation was succesful, false if there were any problems
     */
    boolean validateAll();

    /**
     * @return window manager instance
     */
    WindowManager getWindowManager();

    /** INTERNAL. Don't call from application code. */
    void setWindowManager(WindowManager windowManager);

    /**
     * @return dialog options of window. Options will be applied only if window opened with {@link OpenMode#DIALOG}.
     */
    DialogOptions getDialogOptions();

    /**
     * Represents a window that can be committed on close.
     * <p/>
     * Implement this interface in controller if you want to support saving uncommitted changes on window close.
     * {@link AbstractEditor} already implements it.
     */
    interface Committable extends Window {

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
     * Represents an edit screen.
     */
    interface Editor extends Committable {

        /**
         * Name that is used to register a client type specific screen implementation in
         * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
         */
        String NAME = "window.editor";

        /**
         * Name of action that commits changes.
         * <p/> If the screen doesn't contain a component with {@link #WINDOW_COMMIT_AND_CLOSE} ID, this action also
         * closes the screen after commit.
         */
        String WINDOW_COMMIT = "windowCommit";

        /** Name of action that commits changes and closes the screen. */
        String WINDOW_COMMIT_AND_CLOSE = "windowCommitAndClose";

        /** Name of action that closes the screen. */
        String WINDOW_CLOSE = "windowClose";

        /**
         * @return currently edited entity instance
         */
        Entity getItem();

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
         * Called by the framework to set an edited entity after creation of all components and datasources, and
         * after <code>init()</code>.
         * @param item  entity instance
         */
        void setItem(Entity item);

        /**
         * Called by the framework to validate the screen components and commit changes.
         * @return true if commit was succesful
         */
        boolean commit();

        /**
         * Called by the framework to commit changes with optional validation.
         * @param validate false to avoid validation
         * @return true if commit was succesful
         */
        boolean commit(boolean validate);

        /**
         * Called by the framework to validate, commit and close the screen if commit was successful.
         * <p/> Passes {@link #COMMIT_ACTION_ID} to associated {@link CloseListener}s.
         */
        @Override
        void commitAndClose();

        /**
         * @return true if the edited item has been pessimistically locked when the screen is opened
         */
        boolean isLocked();
    }

    /**
     * Represents a lookup screen.
     */
    interface Lookup extends Window {

        /**
         * Name that is used to register a client type specific screen implementation in
         * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
         */
        String NAME = "window.lookup";

        /**
         * @return component that is used to lookup entity instances
         */
        Component getLookupComponent();

        /** Set component that is used to lookup entity instances. */
        void setLookupComponent(Component lookupComponent);

        /**
         * @return current lookup handler
         */
        @Nullable
        Handler getLookupHandler();

        /**
         * Set a lookup handler.
         * @param handler   handler implementation
         */
        void setLookupHandler(Handler handler);

        /**
         * @return current lookup validator
         */
        @Nullable
        Validator getLookupValidator();

        /**
         * Set a lookup validator
         * @param validator validator implementation
         */
        void setLookupValidator(Validator validator);

        /**
         * Callback interface to receive selected entities.
         * <p/> Implementations of this interface must be passed to {@link #openLookup} methods or set directly in
         * the screen instance via {@link #setLookupHandler}.
         */
        interface Handler {
            /**
             * Called upon selection.
             * @param items selected entity instances
             */
            void handleLookup(Collection items);
        }

        /**
         * Callback interface to validate the lookup screen upon selection before calling
         * {@link Handler#handleLookup(java.util.Collection)} method.
         * <p/> Implementations of this interface must be set in the screen instance via {@link #setLookupValidator}.
         */
        interface Validator {
            /**
             * Called upon selection.
             * @return true to proceed with selection, false to interrupt the selection and don't close the screen
             */
            boolean validate();
        }
    }

    /**
     * Marker interface implemented by top-level windows of the application: login window and main window. Only one
     * top-level window exists at a time, depending on the connection state.
     */
    interface TopLevelWindow extends Window {
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

    interface MainWindow extends TopLevelWindow, HasWorkArea, HasUserIndicator, HasFoldersPane {
    }

    /**
     * Listener to be notified when a screen is closed.
     */
    interface CloseListener {
        /**
         * Called when a screen is closed.
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

    /**
     * INTERNAL.
     * Interface implemented by screen controllers which are not themselves windows,
     * but has {@link Window} interface and delegate work to wrapped real window.
     */
    interface Wrapper {
        Window getWrappedWindow();
    }
}
