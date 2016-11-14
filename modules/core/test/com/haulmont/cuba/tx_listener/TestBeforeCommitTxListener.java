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

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.listener.BeforeCommitTransactionListener;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestSupport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.FlushModeType;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Component("cuba_TestBeforeCommitTxListener")
public class TestBeforeCommitTxListener implements BeforeCommitTransactionListener {

    public static String test;
    public static UUID createdEntityId;

    @Inject
    private Metadata metadata;

    @Override
    public void beforeCommit(EntityManager entityManager, Collection<Entity> managedEntities) {
        if (test != null) {
            System.out.println("beforeCommit: managedEntities=" + managedEntities);
            switch (test) {
                case "testChangeEntity": changeEntity(managedEntities); break;
                case "testCreateEntity": createEntity(managedEntities, entityManager); break;
                case "testQueryWithFlush": queryWithFlush(managedEntities, entityManager); break;
            }
        }
    }

    private void changeEntity(Collection<Entity> managedEntities) {
        for (Object entity : managedEntities) {
            if (entity instanceof User && ((User) entity).getLogin().startsWith("TxLstnrTst-")) {
                User user = (User) entity;
                if (PersistenceHelper.isNew(user)) {
                    assertEquals(user.getLogin().toLowerCase(), user.getLoginLowerCase()); // user listener has worked
                    user.setName("set by tx listener");
                }
            }
        }
    }

    private void createEntity(Collection<Entity> managedEntities, EntityManager entityManager) {
        Group companyGroup = entityManager.find(Group.class, TestSupport.COMPANY_GROUP_ID);
        User u = metadata.create(User.class);
        createdEntityId = u.getId();
        u.setLogin("TxLstnrTst-" + u.getId());
        u.setLoginLowerCase(u.getLogin().toLowerCase());
        u.setGroup(companyGroup);
        entityManager.persist(u);
    }

    private void queryWithFlush(Collection<Entity> managedEntities, EntityManager entityManager) {
        if (!managedEntities.stream().anyMatch(e -> e instanceof User && ((User) e).getLogin().startsWith("TxLstnrTst-")))
            return;

        TypedQuery<User> query = entityManager.createQuery("select u from sec$User u where u.login like ?1", User.class);
        query.setParameter(1, "TxLstnrTst-2-%");
        query.setFlushMode(FlushModeType.AUTO);
        User result = query.getFirstResult();
        assertNotNull(result);
    }
}
