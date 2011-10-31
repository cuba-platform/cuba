/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.entity.ScheduledTask;

import java.util.List;

/**
 * Interface used by {@link Scheduling} to coordinate work in distributed environment. Class {@link Scheduling} itself
 * doesn't contain any logic to provide synchronization of singleton schedulers, delegating this task to the
 * <code>Coordinator</code>.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Coordinator {

    String NAME = "cuba_SchedulingCoordinator";

    public interface Context {
        List<ScheduledTask> getTasks();
    }

    Context begin();

    void end(Context context);

    boolean isLastExecutionFinished(ScheduledTask task, long now);
}
