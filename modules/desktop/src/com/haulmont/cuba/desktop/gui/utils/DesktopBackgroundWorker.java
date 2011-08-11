/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.utils;

import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Desktop implementation of {@link BackgroundWorker}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DesktopBackgroundWorker implements BackgroundWorker {

    private Log log = LogFactory.getLog(DesktopBackgroundWorker.class);

    private WatchDog watchDog;

    public DesktopBackgroundWorker() {
        watchDog = new DesktopWatchDog();
    }

    @Override
    public <T> BackgroundTaskHandler handle(BackgroundTask<T> task) {
        checkNotNull(task);

        // create task handler
        TaskExecutor<T> taskExecutor = new DesktopTaskExecutor<T>(task);

        return new TaskHandler<T>(taskExecutor, watchDog);
    }

    /**
     * WatchDog
     */
    private class DesktopWatchDog extends SwingWorker<Void, TaskHandler> implements BackgroundWorker.WatchDog {

        private static final int WATCHDOG_INTERVAL = 2000;

        private boolean watching = false;
        private final Set<TaskHandler> watches;

        private DesktopWatchDog() {
            watches = new LinkedHashSet<TaskHandler>();
        }

        @Override
        protected Void doInBackground() {
            try {
                while (watching) {
                    cleanupTasks();
                }
            } catch (Exception ex) {
                log.error("WatchDog crashed", ex);
            }
            return null;
        }

        private void cleanupTasks() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(WATCHDOG_INTERVAL);

            synchronized (watches) {
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
            }
        }

        private void cancelTask(TaskHandler task) {
            publish(task);
        }

        @Override
        protected void process(List<TaskHandler> chunks) {
            for (TaskHandler task : chunks) {
                if (task.isHangup()) {
                    task.close();
                }
            }
        }

        private void startWatching() {
            watching = true;
            execute();
        }

        public void manageTask(TaskHandler backroundTask) {
            synchronized (watches) {
                watches.add(backroundTask);
            }

            if (!watching)
                startWatching();
        }
    }

    /**
     * Task runner
     */
    private class DesktopTaskExecutor<T> extends SwingWorker<Void, T> implements TaskExecutor<T> {

        private BackgroundTask<T> runnableTask;

        private DesktopTaskExecutor(BackgroundTask<T> runnableTask) {
            this.runnableTask = runnableTask;
            runnableTask.setProgressHandler(this);
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
            if (!runnableTask.isInterrupted())
                runnableTask.done();
        }

        @Override
        public void startExecution() {
            execute();
        }

        @Override
        public boolean cancelExecution(boolean mayInterruptIfRunning) {
            runnableTask.setInterrupted(true);

            UUID userId = UserSessionProvider.getUserSession().getId();
            log.info("Cancel background task for user: " + userId);

            if (!isDone()) {
                return cancel(mayInterruptIfRunning);
            } else
                return false;
        }

        @Override
        public BackgroundTask<T> getTask() {
            return runnableTask;
        }

        @Override
        public void handleProgress(T ... changes) {
            publish(changes);
        }
    }
}