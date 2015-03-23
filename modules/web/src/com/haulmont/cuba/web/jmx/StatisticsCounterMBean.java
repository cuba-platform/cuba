/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedResource(description = "Provides information about web-client requests statistics")
public interface StatisticsCounterMBean {

    void incWebClientRequestsCount();

    Long getWebClientRequestsCount();
}
