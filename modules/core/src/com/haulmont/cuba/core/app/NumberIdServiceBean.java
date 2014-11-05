/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(NumberIdService.NAME)
public class NumberIdServiceBean implements NumberIdService {

    @Inject
    protected NumberIdWorker worker;

    @Override
    public Long createLongId(String entityName) {
        return worker.createLongId(entityName);
    }
}
