/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.app.EntitySnapshotAPI
import com.haulmont.cuba.core.entity.EntitySnapshot
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntitySnapshotApiTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private EntitySnapshotAPI snapshotApi
    private Role role
    private User user

    void setup() {
        cont.persistence().runInTransaction { em ->
            role = cont.metadata().create(Role)
            role.setName('testRole')
            em.persist(role)

            def group = em.find(Group.class, TestSupport.COMPANY_GROUP_ID)
            user = new User()
            user.setGroup(group)
            user.setLogin("test")
            user.setName("test-name")
            em.persist(user)
        }

        snapshotApi = AppBeans.get(EntitySnapshotAPI.class)
    }

    void cleanup() {
        def runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update("delete from SYS_ENTITY_SNAPSHOT")

        if (role != null) {
            cont.deleteRecord("SEC_ROLE", role.getId())
        }

        if (user != null) {
            cont.deleteRecord("SEC_USER", user.getId())
        }
    }

    def "Get last EntitySnapshot for the Entity"() {
        Date date = new Date(100)

        when:
            // create first snapshot
            View viewRole = cont.metadata().getViewRepository().getView(Role.class, View.LOCAL)
            def snapshot = snapshotApi.createSnapshot(role, viewRole) // used current date

            // change entity and create last snapshot
            role.setName('lastRole')

            snapshotApi.createSnapshot(role, viewRole, date)
        then:
            // it should return snapshot by the last date
            def lastSnapshot = snapshotApi.getLastEntitySnapshot(role)
            snapshot.getId() == lastSnapshot.getId()

            def lastSnapshot1 = snapshotApi.getLastEntitySnapshot(role.getMetaClass(), role.getId())
            snapshot.getId() == lastSnapshot1.getId()

        // cases for non persistence entity
        when:
            def nonPersistRole = cont.metadata().create(Role)
            nonPersistRole.setName("nonPersistenceRole")

            snapshotApi.createSnapshot(nonPersistRole, viewRole) // used current  date

            nonPersistRole.setName("changedNonPersistenceRole")
            snapshotApi.createSnapshot(nonPersistRole, viewRole, date)
        then:
            def snapshot1 = snapshotApi.getLastEntitySnapshot(nonPersistRole)
            snapshot1.getSnapshotXml().contains("nonPersistenceRole") == true

            def snapshot2 = snapshotApi.getLastEntitySnapshot(nonPersistRole.getMetaClass(), nonPersistRole.getId())
            snapshot2.getSnapshotXml().contains("nonPersistenceRole") == true
    }

    def "Create non-persistent snapshot"() {
        View viewRole = cont.metadata().getViewRepository().getView(Role.class, View.LOCAL)
        Date snapshotDate = new Date(100)

        when:
            def snapshot = snapshotApi.createTempSnapshot(role, viewRole)

            snapshot.getSnapshotXml().contains('testRole') == true
        then:
            def items = getSnapshotsList()
            items.size() == 0

        when:
            EntitySnapshot snapshot2 = snapshotApi.createTempSnapshot(role, viewRole, snapshotDate)
            snapshot2.getSnapshotXml().contains('testRole') == true
        then:
            def snapshots2 = snapshotApi.getSnapshots(role.getMetaClass(), role.getId())

            snapshots2.size() == 0
            snapshot2.getSnapshotDate() == snapshotDate

        when:
            EntitySnapshot snapshot3 = snapshotApi.createTempSnapshot(role, viewRole, snapshotDate, user)
            snapshot3.getSnapshotXml().contains('testRole') == true
        then:
            def snapshots3 = snapshotApi.getSnapshots(role.getMetaClass(), role.getId())

            snapshots3.size() == 0
            snapshot3.getAuthor() == user
    }

    private List<EntitySnapshot> getSnapshotsList() {
        def tx = cont.persistence().createTransaction()
        try {
            EntityManager em = cont.persistence().getEntityManager()
            TypedQuery<EntitySnapshot> query = em.createQuery(
                    'select e from sys$EntitySnapshot e', EntitySnapshot.class)
            return query.getResultList()
        } finally {
            tx.close()
        }
    }
}