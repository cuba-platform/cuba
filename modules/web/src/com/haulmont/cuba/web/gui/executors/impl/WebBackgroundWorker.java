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

package com.haulmont.cuba.web.gui.executors.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.event.BackgroundTaskUnhandledExceptionEvent;
import com.haulmont.cuba.gui.executors.*;
import com.haulmont.cuba.gui.executors.impl.TaskExecutor;
import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web implementation of {@link BackgroundWorker}
 */
@Component(BackgroundWorker.NAME)
public class WebBackgroundWorker implements BackgroundWorker {

    private static final Logger log = LoggerFactory.getLogger(WebBackgroundWorker.class);

    private static final String THREAD_NAME_PREFIX = "BackgroundTask-";
    private static final Pattern THREAD_NAME_PATTERN = Pattern.compile("BackgroundTask-([0-9]+)");

    @Inject
    protected WatchDog watchDog;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Events events;

    protected Configuration configuration;

    protected ExecutorService executorService;

    public WebBackgroundWorker() {
    }

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;

        createThreadPoolExecutor();
    }

    protected void createThreadPoolExecutor() {
        if (executorService != null) {
            return;
        }

        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        this.executorService = new ThreadPoolExecutor(
                webConfig.getMinBackgroundThreadsCount(),
                webConfig.getMaxActiveBackgroundTasksCount(),
                10L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                        .setNameFormat(THREAD_NAME_PREFIX + "%d")
                        .build()
        );
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdownNow();
    }

    @Override
    public <T, V> BackgroundTaskHandler<V> handle(final BackgroundTask<T, V> task) {
        checkNotNull(task);
        checkUIAccess();

        App appInstance;
        try {
            appInstance = App.getInstance();
        } catch (IllegalStateException ex) {
            log.error("Couldn't handle task", ex);
            throw ex;
        }

        // create task executor
        final WebTaskExecutor<T, V> taskExecutor = new WebTaskExecutor<>(appInstance.getAppUI(), task);

        // add thread to taskSet
        appInstance.addBackgroundTask(taskExecutor.getFuture());

        // create task handler
        TaskHandlerImpl<T, V> taskHandler = new TaskHandlerImpl<>(getUIAccessor(), taskExecutor, watchDog);
        taskExecutor.setTaskHandler(taskHandler);

        return taskHandler;
    }

    @Override
    public UIAccessor getUIAccessor() {
        checkUIAccess();

        return new WebUIAccessor(UI.getCurrent());
    }

    @Override
    public void checkUIAccess() {
        VaadinSession vaadinSession = VaadinSession.getCurrent();

        if (vaadinSession == null || !vaadinSession.hasLock()) {
            throw new IllegalConcurrentAccessException();
        }
    }

    private class WebTaskExecutor<T, V> implements TaskExecutor<T, V>, Callable<V> {

        private AppUI ui;

        private FutureTask<V> future;

        private BackgroundTask<T, V> runnableTask;
        private Runnable finalizer;

        private volatile boolean isClosed = false;
        private volatile boolean doneHandled = false;

        private SecurityContext securityContext;
        private String userLogin;

        private Map<String, Object> params;
        private TaskHandlerImpl<T, V> taskHandler;

        private WebTaskExecutor(AppUI ui, BackgroundTask<T, V> runnableTask) {
            this.runnableTask = runnableTask;
            this.ui = ui;

            this.params = runnableTask.getParams() != null ?
                    Collections.unmodifiableMap(runnableTask.getParams()) :
                    Collections.emptyMap();

            // copy security context
            this.securityContext = new SecurityContext(AppContext.getSecurityContextNN().getSession());

            UserSession userSession = userSessionSource.getUserSession();
            this.userLogin = userSession.getUser().getLogin();

            this.future = new FutureTask<V>(this) {
                @Override
                protected void done() {
                    WebTaskExecutor.this.ui.access(() ->
                            handleDone()
                    );
                }
            };
        }

        @Override
        public final V call() throws Exception {
            String threadName = Thread.currentThread().getName();
            Matcher matcher = THREAD_NAME_PATTERN.matcher(threadName);
            if (matcher.find()) {
                Thread.currentThread().setName(THREAD_NAME_PREFIX + matcher.group(1) + "-" + userLogin);
            }

            // Set security permissions
            AppContext.setSecurityContext(securityContext);
            try {
                // do not run any activity if canceled before start
                return runnableTask.run(new TaskLifeCycle<T>() {
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
            } finally {
                // Set null security permissions
                AppContext.setSecurityContext(null);
            }
        }

        @SafeVarargs
        @Override
        public final void handleProgress(T... changes) {
            if (changes != null) {
                ui.access(() ->
                        process(Arrays.asList(changes))
                );
            }
        }

        @ExecutedOnUIThread
        protected final void process(List<T> chunks) {
            runnableTask.progress(chunks);
            // Notify listeners
            for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                listener.onProgress(chunks);
            }
        }

        @ExecutedOnUIThread
        protected final void handleDone() {
            if (isCancelled()) {
                // handle cancel from edt before execution start
                log.trace("Done statement is not processed because it is canceled task");
                return;
            }

            if (isClosed) {
                log.trace("Done statement is not processed because it is already closed");
                return;
            }

            log.debug("Done task. User: {}", userLogin);

            // do not allow to cancel task from done listeners and exception handler
            isClosed = true;

            ui.getApp().removeBackgroundTask(future);
            watchDog.removeTask(taskHandler);

            try {
                V result = future.get();

                runnableTask.done(result);
                // Notify listeners
                for (BackgroundTask.ProgressListener<T, V> listener : runnableTask.getProgressListeners()) {
                    listener.onDone(result);
                }
            } catch (CancellationException e) {
                log.debug("Cancellation exception in background task", e);
            } catch (InterruptedException e) {
                log.debug("Interrupted exception in background task", e);
            } catch (ExecutionException e) {
                // do not call log.error, exception may be handled later
                log.debug("Exception in background task", e);
                if (!future.isCancelled()) {
                    boolean handled = false;

                    if (e.getCause() instanceof Exception) {
                        handled = runnableTask.handleException((Exception) e.getCause());
                    }

                    if (!handled) {
                        log.error("Unhandled exception in background task", e);
                        events.publish(new BackgroundTaskUnhandledExceptionEvent(this, runnableTask, e));
                    }
                }
            } finally {
                if (finalizer != null) {
                    finalizer.run();
                    finalizer = null;
                }

                doneHandled = true;
            }
        }

        @ExecutedOnUIThread
        @Override
        public final boolean cancelExecution() {
            if (isClosed) {
                return false;
            }

            log.debug("Cancel task. User: {}", userLogin);

            boolean isCanceledNow = future.cancel(true);
            if (isCanceledNow) {
                log.trace("Task was cancelled. User: {}", userLogin);
            } else {
                log.trace("Cancellation of task isn't processed. User: {}", userLogin);
            }

            if (!doneHandled) {
                log.trace("Done was not handled. Return 'true' as canceled status. User: {}", userLogin);

                this.isClosed = true;
                return true;
            }

            return isCanceledNow;
        }

        @ExecutedOnUIThread
        @Override
        public final V getResult() {
            V result;
            try {
                result = future.get();
            } catch (InterruptedException | ExecutionException | CancellationException e) {
                log.debug("{} exception in background task", e.getClass().getName(), e);
                return null;
            }

            this.handleDone();

            return result;
        }

        @Override
        public final BackgroundTask<T, V> getTask() {
            return runnableTask;
        }

        @ExecutedOnUIThread
        @Override
        public final void startExecution() {
            // Start thread
            executorService.execute(() ->
                    future.run()
            );
        }

        @Override
        public final boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public final boolean isDone() {
            return future.isDone();
        }

        @Override
        public final boolean inProgress() {
            return !isClosed;
        }

        @ExecutedOnUIThread
        @Override
        public final void setFinalizer(Runnable finalizer) {
            this.finalizer = finalizer;
        }

        @Override
        public final Runnable getFinalizer() {
            return finalizer;
        }

        public void setTaskHandler(TaskHandlerImpl<T,V> taskHandler) {
            this.taskHandler = taskHandler;
        }

        public FutureTask<V> getFuture() {
            return future;
        }
    }

    private static class WebUIAccessor implements UIAccessor {
        private UI ui;

        public WebUIAccessor(UI ui) {
            this.ui = ui;
        }

        @Override
        public void access(Runnable runnable) {
            ui.access(runnable);
        }

        @Override
        public void accessSynchronously(Runnable runnable) {
            ui.accessSynchronously(runnable);
        }
    }
}