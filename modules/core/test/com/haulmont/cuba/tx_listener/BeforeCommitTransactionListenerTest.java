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
 */

package com.haulmont.cuba.tx_listener;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BeforeCommitTransactionListenerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Persistence persistence;
    private Metadata metadata;
    private Group companyGroup;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();

        try (Transaction tx = persistence.createTransaction()) {
            companyGroup = persistence.getEntityManager().find(Group.class, TestSupport.COMPANY_GROUP_ID);
            tx.commit();
        }
    }

    @After
    public void tearDown() throws Exception {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        runner.update("delete from SEC_USER where LOGIN_LC like 'txlstnrtst-%'");
    }

    @Test
    public void testChangeEntity() throws Exception {
        User u1 = metadata.create(User.class);
        u1.setLogin("TxLstnrTst-1-" + u1.getId());
        u1.setGroup(companyGroup);

        TestBeforeCommitTxListener.test = "testChangeEntity";
        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(u1);
            tx.commit();
        } finally {
            TestBeforeCommitTxListener.test = null;
        }

        try (Transaction tx = persistence.createTransaction()) {
            User user = persistence.getEntityManager().find(User.class, u1.getId());
            assertEquals("set by tx listener", user.getName());
        }
    }

    @Test
    public void testCreateEntity() throws Exception {
        User u = metadata.create(User.class);
        u.setLogin("u-" + u.getId());
        u.setGroup(companyGroup);

        TestBeforeCommitTxListener.test = "testCreateEntity";
        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(u);
            tx.commit();
        } finally {
            TestBeforeCommitTxListener.test = null;
        }

        try (Transaction tx = persistence.createTransaction()) {
            User user = persistence.getEntityManager().find(User.class, TestBeforeCommitTxListener.createdEntityId);
            assertNotNull(user);
        }
    }

    @Test
    public void testQueryWithFlush() throws Exception {
        User u1 = metadata.create(User.class);
        u1.setLogin("TxLstnrTst-2-" + u1.getId());
        u1.setGroup(companyGroup);

        TestBeforeCommitTxListener.test = "testQueryWithFlush";
        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(u1);
            tx.commit();
        } finally {
            TestBeforeCommitTxListener.test = null;
        }

        try (Transaction tx = persistence.createTransaction()) {
            User user = persistence.getEntityManager().find(User.class, u1.getId());
            assertEquals(u1, user);
        }
    }
}
