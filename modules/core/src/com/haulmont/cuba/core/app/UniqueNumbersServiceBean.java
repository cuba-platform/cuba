/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:16:22
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.sys.ServiceInterceptor;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;

import javax.ejb.*;
import javax.interceptor.Interceptors;

@Stateless(name = UniqueNumbersService.JNDI_NAME)
@Interceptors(ServiceInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class UniqueNumbersServiceBean implements UniqueNumbersService
{
    public long getNextNumber(String domain) {
        UniqueNumbersMBean mbean = Locator.lookupMBean(UniqueNumbersMBean.class, UniqueNumbersMBean.OBJECT_NAME);
        Transaction tx = Locator.createTransaction();
        try {
            long number = mbean.getAPI().getNextNumber(domain);
            tx.commit();
            return number;
        } finally {
            tx.end();
        }
    }
}
