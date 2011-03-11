/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 12.10.2010 19:21:36
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.oo;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.XComponentContext;

import static com.haulmont.cuba.report.formatters.oo.ODTUnoConverter.*;

public class OOOConnection {
    private XComponentContext xComponentContext;

    public OOOConnection(XComponentContext xComponentContext) {
        this.xComponentContext = xComponentContext;
    }

    public XMultiComponentFactory getXMultiComponentFactory() {
        return xComponentContext.getServiceManager();
    }

    public XDesktop createDesktop() throws com.sun.star.uno.Exception {
        Object o = xComponentContext.getServiceManager().createInstanceWithContext(
                "com.sun.star.frame.Desktop", xComponentContext);
        return asXDesktop(o);
    }

    public XComponentLoader createXComponentLoader() throws com.sun.star.uno.Exception {
        return asXComponentLoader(createDesktop());
    }

    public XDispatchHelper createXDispatchHelper() throws Exception {
        Object o = xComponentContext.getServiceManager().createInstanceWithContext(
                "com.sun.star.frame.DispatchHelper", xComponentContext);
        return asXDispatchHelper(o);
    }

    public XComponentContext getxComponentContext() {
        return xComponentContext;
    }
}
