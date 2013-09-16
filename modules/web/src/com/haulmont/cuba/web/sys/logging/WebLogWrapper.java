/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.logging.AbstractLogWrapper;
import org.apache.commons.logging.Log;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebLogWrapper extends AbstractLogWrapper {

    public WebLogWrapper(Log delegate) {
        super(delegate);
    }

    @Override
    protected String getUserInfo() {
        String logUserName = AppContext.getProperty("cuba.logUserName");
        if (logUserName == null || logUserName.equals("") || Boolean.valueOf(logUserName)) {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext != null) {
                return securityContext.getUser();
            }
        }
        return null;
    }
}