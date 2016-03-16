/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

