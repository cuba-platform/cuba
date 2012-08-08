/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.executors;

import com.haulmont.cuba.gui.executors.impl.TaskHandler;

/**
 * @author artamonov
 * @version $Id$
 */
public interface WatchDog {

    String NAME = "cuba_BackgroundWorker_WatchDog";

    /**
     * Add task under WatchDog control
     *
     * @param backroundTask Task handler
     */
    void manageTask(TaskHandler backroundTask);

    /**
     * Remove finished, canceled or hangup tasks
     */
    void cleanupTasks();

    /**
     * Stop execution of all background tasks
     */
    void stopTasks();

    /**
     * @return Active tasks count
     */
    int getActiveTasksCount();
}