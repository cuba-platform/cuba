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

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Map;

/**
 * Local interface for {@link com.haulmont.cuba.core.app.ReportServiceBean}
 */
@Local
public interface ReportService
{
    String JNDI_NAME = "cuba/core/ReportService";

    JasperPrint executeJasperReport(String name, Map<String, Object> params);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource);
}
