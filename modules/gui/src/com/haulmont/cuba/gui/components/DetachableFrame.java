/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * Represents detachable part of window
 *
 * @author devyatkin
 * @version $Id$
 */
public interface DetachableFrame extends Frame {

    /**
     * Listener for frame attach/detach
     */
    interface DetachListener {

        /**
         * Invoked after frame attached
         *
         * @param frame
         */
        void frameAttached(Frame frame);

        /**
         * Invoked after frame detached
         *
         * @param frame
         */
        void frameDetached(Frame frame);
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