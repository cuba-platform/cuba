/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app;

import javax.annotation.ManagedBean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedBean(StatisticsCounterBean.NAME)
public class StatisticsCounterBean {
    public static final String NAME = "cuba_StatisticsCounter";

    private AtomicLong webClientRequestsCount = new AtomicLong();

    public void incWebClientRequestsCount() {
        webClientRequestsCount.incrementAndGet();
    }

    public Long getWebClientRequestsCount() {
        return webClientRequestsCount.get();
    }
}
