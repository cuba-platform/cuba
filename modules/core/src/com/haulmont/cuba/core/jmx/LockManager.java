/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.LockManagerAPI;
import com.haulmont.cuba.core.global.LockInfo;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_LockManagerMBean")
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
