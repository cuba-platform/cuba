/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

/**
 * Adapter containing default implementation for simple cluster listeners that don't need state transfer.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class ClusterListenerAdapter<T> implements ClusterListener<T> {

    @Override
    public byte[] getState() {
        return new byte[0];
    }

    @Override
    public void setState(byte[] state) {
    }
}
