/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.TimeSource;

import javax.annotation.ManagedBean;
import java.util.Date;

/**
 * Standard implementation of {@link TimeSource} interface.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(TimeSource.NAME)
public class TimeSourceImpl implements TimeSource {

    @Override
    public Date currentTimestamp() {
        return new Date();
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
