/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:16:22
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;
import org.springframework.stereotype.Service;

/**
 * Service facade for {@link com.haulmont.cuba.core.app.UniqueNumbers} MBean
 */
@Service(UniqueNumbersService.NAME)
public class UniqueNumbersServiceBean implements UniqueNumbersService
{
    public long getNextNumber(String domain) {
        UniqueNumbersAPI mbean = Locator.lookup(UniqueNumbersAPI.NAME);
        Transaction tx = Locator.createTransaction();
        try {
            long number = mbean.getNextNumber(domain);
            tx.commit();
            return number;
        } finally {
            tx.end();
        }
    }
}
