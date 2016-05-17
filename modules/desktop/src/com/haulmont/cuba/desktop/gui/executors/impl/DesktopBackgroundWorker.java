/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
        DesktopTaskExecutor<T, V> taskExecutor = new DesktopTaskExecutor<>(task);
        TaskHandlerImpl<T, V> taskHandler = new TaskHandlerImpl<>(taskExecutor, watchDog);

        taskExecutor.setTaskHandler(taskHandler);

        return taskHandler;
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
        private TaskHandlerImpl<T, V> taskHandler;

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
            Thread.currentThread().setName("BackgroundTaskThread");
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
                log.error("Exception occurred in background task", ex);
                if (!(ex instanceof InterruptedException) && !isCancelled())
                    taskException = ex;
            } finally {
                watchDog.removeTask(taskHandler);
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
            if (isClosed) {
                log.trace("Done statement is not processed because it is already closed");
                return;
            }

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
            } else {
                log.trace("Done statement is not processed because task is interrupted");
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
                if (isClosed) {
                    log.trace("Task was cancelled. User: " + userId);
                } else {
                    log.trace("Cancellation of task isn't processed. User: " + userId);
                }
                return isClosed;
            } else {
                log.trace("Cancellation of task isn't processed because it's already done or cancelled. User: " + userId);
                return false;
            }
        }

        @Override
        public final V getResult() {
            V result;
            try {
                result = get();
                this.done();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Interrupted or execution exception in background task", e);
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

        public void setTaskHandler(TaskHandlerImpl<T,V> taskHandler) {
            this.taskHandler = taskHandler;
        }
    }
}