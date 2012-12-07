/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.jmx;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.LockManagerAPI}.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface LockManagerMBean {

    int getLockCount();

    String showLocks();

    void reloadConfiguration();
}
