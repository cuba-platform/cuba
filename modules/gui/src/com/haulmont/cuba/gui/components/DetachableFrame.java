/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * Represents detachable part of window
 *
 * @author devyatkin
 * @version $Id$
 */

public interface DetachableFrame extends IFrame {

    /**
     * Listener for frame attach/detach
     */
    public interface DetachListener {

        /**
         * Invoked after frame attached
         *
         * @param frame
         */
        void frameAttached(IFrame frame);

        /**
         * Invoked after frame detached
         *
         * @param frame
         */
        void frameDetached(IFrame frame);
    }

    /**
     * Detach frame to new Window.
     *
     * @param caption
     */
    void detachFrame(String caption);

    /**
     * Attach already detached frame to parent
     */
    void attachFrame();


    void addDetachListener(DetachListener listener);

    void removeDetachListener(DetachListener listener);

    boolean isDetached();
}
