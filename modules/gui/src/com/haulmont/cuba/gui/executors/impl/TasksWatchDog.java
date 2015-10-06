/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors.impl;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.WatchDog;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * WatchDog for {@link com.haulmont.cuba.gui.executors.BackgroundWorker}.
 *
 * @author artamonov
 * @version $Id$
 */
@ThreadSafe
public abstract class TasksWatchDog implements WatchDog {

    @Inject
    protected TimeSource timeSource;

    private final Set<TaskHandlerImpl> watches;

    public TasksWatchDog() {
        watches = new LinkedHashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void cleanupTasks() {
        if (!AppContext.isStarted())
            return;

        long actual = timeSource.currentTimestamp().getTime();

        List<TaskHandlerImpl> forRemove = new LinkedList<>();
        for (TaskHandlerImpl task : watches) {
            if (task.isCancelled() || task.isDone()) {
                forRemove.add(task);
            } else if (checkHangup(actual, task)) {
                task.close();
                forRemove.add(task);
            }
        }

        watches.removeAll(forRemove);
    }

    protected abstract boolean checkHangup(long actualTimeMs, TaskHandlerImpl taskHandler);

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void stopTasks() {
        if (!AppContext.isStarted())
            return;

        for (TaskHandlerImpl task : watches) {
            task.close();
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