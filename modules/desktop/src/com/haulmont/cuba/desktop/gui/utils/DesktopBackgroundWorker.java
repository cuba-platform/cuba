/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.utils;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.executors.*;
import com.haulmont.cuba.gui.executors.impl.TaskExecutor;
import com.haulmont.cuba.gui.executors.impl.TaskHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Desktop implementation of {@link BackgroundWorker}
 *
 * @author artamonov
 * @version $Id$
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

        // create task handler
        TaskExecutor<T, V> taskExecutor = new DesktopTaskExecutor<>(task);

        return new TaskHandler<>(taskExecutor, watchDog);
    }

    /**
     * Task runner
     */
    private class DesktopTaskExecutor<T, V> extends SwingWorker<V, T> implements TaskExecutor<T, V> {

        private BackgroundTask<T, V> runnableTask;
        private Runnable finalizer;

        private volatile V result;
        private volatile Exception taskException = null;

        private UUID userId;

        private volatile boolean isClosed = false;

        private volatile boolean isInterrupted = false;

        private Map<String, Object> params;

        private DesktopTaskExecutor(BackgroundTask<T, V> runnableTask) {
            this.runnableTask = runnableTask;
            userId = UserSessionProvider.getUserSession().getId();

            //noinspection unchecked
            this.params = runnableTask.getParams();
            if (this.params != null)
                this.params = Collections.unmodifiableMap(this.params);
            else
                this.params = Collections.emptyMap();
        }

        @Override
        protected final V doInBackground() throws Exception {
            // assign thread local handler
            this.isInterrupted = false;
            try {
                result = runnableTask.run(new TaskLifeCycle<T>() {
                    @SafeVarargs
                    @Override
                    public final void publish(T... changes) {
                        handleProgress(changes);
                    }

                    @Override
                    public boolean isInterrupted() {
                        return DesktopTaskExecutor.this.isInterrupted;
                    }

                    @Override
                    public Map<String, Object> getParams() {
                        return params;
                    }
                });
            } catch (Exception ex) {
                if (!(ex instanceof InterruptedException) && !isCancelled())
                    taskException = ex;
            }
            return result;
        }

        @Override
        protected final void process(List<T> chunks) {
            try {
                runnableTask.progress(chunks);
                // Notify listeners
                for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                    listener.onProgress(chunks);
                }
            } catch (Exception ex) {
                runnableTask.handleException(ex);
            }
        }

        @Override
        protected final void done() {
            if (isClosed)
                return;

            if (!isInterrupted) {
                try {
                    if (this.taskException == null) {
                        try {
                            runnableTask.done(result);
                            // Notify listeners
                            for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                                listener.onDone(result);
                            }
                        } catch (Exception ex) {
                            runnableTask.handleException(ex);
                        }
                    } else {
                        runnableTask.handleException(taskException);
                    }
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
        public final void startExecution() {
            execute();
        }

        @Override
        public final boolean cancelExecution() {
            if (isClosed)
                return false;

            this.isInterrupted = true;

            if (!isDone() && !isCancelled()) {
                log.debug("Cancel task. User: " + userId);
                isClosed = cancel(true);
                return isClosed;
            } else
                return false;
        }

        @Override
        public final V getResult() {
            V result;
            try {
                result = get();
                this.done();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
            return result;
        }

        @Override
        public final BackgroundTask<T, V> getTask() {
            return runnableTask;
        }

        @Override
        public final boolean inProgress() {
            return !isClosed;
        }

        @Override
        public final void setFinalizer(Runnable finalizer) {
            this.finalizer = finalizer;
        }

        @Override
        public final Runnable getFinalizer() {
            return finalizer;
        }

        @SafeVarargs
        @Override
        public final void handleProgress(T... changes) {
            publish(changes);
        }
    }
}