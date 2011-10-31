/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.util.Date;

/**
 * Global time source interface. Must be used everywhere instead of <code>new Date()</code> and
 * <code>System.currentTimeMillis()</code>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface TimeSource {

    String NAME = "cuba_TimeSource";

    /**
     * Return current timestamp as Date instance.
     * @return  current timestamp
     */
    Date currentTimestamp();

    /**
     * Return current timestamp in milliseconds.
     * @return  number of milliseconds since 1970-01-01 00:00
     */
    long currentTimeMillis();
}
