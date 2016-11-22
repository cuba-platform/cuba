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

public class AfterCompleteTransactionListenerTest {

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
    public void testCommit() throws Exception {
        User u = metadata.create(User.class);
        u.setLogin("TxLstnrTst-1-" + u.getId());
        u.setGroup(companyGroup);
        TestAfterCompleteTxListener.test = "testCommit";
        try {
            try (Transaction tx = persistence.createTransaction()) {
                persistence.getEntityManager().persist(u);
                tx.commit();
            }
        } finally {
            TestAfterCompleteTxListener.test = null;
        }

        try (Transaction tx = persistence.createTransaction()) {
            User user = persistence.getEntityManager().find(User.class, u.getId());
            assertEquals("updated by TestAfterCompleteTxListener", user.getName());
        }
    }

    @Test
    public void testRollback() throws Exception {
        User u = metadata.create(User.class);
        u.setLogin("TxLstnrTst-1-" + u.getId());
        u.setGroup(companyGroup);
        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(u);
            tx.commit();
        }

        TestAfterCompleteTxListener.test = "testRollback";
        try {
            try (Transaction tx = persistence.createTransaction()) {
                User user = persistence.getEntityManager().find(User.class, u.getId());
                user.setName("updated by testRollback");
            }
        } finally {
            TestAfterCompleteTxListener.test = null;
        }

        try (Transaction tx = persistence.createTransaction()) {
            User user = persistence.getEntityManager().find(User.class, u.getId());
            assertEquals("updated by TestAfterCompleteTxListener", user.getName());
        }
    }
}
