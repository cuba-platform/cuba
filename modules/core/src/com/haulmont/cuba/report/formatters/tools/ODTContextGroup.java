/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 07.10.2010 14:18:12
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;
import ooo.connector.BootstrapSocketConnector;

import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.asXComponentLoader;
import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.asXDesktop;

// Objects for document manipulating
public class ODTContextGroup {
    private XComponentContext xRemoteContext = null;
    private XMultiComponentFactory xRemoteServiceManager = null;
    private XDesktop xDesktop = null;

    public ODTContextGroup(XComponentContext xContext, XMultiComponentFactory xMCF, XDesktop desktop){
        xDesktop = desktop;
        xRemoteContext = xContext;
        xRemoteServiceManager = xMCF;
    }

    public XDesktop getDesktop(){
        return xDesktop;
    }

    public XMultiComponentFactory getMCF(){
        return xRemoteServiceManager;
    }

    public XComponentContext getContext(){
        return xRemoteContext;
    }

    public XComponentLoader getLoader(){
        return asXComponentLoader(xDesktop);
    }
}
