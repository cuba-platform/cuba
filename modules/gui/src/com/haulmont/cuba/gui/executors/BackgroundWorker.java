/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.core.global.TimeProvider;

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

        void execute();

        boolean cancelExecution(boolean mayInterruptIfRunning);

        BackgroundTask<T> getTask();

        boolean isCancelled();

        boolean isDone();
    }

    /**
     * Task handler
     */
    class TaskHandler<T> implements BackgroundTaskHandler {

        private TaskExecutor<T> taskExecutor;
        private WatchDog watchDog;

        private volatile boolean started = false;
        private boolean hangup = false;

        private long timeout = 0;
        private TimeUnit timeUnit;
        private long timeoutMillis;

        private long startTimeStamp;

        public TaskHandler(TaskExecutor<T> taskExecutor, WatchDog watchDog) {
            this.taskExecutor = taskExecutor;
            this.watchDog = watchDog;
        }

        @Override
        public void execute() {
            checkState(!started, "Task is already started");

            this.started = true;

            taskExecutor.execute();
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

            taskExecutor.execute();
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
