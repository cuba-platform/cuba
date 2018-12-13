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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.event.BackgroundTaskTimeoutEvent;
import com.haulmont.cuba.gui.executors.*;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkState;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.addAfterDetachListener;

public class TaskHandlerImpl<T, V> implements BackgroundTaskHandler<V> {

    private static final Logger log = LoggerFactory.getLogger(BackgroundWorker.class);

    private UIAccessor uiAccessor;
    private final TaskExecutor<T, V> taskExecutor;
    private final WatchDog watchDog;
    private Events events;

    private volatile boolean started = false;
    private volatile boolean timeoutHappens = false;

    private long startTimeStamp;
    private UserSession userSession;

    private Subscription afterDetachSubscription;

    public TaskHandlerImpl(UIAccessor uiAccessor, TaskExecutor<T, V> taskExecutor, WatchDog watchDog) {
        this.uiAccessor = uiAccessor;
        this.taskExecutor = taskExecutor;
        this.watchDog = watchDog;
        this.events = AppBeans.get(Events.NAME);

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        this.userSession = sessionSource.getUserSession();

        BackgroundTask<T, V> task = taskExecutor.getTask();
        if (task.getOwnerScreen() != null) {
            Screen ownerFrame = task.getOwnerScreen();

            afterDetachSubscription = addAfterDetachListener(ownerFrame, e -> ownerWindowRemoved(e.getSource()));

            // remove close listener on done
            taskExecutor.setFinalizer(() -> {
                log.trace("Start task finalizer. Task: {}", taskExecutor.getTask());

                removeAfterDetachListener();

                log.trace("Finish task finalizer. Task: {}", taskExecutor.getTask());
            });
        }
    }

    protected void ownerWindowRemoved(FrameOwner frameOwner) {
        if (log.isTraceEnabled()) {
            String windowClass = frameOwner.getClass().getCanonicalName();
            log.trace("Window removed. User: {}. Window: {}", getUserSession().getId(), windowClass);
        }

        taskExecutor.cancelExecution();
    }

    @Override
    public final void execute() {
        checkState(!started, "Task is already started. Task: " + taskExecutor.getTask().toString());

        this.started = true;

        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        this.startTimeStamp = timeSource.currentTimestamp().getTime();

        this.watchDog.manageTask(this);

        log.trace("Run task: {}. User: {}", taskExecutor.getTask(), getUserSession().getId());

        taskExecutor.startExecution();
    }

    @Override
    public final boolean cancel() {
        checkState(started, "Task is not running. Task: " + taskExecutor.getTask().toString());

        boolean canceled = taskExecutor.cancelExecution();
        if (canceled) {
            removeAfterDetachListener();

            BackgroundTask<T, V> task = taskExecutor.getTask();
            task.canceled();

            // Notify listeners
            for (BackgroundTask.ProgressListener listener : task.getProgressListeners()) {
                listener.onCancel();
            }

            if (log.isTraceEnabled()) {
                Screen ownerFrame = getTask().getOwnerScreen();
                if (ownerFrame != null) {
                    String windowClass = ownerFrame.getClass().getCanonicalName();

                    log.trace("Task was cancelled. Task: {}. User: {}. Frame: {}", taskExecutor.getTask(), getUserSession().getId(), windowClass);
                } else {
                    log.trace("Task was cancelled. Task: {}. User: {}", taskExecutor.getTask(), getUserSession().getId());
                }
            }
        } else {
            log.trace("Task wasn't cancelled. Execution is already cancelled. Task: {}", taskExecutor.getTask());
        }

        return canceled;
    }

    protected void removeAfterDetachListener() {
        if (afterDetachSubscription != null) {
            afterDetachSubscription.remove();
            afterDetachSubscription = null;
        }
    }

    /**
     * Join task thread to current <br>
     * <b>Caution!</b> Call this method only from synchronous gui action
     *;
     * @return Task result
     */
    @Override
    public final V getResult() {
        checkState(started, "Task is not running");

        return taskExecutor.getResult();
    }

    /**
     * Cancel without events for tasks. Need to execute #timeoutExceeded after this method
     */
    public final void closeByTimeout() {
        timeoutHappens = true;
        kill();
    }

    /**
     * Cancel without events for tasks
     */
    public final void kill() {
        uiAccessor.access(() -> {
            Screen ownerFrame = getTask().getOwnerScreen();

            removeAfterDetachListener();

            if (log.isTraceEnabled()) {
                if (ownerFrame != null) {
                    String windowClass = ownerFrame.getClass().getCanonicalName();
                    log.trace("Task killed. Task: {}. User: {}. Frame: {}", taskExecutor.getTask(), getUserSession().getId(), windowClass);
                } else {
                    log.trace("Task killed. Task: {}. User: {}", taskExecutor.getTask(), getUserSession().getId());
                }
            }

            taskExecutor.cancelExecution();
        });
    }

    /**
     * Cancel with timeout exceeded event
     */
    public final void timeoutExceeded() {
        uiAccessor.access(() -> {
            Screen ownerFrame = getTask().getOwnerScreen();
            if (log.isTraceEnabled()) {
                if (ownerFrame != null) {
                    String windowClass = ownerFrame.getClass().getCanonicalName();
                    log.trace("Task timeout exceeded. Task: {}. Frame: {}", taskExecutor.getTask(), windowClass);
                } else {
                    log.trace("Task timeout exceeded. Task: {}", taskExecutor.getTask());
                }
            }

            checkState(started, "Task is not running");

            boolean canceled = taskExecutor.cancelExecution();
            if (canceled || timeoutHappens) {
                removeAfterDetachListener();

                BackgroundTask<T, V> task = taskExecutor.getTask();
                boolean handled = task.handleTimeoutException();
                if (!handled) {
                    log.error("Unhandled timeout exception in background task. Task: " + task.toString());
                    events.publish(new BackgroundTaskTimeoutEvent(this, task));
                }
            }

            if (log.isTraceEnabled()) {
                if (ownerFrame != null) {
                    String windowClass = ownerFrame.getClass().getCanonicalName();
                    log.trace("Timeout was processed. Task: {}. Frame: {}", taskExecutor.getTask(), windowClass);
                } else {
                    log.trace("Timeout was processed. Task: {}", taskExecutor.getTask());
                }
            }
        });
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