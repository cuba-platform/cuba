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
import net.sf.jasperreports.engine.JRDataSource;

import javax.ejb.*;
import javax.interceptor.Interceptors;
import java.util.Map;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.sys.ServiceInterceptor;

@Stateless(name = ReportService.JNDI_NAME)
@Interceptors(ServiceInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class ReportServiceBean implements ReportService
{
    public JasperPrint executeJasperReport(String name, Map<String, Object> params) {
        ReportEngineMBean mbean = Locator.lookupMBean(ReportEngineMBean.class, ReportEngineMBean.OBJECT_NAME);
        JasperPrint print;
        Transaction tx = Locator.createTransaction();
        try {
            print = mbean.getAPI().executeJasperReport(name, params, null);
            tx.commit();
        } finally {
            tx.end();
        }
        return print;
    }

    public JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource) {
        ReportEngineMBean mbean = Locator.lookupMBean(ReportEngineMBean.class, ReportEngineMBean.OBJECT_NAME);
        JasperPrint print;
        Transaction tx = Locator.createTransaction();
        try {
            print = mbean.getAPI().executeJasperReport(name, params, dataSource);
            tx.commit();
        } finally {
            tx.end();
        }
        return print;
    }
}
