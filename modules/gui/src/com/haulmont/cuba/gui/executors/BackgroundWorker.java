/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

/**
 * Task executor service for GUI layer
 *
 * @author artamonov
 * @version $Id$
 */
public interface BackgroundWorker {
    String NAME = "cuba_BackgroundWorker";

    /**
     * Create handler for background task
     *
     * @param <T>  progress measure unit
     * @param task heavy background task
     * @return Task handler
     */
    <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task);

    /**
     * Task runner
     */
    @SuppressWarnings("unused")
    interface TaskExecutor<T, V> {

        void startExecution();

        boolean cancelExecution(boolean mayInterruptIfRunning);

        V getResult();

        BackgroundTask<T, V> getTask();

        boolean isCancelled();

        boolean isDone();

        boolean inProgress();

        /**
         * Done handler for clear resources
         *
         * @param finalizer Runnable handler
         */
        void setFinalizer(Runnable finalizer);

        Runnable getFinalizer();

        /**
         * Handle changes from working thread
         *
         * @param changes Changes
         */
        @SuppressWarnings({"unchecked"})
        void handleProgress(T... changes);
    }

    /**
     * Special container for private platform realization of ProgressHandler
     */
    class ProgressManager {

        private static ThreadLocal<BackgroundWorker.TaskExecutor> executorThreadLocal = new ThreadLocal<>();

        public static void setExecutor(BackgroundWorker.TaskExecutor progressHandler) {
            executorThreadLocal.set(progressHandler);
        }

        @SuppressWarnings("unchecked")
        public static <T, V> BackgroundWorker.TaskExecutor<T, V> getExecutor() {
            return executorThreadLocal.get();
        }
    }

    /**
     * Task handler
     */
    class TaskHandler<T, V> implements BackgroundTaskHandler<V> {

        private Log log = LogFactory.getLog(BackgroundWorker.class);

        private TaskExecutor<T, V> taskExecutor;
        private WatchDog watchDog;

        private volatile boolean started = false;

        private long timeout = 0;
        private TimeUnit timeUnit;
        private long timeoutMillis;

        private long startTimeStamp;
        private UserSession userSession;
        private Window.CloseListener closeListener;

        public TaskHandler(TaskExecutor<T, V> taskExecutor, WatchDog watchDog) {
            this.taskExecutor = taskExecutor;
            this.watchDog = watchDog;
            this.userSession = UserSessionProvider.getUserSession();

            BackgroundTask<T, V> task = taskExecutor.getTask();
            if (task.getOwnerWindow() != null) {
                closeListener = new Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        ownerWindowClosed();
                    }
                };
                task.getOwnerWindow().addListener(closeListener);
            }
            // remove close listener on done
            taskExecutor.setFinalizer(new Runnable() {
                @Override
                public void run() {
                    disposeResources();
                }
            });
        }

        private void ownerWindowClosed() {
            if (isAlive()) {
                UUID userId = getUserSession().getId();
                Window ownerWindow = getTask().getOwnerWindow();
                String windowClass = ownerWindow.getClass().getCanonicalName();
                log.debug("Window closed. User: " + userId + " Window: " + windowClass);

                taskExecutor.cancelExecution(true);
            }
        }

        @Override
        public final void execute(long timeout, TimeUnit unit) {
            checkState(!started, "Task is already started");
            checkState(timeout >= 0, "Timeout cannot be zero or less than zero");

            this.started = true;

            this.timeout = timeout;
            this.timeUnit = unit;
            this.timeoutMillis = timeUnit.toMillis(timeout);
            this.startTimeStamp = TimeProvider.currentTimestamp().getTime();

            this.watchDog.manageTask(this);

            UUID userId = getUserSession().getId();
            log.debug("Run task. User: " + userId);

            taskExecutor.startExecution();
        }

        @Override
        public final boolean cancel(boolean mayInterruptIfRunning) {
            checkState(started, "Task is not running");

            boolean canceled = false;
            if (isAlive()) {
                canceled = taskExecutor.cancelExecution(mayInterruptIfRunning);
                if (canceled) {
                    BackgroundTask<T, V> task = taskExecutor.getTask();
                    task.canceled();

                    try {
                        // Notify listeners
                        for (BackgroundTask.ProgressListener listener : task.getProgressListeners()) {
                            listener.onCancel();
                        }
                    } finally {
                        disposeResources();
                    }
                }
            }
            return canceled;
        }

        private void disposeResources() {
            // force remove close listener
            Window ownerWindow = getTask().getOwnerWindow();
            if (ownerWindow != null)
                ownerWindow.removeListener(closeListener);
            closeListener = null;
        }

        /**
         * Join task thread to current <br/>
         * <b>Attention!</b> Call this method only from synchronous gui action
         *
         * @return Task result
         */
        @Override
        public final V getResult() {
            checkState(started, "Task is not running");

            return taskExecutor.getResult();
        }

        /**
         * Cancel without events for tasks
         */
        public final void close() {
            if (AppContext.isStarted()) {
                UUID userId = getUserSession().getId();
                Window ownerWindow = getTask().getOwnerWindow();

                disposeResources();

                if (ownerWindow != null) {
                    String windowClass = ownerWindow.getClass().getCanonicalName();
                    log.debug("Task killed. User: " + userId + " Window: " + windowClass);
                } else
                    log.debug("Task killed. User: " + userId);
            }

            taskExecutor.cancelExecution(true);
        }

        @Override
        public final boolean isDone() {
            return taskExecutor.isDone();
        }

        @Override
        public final boolean isCancelled() {
            return taskExecutor.isCancelled();
        }

        @Override
        public final boolean isAlive() {
            return taskExecutor.inProgress() && started;
        }

        public final BackgroundTask<T, V> getTask() {
            return taskExecutor.getTask();
        }

        public final UserSession getUserSession() {
            return userSession;
        }

        /**
         * If task is executing too long
         *
         * @param time Actual time
         * @return Hangup flag
         */
        public final boolean checkHangup(long time) {
            if (isDone() || isCancelled())
                return false;
            return timeout > 0 && (time - startTimeStamp) > timeoutMillis;
        }
    }
}