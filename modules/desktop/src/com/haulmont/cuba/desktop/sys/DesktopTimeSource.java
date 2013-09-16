/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.TimeSource;

import javax.annotation.concurrent.GuardedBy;
import java.util.Date;

/**
 * Desktop client implementation of {@link TimeSource} interface.
 * <p>Can adjust returned time according to the middleware time.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTimeSource implements TimeSource {

    /**
     * Time offset (time difference between server and client machine).
     * It will be used to correct time obtained from system clock.
     */
    @GuardedBy("this")
    protected long timeOffset;

    @Override
    public Date currentTimestamp() {
        return new Date(currentTimeMillis());
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis() + getTimeOffset();
    }

    /* Must be used to access time offset */
    protected synchronized long getTimeOffset() {
        return timeOffset;
    }

    public synchronized void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }
}
