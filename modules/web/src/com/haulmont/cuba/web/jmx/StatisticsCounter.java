/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.web.app.StatisticsCounterBean;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedBean("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    @Inject
    protected StatisticsCounterBean counterBean;

    @Override
    public Long getWebClientRequestsCount() {
        return counterBean.getWebClientRequestsCount();
    }

}
