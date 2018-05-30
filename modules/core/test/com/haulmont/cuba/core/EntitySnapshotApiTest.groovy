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
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntitySnapshotApiTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private EntitySnapshotAPI snapshotApi
    private def role

    void setup() {
        cont.persistence().runInTransaction { em ->
            role = cont.metadata().create(Role)
            role.setName('testRole')
            em.persist(role)
        }

        snapshotApi = AppBeans.get(EntitySnapshotAPI.class)
    }

    void cleanup() {
        def runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update("delete from SYS_ENTITY_SNAPSHOT")

        if (role != null) {
            cont.deleteRecord("SEC_ROLE", role.getId())
        }
    }

    def "Get last added to the database snapshot for the entity"() {
        when:
            // create first snapshot
            View view = cont.metadata().getViewRepository().getView(Role.class, View.LOCAL)
            def firstSnapshot = snapshotApi.createSnapshot(role, view)
        then:
            firstSnapshot.getSnapshotXml().contains('testRole') == true
        when:
            // change entity and create last snapshot
            role.setName('lastRole')
            def secondSnapshot = snapshotApi.createSnapshot(role, view)
        then:
            // it should return last added snapshot
            def lastSnapshot = snapshotApi.getLastEntitySnapshot(role)
            secondSnapshot.getId() == lastSnapshot.getId()

            def lastSnapshot1 = snapshotApi.getLastEntitySnapshot(role.getMetaClass(), role.getId())
            secondSnapshot.getId() == lastSnapshot1.getId()
    }

    def "Create non-persist snapshot"() {
        when:
            View view = cont.metadata().getViewRepository().getView(Role.class, View.LOCAL)
            def snapshot = snapshotApi.createTempSnapshot(role, view)

            snapshot.getSnapshotXml().contains('testRole') == true
        then:
            def items = getSnapshotsList()

            items.size() == 0
    }

    private List<EntitySnapshot> getSnapshotsList() {
        def tx = cont.persistence().createTransaction()
        def items
        try {
            EntityManager em = cont.persistence().getEntityManager()
            TypedQuery<EntitySnapshot> query = em.createQuery(
                    'select e from sys$EntitySnapshot e', EntitySnapshot.class)
            items = query.getResultList()

            tx.commit()
        } finally {
            tx.end()
        }
        return items
    }
}