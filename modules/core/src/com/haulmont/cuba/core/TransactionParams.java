/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
