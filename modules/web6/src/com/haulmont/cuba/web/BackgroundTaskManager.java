/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class BackgroundTaskManager {

    private transient Set<Thread> taskSet;

    public BackgroundTaskManager() {
        taskSet = Collections.synchronizedSet(new LinkedHashSet<Thread>());
    }

    /**
     * Add task to task set
     * @param task Task
     */
    public void addTask(Thread task) {
        taskSet.add(task);
    }

    /**
     * Stop manage of stopped task
     * @param task Task
     */
    public void removeTask(Thread task) {
        taskSet.remove(task);
    }

    /**
     * Interrupt all tasks
     */
    public void cleanupTasks() {
        // Stop threads
        for (Thread taskThread : taskSet) {
            if (taskThread.isAlive())
                taskThread.interrupt();
        }
        // Clean task set
        taskSet.clear();
    }
}
