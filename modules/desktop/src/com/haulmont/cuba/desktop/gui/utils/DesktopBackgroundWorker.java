/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.utils;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.WatchDog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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

    public DesktopBackgroundWorker(WatchDog watchDog) {
        this.watchDog = watchDog;
    }

    @Override
    public <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task) {
        checkNotNull(task);
        checkNotNull(task.getOwnerWindow());

        // create task handler
        TaskExecutor<T, V> taskExecutor = new DesktopTaskExecutor<T, V>(task);

        return new TaskHandler<T, V>(taskExecutor, watchDog);
    }

    /**
     * Task runner
     */
    private class DesktopTaskExecutor<T, V> extends SwingWorker<V, T> implements TaskExecutor<T, V> {

        private BackgroundTask<T, V> runnableTask;
        private Runnable finalizer;
        private V result;

        private UUID userId;

        private volatile boolean isClosed = false;

        private DesktopTaskExecutor(BackgroundTask<T, V> runnableTask) {
            this.runnableTask = runnableTask;
            runnableTask.setProgressHandler(this);
            userId = UserSessionProvider.getUserSession().getId();
        }

        @Override
        protected V doInBackground() throws Exception {
            runnableTask.setInterrupted(false);
            try {
                result = runnableTask.run();
            } catch (Exception ex) {
                log.error("Internal background task error", ex);
            }
            return result;
        }

        @Override
        protected void process(List<T> chunks) {
            try {
                runnableTask.progress(chunks);
                // Notify listeners
                for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                    listener.onProgress(chunks);
                }
            } catch (Exception ex) {
                log.error("Internal background task error", ex);
            }
        }

        @Override
        protected void done() {
            if (isClosed)
                return;

            if (!runnableTask.isInterrupted()) {
                try {
                    runnableTask.done(result);
                    // Notify listeners
                    for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                        listener.onDone(result);
                    }
                } catch (Exception ex) {
                    log.error("Internal background task error", ex);
                } finally {
                    if (finalizer != null) {
                        finalizer.run();
                        finalizer = null;
                    }

                    isClosed = true;
                }
            }
        }

        @Override
        public void startExecution() {
            execute();
        }

        @Override
        public boolean cancelExecution(boolean mayInterruptIfRunning) {
            if (isClosed)
                return false;

            runnableTask.setInterrupted(true);

            if (!isDone() && !isCancelled()) {
                log.debug("Cancel task. User: " + userId);
                isClosed = cancel(mayInterruptIfRunning);
                return isClosed;
            } else
                return false;
        }

        @Override
        public V getResult() {
            V result;
            try {
                result = get();
                this.done();
            } catch (InterruptedException e) {
                return null;
            } catch (ExecutionException e) {
                return null;
            }
            return result;
        }

        @Override
        public BackgroundTask<T, V> getTask() {
            return runnableTask;
        }

        @Override
        public void setFinalizer(Runnable finalizer) {
            this.finalizer = finalizer;
        }

        @Override
        public Runnable getFinalizer() {
            return finalizer;
        }

        @Override
        public void handleProgress(T... changes) {
            publish(changes);
        }
    }
}