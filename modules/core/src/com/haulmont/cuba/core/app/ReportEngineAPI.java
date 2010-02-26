/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 18:29:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRDataSource;

import java.util.Map;

/**
 * API of {@link ReportEngine} MBean.<br>
 */
public interface ReportEngineAPI
{
    String NAME = "cuba_ReportEngine";
    
    JasperReport getJasperReport(String name);

    JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource);

    /**
     * Process Freemarker template. Works properly with templates in RTF format.
     * @param name file name relative to conf directory. RTF templates must have .rtf extension
     * @param params binding
     * @return resulting content
     */
    String processFreemarkerTemplate(String name, Map<String, Object> params);
}
