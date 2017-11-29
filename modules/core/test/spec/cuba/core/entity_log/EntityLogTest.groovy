/*
 * Copyright (c) 2008-2017 Haulmont.
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

package spec.cuba.core.entity_log

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.Query
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TypedQuery
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.app.EntityLogAPI
import com.haulmont.cuba.security.entity.EntityLogItem
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.LoggedAttribute
import com.haulmont.cuba.security.entity.LoggedEntity
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.sql.SQLException

class EntityLogTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private UUID user1Id, user2Id
    private UUID roleId

    private def entityLog

    void setup() {
        _cleanup()

        cont.persistence().runInTransaction { em ->
            Query q
            q = em.createNativeQuery("delete from SEC_ENTITY_LOG")
            q.executeUpdate()

            LoggedEntity le = new LoggedEntity()
            le.setName('sec$User')
            le.setAuto(true)
            em.persist(le)

            LoggedAttribute la = new LoggedAttribute()
            la.setEntity(le)
            la.setName('name')
            em.persist(la)

            la = new LoggedAttribute()
            la.setEntity(le)
            la.setName('email')
            em.persist(la)

            le = new LoggedEntity()
            le.setName('sec$Role')
            le.setAuto(true)
            em.persist(le)

            la = new LoggedAttribute()
            la.setEntity(le)
            la.setName('type')
            em.persist(la)
        }
        entityLog = AppBeans.get(EntityLogAPI.class)
        entityLog.invalidateCache()
    }

    void cleanup() {
        _cleanup()

        if (user1Id != null)
            cont.deleteRecord("SEC_USER", user1Id)
        if (user2Id != null)
            cont.deleteRecord("SEC_USER", user2Id)

        if (roleId != null)
            cont.deleteRecord("SEC_ROLE", roleId)
    }

    private void _cleanup() throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update("delete from SEC_LOGGED_ATTR")
        runner.update("delete from SEC_LOGGED_ENTITY")
    }

    private List<EntityLogItem> getEntityLogItems(def userId) {
        Transaction tx
        List<EntityLogItem> items
        tx = cont.persistence().createTransaction()
        try {
            EntityManager em = cont.persistence().getEntityManager()
            TypedQuery<EntityLogItem> query = em.createQuery(
                    'select i from sec$EntityLog i where i.entity = ?1 and i.entityRef.entityId = ?2 order by i.eventTs desc', EntityLogItem.class)
            query.setParameter(1, 'sec$User')
            query.setParameter(2, userId)
            items = query.getResultList()

            tx.commit()
        } finally {
            tx.end()
        }
        return items
    }

    def "PL-10005 missed Entity Log items because of implicit flush"() {

        Group group = cont.persistence().callInTransaction({ em ->
            em.find(Group.class, TestSupport.COMPANY_GROUP_ID)
        })

        when:

        cont.persistence().runInTransaction({ em ->
            User user1 = cont.metadata().create(User)
            user1Id = user1.getId()
            user1.setGroup(group)
            user1.setLogin("test")
            user1.setName("test-name")
            em.persist(user1)

            User user2 = cont.metadata().create(User)
            user2Id = user2.getId()
            user2.setGroup(group)
            user2.setLogin("test2")
            user2.setName("test2-name")
            em.persist(user2)
        })

        then:

        getEntityLogItems(user1Id).size() == 1
        getEntityLogItems(user2Id).size() == 1

        when:

        cont.persistence().runInTransaction({ em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email1')

            em.reload(group, View.BASE)

            def user2 = em.find(User, user2Id)
            user2.setEmail('email1')
        })

        then:

        getEntityLogItems(user1Id).size() == 2
        getEntityLogItems(user2Id).size() == 2
    }

    def "correct old value in case of flush in the middle"() {
        Group group = cont.persistence().callInTransaction({ em ->
            em.find(Group.class, TestSupport.COMPANY_GROUP_ID)
        })

        cont.persistence().runInTransaction({ em ->
            User user1 = cont.metadata().create(User)
            user1Id = user1.getId()
            user1.setGroup(group)
            user1.setLogin("test")
            user1.setName("name1")
            user1.setEmail('email1')
            em.persist(user1)
        })

        when:

        cont.persistence().runInTransaction({ em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email11')
            user1.setName('name11')

            em.reload(group, View.BASE)

            user1 = em.find(User, user1Id)
            user1.setEmail('email111')
        })

        then:

        getEntityLogItems(user1Id).size() == 2
        def item = getEntityLogItems(user1Id)[0] // latest

        item.attributes.find({ it.name == 'email' }).value == 'email111'
        item.attributes.find({ it.name == 'email' }).oldValue == 'email1'

        item.attributes.find({ it.name == 'name' }).value == 'name11'
        item.attributes.find({ it.name == 'name' }).oldValue == 'name1'
    }
}
