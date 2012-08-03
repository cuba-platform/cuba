/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.utils;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.WatchDog;
import com.haulmont.cuba.web.App;
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

    private WatchDog watchDog;

    public WebBackgroundWorker(WatchDog watchDog) {
        this.watchDog = watchDog;
    }

    /**
     * Simple wrapper for {@link Timer.TimerListener}
     */
    private static class WebTimerListener {

        private WebTimer timer;

        private Timer.TimerListener timerListener;

        private WebTimerListener(WebTimer timer) {
            this.timer = timer;
        }

        public Timer.TimerListener getTimerListener() {
            return timerListener;
        }

        public void setTimerListener(Timer.TimerListener timerListener) {
            this.timerListener = timerListener;
        }

        public void startListen() {
            if (timerListener != null)
                timer.addTimerListener(timerListener);
        }

        public void stopListen() {
            timer.removeTimerListener(timerListener);
        }
    }

    @Override
    public <T, V> BackgroundTaskHandler<V> handle(final BackgroundTask<T, V> task) {
        checkNotNull(task);

        App appInstance;
        try {
            appInstance = App.getInstance();
        } catch (IllegalStateException ex) {
            log.error("Couldn't handle task", ex);
            throw ex;
        }

        // UI timer
        WebTimer pingTimer = appInstance.getWorkerTimer();

        final WebTimerListener webTimerListener = new WebTimerListener(pingTimer);

        // create task executor
        final WebTaskExecutor<T, V> taskExecutor = new WebTaskExecutor<>(appInstance, task, webTimerListener);

        // add thread to taskSet
        appInstance.addBackgroundTask(taskExecutor);

        // create task handler
        final TaskHandler<T, V> taskHandler = new TaskHandler<>(taskExecutor, watchDog);

        // add timer to AppWindow for UI ping
        Timer.TimerListener timerListener = new Timer.TimerListener() {
            private long intentVersion = 0;

            @Override
            public void onTimer(Timer timer) {
                if (UserSessionProvider.getUserSession() == null) {
                    log.debug("Null UserSession in background task");
                    return;
                }

                // handle intents
                if (!taskExecutor.isCancelled()) {
                    long newIntent = taskExecutor.getIntentVersion();
                    if (intentVersion != newIntent) {
                        intentVersion = newIntent;
                        taskExecutor.handleIntents();
                    }
                }

                // if completed
                if (taskExecutor.isDone()) {
                    taskExecutor.handleDone();
                }

                if (!taskExecutor.isAlive()) {
                    webTimerListener.stopListen();
                }
            }

            @Override
            public void onStopTimer(Timer timer) {
                // Do nothing
            }
        };

        // Start listen only if task started
        webTimerListener.setTimerListener(timerListener);

        return taskHandler;
    }

    /**
     * Task runner
     */
    private class WebTaskExecutor<T, V> extends Thread implements TaskExecutor<T, V> {

        private App app;

        private BackgroundTask<T, V> runnableTask;
        private WebTimerListener webTimerListener;
        private Runnable finalizer;

        // canceled
        private volatile boolean canceled = false;

        // task body completed
        private volatile boolean done = false;

        // handleDone completed or canceled
        private volatile boolean closed = false;

        private volatile long intentVersion = 0;
        private final List<T> intents = Collections.synchronizedList(new LinkedList<T>());

        private SecurityContext securityContext;
        private UUID userId;

        private volatile V result = null;
        private volatile Exception taskException = null;

        private WebTaskExecutor(App app, BackgroundTask<T, V> runnableTask,
                                WebTimerListener webTimerListener) {
            this.runnableTask = runnableTask;
            this.webTimerListener = webTimerListener;
            this.app = app;
            runnableTask.setProgressHandler(this);

            securityContext = AppContext.getSecurityContext();
            userId = UserSessionProvider.getUserSession().getId();
        }

        @Override
        public final void run() {
            // Set security permissions
            AppContext.setSecurityContext(securityContext);

            runnableTask.setInterrupted(false);

            V result = null;
            try {
                result = runnableTask.run();
            } catch (Exception ex) {
                this.taskException = ex;
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

        @SafeVarargs
        @Override
        public final void handleProgress(T... changes) {
            synchronized (intents) {
                intentVersion++;
                intents.addAll(Arrays.asList(changes));
            }
        }

        @Override
        public final boolean cancelExecution(boolean mayInterruptIfRunning) {
            boolean canceled = super.isAlive();

            runnableTask.setInterrupted(true);

            if (!closed && mayInterruptIfRunning) {
                log.debug("Cancel task. User: " + userId);

                // Interrupt
                if (super.isAlive())
                    interrupt();

                // Check
                canceled = isInterrupted() || isDone();

                // Remove task from execution
                if (canceled)
                    app.removeBackgroundTask(this);

                this.canceled = canceled;
                this.closed = canceled;

                stopTimer(mayInterruptIfRunning);
            }
            return canceled;
        }

        private void stopTimer(boolean mayInterruptIfRunning) {
            if ((webTimerListener != null) && mayInterruptIfRunning) {
                webTimerListener.stopListen();
            }
        }

        @Override
        public final V getResult() {
            try {
                if (this.isAlive()) {
                    this.join();
                    stopTimer(true);
                    runnableTask.done(result);

                    closed = true;
                }
            } catch (InterruptedException e) {
                return null;
            }
            return result;
        }

        @Override
        public final BackgroundTask<T, V> getTask() {
            return runnableTask;
        }

        @Override
        public final void startExecution() {
            // Run timer listener
            webTimerListener.startListen();

            // Start thread
            start();
        }

        @Override
        public final boolean isCancelled() {
            return canceled;
        }

        @Override
        public final boolean isDone() {
            return done;
        }

        @Override
        public final boolean inProgress() {
            return !closed;
        }

        @Override
        public final void setFinalizer(Runnable finalizer) {
            this.finalizer = finalizer;
        }

        @Override
        public final Runnable getFinalizer() {
            return finalizer;
        }

        public final long getIntentVersion() {
            return intentVersion;
        }

        public final void handleIntents() {
            try {
                synchronized (intents) {
                    runnableTask.progress(intents);
                    // notify listeners
                    for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                        listener.onProgress(intents);
                    }
                }
            } catch (Exception ex) {
                runnableTask.handleException(taskException);
            }
        }

        public final void handleDone() {
            if (this.closed)
                return;

            // task cancel here not available
            this.closed = true;

            try {
                if (taskException == null) {
                    try {
                        runnableTask.done(result);
                        // notify listeners
                        for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                            listener.onDone(result);
                        }
                    } catch (Exception ex) {
                        runnableTask.handleException(ex);
                    }
                } else
                    runnableTask.handleException(taskException);
            } finally {
                if (finalizer != null) {
                    finalizer.run();
                    finalizer = null;
                }
            }
        }
    }
}