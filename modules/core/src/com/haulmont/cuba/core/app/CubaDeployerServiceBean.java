/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.08.2009 12:50:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.ServiceInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

/**
 * Service facade for {@link com.haulmont.cuba.core.app.CubaDeployer} MBean
 */
@Stateless(name = CubaDeployerService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
@TransactionManagement(TransactionManagementType.BEAN)
public class CubaDeployerServiceBean implements CubaDeployerService {

    public String getReleaseNumber() {
        CubaDeployerMBean mBean = Locator.lookupMBean(CubaDeployerMBean.class, CubaDeployerMBean.OBJECT_NAME);
        return mBean.getReleaseNumber();
    }

    public String getReleaseTimestamp() {
        CubaDeployerMBean mBean = Locator.lookupMBean(CubaDeployerMBean.class, CubaDeployerMBean.OBJECT_NAME);
        return mBean.getReleaseTimestamp();
    }
}
