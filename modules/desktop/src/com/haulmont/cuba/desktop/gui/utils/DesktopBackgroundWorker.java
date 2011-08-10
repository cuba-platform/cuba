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

import javax.swing.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

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
        watchDogThread = new DesktopWatchDog();
    }

    @Override
    public <T> BackgroundTaskHandler handle(BackgroundTask<T> task) {
        checkNotNull(task);

        // create task handler
        TaskExecutor<T> taskExecutor = new DesktopTaskExecutor<T>(task);
        return new TaskHandler<T>(taskExecutor, watchDogThread);
    }

    /**
     * WatchDog
     */
    private class DesktopWatchDog extends SwingWorker<Void, TaskHandler> implements BackgroundWorker.WatchDog{

        private static final int WATCHDOG_INTERVAL = 100;

        private boolean watching = false;
        private ReentrantLock watchLock;
        private Set<TaskHandler> watches;

        private DesktopWatchDog() {
            watchLock = new ReentrantLock();
            watches = new LinkedHashSet<TaskHandler>();
        }

        @Override
        protected Void doInBackground() throws Exception {
            TimeUnit.MILLISECONDS.sleep(WATCHDOG_INTERVAL);

            watchLock.lock();

            long actualTime = TimeProvider.currentTimestamp().getTime();

            List<TaskHandler> forRemove = new LinkedList<TaskHandler>();
            for (TaskHandler task : watches) {
                if (task.isCancelled() || task.isDone()) {
                    forRemove.add(task);
                } else if (task.checkHangup(actualTime)) {
                    cancelTask(task);
                    forRemove.add(task);
                }
            }

            watches.removeAll(forRemove);

            watchLock.unlock();

            return null;
        }

        private void cancelTask(TaskHandler task) {
            publish(task);
        }

        @Override
        protected void process(List<TaskHandler> chunks) {
            for (TaskHandler task : chunks) {
                if (task.isHangup())
                    task.cancel(true);
            }
        }

        private void startWatching() {
            watching = true;
            execute();
        }

        public void manageTask(TaskHandler backroundTask) {
            watchLock.lock();

            watches.add(backroundTask);

            if (!watching) {
                startWatching();
            }

            watchLock.unlock();
        }
    }

    /**
     * Task runner
     */
    private class DesktopTaskExecutor<T> extends SwingWorker<Void, T> implements TaskExecutor<T> {

        private BackgroundTask<T> runnableTask;

        private DesktopTaskExecutor(BackgroundTask<T> runnableTask) {
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
            runnableTask.done();
        }

        public void execute(long timeout, TimeUnit unit) {
            execute();
        }

        @Override
        public BackgroundTask<T> getTask() {
            return runnableTask;
        }

        @Override
        public void handleProgress(T... changes) {
            publish(changes);
        }
    }
}