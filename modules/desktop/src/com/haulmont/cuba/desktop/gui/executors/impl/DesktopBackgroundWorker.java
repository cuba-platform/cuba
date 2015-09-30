/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.executors.impl;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.executors.*;
import com.haulmont.cuba.gui.executors.impl.TaskExecutor;
import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.annotation.Nonnull;
import javax.inject.Inject;
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
@Component(BackgroundWorker.NAME)
public class DesktopBackgroundWorker implements BackgroundWorker {

    private Logger log = LoggerFactory.getLogger(DesktopBackgroundWorker.class);

    private WatchDog watchDog;

    @Inject
    public DesktopBackgroundWorker(WatchDog watchDog) {
        this.watchDog = watchDog;
    }

    @Override
    public <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task) {
        checkNotNull(task);

        // create task handler
        TaskExecutor<T, V> taskExecutor = new DesktopTaskExecutor<>(task);

        return new TaskHandlerImpl<>(taskExecutor, watchDog);
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
            UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
            userId = sessionSource.getUserSession().getId();

            //noinspection unchecked
            this.params = runnableTask.getParams();
            if (this.params != null)
                this.params = Collections.unmodifiableMap(this.params);
            else
                this.params = Collections.emptyMap();
        }

        @Override
        protected final V doInBackground() throws Exception {
            try {
                if (!isInterrupted) {
                    // do not run any activity if canceled before start
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
                        @Nonnull
                        public Map<String, Object> getParams() {
                            return params;
                        }
                    });
                }
            } catch (Exception ex) {
                if (!(ex instanceof InterruptedException) && !isCancelled())
                    taskException = ex;
            }
            return result;
        }

        @Override
        protected final void process(List<T> chunks) {
            runnableTask.progress(chunks);
            // Notify listeners
            for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                listener.onProgress(chunks);
            }
        }

        @Override
        protected final void done() {
            if (isClosed)
                return;

            if (!isInterrupted) {
                try {
                    if (this.taskException == null) {
                        runnableTask.done(result);
                        // Notify listeners
                        for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                            listener.onDone(result);
                        }
                    } else {
                        boolean handled = runnableTask.handleException(taskException);
                        if (!handled) {
                            log.error("Unhandled exception in background task", taskException);
                        }
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