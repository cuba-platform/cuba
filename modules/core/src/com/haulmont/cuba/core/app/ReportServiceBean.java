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

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service facade for {@link com.haulmont.cuba.core.app.ReportEngine} MBean
 */
@Service(ReportService.NAME)
public class ReportServiceBean implements ReportService
{
    public JasperPrint executeJasperReport(String name, Map<String, Object> params) {
        ReportEngineAPI mbean = Locator.lookup(ReportEngineAPI.NAME);
        JasperPrint print;
        Transaction tx = Locator.createTransaction();
        try {
            print = mbean.executeJasperReport(name, params, null);
            tx.commit();
        } finally {
            tx.end();
        }
        return print;
    }

    public JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource) {
        ReportEngineAPI mbean = Locator.lookup(ReportEngineAPI.NAME);
        JasperPrint print;
        Transaction tx = Locator.createTransaction();
        try {
            print = mbean.executeJasperReport(name, params, dataSource);
            tx.commit();
        } finally {
            tx.end();
        }
        return print;
    }

    public String processFreemarkerTemplate(String name, Map<String, Object> params) {
        ReportEngineAPI mbean = Locator.lookup(ReportEngineAPI.NAME);
        return mbean.processFreemarkerTemplate(name, params);
    }
}
