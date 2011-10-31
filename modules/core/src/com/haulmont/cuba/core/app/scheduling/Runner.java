/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.security.global.UserSession;

/**
 * Interface used by {@link Scheduling} to run scheduled tasks.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Runner {

    String NAME = "cuba_SchedulingRunner";

    void runTask(ScheduledTask task, long now, UserSession userSession);
}
