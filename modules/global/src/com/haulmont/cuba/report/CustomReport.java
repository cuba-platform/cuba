/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 02.06.2010 16:45:40
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import java.util.Map;

public interface CustomReport {
    byte[] createReport(Report report, Map<String, Object> params);
}
