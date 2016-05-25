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

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.executors.WatchDog;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * WatchDog for {@link com.haulmont.cuba.gui.executors.BackgroundWorker}.
 */
@ThreadSafe
public abstract class TasksWatchDog implements WatchDog {

    public enum ExecutionStatus {
        NORMAL,
        TIMEOUT_EXCEEDED,
        SHOULD_BE_KILLED
    }

    @Inject
    protected TimeSource timeSource;

    private final Set<TaskHandlerImpl> watches;

    public TasksWatchDog() {
        watches = new LinkedHashSet<>();
    }

    @Override
    public synchronized void cleanupTasks() {
        if (!AppContext.isStarted()) {
            return;
        }

        long actual = timeSource.currentTimestamp().getTime();

        List<TaskHandlerImpl> forRemove = new LinkedList<>();
        for (TaskHandlerImpl task : watches) {
            if (task.isCancelled() || task.isDone()) {
                forRemove.add(task);
            } else {
                ExecutionStatus status = getExecutionStatus(actual, task);

                switch (status) {
                    case TIMEOUT_EXCEEDED:
                        task.timeoutExceeded();
                        forRemove.add(task);
                        break;

                    case SHOULD_BE_KILLED:
                        task.kill();
                        forRemove.add(task);
                        break;

                    default:
                        break;
                }
            }
        }

        watches.removeAll(forRemove);
    }

    protected abstract ExecutionStatus getExecutionStatus(long actualTimeMs, TaskHandlerImpl taskHandler);

    @Override
    public synchronized void stopTasks() {
        if (!AppContext.isStarted()) {
            return;
        }

        for (TaskHandlerImpl task : watches) {
            task.kill();
        }
        watches.clear();
    }

    @Override
    public synchronized int getActiveTasksCount() {
        return watches.size();
    }

    /**
     * {@inheritDoc}
     *
     * @param taskHandler Task handler
     */
    @Override
    public synchronized void manageTask(TaskHandlerImpl taskHandler) {
        watches.add(taskHandler);
    }

    @Override
    public synchronized void removeTask(TaskHandlerImpl taskHandler) {
        watches.remove(taskHandler);
    }
}