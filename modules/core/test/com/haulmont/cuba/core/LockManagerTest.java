/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.LockManagerAPI;
import com.haulmont.cuba.core.entity.LockDescriptor;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LockManagerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private LockManagerAPI lockManager;

    @Before
    public void setUp() throws Exception {
        cont.persistence().runInTransaction(em -> {
            LockDescriptor lockDescriptor = cont.metadata().create(LockDescriptor.class);
            lockDescriptor.setName("sys$Server");
            lockDescriptor.setTimeoutSec(300);
            em.persist(lockDescriptor);
        });

        lockManager = AppBeans.get(LockManagerAPI.class);
        lockManager.reloadConfiguration();
    }

    @After
    public void tearDown() throws Exception {
        cont.persistence().runInTransaction(em -> {
            em.createQuery("delete from sys$LockDescriptor d").executeUpdate();
        });
    }

    @Test
    public void testLock() throws Exception {
        Server entity = new Server();

        LockInfo lockInfo = lockManager.lock(entity);
        assertNull(lockInfo);

        lockInfo = lockManager.lock(entity);
        assertNotNull(lockInfo);

        lockManager.unlock(entity);

        lockInfo = lockManager.getLockInfo("sys$Server", entity.getId().toString());
        assertNull(lockInfo);
    }
}

