/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.TimeSource;

import javax.annotation.concurrent.GuardedBy;
import java.util.Date;

/**
 * Desktop client implementation of {@link TimeSource} interface.
 * <p>Can adjust returned time according to the middleware time.</p>
 *
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
