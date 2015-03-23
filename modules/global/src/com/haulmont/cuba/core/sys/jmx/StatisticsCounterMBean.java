/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import javax.management.openmbean.CompositeType;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedResource(description = "Provides various information about middleware performance statistics")
public interface StatisticsCounterMBean {

    void incStartedTransactionsCount();

    void incCommittedTransactionsCount();

    void incRolledBackTransactionsCount();

    void incMiddlewareRequestsCount();

    void incSchedulersCallsCount();

    Long getActiveTransactionsCount();

    Long getStartedTransactionsCount();

    Long getCommittedTransactionsCount();

    Long getRolledBackTransactionsCount();

    Long getMiddlewareRequestsCount();

    Long getSchedulersCallsCount();
}
