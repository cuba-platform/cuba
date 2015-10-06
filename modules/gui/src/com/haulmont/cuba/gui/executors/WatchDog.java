/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;

/**
 * @author artamonov
 * @version $Id$
 */
@SuppressWarnings("unused")
public interface WatchDog {

    String NAME = "cuba_BackgroundWorker_WatchDog";

    /**
     * Add task under WatchDog control.
     *
     * @param taskHandler task handler
     */
    void manageTask(TaskHandlerImpl taskHandler);

    /**
     * Task completed, remove it from watches.
     *
     * @param taskHandler task handler
     */
    void removeTask(TaskHandlerImpl taskHandler);

    /**
     * Remove finished, canceled or hangup tasks.
     */
    void cleanupTasks();

    /**
     * Stop execution of all background tasks.
     */
    void stopTasks();

    /**
     * @return active tasks count
     */
    int getActiveTasksCount();
}