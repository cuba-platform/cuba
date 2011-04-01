/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class HttpServiceProxy extends HttpInvokerProxyFactoryBean {

    public HttpServiceProxy() {
        super();
        setRemoteInvocationFactory(new CubaRemoteInvocationFactory());
    }
}
