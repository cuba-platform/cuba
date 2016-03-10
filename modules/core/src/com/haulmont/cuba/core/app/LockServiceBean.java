/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LockInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(LockService.NAME)
public class LockServiceBean implements LockService {

    @Inject
    private LockManagerAPI lockManager;

    @Override
    public LockInfo lock(String name, String id) {
        return lockManager.lock(name, id);
    }

    @Nullable
    @Override
    public LockInfo lock(Entity entity) {
        return lockManager.lock(entity);
    }

    @Override
    public void unlock(String name, String id) {
        lockManager.unlock(name, id);
    }

    @Override
    public void unlock(Entity entity) {
        lockManager.unlock(entity);
    }

    @Override
    public LockInfo getLockInfo(String name, String id) {
        return lockManager.getLockInfo(name, id);
    }

    @Override
    public List<LockInfo> getCurrentLocks() {
        return lockManager.getCurrentLocks();
    }

    @Override
    public void reloadConfiguration(){
        lockManager.reloadConfiguration();
    }
}