/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.SupportedByClient;

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

    String executeWithException() throws TestException;

    /**
     * Warning! Removes all scheduled tasks from the database!
     */
    void clearScheduledTasks();

    @SupportedByClient
    public static class TestException extends Exception {

        public TestException(String message) {
            super(message);
        }
    }
}
