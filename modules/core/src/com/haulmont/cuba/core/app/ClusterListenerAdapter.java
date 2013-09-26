/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

/**
 * Adapter containing default implementation for simple cluster listeners that don't need state transfer.
 *
 * @author krivopustov
 * @version $Id$
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
