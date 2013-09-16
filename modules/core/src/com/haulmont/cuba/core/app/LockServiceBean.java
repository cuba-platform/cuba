/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.LockInfo;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(LockService.NAME)
public class LockServiceBean implements LockService {

    @Inject
    private LockManagerAPI lockManager;

    public LockInfo lock(String name, String id) {
        return lockManager.lock(name, id);
    }

    public void unlock(String name, String id) {
        lockManager.unlock(name, id);
    }

    public LockInfo getLockInfo(String name, String id) {
        return lockManager.getLockInfo(name, id);
    }

    public List<LockInfo> getCurrentLocks() {
        return lockManager.getCurrentLocks();
    }

    public void reloadConfiguration(){
        lockManager.reloadConfiguration();
    }
}
