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

import javax.ejb.Local;
import java.util.Map;

@Local
public interface ReportService
{
    String JNDI_NAME = "cuba/core/ReportService";

    JasperPrint executeJasperReport(String name, Map<String, Object> params);
}
