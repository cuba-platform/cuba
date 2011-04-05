/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface LocalServiceInvoker {

    LocalServiceInvocationResult invoke(LocalServiceInvocation invocation);
}
