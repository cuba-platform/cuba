/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.util.Date;

/**
 * Global time source interface. Must be used everywhere instead of <code>new Date()</code
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface TimeSource {

    String NAME = "cuba_TimeSource";

    Date currentTimestamp();
}
