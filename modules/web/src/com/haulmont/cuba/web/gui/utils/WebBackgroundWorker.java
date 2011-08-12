/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.utils;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.WebTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web implementation of {@link BackgroundWorker}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class WebBackgroundWorker implements BackgroundWorker {

    private Log log = LogFactory.getLog(WebBackgroundWorker.class);

    private final int uiCheckInterval;

    private WatchDog watchDog;

    public WebBackgroundWorker(ConfigProvider configProvider) {
        watchDog = new WebWatchDog(configProvider);
        uiCheckInterval = configProvider.doGetConfig(ClientConfig.class).getUiCheckInterval();
    }

    @Override
    public <T, V> BackgroundTaskHandler<V> handle(final BackgroundTask<T, V> task) {
        checkNotNull(task);
        checkNotNull(task.getOwnerWindow());

        App appInstance = App.getInstance();

        // UI timer
        WebTimer pingTimer = new WebTimer(uiCheckInterval, true);

        // create task executor
        final WebTaskExecutor<T, V> taskExecutor = new WebTaskExecutor<T, V>(appInstance, task, pingTimer);

        // add thread to taskSet
        appInstance.addBackgroundTask(taskExecutor);

        // create task handler
        final TaskHandler<T, V> taskHandler = new TaskHandler<T, V>(taskExecutor, watchDog);

        // add timer to AppWindow for UI ping
        pingTimer.addTimerListener(new com.haulmont.cuba.gui.components.Timer.TimerListener() {

            private long intentVersion = 0;

            @Override
            public void onTimer(Timer timer) {
                // handle intents
                if (!taskHandler.isCancelled()) {
                    if (intentVersion != taskExecutor.getIntentVersion()) {
                        intentVersion = taskExecutor.getIntentVersion();
                        taskExecutor.handleIntents();
                    }
                }

                // if completed
                if (taskHandler.isDone()) {
                    task.done();
                    timer.stopTimer();
                }
            }

            @Override
            public void onStopTimer(Timer timer) {
                // Do nothing
            }
        });
        appInstance.addTimer(pingTimer, task.getOwnerWindow());

        return taskHandler;
    }

    /**
     * WatchDog
     */
    private class WebWatchDog extends Thread implements WatchDog {

        private volatile boolean watching = false;
        private final Set<TaskHandler> watches;

        private final Integer watchDogInterval;

        private WebWatchDog(ConfigProvider configProvider) {
            watches = new LinkedHashSet<TaskHandler>();
            watchDogInterval = configProvider.doGetConfig(ClientConfig.class).getWatchDogInterval();
        }

        @Override
        public void run() {
            try {
                while (watching) {
                    cleanupTasks();
                }
            } catch (Exception ex) {
                log.error("WatchDog crashed", ex);
            }
        }

        private void cleanupTasks() throws Exception {
            TimeUnit.MILLISECONDS.sleep(watchDogInterval);

            synchronized (watches) {
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
            }
        }

        private void cancelTask(TaskHandler task) {
            task.close();
        }

        private void startWatching() {
            watching = true;
            start();
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
    private class WebTaskExecutor<T, V> extends Thread implements TaskExecutor<T, V> {

        private App app;

        private BackgroundTask<T, V> runnableTask;
        private WebTimer pingTimer;

        private volatile boolean canceled = false;
        private volatile boolean done = false;

        private volatile long intentVersion = 0;
        private final List<T> intents = Collections.synchronizedList(new LinkedList<T>());

        private SecurityContext securityContext;

        private WebTaskExecutor(App app, BackgroundTask<T, V> runnableTask, WebTimer pingTimer) {
            this.runnableTask = runnableTask;
            this.pingTimer = pingTimer;
            this.app = app;
            runnableTask.setProgressHandler(this);

            securityContext = AppContext.getSecurityContext();
        }

        @Override
        public void run() {
            // Set security permissions
            AppContext.setSecurityContext(securityContext);

            runnableTask.setInterrupted(false);

            V result = null;
            try {
                result = runnableTask.run();
            } catch (Exception ex) {
                log.error(ex);
            } finally {
                runnableTask.setResult(result);
                // Is done
                if (!runnableTask.isInterrupted())
                    done = true;
                // Remove from executions
                app.removeBackgroundTask(this);
                // Clear security permissions
                AppContext.setSecurityContext(null);
            }
        }

        @Override
        public void handleProgress(T ... changes) {
            synchronized (intents) {
                intentVersion++;
                intents.addAll(Arrays.asList(changes));
            }
        }

        @Override
        public boolean cancelExecution(boolean mayInterruptIfRunning) {
            boolean canceled = false;

            runnableTask.setInterrupted(true);

            if (super.isAlive() && mayInterruptIfRunning) {
                // Interrupt
                interrupt();

                // Check
                canceled = isInterrupted() || isDone();

                // Remove task from execution
                if (canceled)
                    app.removeBackgroundTask(this);

                this.canceled = canceled;
            }
            stopTimer(mayInterruptIfRunning);
            return canceled;
        }

        private void stopTimer(boolean mayInterruptIfRunning) {
            if ((pingTimer != null) && mayInterruptIfRunning) {
                pingTimer.stopTimer();
                pingTimer = null;
            }
        }

        @Override
        public V getResult() {
            try {
                if (this.isAlive()) {
                    this.join();
                    stopTimer(true);
                    runnableTask.done();
                }
            } catch (InterruptedException e) {
                return null;
            }
            return runnableTask.getResult();
        }

        @Override
        public BackgroundTask<T, V> getTask() {
            return runnableTask;
        }

        @Override
        public void startExecution() {
            start();
        }

        @Override
        public boolean isCancelled() {
            return canceled;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        public long getIntentVersion() {
            return intentVersion;
        }

        public void handleIntents() {
            synchronized (intents) {
                runnableTask.progress(intents);
            }
        }
    }
}