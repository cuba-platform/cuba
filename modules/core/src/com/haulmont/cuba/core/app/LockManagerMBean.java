/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2010 10:33:28
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface LockManagerMBean {

    int getLockCount();

    String showLocks();

    void reloadConfiguration();
}
