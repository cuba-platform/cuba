/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.components.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Backround task for execute in {@link BackgroundWorker}
 * <p>
 * <b>It is strongly recommended to be able to interrupt working thread. <br/>
 * Don't ignore {@link InterruptedException} or its ancestors.</b>
 * </p>
 *
 * @param <T> measure unit which shows progress of task
 * @param <V> result type
 * @author artamonov
 * @version $Id$
 */
public abstract class BackgroundTask<T, V> {

    private final Window ownerWindow;

    private final List<ProgressListener<T, V>> progressListeners =
            Collections.synchronizedList(new ArrayList<ProgressListener<T, V>>());

    protected BackgroundTask(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    protected BackgroundTask() {
        this.ownerWindow = null;
    }

    /**
     * Main tasks method
     *
     * @param taskLifeCycle Task life cycle
     * @return Result
     */
    public abstract V run(TaskLifeCycle<T> taskLifeCycle);

    /**
     * Task completed handler
     *
     * @param result of execution
     */
    public void done(V result) {
    }

    /**
     * Task canceled handler
     */
    public void canceled() {
    }

    /**
     * Handle exception
     *
     * @param ex Exception
     */
    public void handleException(Exception ex) {
    }

    /**
     * On progress change
     *
     * @param changes Changes list
     */
    public void progress(List<T> changes) {
    }

    public final Window getOwnerWindow() {
        return ownerWindow;
    }

    public final void addProgressListener(ProgressListener<T, V> progressListener) {
        if (!progressListeners.contains(progressListener))
            progressListeners.add(progressListener);
    }

    public final List<ProgressListener<T, V>> getProgressListeners() {
        return Collections.unmodifiableList(progressListeners);
    }

    public final void removeProgressListener(ProgressListener<T, V> progressListener) {
        progressListeners.remove(progressListener);
    }

    /**
     * Listener for task life cycle
     *
     * @param <T> Progress unit
     * @param <V> Result
     */
    public interface ProgressListener<T, V> {
        /**
         * On task progress changed
         *
         * @param changes Progress units
         */
        void onProgress(List<T> changes);

        /**
         * On task completed
         *
         * @param result Result
         */
        void onDone(V result);

        /**
         * On task canceled
         */
        void onCancel();
    }

    public static class ProgressListenerAdapter<T, V> implements ProgressListener<T, V> {

        @Override
        public void onProgress(List<T> changes) {
        }

        @Override
        public void onDone(V result) {
        }

        @Override
        public void onCancel() {
        }
    }
}