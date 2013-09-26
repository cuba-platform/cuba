/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(UniqueNumbersService.NAME)
public class UniqueNumbersServiceBean implements UniqueNumbersService {

    @Inject
    protected UniqueNumbersAPI uniqueNumbers;

    @Override
    @Transactional
    public long getNextNumber(String domain) {
        return uniqueNumbers.getNextNumber(domain);
    }
}
