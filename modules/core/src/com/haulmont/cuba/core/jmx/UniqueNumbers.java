/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
