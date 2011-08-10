/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface TestingService {

    String NAME = "cuba_TestingService";

    String executeFor(int timeMillis);

    boolean primitiveParameters(boolean b, int i, long l, double d);
}
