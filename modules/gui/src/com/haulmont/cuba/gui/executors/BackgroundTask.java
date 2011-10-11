/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.components.Window;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Backround task for execute in {@link BackgroundWorker}
 * <p>
 * <b>It is strongly recommended to be able to interrupt working thread, <br/>
 * don't ignore {@link InterruptedException} or its ancestors</b>
 * </p>
 * <p>$Id$</p>
 *
 * @param <T> measure unit which shows progress of task
 * @param <V> result type
 * @author artamonov
 */
public abstract class BackgroundTask<T, V> {

    private ProgressHandler<T> progressHandler;
    private Window ownerWindow;

    private List<ProgressListener<T,V>> progressListeners = new LinkedList<ProgressListener<T,V>>();

    private volatile boolean isInterrupted = false;

    protected BackgroundTask(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    /**
     * Main tasks method
     * @return Result
     */
    public abstract V run();

    /**
     * Task completed handler
     * @param result of execution
     */
    public void done(V result) {
    }

    /**
     * Task canceled handler
     */
    public void canceled(){
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

    public Window getOwnerWindow() {
        return ownerWindow;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(boolean interrupted) {
        isInterrupted = interrupted;
    }

    public void addProgressListener(ProgressListener<T, V> progressListener) {
        if (!progressListeners.contains(progressListener))
            progressListeners.add(progressListener);
    }

    public List<ProgressListener<T,V>> getProgressListeners() {
        return Collections.unmodifiableList(progressListeners);
    }

    public void removeProgressListener(ProgressListener<T, V> progressListener) {
        progressListeners.remove(progressListener);
    }

    /**
     * Listener for task life cycle
     * @param <T> Progress unit
     * @param <V> Result
     */
    public interface ProgressListener<T, V> {
        /**
         * On task progress changed
         * @param changes Progress units
         */
        void onProgress(List<T> changes);

        /**
         * On task completed
         * @param result Result
         */
        void onDone(V result);

        /**
         * On task canceled
         */
        void onCancel();
    }
}