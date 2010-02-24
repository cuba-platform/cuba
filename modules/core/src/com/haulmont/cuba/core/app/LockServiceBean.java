/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2010 17:22:52
 *
 * $Id$
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
}
