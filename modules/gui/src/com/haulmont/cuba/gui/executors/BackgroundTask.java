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
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Background task for execute in {@link BackgroundWorker}
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
@SuppressWarnings("unused")
public abstract class BackgroundTask<T, V> {

    private final Window ownerWindow;

    private final long timeoutMilliseconds;

    private final List<ProgressListener<T, V>> progressListeners =
            Collections.synchronizedList(new ArrayList<ProgressListener<T, V>>());

    /**
     * Task with timeout
     *
     * @param timeout     Timeout
     * @param timeUnit    Time unit
     * @param ownerWindow Owner window
     */
    protected BackgroundTask(long timeout, TimeUnit timeUnit, Window ownerWindow) {
        this.ownerWindow = ownerWindow;
        this.timeoutMilliseconds = timeUnit.toMillis(timeout);
    }

    /**
     * Task with timeout
     *
     * @param timeout  Timeout
     * @param timeUnit Time unit
     */
    protected BackgroundTask(long timeout, TimeUnit timeUnit) {
        this.ownerWindow = null;
        this.timeoutMilliseconds = timeUnit.toMillis(timeout);
    }

    /**
     * Task with timeout in default SECONDS unit
     *
     * @param timeoutSeconds Timeout in seconds
     */
    protected BackgroundTask(long timeoutSeconds) {
        this.ownerWindow = null;
        this.timeoutMilliseconds = TimeUnit.SECONDS.toMillis(timeoutSeconds);
    }

    /**
     * Task with timeout in default SECONDS unit
     *
     * @param timeoutSeconds Timeout in seconds
     * @param ownerWindow    Owner window
     */
    protected BackgroundTask(long timeoutSeconds, Window ownerWindow) {
        this.ownerWindow = ownerWindow;
        this.timeoutMilliseconds = TimeUnit.SECONDS.toMillis(timeoutSeconds);
    }

    /**
     * Main tasks method
     *
     * @param taskLifeCycle Task life cycle
     * @return Result
     * @throws Exception exception in worker thread
     */
    public abstract V run(TaskLifeCycle<T> taskLifeCycle) throws Exception;

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
     * Task canceled by watchdog handler
     */
    public void timeoutExceeded() {
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

    /**
     * Synchronous get parameters for run from UI
     *
     * @return Run parameters
     */
    public Map<String, Object> getParams() {
        return null;
    }

    public final Window getOwnerWindow() {
        return ownerWindow;
    }

    public final long getTimeoutMilliseconds() {
        return timeoutMilliseconds;
    }

    public final long getTimeoutSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(timeoutMilliseconds);
    }

    public final void addProgressListener(ProgressListener<T, V> progressListener) {
        if (!progressListeners.contains(progressListener))
            progressListeners.add(progressListener);
    }

    public final List<ProgressListener<T, V>> getProgressListeners() {
        return new ArrayList<>(progressListeners);
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