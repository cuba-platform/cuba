/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.UniqueNumbers;
import com.haulmont.cuba.core.global.NumberIdSource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(NumberIdSource.NAME)
public class NumberIdSourceImpl implements NumberIdSource {

    @Inject
    protected UniqueNumbers uniqueNumbers;

    @Override
    public Long createLongId(String entityName) {
        return uniqueNumbers.getNextNumber(getDomain(entityName));
    }

    @Override
    public Integer createIntegerId(String entityName) {
        long nextLong = uniqueNumbers.getNextNumber(getDomain(entityName));
        int nextInt = (int) nextLong;
        if (nextInt != nextLong)
            throw new IllegalStateException("Error creating a new Integer ID for entity " + entityName
                    + ": sequence overflow");
        return nextInt;
    }

    protected String getDomain(String entityName) {
        return entityName.replace("$", "_");
    }
}
