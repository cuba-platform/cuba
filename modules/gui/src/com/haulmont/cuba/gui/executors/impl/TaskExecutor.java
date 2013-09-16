/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors.impl;

import com.haulmont.cuba.gui.executors.BackgroundTask;

/**
 * Task runner
 */
@SuppressWarnings("unused")
public interface TaskExecutor<T, V> {

    void startExecution();

    boolean cancelExecution();

    V getResult();

    BackgroundTask<T, V> getTask();

    boolean isCancelled();

    boolean isDone();

    boolean inProgress();

    /**
     * Done handler for clear resources
     *
     * @param finalizer Runnable handler
     */
    void setFinalizer(Runnable finalizer);

    Runnable getFinalizer();

    /**
     * Handle changes from working thread
     *
     * @param changes Changes
     */
    @SuppressWarnings({"unchecked"})
    void handleProgress(T... changes);
}