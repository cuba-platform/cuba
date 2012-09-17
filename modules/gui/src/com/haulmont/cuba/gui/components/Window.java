/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:11:57
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.settings.Settings;

import java.util.Collection;

/**
 * Represents an independent window
 */
public interface Window extends IFrame, Component.HasCaption {

    String NAME = "window";

    /** Standard actionId passed to {@link CloseListener}s after succesful commit */
    String COMMIT_ACTION_ID = "commit";

    /** Standard actionId passed to {@link CloseListener}s after close */
    String CLOSE_ACTION_ID = "close";

    void addListener(CloseListener listener);
    void removeListener(CloseListener listener);

    /** Apply user settings to all components of this window */
    void applySettings(Settings settings);

    /** Save this window user settings if they have been changed */
    void saveSettings();

    /** Set focus to component **/
    void setFocusComponent(String componentId);

    /** Get this window user settings */
    Settings getSettings();

    /** Close this window.
     * If the window has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * the confirmation dialog will be showed.
     * @param actionId action ID will be propagated to {@link CloseListener}s
     */
    boolean close(String actionId);

    /** Close this window.
     * If the window has uncommitted changes in its {@link com.haulmont.cuba.gui.data.DsContext},
     * and force=false, the confirmation dialog will be shown.
     * @param actionId action ID will be propagated to {@link CloseListener}s
     * @param force if true, no confirmation dialog will be shown in any case
     */
    boolean close(String actionId, boolean force);

    /*
     * Needed by bmc
     */
    void closeAndRun(String actionId, Runnable runnable);

    /**
     * Assign a {@link Timer} component to this window.
     * @param timer Timer component
     */
    void addTimer(Timer timer);

    /**
     * Returns a {@link Timer} assigned to this window by it's own ID
     * @param id Timer ID
     * @return timer or null if not found
     */
    Timer getTimer(String id);

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification.
     * @return true if the validation was succesful, false if there were any problems
     */
    boolean validateAll();

    /**
     * Returns current {@link WindowManager} of this window
     * @return window manager
     */
    WindowManager getWindowManager();

    /**
     * Assign {@link WindowManager} to this window
     *
     * @param windowManager
     */
    void setWindowManager(WindowManager windowManager);

    /**
     * Window intended for editing an entity instance
     */
    interface Editor extends Window {

        String NAME = "window.editor";

        String WINDOW_COMMIT = "windowCommit";
        String WINDOW_COMMIT_AND_CLOSE = "windowCommitAndClose";
        String WINDOW_CLOSE = "windowClose";

        /**
         * @return edited entity
         */
        Entity getItem();

        /** 
         * Set parent datasource to commit into this datasource instead of database.
         * This method must be followed by {@link #setItem(com.haulmont.cuba.core.entity.Entity)}
         */
        void setParentDs(Datasource parentDs);

        /**
         * Set edited entity. Invoked by the framework after opening the window.
         * @param item  entity instance
         */
        void setItem(Entity item);

        /**
         * Validate and commit changes.
         * @return true if commit was succesful
         */
        boolean commit();

        /**
         * Commit changes with optional validation.
         * @param validate false to avoid validation
         * @return true if commit was succesful
         */
        boolean commit(boolean validate);

        /**
         * Validate, commit and close the window if commit was successful.
         * Passes {@link #COMMIT_ACTION_ID} to associated {@link CloseListener}s
         */
        void commitAndClose();

        /**
         * Check whether the item was pessimistically locked when editor was opened
         */
        boolean isLocked();

    }

    /**
     * Window intended for looking up entities
     */
    interface Lookup extends Window {

        String NAME = "window.lookup";

        String LOOKUP_ITEM_CLICK_ACTION_ID = "lookupItemClickAction";

        String LOOKUP_ENTER_PRESSED_ACTION_ID="lookupEnterPressed";

        String LOOKUP_SELECTED_ACTION_ID="lookupAction";

        /** Component showing a list of entities to look up from it */
        Component getLookupComponent();

        /** Set component showing a list of entities to look up from it. Usually defined in XML descriptor */
        void setLookupComponent(Component lookupComponent);

        /**
         * Interface implemented by invoking code to receive selected entities
         */
        interface Handler {
            /** Invoked on selection and contains selected entities */
            void handleLookup(Collection items);
        }

        Handler getLookupHandler();
        void setLookupHandler(Handler handler);

        interface Validator {
            boolean validate();
        }

        Validator getLookupValidator();
        void setLookupValidator(Validator validator);
    }

    /**
     * Listener to window closing event
     */
    interface CloseListener {
        /**
         * Invoked after window was closed
         * @param actionId ID of action caused window to close
         */
        void windowClosed(String actionId);
    }

    /**
     * Interface implemented by screen controllers which are not themselves windows,
     * but has {@link Window} interface and delegate work to wrapped real window
     */
    interface Wrapper {
        <T extends Window> T getWrappedWindow();
    }
}
