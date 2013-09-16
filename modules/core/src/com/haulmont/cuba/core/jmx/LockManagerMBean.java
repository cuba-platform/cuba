/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
