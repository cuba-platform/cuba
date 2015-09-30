/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.app.NumberIdService;
import com.haulmont.cuba.core.global.NumberIdSource;
import com.haulmont.cuba.core.sys.NumberIdCache;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(NumberIdSource.NAME)
public class NumberIdSourceClientImpl implements NumberIdSource {

    @Inject
    protected NumberIdService service;

    @Inject
    protected NumberIdCache cache;

    @Override
    public Long createLongId(String entityName) {
        return cache.createLongId(entityName, service);
    }

    @Override
    public Integer createIntegerId(String entityName) {
        long nextLong = createLongId(entityName);
        int nextInt = (int) nextLong;
        if (nextInt != nextLong)
            throw new IllegalStateException("Error creating a new Integer ID for entity " + entityName
                    + ": sequence overflow");
        return nextInt;
    }
}
