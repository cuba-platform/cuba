package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.XComponentContext;
import ooo.connector.BootstrapSocketConnector;

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: FONTANENKO VASILIY
 * Created: 12.10.2010 19:21:08
 *
 * $Id$
 */

public class OOOConnector {
    public static OOOConnection createConnection(String openOfficePath) throws BootstrapException {
        XComponentContext xComponentContext = new BootstrapSocketConnector(openOfficePath).connect();
        OOOConnection oooConnection = new OOOConnection(xComponentContext);
        return oooConnection;
    }
}
