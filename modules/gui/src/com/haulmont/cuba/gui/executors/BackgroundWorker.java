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
 * Task executor service
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface BackgroundWorker {
    String NAME = "cuba_BackgroundWorker";

    /**
     * Create handler for background task
     * @param <T> progress measure unit
     * @param task heavy background task
     * @return Task handler
     */
    <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task);

    interface WatchDog {
        void manageTask(TaskHandler backroundTask);
    }

    /**
     * Task runner
     */
    interface TaskExecutor<T, V> extends ProgressHandler<T> {

        void startExecution();

        boolean cancelExecution(boolean mayInterruptIfRunning);

        V getResult();

        BackgroundTask<T, V> getTask();

        boolean isCancelled();

        boolean isDone();
    }

    /**
     * Task handler
     */
    class TaskHandler<T, V> implements BackgroundTaskHandler<V> {

        private Log log = LogFactory.getLog(BackgroundWorker.class);

        private TaskExecutor<T, V> taskExecutor;
        private WatchDog watchDog;

        private volatile boolean started = false;
        private boolean hangup = false;

        private long timeout = 0;
        private TimeUnit timeUnit;
        private long timeoutMillis;

        private long startTimeStamp;
        private UserSession userSession;

        public TaskHandler(TaskExecutor<T, V> taskExecutor, WatchDog watchDog) {
            this.taskExecutor = taskExecutor;
            this.watchDog = watchDog;
            this.userSession = UserSessionProvider.getUserSession();

            BackgroundTask task = taskExecutor.getTask();

            task.getOwnerWindow().addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    ownerWindowClosed();
                }
            });
        }

        private void ownerWindowClosed() {
            if (!taskExecutor.isCancelled() && !taskExecutor.isDone() && started) {
                UUID userId = getUserSession().getId();
                Window ownerWindow = getTask().getOwnerWindow();
                String windowClass = ownerWindow.getClass().getCanonicalName();
                log.debug("Window closed. User: " + userId + " Window: " + windowClass);

                taskExecutor.cancelExecution(true);
            }
        }

        @Override
        public void execute(long timeout, TimeUnit unit) {
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
        public boolean cancel(boolean mayInterruptIfRunning) {
            checkState(started, "Task is not running");

            boolean canceled = false;
            if (isAlive()) {
                canceled = taskExecutor.cancelExecution(mayInterruptIfRunning);
                if (canceled) {
                    taskExecutor.getTask().canceled();
                }
            }
            return canceled;
        }

        @Override
        public V getResult() {
            checkState(started, "Task is not running");

            return taskExecutor.getResult();
        }

        /**
         * Cancel without events for tasks
         */
        public void close() {
            if (AppContext.isStarted()) {
                UUID userId = getUserSession().getId();
                Window ownerWindow = getTask().getOwnerWindow();
                String windowClass = ownerWindow.getClass().getCanonicalName();
                log.debug("Task killed. User: " + userId + " Window: " + windowClass);
            }

            taskExecutor.cancelExecution(true);
        }

        @Override
        public boolean isDone() {
            return taskExecutor.isDone();
        }

        @Override
        public boolean isCancelled() {
            return taskExecutor.isCancelled();
        }

        @Override
        public boolean isAlive() {
            return !isCancelled() && !isDone() && started;
        }

        public BackgroundTask<T, V> getTask() {
            return taskExecutor.getTask();
        }

        public UserSession getUserSession() {
            return userSession;
        }

        /**
         * If task is executing too long
         * @param time Actual time
         * @return Hangup flag
         */
        public boolean checkHangup(long time) {
            if (isDone() || isCancelled())
                return false;
            if (timeout <= 0)
                return false;
            else {
                hangup = (time - startTimeStamp) > timeoutMillis;
                return hangup;
            }
        }

        public boolean isHangup() {
            return hangup;
        }
    }
}
