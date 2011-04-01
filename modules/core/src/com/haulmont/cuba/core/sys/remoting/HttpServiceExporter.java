/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class HttpServiceExporter extends HttpInvokerServiceExporter {

    public HttpServiceExporter() {
        super();
        setRemoteInvocationExecutor(new CubaRemoteInvocationExecutor());
    }
}
