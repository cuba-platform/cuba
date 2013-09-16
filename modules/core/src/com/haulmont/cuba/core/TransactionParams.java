/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

/**
 * Encapsulates a new transaction parameters.
 *
 * @author krivopustov
 * @version $Id$
*/
public class TransactionParams {

    private int timeout;

    /**
     * @param timeout Database query timeout in seconds. 0 means undefined.
     * @return this instance for chaining
     */
    public TransactionParams setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * @return  Database query timeout in seconds. 0 means undefined.
     */
    public int getTimeout() {
        return timeout;
    }
}
