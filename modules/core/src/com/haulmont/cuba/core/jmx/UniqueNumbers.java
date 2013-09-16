/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.UniqueNumbersAPI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_UniqueNumbersMBean")
public class UniqueNumbers implements UniqueNumbersMBean {

    @Inject
    protected UniqueNumbersAPI uniqueNumbers;

    @Override
    public long getCurrentNumber(String domain) {
        return uniqueNumbers.getCurrentNumber(domain);
    }

    @Override
    public void setCurrentNumber(String domain, long value) {
        uniqueNumbers.setCurrentNumber(domain, value);
    }

    @Override
    public long getNextNumber(String domain) {
        return uniqueNumbers.getNextNumber(domain);
    }
}
