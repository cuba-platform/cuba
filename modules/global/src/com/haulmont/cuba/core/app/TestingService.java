/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

/**
 * Service interface for integration testing. Don't use it in application code!
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface TestingService {

    String NAME = "cuba_TestingService";

    String executeFor(int timeMillis);

    String execute();

    boolean primitiveParameters(boolean b, int i, long l, double d);

    /**
     * Warning! Removes all scheduled tasks from the database!
     */
    void clearScheduledTasks();
}
