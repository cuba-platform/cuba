/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.05.2009 11:04:57
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import net.sf.jasperreports.engine.JasperPrint;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.Map;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.ServiceInterceptor;

@Stateless(name = ReportService.JNDI_NAME)
@Interceptors(ServiceInterceptor.class)
public class ReportServiceBean implements ReportService
{
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public JasperPrint executeJasperReport(String name, Map<String, Object> params) {
        ReportEngineMBean mbean = Locator.lookupMBean(ReportEngineMBean.class, ReportEngineMBean.OBJECT_NAME);
        JasperPrint print = mbean.getAPI().executeJasperReport(name, params, null);
        return print;
    }
}
