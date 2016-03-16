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

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Convenience bean providing some methods for working with time zones.
 *
 */
@Component(TimeZones.NAME)
public class TimeZones {

    public static final String NAME = "cuba_TimeZones";

    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static final Pattern AD_HOC_TZ_PATTERN = Pattern.compile("GMT[+-].+");

    @Inject
    protected TimeSource timeSource;

    /**
     * Converts date between time zones.
     *
     * @param srcDate       date
     * @param srcTimeZone   source time zone
     * @param dstTimeZone   destination time zone
     * @return              date in destination time zone, or null if the source date is null
     */
    @Nullable
    public Date convert(@Nullable Date srcDate, TimeZone srcTimeZone, TimeZone dstTimeZone) {
        if (srcDate == null)
            return null;
        Preconditions.checkNotNullArgument(srcTimeZone, "srcTimeZone is null");
        Preconditions.checkNotNullArgument(dstTimeZone, "dstTimeZone is null");
        int srcOffset = srcTimeZone.getOffset(srcDate.getTime());
        int dstOffset = dstTimeZone.getOffset(srcDate.getTime());
        return new Date(srcDate.getTime() - srcOffset + dstOffset);
    }

    /**
     * @return string representing the offset of the given time zone from GMT
     */
    public String getDisplayOffset(@Nullable TimeZone timeZone) {
        if (timeZone == null)
            return "";

        int offsetSec = timeZone.getOffset(timeSource.currentTimeMillis()) / 1000;
        int offsetHours = offsetSec / 3600;
        int offsetMins = (offsetSec % 3600) / 60;
        String str = StringUtils.leftPad(String.valueOf(Math.abs(offsetHours)), 2, '0')
                + ":" + StringUtils.leftPad(String.valueOf(Math.abs(offsetMins)), 2, '0');
        String sign = offsetHours >= 0 ? "+" : "-";
        return "GMT" + sign + str;
    }

    /**
     * @return long string representation of the given time zone
     */
    public String getDisplayNameLong(@Nullable TimeZone timeZone) {
        if (timeZone == null)
            return "";
        String name = timeZone.getID();
        if (AD_HOC_TZ_PATTERN.matcher(name).matches())
            return name;
        else
            return name + " (" + getDisplayOffset(timeZone) + ")";
    }

    /**
     * @return short string representation of the given time zone
     */
    public String getDisplayNameShort(@Nullable TimeZone timeZone) {
        if (timeZone == null)
            return "";

        boolean dst = timeZone.inDaylightTime(timeSource.currentTimestamp());
        String name = timeZone.getDisplayName(dst, TimeZone.SHORT);

        if (AD_HOC_TZ_PATTERN.matcher(name).matches())
            return name;
        else
            return name + " (" + getDisplayOffset(timeZone) + ")";
    }
}
