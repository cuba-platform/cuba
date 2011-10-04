/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.utils;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.WatchDog;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

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

    public WebBackgroundWorker(Configuration configuration, WatchDog watchDog) {
        this.watchDog = watchDog;
        this.uiCheckInterval = configuration.getConfig(WebConfig.class).getUiCheckInterval();
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
                    long newIntent = taskExecutor.getIntentVersion();
                    if (intentVersion != newIntent) {
                        intentVersion = newIntent;
                        taskExecutor.handleIntents();
                    }
                }

                // if completed
                if (taskHandler.isAlive()) {
                    task.done(taskExecutor.getExecutionResult());
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
        private V result = null;
        protected UUID userId;

        private WebTaskExecutor(App app, BackgroundTask<T, V> runnableTask, WebTimer pingTimer) {
            this.runnableTask = runnableTask;
            this.pingTimer = pingTimer;
            this.app = app;
            runnableTask.setProgressHandler(this);

            securityContext = AppContext.getSecurityContext();
            userId = UserSessionProvider.getUserSession().getId();
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
                this.result = result;
                // Is done
                if (!runnableTask.isInterrupted())
                    done = true;
                // Remove from executions
                app.removeBackgroundTask(this);
                // Set null security permissions
                securityContext = null;
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
                log.debug("Cancel task. User: " + userId);

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
                    runnableTask.done(result);
                }
            } catch (InterruptedException e) {
                return null;
            }
            return result;
        }

        public V getExecutionResult() {
            return result;
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