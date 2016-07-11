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
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Desktop implementation of {@link BackgroundWorker}
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
        checkUIAccess();

        // create task handler
        DesktopTaskExecutor<T, V> taskExecutor = new DesktopTaskExecutor<>(task);
        TaskHandlerImpl<T, V> taskHandler = new TaskHandlerImpl<>(getUIAccessor(), taskExecutor, watchDog);

        taskExecutor.setTaskHandler(taskHandler);

        return taskHandler;
    }

    @Override
    public UIAccessor getUIAccessor() {
        checkUIAccess();

        return new DesktopUIAccessor();
    }

    @Override
    public void checkUIAccess() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalConcurrentAccessException();
        }
    }

    public static void checkSwingUIAccess() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalConcurrentAccessException();
        }
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
        private volatile boolean doneHandled = false;

        private Map<String, Object> params;
        private TaskHandlerImpl<T, V> taskHandler;

        private DesktopTaskExecutor(BackgroundTask<T, V> runnableTask) {
            this.runnableTask = runnableTask;

            UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
            this.userId = sessionSource.getUserSession().getId();

            this.params = runnableTask.getParams() != null ?
                    Collections.unmodifiableMap(runnableTask.getParams()) :
                    Collections.emptyMap();
        }

        @Override
        protected final V doInBackground() throws Exception {
            Thread.currentThread().setName(String.format("BackgroundTaskThread-%s",
                    System.identityHashCode(Thread.currentThread())));
            try {
                if (!Thread.currentThread().isInterrupted()) {
                    // do not run any activity if canceled before start
                    result = runnableTask.run(new TaskLifeCycle<T>() {
                        @SafeVarargs
                        @Override
                        public final void publish(T... changes) throws InterruptedException {
                            if (Thread.currentThread().isInterrupted()) {
                                throw new InterruptedException("Task is interrupted and is trying to publish changes");
                            }

                            handleProgress(changes);
                        }

                        @Override
                        public boolean isInterrupted() {
                            return Thread.currentThread().isInterrupted();
                        }

                        @Override
                        @Nonnull
                        public Map<String, Object> getParams() {
                            return params;
                        }
                    });
                }
            } catch (Exception ex) {
                // do not call log.error, exception may be handled later
                log.debug("Exception occurred in background task. Task: {}", runnableTask, ex);
                if (!(ex instanceof InterruptedException) && !isCancelled()) {
                    taskException = ex;
                }
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
            if (isCancelled()) {
                // handle cancel from edt before execution start
                log.trace("Done statement is not processed because it is canceled task. Task: {}", runnableTask);
                return;
            }

            if (isClosed) {
                log.trace("Done statement is not processed because it is already closed. Task: {}", runnableTask);
                return;
            }

            log.debug("Done task: {}. User: {}", runnableTask, userId);

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
                        log.error("Unhandled exception in background task. Task: {}", runnableTask, taskException);
                    }
                }

                log.trace("Task finished correctly. Task: {}", runnableTask);
            } finally {
                if (finalizer != null) {
                    finalizer.run();
                    finalizer = null;
                }

                isClosed = true;
                doneHandled = true;
            }
        }

        @Override
        public final void startExecution() {
            execute();
        }

        @Override
        public final boolean cancelExecution() {
            if (isClosed) {
                log.trace("Cancel will not be processed because it is already closed. Task: {}", runnableTask);
                return false;
            }

            log.debug("Cancel task. Task: {}. User: {}", runnableTask, userId);

            boolean isCanceledNow = cancel(true);

            if (isCanceledNow) {
                log.trace("Task was cancelled. Task: {}. User: {}", runnableTask, userId);
            } else {
                log.trace("Cancellation of task isn't processed. Task: {}. User: {}", runnableTask, userId);
            }

            if (!doneHandled) {
                log.trace("Done was not handled. Return 'true' as canceled status. Task: {}. User: {}",
                        runnableTask, userId);

                this.isClosed = true;
                return true;
            }

            return isCanceledNow;
        }

        @Override
        public final V getResult() {
            V result;
            try {
                result = get();
            } catch (InterruptedException | ExecutionException | CancellationException e) {
                log.debug("{} exception in background task: {}", e.getClass().getName(),
                        runnableTask, e);
                return null;
            }

            this.done();

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

    private static class DesktopUIAccessor implements UIAccessor {
        @Override
        public void access(Runnable runnable) {
            SwingUtilities.invokeLater(runnable);
        }

        @Override
        public void accessSynchronously(Runnable runnable) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException("Exception on access to UI from background thread", e);
            }
        }
    }
}