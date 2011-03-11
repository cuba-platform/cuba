/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 11.03.11 18:46
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.oo;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;

public class OfficeComponent {
    private OOOConnection officeConnection;
    private XComponentLoader officeLoader;
    private XComponent officeComponent;

    public OfficeComponent(OOOConnection officeConnection, XComponentLoader officeLoader, XComponent officeComponent) {
        this.officeConnection = officeConnection;
        this.officeLoader = officeLoader;
        this.officeComponent = officeComponent;
    }

    public OOOConnection getOfficeConnection() {
        return officeConnection;
    }

    public XComponentLoader getOfficeLoader() {
        return officeLoader;
    }

    public XComponent getOfficeComponent() {
        return officeComponent;
    }
}