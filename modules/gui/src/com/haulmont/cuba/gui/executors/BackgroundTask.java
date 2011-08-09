/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import java.util.List;

/**
 * Backround task for execute in {@link BackgroundWorker}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public abstract class BackgroundTask<T> {

    private ProgressHandler<T> progressHandler;

    /**
     * Main tasks method
     */
    public abstract void run();

    /**
     * Task completed handler
     */
    public void done() {
    }

    /**
     * Task canceled handler
     */
    public void canceled(){
    }

    /**
     * Task cancel handler
     */
    public void beforeCancel(){
    }

    /**
     * Publish changes from working thread
     * @param changes Changes
     */
    public final void publish(T ... changes) {
        progressHandler.handleProgress(changes);
    }

    /**
     * On progress change
     * @param changes Changes list
     */
    public void progress(List<T> changes) {
    }

    public ProgressHandler<T> getProgressHandler() {
        return progressHandler;
    }

    public void setProgressHandler(ProgressHandler<T> progressHandler) {
        this.progressHandler = progressHandler;
    }
}