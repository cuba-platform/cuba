/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.utils;

import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.ProgressHandler;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Desktop implementation of {@link BackgroundWorker}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@SuppressWarnings("unused")
public class DesktopBackgroundWorker implements BackgroundWorker {

    private WatchDog watchDogThread;

    public DesktopBackgroundWorker() {
        watchDogThread = new WatchDog();
    }

    @Override
    public <T> BackgroundTaskHandler handle(BackgroundTask<T> task) {
        checkNotNull(task);

        return new TaskHandler<T>(task);
    }

    /**
     * WatchDog
     */
    private class WatchDog extends SwingWorker<Void, TaskExecutor> {

        private boolean watching = false;
        private ReentrantLock watchLock;
        private Set<TaskExecutor> watches;

        private WatchDog() {
            watchLock = new ReentrantLock();
        }

        @Override
        protected Void doInBackground() throws Exception {
            TimeUnit.MILLISECONDS.sleep(WATCHDOG_INTERVAL);

            watchLock.lock();

            long actual = TimeProvider.currentTimestamp().getTime();

            List<TaskExecutor> forRemove = new LinkedList<TaskExecutor>();
            List<TaskExecutor> hangupTasks = new LinkedList<TaskExecutor>();
            for (TaskExecutor task : watches) {
                if (task.isCancelled() || task.isDone()) {
                    forRemove.add(task);
                } else if (task.checkHangup(actual)) {
                    publish(task);
                    forRemove.add(task);
                }
            }

            watches.removeAll(forRemove);

            watchLock.unlock();

            return null;
        }

        @Override
        protected void process(List<TaskExecutor> chunks) {
            for (TaskExecutor task : chunks) {
                if (task.isHangup())
                    task.cancel(true);
            }
        }

        private void startWatching() {
            watching = true;
            watchDogThread.execute();
        }

        public void manageTask(TaskExecutor tDesktopBackroundTask) {
            watchLock.lock();

            watches.add(tDesktopBackroundTask);

            watchLock.unlock();

            if (!watching) {
                startWatching();
            }
        }
    }

    /**
     * Task runner
     */
    private class TaskExecutor<T> extends SwingWorker<Void, T> implements ProgressHandler<T> {

        private long timeout = 0;
        private TimeUnit timeUnit;
        private long timeoutMillis;

        private long startTimeStamp;
        private boolean isHangup = false;

        private BackgroundTask<T> runnableTask;

        private TaskExecutor(BackgroundTask<T> runnableTask) {
            this.runnableTask = runnableTask;
        }

        @Override
        protected Void doInBackground() throws Exception {
            runnableTask.run();
            return null;
        }

        @Override
        protected void process(List<T> chunks) {
            runnableTask.progress(chunks);
        }

        @Override
        protected void done() {
            super.done();
            runnableTask.done();
        }

        @Override
        public void handleProgress(T... changes) {
            publish(changes);
        }

        /**
         * If task is executing too long
         * @param time Actual time
         * @return Hangup flag
         */
        public boolean checkHangup(long time) {
            if (isDone() || isCancelled())
                return false;
            else if (timeout <= 0)
                return false;
            else {

                return false;
            }
        }

        public boolean isHangup() {
            return isHangup;
        }

        public void execute(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.timeUnit = unit;
            this.timeoutMillis = timeUnit.toMillis(timeout);
            this.startTimeStamp = TimeProvider.currentTimestamp().getTime();

            watchDogThread.manageTask(this);

            execute();
        }

        public long getTimeout() {
            return timeout;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }

    /**
     * Task handler
     */
    private class TaskHandler<T> extends BackgroundTaskHandler<T> {

        private volatile boolean executed = false;
        private TaskExecutor<T> backroundTaskExecutor;

        protected TaskHandler(BackgroundTask<T> task) {
            super(task);
            this.backroundTaskExecutor = new TaskExecutor<T>(task);
            task.setProgressHandler(backroundTaskExecutor);
        }

        @Override
        public void execute() {
            checkState(!executed, "Task is already started");
            executed = true;
            backroundTaskExecutor.execute();
        }

        @Override
        public void execute(long timeout, TimeUnit unit) {
            checkState(!executed, "Task is already started");
            executed = true;
            backroundTaskExecutor.execute(timeout, unit);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            backgroundTask.beforeCancel();
            boolean canceled = backroundTaskExecutor.cancel(mayInterruptIfRunning);
            if (canceled)
                backgroundTask.canceled();
            return canceled;
        }

        @Override
        public boolean isDone() {
            return backroundTaskExecutor.isDone();
        }

        @Override
        public boolean isCancelled() {
            return backroundTaskExecutor.isCancelled();
        }
    }
}