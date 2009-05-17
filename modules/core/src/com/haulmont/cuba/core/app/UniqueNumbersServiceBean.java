/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:16:22
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.sys.ServiceInterceptor;
import com.haulmont.cuba.core.Locator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

@Stateless(name = UniqueNumbersService.JNDI_NAME)
@Interceptors(ServiceInterceptor.class)
public class UniqueNumbersServiceBean implements UniqueNumbersService
{
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long getNextNumber(String domain) {
        UniqueNumbersMBean mbean = Locator.lookupMBean(UniqueNumbersMBean.class, UniqueNumbersMBean.OBJECT_NAME);
        long number = mbean.getAPI().getNextNumber(domain);
        return number;
    }
}
