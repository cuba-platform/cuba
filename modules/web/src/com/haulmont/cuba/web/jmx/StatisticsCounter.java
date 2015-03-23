/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import javax.annotation.ManagedBean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedBean("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    private AtomicLong webClientRequestsCount = new AtomicLong();

    @Override
    public void incWebClientRequestsCount() {
        webClientRequestsCount.incrementAndGet();
    }

    @Override
    public Long getWebClientRequestsCount() {
        return webClientRequestsCount.get();
    }

}
