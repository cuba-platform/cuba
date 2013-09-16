/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
