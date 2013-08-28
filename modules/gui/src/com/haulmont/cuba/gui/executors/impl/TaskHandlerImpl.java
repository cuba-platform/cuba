/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors.impl;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.WatchDog;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkState;

/**
 * Task handler
 *
 * @author artamonov
 * @version $Id$
 */
public class
        TaskHandlerImpl<T, V> implements BackgroundTaskHandler<V> {

    private Log log = LogFactory.getLog(BackgroundWorker.class);

    private final TaskExecutor<T, V> taskExecutor;
    private final WatchDog watchDog;

    private volatile boolean started = false;

    private long startTimeStamp;
    private UserSession userSession;
    private Window.CloseListener closeListener;

    public TaskHandlerImpl(TaskExecutor<T, V> taskExecutor, WatchDog watchDog) {
        this.taskExecutor = taskExecutor;
        this.watchDog = watchDog;
        this.userSession = AppBeans.get(UserSessionSource.class).getUserSession();

        BackgroundTask<T, V> task = taskExecutor.getTask();
        if (task.getOwnerWindow() != null) {
            closeListener = new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    ownerWindowClosed();
                }
            };
            Window ownerWindow = task.getOwnerWindow();
            if (ownerWindow.getFrame() != null) {
                ownerWindow = ComponentsHelper.getWindow(ownerWindow);
            }
            ownerWindow.addListener(closeListener);
        }
        // remove close listener on done
        taskExecutor.setFinalizer(new Runnable() {
            @Override
            public void run() {
                disposeResources();
            }
        });
    }

    private void ownerWindowClosed() {
        if (isAlive()) {
            UUID userId = getUserSession().getId();
            Window ownerWindow = getTask().getOwnerWindow();
            String windowClass = ownerWindow.getClass().getCanonicalName();
            log.trace("Window closed. User: " + userId + " Window: " + windowClass);

            taskExecutor.cancelExecution();
        }
    }

    @Override
    public final void execute() {
        checkState(!started, "Task is already started");

        this.started = true;

        this.startTimeStamp = AppBeans.get(TimeSource.class).currentTimestamp().getTime();

        this.watchDog.manageTask(this);

        UUID userId = getUserSession().getId();
        log.trace("Run task. User: " + userId);

        taskExecutor.startExecution();
    }

    @Override
    public final boolean cancel() {
        checkState(started, "Task is not running");

        boolean canceled = false;
        if (isAlive()) {
            canceled = taskExecutor.cancelExecution();
            if (canceled) {
                BackgroundTask<T, V> task = taskExecutor.getTask();
                task.canceled();

                try {
                    // Notify listeners
                    for (BackgroundTask.ProgressListener listener : task.getProgressListeners()) {
                        listener.onCancel();
                    }
                } finally {
                    disposeResources();
                }
            }
        }
        return canceled;
    }

    private void disposeResources() {
        // force remove close listener
        Window ownerWindow = getTask().getOwnerWindow();
        if (ownerWindow != null) {
            if (ownerWindow.getFrame() != null) {
                ownerWindow = ownerWindow.getFrame();
            }

            ownerWindow.removeListener(closeListener);
        }
        closeListener = null;
    }

    /**
     * Join task thread to current <br/>
     * <b>Caution!</b> Call this method only from synchronous gui action
     *
     * @return Task result
     */
    @Override
    public final V getResult() {
        checkState(started, "Task is not running");

        return taskExecutor.getResult();
    }

    /**
     * Cancel without events for tasks
     */
    public final void close() {
        if (AppContext.isStarted()) {
            UUID userId = getUserSession().getId();
            Window ownerWindow = getTask().getOwnerWindow();

            disposeResources();

            if (ownerWindow != null) {
                String windowClass = ownerWindow.getClass().getCanonicalName();
                log.trace("Task killed. User: " + userId + " Window: " + windowClass);
            } else {
                log.trace("Task killed. User: " + userId);
            }
        }

        taskExecutor.cancelExecution();
    }

    /**
     * Cancel with timeout exceeded event
     */
    public final void timeoutExceeded() {
        checkState(started, "Task is not running");

        if (isAlive()) {
            boolean canceled = taskExecutor.cancelExecution();
            if (canceled) {
                BackgroundTask<T, V> task = taskExecutor.getTask();
                task.handleTimeoutException();

                disposeResources();
            }
        }
    }

    @Override
    public final boolean isDone() {
        return taskExecutor.isDone();
    }

    @Override
    public final boolean isCancelled() {
        return taskExecutor.isCancelled();
    }

    @Override
    public final boolean isAlive() {
        return taskExecutor.inProgress() && started;
    }

    public final BackgroundTask<T, V> getTask() {
        return taskExecutor.getTask();
    }

    public final UserSession getUserSession() {
        return userSession;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public long getTimeoutMs() {
        return taskExecutor.getTask().getTimeoutMilliseconds();
    }
}