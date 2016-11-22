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

package com.haulmont.cuba.soft_delete;

import com.haulmont.bali.db.ArrayHandler;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SoftDeleteDataManagerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Persistence persistence;
    private Metadata metadata;
    private User user;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();
        try (Transaction tx = persistence.createTransaction()) {
            user = metadata.create(User.class);
            user.setGroup(persistence.getEntityManager().find(Group.class, TestSupport.COMPANY_GROUP_ID));
            user.setLogin("user-" + user.getId());
            persistence.getEntityManager().persist(user);
            tx.commit();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(user);
    }

    @Test
    public void testHardDelete() throws Exception {
        DataManager dataManager = AppBeans.get(DataManager.class);
        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()));
        CommitContext commitContext = new CommitContext().addInstanceToRemove(loadedUser);
        commitContext.setSoftDeletion(false);
        dataManager.commit(commitContext);

        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        Object[] row = runner.query("select count(*) from sec_user where id = ?", user.getId().toString(), new ArrayHandler());
        assertEquals(0, ((Number) row[0]).intValue());
    }
}
