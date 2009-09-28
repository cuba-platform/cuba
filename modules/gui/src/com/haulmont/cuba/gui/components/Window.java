/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:11:57
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * Represents an independent window
 */
public interface Window extends IFrame, Component.HasCaption, Component.ActionsHolder {

    /** Standard actionId passed to {@link CloseListener}s after succesful commit */
    String COMMIT_ACTION_ID = "commit";

    void addListener(CloseListener listener);
    void removeListener(CloseListener listener);

    /** Apply user settings to all components of this window */
    void applySettings(Settings settings);

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

    /**
     * Assign a {@link Timer} component to this window.
     * @param timer Timer component
     */
    void addTimer(Timer timer);

    /**
     * Returns a {@link Timer} assigned to this window by it's own ID
     * @param id Timer ID
     */
    Timer getTimer(String id);

    /**
     * Window intended for editing an entity instance
     */
    interface Editor extends Window {

        /** Get edited entity  */
        Entity getItem();

        /** 
         * Set parent datasource to commit into this datasource instead of database.
         * This method must be followed by {@link #setItem(com.haulmont.cuba.core.entity.Entity)}
         */
        void setParentDs(Datasource parentDs);

        /** Set edited entity. Invoked by the framework on opening the window. */
        void setItem(Entity item);

        /** Check validity by invoking validators on all components which support them */
        boolean isValid();

        /** Check validity by invoking validators on all components which support them */
        void validate() throws ValidationException;

        /** Validate and commit changes */
        boolean commit();

        /** Commit changes with optional validating */
        boolean commit(boolean validate);

        /**
         * Validate, commit and close if commit was successful.
         * Passes {@link #COMMIT_ACTION_ID} to associated {@link CloseListener}s
         */
        void commitAndClose();
    }

    /**
     * Window intended for looking up entities
     */
    interface Lookup extends Window {

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
