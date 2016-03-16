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

package com.haulmont.cuba.web.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 */
public class BackgroundTaskManager {

    private static Logger log = LoggerFactory.getLogger(BackgroundTaskManager.class);

    private transient Set<Thread> taskSet;

    public BackgroundTaskManager() {
        taskSet = Collections.synchronizedSet(new LinkedHashSet<>());
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
        int count = 0;
        // Stop threads
        for (Thread taskThread : taskSet) {
            if (taskThread.isAlive())   {
                taskThread.interrupt();
                count++;
            }
        }
        // Clean task set
        taskSet.clear();
        // Clean task set
        taskSet.clear();
        if (count > 0) {
            log.debug(String.format("Interrupted %s background tasks", count));
        }
    }
}