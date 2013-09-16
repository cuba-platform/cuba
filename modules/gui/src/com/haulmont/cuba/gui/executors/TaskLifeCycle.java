/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.executors;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Lifecycle object that is passed to {@link BackgroundTask#run(TaskLifeCycle)} method to allow working thread to
 * interact with the execution environment.
 *
 * @param <T> task progress measurement unit
 *
 * @author artamonov
 * @version $Id$
 */
public interface TaskLifeCycle<T> {

    /**
     * Publish changes to show progress.
     *
     * @param changes Changes
     */
    @SuppressWarnings({"unchecked"})
    void publish(T... changes);

    /**
     * @return true if the working thread has been interrupted
     */
    boolean isInterrupted();

    /**
     * @return execution parameters that was set by {@link BackgroundTask#getParams()}
     */
    @Nonnull
    Map<String, Object> getParams();
}