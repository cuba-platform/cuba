/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.05.2009 11:04:05
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRDataSource;

import java.util.Map;

/**
 * Service interface for ReportEngine
 */
public interface ReportService
{
    String NAME = "cuba_ReportService";

    @Deprecated
    String JNDI_NAME = NAME;

    JasperPrint executeJasperReport(String name, Map<String, Object> params);

    JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource);

    /**
     * Process Freemarker template. Works properly with templates in RTF format.
     * @param name file name relative to conf directory. RTF templates must have .rtf extension
     * @param params binding
     * @return resulting content
     */
    String processFreemarkerTemplate(String name, Map<String, Object> params);

    String processFreemarkerTemplate(String name, Map<String, Object> params, String outputCharset);
}
