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

package com.haulmont.cuba.gui.executors.impl;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.WatchDog;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkState;

/**
 * Task handler
 */
public class TaskHandlerImpl<T, V> implements BackgroundTaskHandler<V> {

    private Logger log = LoggerFactory.getLogger(BackgroundWorker.class);

    private final TaskExecutor<T, V> taskExecutor;
    private final WatchDog watchDog;

    private volatile boolean started = false;

    private long startTimeStamp;
    private UserSession userSession;
    private Window.CloseListener closeListener;

    public TaskHandlerImpl(TaskExecutor<T, V> taskExecutor, WatchDog watchDog) {
        this.taskExecutor = taskExecutor;
        this.watchDog = watchDog;

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        this.userSession = sessionSource.getUserSession();

        BackgroundTask<T, V> task = taskExecutor.getTask();
        if (task.getOwnerFrame() != null) {
            Frame ownerFrame = task.getOwnerFrame();
            if (ownerFrame.getFrame() != null) {
                closeListener = actionId -> ownerWindowClosed();
                Window ownerWindow = ComponentsHelper.getWindowImplementation(ownerFrame);

                if (ownerWindow != null) {
                    ownerWindow.addCloseListener(closeListener);
                } else {
                    log.warn("Unable to find window for task owner frame");
                }
            }
        }

        // remove close listener on done
        taskExecutor.setFinalizer(new Runnable() {
            @Override
            public void run() {
                log.trace("Start task finalizer");

                disposeResources();

                log.trace("Finish task finalizer");
            }
        });
    }

    private void ownerWindowClosed() {
        if (log.isTraceEnabled()) {
            UUID userId = getUserSession().getId();
            Frame ownerFrame = getTask().getOwnerFrame();
            String windowClass = ownerFrame.getClass().getCanonicalName();
            log.trace("Window closed. User: " + userId + " Window: " + windowClass);
        }

        taskExecutor.cancelExecution();
    }

    @Override
    public final void execute() {
        checkState(!started, "Task is already started");

        this.started = true;

        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        this.startTimeStamp = timeSource.currentTimestamp().getTime();

        this.watchDog.manageTask(this);

        UUID userId = getUserSession().getId();
        if (log.isTraceEnabled()) {
            log.trace("Run task. User: " + userId);
        }

        taskExecutor.startExecution();
    }

    @Override
    public final boolean cancel() {
        checkState(started, "Task is not running");

        boolean canceled = taskExecutor.cancelExecution();
        if (canceled) {
            BackgroundTask<T, V> task = taskExecutor.getTask();
            try {
                task.canceled();
                // Notify listeners
                for (BackgroundTask.ProgressListener listener : task.getProgressListeners()) {
                    listener.onCancel();
                }
            } finally {
                disposeResources();
            }

            if (log.isTraceEnabled()) {
                UUID userId = getUserSession().getId();
                Frame ownerFrame = getTask().getOwnerFrame();
                String windowClass = ownerFrame.getClass().getCanonicalName();

                log.trace(String.format("Task was cancelled. User: %s Frame: %s", userId.toString(), windowClass));
            }
        } else {
            log.trace("Task wasn't cancelled. Execution is already cancelled");
        }

        return canceled;
    }

    private void disposeResources() {
        // force remove close listener
        Frame ownerFrame = getTask().getOwnerFrame();
        if (ownerFrame != null) {
            Window ownerWindow = ComponentsHelper.getWindowImplementation(ownerFrame);
            if (ownerWindow != null) {
                ownerWindow.removeCloseListener(closeListener);

                if (log.isTraceEnabled()) {
                    String windowClass = ownerFrame.getClass().getCanonicalName();
                    log.trace("Resources were disposed. Frame: " + windowClass);
                }
            } else {
                log.trace("Empty ownerWindow. Resources were not disposed");
            }
        } else {
            log.trace("Empty ownerFrame. Resources were not disposed");
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
            Frame ownerFrame = getTask().getOwnerFrame();

            disposeResources();

            if (log.isTraceEnabled()) {
                if (ownerFrame != null) {
                    String windowClass = ownerFrame.getClass().getCanonicalName();
                    log.trace("Task killed. User: " + userId + " Frame: " + windowClass);
                } else {
                    log.trace("Task killed. User: " + userId);
                }
            }
        }

        taskExecutor.cancelExecution();
    }

    /**
     * Cancel with timeout exceeded event
     */
    public final void timeoutExceeded() {
        Frame ownerFrame = getTask().getOwnerFrame();
        if (log.isTraceEnabled()) {
            if (ownerFrame != null) {
                String windowClass = ownerFrame.getClass().getCanonicalName();
                log.trace("Task timeout happened. Frame: " + windowClass);
            } else {
                log.trace("Task timeout happened");
            }
        }

        checkState(started, "Task is not running");

        boolean canceled = taskExecutor.cancelExecution();
        if (canceled) {
            try {
                BackgroundTask<T, V> task = taskExecutor.getTask();
                task.handleTimeoutException();
            } finally {
                disposeResources();
            }
        }

        if (log.isTraceEnabled()) {
            if (ownerFrame != null) {
                String windowClass = ownerFrame.getClass().getCanonicalName();
                log.trace("Timeout was processed. Frame: " + windowClass);
            } else {
                log.trace("Timeout was processed");
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