/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app;

import com.haulmont.cuba.core.sys.StatisticsAccumulator;

import javax.annotation.ManagedBean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedBean(WebStatisticsAccumulator.NAME)
public class WebStatisticsAccumulator extends StatisticsAccumulator {

    public static final String NAME = "cuba_WebStatisticsAccumulator";

    protected AtomicLong webRequestsCount = new AtomicLong();

    public void incWebRequestsCount() {
        webRequestsCount.incrementAndGet();
    }

    public Long getWebRequestsCount() {
        return webRequestsCount.get();
    }

    public double getWebRequestsPerSecond() {
        return getWebRequestsCount() / ((System.currentTimeMillis() - startTime) / 1000.0);
    }
}
