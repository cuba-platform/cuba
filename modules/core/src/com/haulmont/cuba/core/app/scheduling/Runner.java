/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.security.global.UserSession;

/**
 * Interface used by {@link Scheduling} to run scheduled tasks.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Runner {

    String NAME = "cuba_SchedulingRunner";

    void runTask(ScheduledTask task, long now, UserSession userSession);
}
