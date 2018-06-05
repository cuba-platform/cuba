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
import com.haulmont.cuba.testmodel.primary_keys.CompositeKeyUuidEntity
import com.haulmont.cuba.testmodel.primary_keys.EntityKey
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
    private CompositeKeyUuidEntity entity
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

            entity = createNonPersistenceCompKeyEntity(1)
            em.persist(entity)
        }

        snapshotApi = AppBeans.get(EntitySnapshotAPI.class)
    }

    void cleanup() {
        def runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update("delete from SYS_ENTITY_SNAPSHOT")
        runner.update("delete from TEST_COMPOSITE_KEY_UUID")

        if (role != null) {
            cont.deleteRecord("SEC_ROLE", role.getId())
        }

        if (user != null) {
            cont.deleteRecord("SEC_USER", user.getId())
        }
    }

    def "Get last added EntitySnapshot for the Entity"() {
        when:
            // create first snapshot
            View viewRole = cont.metadata().getViewRepository().getView(Role.class, View.LOCAL)
            snapshotApi.createSnapshot(role, viewRole)

            // change entity and create last snapshot
            role.setName('lastRole')

            def secondSnapshot = snapshotApi.createSnapshot(role, viewRole)
        then:
            // it should return last added snapshot
            def lastSnapshot = snapshotApi.getLastEntitySnapshot(role)
            secondSnapshot.getId() == lastSnapshot.getId()

            def lastSnapshot1 = snapshotApi.getLastEntitySnapshot(role.getMetaClass(), role.getId())
            secondSnapshot.getId() == lastSnapshot1.getId()

        // cases for non persistence entity
        when:
            def nonPersistRole = cont.metadata().create(Role)
            nonPersistRole.setName("nonPersistenceRole")

            snapshotApi.createSnapshot(nonPersistRole, viewRole)

            nonPersistRole.setName("changedNonPersistenceRole")
            snapshotApi.createSnapshot(nonPersistRole, viewRole)
        then:
            def snapshot1 = snapshotApi.getLastEntitySnapshot(nonPersistRole)
            snapshot1.getSnapshotXml().contains("changedNonPersistenceRole") == true

            def snapshot2 = snapshotApi.getLastEntitySnapshot(nonPersistRole.getMetaClass(), nonPersistRole.getId())
            snapshot2.getSnapshotXml().contains("changedNonPersistenceRole") == true
    }

    def "Create non-persistent snapshot"() {
        when:
            View viewRole = cont.metadata().getViewRepository().getView(Role.class, View.LOCAL)
            def snapshot = snapshotApi.createTempSnapshot(role, viewRole)

            snapshot.getSnapshotXml().contains('testRole') == true
        then:
            def items = getSnapshotsList()
            items.size() == 0

        // cases for entity with composite key
        View view = cont.metadata().getViewRepository().getView(CompositeKeyUuidEntity.class, View.LOCAL)
        Date snapshotDate = new Date(100)

        when:
            EntitySnapshot snapshot1 = snapshotApi.createTempSnapshot(entity, view)
            snapshot1.getSnapshotXml().contains('compositeKeyUuidEntity') == true
        then:
            def snapshots = snapshotApi.getSnapshots(entity.getMetaClass(), entity.getId())
            snapshots.size() == 0

        when:
            EntitySnapshot snapshot2 = snapshotApi.createTempSnapshot(entity, view, snapshotDate)
            snapshot2.getSnapshotXml().contains('compositeKeyUuidEntity') == true
        then:
            def snapshots2 = snapshotApi.getSnapshots(entity.getMetaClass(), entity.getId())
            snapshots2.size() == 0
        and:
            snapshot2.getSnapshotDate() == snapshotDate


        when:
            EntitySnapshot snapshot3 = snapshotApi.createTempSnapshot(entity, view, snapshotDate, user)
            snapshot3.getSnapshotXml().contains('compositeKeyUuidEntity') == true
        then:
            def snapshots3 = snapshotApi.getSnapshots(entity.getMetaClass(), entity.getId())
            snapshots3.size() == 0
        and:
            snapshot3.getAuthor() == user
    }

    def "Get last added EntitySnapshot for the Entity with composite key"() {
        when:
            // create two snapshot
            View view = cont.metadata().getViewRepository().getView(CompositeKeyUuidEntity.class, View.LOCAL)
            snapshotApi.createSnapshot(entity, view)

            entity.setName("changedName")

            snapshotApi.createSnapshot(entity, view)
        then:
            // get last snapshot using composite key as id
            EntitySnapshot snapshot1 = snapshotApi.getLastEntitySnapshot(entity.getMetaClass(), entity.getId())
            snapshot1.getSnapshotXml().contains("changedName") == true

            // get last snapshot using reference id as id
            EntitySnapshot snapshot2 = snapshotApi.getLastEntitySnapshot(entity.getMetaClass(), entity.getUuid())
            snapshot2.getSnapshotXml().contains("changedName") == true

        when:
            // create two snapshot for non-persistence entity
            def entity = createNonPersistenceCompKeyEntity(2)
            entity.setName("nonPersistence")

            snapshotApi.createSnapshot(entity, view)

            entity.setName("changedNonPersistence")

            snapshotApi.createSnapshot(entity, view)
        then:
            // get snapshot for non persistence entity using compositeKey
            EntitySnapshot snapshot3 = snapshotApi.getLastEntitySnapshot(entity.getMetaClass(), entity.getId())
            snapshot3 == null // because it needs reference id that stored in EntitySnapshot for this entity

            // get snapshot non persistence entity using reference id
            EntitySnapshot snapshot4 = snapshotApi.getLastEntitySnapshot(entity.getMetaClass(), entity.getUuid())
            snapshot4.getSnapshotXml().contains("changedNonPersistence") == true
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

    private CompositeKeyUuidEntity createNonPersistenceCompKeyEntity(int id) {
        def compKey = cont.metadata().create(EntityKey.class)
        compKey.setTenant(1)
        compKey.setEntityId(id)

        def entity = cont.metadata().create(CompositeKeyUuidEntity.class)
        entity.setId(compKey)
        entity.setUuid(UUID.randomUUID())
        entity.setName('compositeKeyUuidEntity')

        return entity
    }
}