/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.logging.AbstractLogWrapper;
import org.apache.commons.logging.Log;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
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
