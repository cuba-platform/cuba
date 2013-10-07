/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
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
