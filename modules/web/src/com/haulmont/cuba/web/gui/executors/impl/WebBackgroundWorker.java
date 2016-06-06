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

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.executors.*;
import com.haulmont.cuba.gui.executors.impl.TaskExecutor;
import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web implementation of {@link BackgroundWorker}
 */
@Component(BackgroundWorker.NAME)
public class WebBackgroundWorker implements BackgroundWorker {

    private Logger log = LoggerFactory.getLogger(WebBackgroundWorker.class);

    @Inject
    private WatchDog watchDog;

    @Inject
    private UserSessionSource userSessionSource;

    private Configuration configuration;

    private ExecutorService executorService;

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
        ThreadFactory threadFactory = new ThreadFactory() {
            final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(@Nonnull final Runnable r) {
                Thread thread = defaultFactory.newThread(r);
                thread.setName("BackgroundTaskThread-" + thread.getName());
                thread.setDaemon(true);
                return thread;
            }
        };

        this.executorService = new ThreadPoolExecutor(
                webConfig.getMinBackgroundThreadsCount(),
                webConfig.getMaxActiveBackgroundTasksCount(),
                10L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                threadFactory);
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
        AppWindow appWindow = appInstance.getAppWindow();
        final WebTaskExecutor<T, V> taskExecutor = new WebTaskExecutor<>(appWindow, task);

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

    protected static void withUserSessionAsync(UI ui, Runnable handler) {
        ui.access(() -> {
            SecurityContext oldSecurityContext = AppContext.getSecurityContext();
            try {
                UserSession userSession = ui.getSession().getAttribute(UserSession.class);
                if (userSession != null) {
                    AppContext.setSecurityContext(new SecurityContext(userSession));
                }

                handler.run();
            } finally {
                AppContext.setSecurityContext(oldSecurityContext);
            }
        });
    }

    protected static void withUserSessionInvoke(UI ui, Runnable handler) {
        ui.accessSynchronously(() -> {
            SecurityContext oldSecurityContext = AppContext.getSecurityContext();
            try {
                UserSession userSession = ui.getSession().getAttribute(UserSession.class);
                if (userSession != null) {
                    AppContext.setSecurityContext(new SecurityContext(userSession));
                }

                handler.run();
            } finally {
                AppContext.setSecurityContext(oldSecurityContext);
            }
        });
    }

    private class WebTaskExecutor<T, V> implements TaskExecutor<T, V>, Callable<V> {

        private App app;
        private AppWindow appWindow;

        private FutureTask<V> future;

        private BackgroundTask<T, V> runnableTask;
        private Runnable finalizer;

        private volatile boolean isClosed = false;
        private volatile boolean doneHandled = false;

        private SecurityContext securityContext;
        private UUID userId;

        private Map<String, Object> params;
        private TaskHandlerImpl<T, V> taskHandler;

        private WebTaskExecutor(AppWindow appWindow, BackgroundTask<T, V> runnableTask) {
            this.runnableTask = runnableTask;
            this.appWindow = appWindow;
            this.app = appWindow.getAppUI().getApp();

            this.params = runnableTask.getParams() != null ?
                    Collections.unmodifiableMap(runnableTask.getParams()) :
                    Collections.emptyMap();

            // copy security context
            this.securityContext = new SecurityContext(AppContext.getSecurityContextNN().getSession());
            this.userId = userSessionSource.getUserSession().getId();

            this.future = new FutureTask<V>(this) {
                @Override
                protected void done() {
                    withUserSessionAsync(() ->
                            handleDone()
                    );
                }
            };
        }

        @Override
        public final V call() throws Exception {
            Thread.currentThread().setName(String.format("BackgroundTaskThread-%s-%s",
                    System.identityHashCode(Thread.currentThread()), userId));

            // Set security permissions
            AppContext.setSecurityContext(securityContext);
            try {
                // do not run any activity if canceled before start
                return runnableTask.run(new TaskLifeCycle<T>() {
                    @SafeVarargs
                    @Override
                    public final void publish(T... changes) {
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
                withUserSessionAsync(() ->
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

            if (log.isDebugEnabled()) {
                log.debug("Done task. User: " + userId);
            }

            // do not allow to cancel task from done listeners and exception handler
            isClosed = true;

            app.removeBackgroundTask(future);
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

            log.debug("Cancel task. User: {}", userId);

            boolean isCanceledNow = future.cancel(true);
            if (isCanceledNow) {
                log.trace("Task was cancelled. User: {}", userId);
            } else {
                log.trace("Cancellation of task isn't processed. User: {}", userId);
            }

            if (!doneHandled) {
                log.trace("Done was not handled. Return 'true' as canceled status. User: {}", userId);

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
            executorService.execute(() -> future.run());
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

        protected final void withUserSessionAsync(Runnable handler) {
            AppUI ui = appWindow.getAppUI();

            WebBackgroundWorker.withUserSessionAsync(ui, handler);
        }
    }

    private static class WebUIAccessor implements UIAccessor {
        private UI ui;

        public WebUIAccessor(UI ui) {
            this.ui = ui;
        }

        @Override
        public void access(Runnable runnable) {
            WebBackgroundWorker.withUserSessionAsync(ui, runnable);
        }

        @Override
        public void accessSynchronously(Runnable runnable) {
            WebBackgroundWorker.withUserSessionInvoke(ui, runnable);
        }
    }
}