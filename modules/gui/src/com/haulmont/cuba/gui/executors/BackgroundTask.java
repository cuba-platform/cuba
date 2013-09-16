/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Background task for execute by {@link BackgroundWorker}.
 * <p/> If the task is associated with a screen through ownerFrame constructor parameter, it will be canceled when
 * the screen is closed.
 * <p/> If timeout passed to constructor is exceeded, the task is canceled by special {@link WatchDog} thread.
 *
 * <p/> Simplest usage example:
 * <pre>
 *    BackgroundTask<Integer, Void> task = new BackgroundTask<Integer, Void>(10, this) {
 *        public Void run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
 *            for (int i = 0; i < 5; i++) {
 *                TimeUnit.SECONDS.sleep(1);
 *            }
 *            return null;
 *        }
 *    };
 *    BackgroundTaskHandler taskHandler = backgroundWorker.handle(task);
 *    taskHandler.execute();
 * </pre>
 *
 * @param <T> task progress measurement unit
 * @param <V> result type
 * @author artamonov
 * @version $Id$
 */
@SuppressWarnings("unused")
public abstract class BackgroundTask<T, V> {

    private final IFrame ownerFrame;

    private final long timeoutMilliseconds;

    private final List<ProgressListener<T, V>> progressListeners =
            Collections.synchronizedList(new ArrayList<ProgressListener<T, V>>());

    /**
     * Create a task with timeout.
     *
     * @param timeout     timeout
     * @param timeUnit    timeout time unit
     * @param ownerFrame
     */
    protected BackgroundTask(long timeout, TimeUnit timeUnit, IFrame ownerFrame) {
        this.ownerFrame = ownerFrame;
        this.timeoutMilliseconds = timeUnit.toMillis(timeout);
    }

    /**
     * Create a task with timeout.
     * <p/> The task will not be associated with any window.
     *
     * @param timeout  timeout
     * @param timeUnit timeout time unit
     */
    protected BackgroundTask(long timeout, TimeUnit timeUnit) {
        this.ownerFrame = null;
        this.timeoutMilliseconds = timeUnit.toMillis(timeout);
    }

    /**
     * Create a task with timeout in default SECONDS unit.
     * <p/> The task will not be associated with any window.
     *
     * @param timeoutSeconds timeout in seconds
     */
    protected BackgroundTask(long timeoutSeconds) {
        this.ownerFrame = null;
        this.timeoutMilliseconds = TimeUnit.SECONDS.toMillis(timeoutSeconds);
    }

    /**
     * Create a task with timeout in default SECONDS unit.
     *
     * @param timeoutSeconds timeout in seconds
     * @param ownerWindow    owner window
     */
    protected BackgroundTask(long timeoutSeconds, Window ownerWindow) {
        this.ownerFrame = ownerWindow;
        this.timeoutMilliseconds = TimeUnit.SECONDS.toMillis(timeoutSeconds);
    }

    /**
     * Main method that performs a task.
     * <p/> Called by the execution environment in a separate working thread.
     *
     * <p/> Implementation of this method should support interruption:
     * <ul>
     *     <li/> In long loops check {@link TaskLifeCycle#isInterrupted()} and return if it is true
     *     <li/> Don't swallow {@link InterruptedException} - return from the method or don't catch it at all
     * </ul>
     *
     * @param taskLifeCycle lifecycle object that allows the main method to interact with the execution environment
     * @return task result
     * @throws Exception exception in working thread
     */
    public abstract V run(TaskLifeCycle<T> taskLifeCycle) throws Exception;

    /**
     * Called by the execution environment in UI thread when the task is completed.
     *
     * @param result result of execution returned by {@link #run(TaskLifeCycle)} method
     */
    public void done(V result) {
    }

    /**
     * Called by the execution environment in UI thread if the task is canceled by
     * {@link BackgroundTaskHandler#cancel()} invocation.
     * <p/> This method is not called in case of timeout expiration or owner window closing.
     */
    public void canceled() {
    }

    /**
     * Called by the execution environment in UI thread if the task timeout is exceeded.
     *
     * @return true if this method implementation actualy handles this event. Used for chaining handlers.
     */
    public boolean handleTimeoutException() {
        return false;
    }

    /**
     * Called by the execution environment in UI thread if the task {@link #run(TaskLifeCycle)} method raised an
     * exception.
     *
     * @param ex exception
     * @return true if this method implementation actualy handles the exception. Used for chaining handlers.
     */
    public boolean handleException(Exception ex) {
        return false;
    }

    /**
     * Called by the execution environment in UI thread on progress change.
     *
     * @param changes list of changes since previous invocation
     */
    public void progress(List<T> changes) {
    }

    /**
     * Called by the execution environment in UI thread to prepare some execution parameters. These parameters can be
     * requested by the working thread inside the {@link #run(TaskLifeCycle)} method by calling
     * {@link TaskLifeCycle#getParams()}.
     *
     * @return parameters map or null if parameters are not needed
     */
    public Map<String, Object> getParams() {
        return null;
    }

    /**
     * @return owner window
     */
    public final IFrame getOwnerFrame() {
        return ownerFrame;
    }

    /**
     * @return timeout in ms
     */
    public final long getTimeoutMilliseconds() {
        return timeoutMilliseconds;
    }

    /**
     * @return timeout in sec
     */
    public final long getTimeoutSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(timeoutMilliseconds);
    }

    /**
     * Add additional progress listener.
     * @param progressListener listener
     */
    public final void addProgressListener(ProgressListener<T, V> progressListener) {
        if (!progressListeners.contains(progressListener))
            progressListeners.add(progressListener);
    }

    /**
     * Additional progress listeners.
     * @return copy of the progress listeners collection
     */
    public final List<ProgressListener<T, V>> getProgressListeners() {
        return new ArrayList<>(progressListeners);
    }

    /**
     * Remove a progress listener.
     * @param progressListener listener
     */
    public final void removeProgressListener(ProgressListener<T, V> progressListener) {
        progressListeners.remove(progressListener);
    }

    /**
     * Listener of the task life cycle events, complementary to the tasks own methods:
     * {@link BackgroundTask#progress(java.util.List)}, {@link BackgroundTask#done(Object)},
     * {@link com.haulmont.cuba.gui.executors.BackgroundTask#canceled()}.
     *
     * @param <T> progress measurement unit
     * @param <V> result type
     */
    public interface ProgressListener<T, V> {

        /**
         * Called by the execution environment in UI thread on progress change.
         *
         * @param changes list of changes since previous invocation
         */
        void onProgress(List<T> changes);

        /**
         * Called by the execution environment in UI thread when the task is completed.
         *
         * @param result result of execution returned by {@link #run(TaskLifeCycle)} method
         */
        void onDone(V result);

        /**
         * Called by the execution environment in UI thread if the task is canceled.
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