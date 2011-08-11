/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
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
    <T> BackgroundTaskHandler handle(BackgroundTask<T> task);

    interface WatchDog {
        void manageTask(TaskHandler backroundTask);
    }

    /**
     * Task runner
     */
    interface TaskExecutor<T> extends ProgressHandler<T> {

        void startExecution();

        boolean cancelExecution(boolean mayInterruptIfRunning);

        BackgroundTask<T> getTask();

        boolean isCancelled();

        boolean isDone();
    }

    /**
     * Task handler
     */
    class TaskHandler<T> implements BackgroundTaskHandler {

        private Log log = LogFactory.getLog(BackgroundWorker.class);

        private TaskExecutor<T> taskExecutor;
        private WatchDog watchDog;

        private volatile boolean started = false;
        private boolean hangup = false;

        private long timeout = 0;
        private TimeUnit timeUnit;
        private long timeoutMillis;

        private long startTimeStamp;
        private UserSession userSession;

        public TaskHandler(TaskExecutor<T> taskExecutor, WatchDog watchDog) {
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
                cancel(true);
            }
        }

        @Override
        public void execute(long timeout, TimeUnit unit) {
            checkState(!started, "Task is already started");

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
            boolean canceled = taskExecutor.cancelExecution(mayInterruptIfRunning);
            if (canceled) {
                taskExecutor.getTask().canceled();
            }
            return canceled;
        }

        /**
         * Cancel without events for tasks
         */
        public void close() {
            UUID userId = getUserSession().getId();
            Window ownerWindow = getTask().getOwnerWindow();
            String windowClass = ownerWindow.getClass().getCanonicalName();
            log.debug("Task killed. User: " + userId + " Window: " + windowClass);

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

        public BackgroundTask<T> getTask() {
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
