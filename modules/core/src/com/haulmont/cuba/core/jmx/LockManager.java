/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.LockManagerAPI;
import com.haulmont.cuba.core.global.LockInfo;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_LockManagerMBean")
public class LockManager implements LockManagerMBean {

    @Inject
    protected LockManagerAPI lockManager;

    @Override
    public int getLockCount() {
        return lockManager.getCurrentLocks().size();
    }


    @Override
    public String showLocks() {
        StringBuilder sb = new StringBuilder();
        for (LockInfo lockInfo : lockManager.getCurrentLocks()) {
            sb.append(lockInfo).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void reloadConfiguration() {
        lockManager.reloadConfiguration();
    }
}
