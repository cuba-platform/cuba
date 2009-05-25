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

public interface ReportEngineAPI
{
    JasperReport getJasperReport(String name);

    JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource);
}
