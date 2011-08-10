/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.utils;

import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.gui.WebWindow;
import com.vaadin.ui.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web implementation of {@link BackgroundWorker}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class WebBackgroundWorker implements BackgroundWorker, HttpSessionListener {

    private static final int UI_TIMER_UPDATE_MS = 500;

    private WatchDog watchDogThread;

    public WebBackgroundWorker() {
        watchDogThread = new WebWatchDog();
    }

    @Override
    public <T> BackgroundTaskHandler handle(final BackgroundTask<T> task) {
        checkNotNull(task);

        // UI timer
        WebTimer pingTimer = new WebTimer(UI_TIMER_UPDATE_MS, true);

        // create task handler
        TaskExecutor<T> taskExecutor = new WebTaskExecutor<T>(task, pingTimer);
        final TaskHandler<T> taskHandler = new TaskHandler<T>(taskExecutor, watchDogThread);

        // add timer to AppWindow for UI ping
        pingTimer.addTimerListener(new com.haulmont.cuba.gui.components.Timer.TimerListener() {

            @Override
            public void onTimer(Timer timer) {
                // if completed
                if (taskHandler.isDone()) {
                    task.done();
                    timer.stopTimer();
                } else {
                    if (!taskHandler.isCancelled()) {
                        IFrame frame = task.getOwnerWindow().getFrame();
                        Component webWindow = ((WebWindow) frame).getComponent();
                        webWindow.requestRepaint();
                    }
                }
            }

            @Override
            public void onStopTimer(Timer timer) {
                // Do nothing
            }
        });
        App.getInstance().addTimer(pingTimer, task.getOwnerWindow());

        return taskHandler;
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // Do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // Purge session tasks and threads
    }

    /**
     * WatchDog
     */
    private class WebWatchDog extends Thread implements WatchDog{

        private static final int WATCHDOG_INTERVAL = 100;

        private volatile boolean watching = false;
        private ReentrantLock watchLock;
        private Set<TaskHandler> watches;

        private WebWatchDog() {
            watchLock = new ReentrantLock();
            watches = new LinkedHashSet<TaskHandler>();
        }

        @Override
        public void run() {
            try {
                while (watching) {
                    cleanupTasks();
                }
            } catch (Exception ignored) {
            }
        }

        private void cleanupTasks() throws Exception {
            TimeUnit.MILLISECONDS.sleep(WATCHDOG_INTERVAL);

            watchLock.lock();

            long actual = TimeProvider.currentTimestamp().getTime();

            List<TaskHandler> forRemove = new LinkedList<TaskHandler>();
            for (TaskHandler task : watches) {
                if (task.isCancelled() || task.isDone()) {
                    forRemove.add(task);
                } else if (task.checkHangup(actual)) {
                    cancelTask(task);
                    forRemove.add(task);
                }
            }

            watches.removeAll(forRemove);

            watchLock.unlock();
        }

        private void cancelTask(TaskHandler task) {
            task.cancel(true);
        }

        private void startWatching() {
            watching = true;
            start();
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
    private class WebTaskExecutor<T> extends Thread implements TaskExecutor<T> {

        private BackgroundTask<T> runnableTask;
        private WebTimer pingTimer;

        private volatile boolean canceled = false;
        private volatile boolean done = false;

        private WebTaskExecutor(BackgroundTask<T> runnableTask, WebTimer pingTimer) {
            this.runnableTask = runnableTask;
            this.pingTimer = pingTimer;
            runnableTask.setProgressHandler(this);
        }

        @Override
        public void run() {
            runnableTask.run();
            done = true;
        }

        @Override
        public void handleProgress(T ... changes) {
            runnableTask.progress(Arrays.asList(changes));
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            if (super.isAlive() && mayInterruptIfRunning) {
                interrupt();
                canceled = isInterrupted();
            }
            if ((pingTimer != null) && canceled) {
                pingTimer.stopTimer();
                pingTimer = null;
            }
            return canceled;
        }

        @Override
        public BackgroundTask<T> getTask() {
            return runnableTask;
        }

        public void execute() {
            start();
        }

        public boolean isCancelled() {
            return canceled;
        }

        public boolean isDone() {
            return done;
        }
    }
}