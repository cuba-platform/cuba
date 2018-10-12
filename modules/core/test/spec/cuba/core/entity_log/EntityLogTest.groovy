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
import com.haulmont.cuba.core.*
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.app.EntityAttributeChanges
import com.haulmont.cuba.security.app.EntityLogAPI
import com.haulmont.cuba.security.entity.*
import com.haulmont.cuba.testmodel.primary_keys.IdentityEntity
import com.haulmont.cuba.testmodel.primary_keys.IntIdentityEntity
import com.haulmont.cuba.testmodel.primary_keys.StringKeyEntity
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.sql.SQLException

class EntityLogTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private UUID user1Id, user2Id
    private UUID roleId

    private EntityLogAPI entityLog
    private PersistenceTools persistenceTools

    void setup() {
        _cleanup()

        cont.persistence().runInTransaction { em ->
            Query q
            q = em.createNativeQuery("delete from SEC_ENTITY_LOG")
            q.executeUpdate()

            initEntityLogConfiguration(em)

        }
        entityLog = AppBeans.get(EntityLogAPI.class)
        entityLog.invalidateCache()
        persistenceTools = AppBeans.get(PersistenceTools.class)
    }

    protected void initEntityLogConfiguration(EntityManager em) {

        saveEntityLogAutoConfFor(em, 'sec$User', 'name', 'email')

        saveEntityLogAutoConfFor(em, 'sec$Role', 'type')

        saveEntityLogAutoConfFor(em, 'test$IntIdentityEntity', 'name')

        saveEntityLogAutoConfFor(em, 'test$IdentityEntity', 'name')

        saveManualEntityLogAutoConfFor(em, 'test$StringKeyEntity', 'name', 'description')
    }

    protected void saveEntityLogAutoConfFor(EntityManager em, String entityName, String... attributes) {

        LoggedEntity le = new LoggedEntity(name: entityName, auto: true)
        em.persist(le)

        attributes.each {
            LoggedAttribute la = new LoggedAttribute(entity: le, name: it)
            em.persist(la)
        }

    }
    protected void saveManualEntityLogAutoConfFor(EntityManager em, String entityName, String... attributes) {

        LoggedEntity le = new LoggedEntity(name: entityName, auto: false, manual: true)
        em.persist(le)

        attributes.each {
            LoggedAttribute la = new LoggedAttribute(entity: le, name: it)
            em.persist(la)
        }

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

    private List<EntityLogItem> getEntityLogItems(String entityName, def entityId) {
        Transaction tx
        List<EntityLogItem> items
        tx = cont.persistence().createTransaction()
        try {
            EntityManager em = cont.persistence().getEntityManager()
            String entityIdField
            if (entityId instanceof Integer) entityIdField = 'intEntityId'
            else if (entityId instanceof Long) entityIdField = 'longEntityId'
            else if (entityId instanceof String) entityIdField = 'stringEntityId'
            else entityIdField = 'entityId'

            TypedQuery<EntityLogItem> query = em.createQuery(
                    "select i from sec\$EntityLog i where i.entity = ?1 and i.entityRef.$entityIdField = ?2 order by i.eventTs desc", EntityLogItem.class)
            query.setParameter(1, entityName)
            query.setParameter(2, entityId)
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

        getEntityLogItems('sec$User', user1Id).size() == 1
        getEntityLogItems('sec$User', user2Id).size() == 1

        when:

        cont.persistence().runInTransaction({ em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email1')

            em.reload(group, View.BASE)

            def user2 = em.find(User, user2Id)
            user2.setEmail('email1')
        })

        then:

        getEntityLogItems('sec$User', user1Id).size() == 2
        getEntityLogItems('sec$User', user2Id).size() == 2
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

            persistenceTools.getDirtyFields(user1)

            em.reload(group, View.BASE)

            user1 = em.find(User, user1Id)
            user1.setEmail('email111')
        })

        then:

        getEntityLogItems('sec$User', user1Id).size() == 2
        def item = getEntityLogItems('sec$User', user1Id)[0] // latest

        item.attributes.find({ it.name == 'email' }).value == 'email111'
        item.attributes.find({ it.name == 'email' }).oldValue == 'email1'

        item.attributes.find({ it.name == 'name' }).value == 'name11'
        item.attributes.find({ it.name == 'name' }).oldValue == 'name1'
    }

    def "works for BaseIdentityIdEntity"() {

        when:

        IdentityEntity entity = null
        Transaction tx = cont.persistence().createTransaction()
        try {
            entity = new IdentityEntity(name: 'test1')
            cont.persistence().entityManager.persist(entity)
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        noExceptionThrown()

        def item1 = getEntityLogItems('test$IdentityEntity', entity.id.get())[0]
        item1.attributes.find({ it.name == 'name' }).value == 'test1'
        item1.attributes.find({ it.name == 'name' }).oldValue == null

        when:

        tx = cont.persistence().createTransaction()
        try {
            IdentityEntity e = cont.persistence().entityManager.find(IdentityEntity, entity.id)
            e.name = 'test2'
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        def item2 = getEntityLogItems('test$IdentityEntity', entity.id.get())[0]
        item2.attributes.find({ it.name == 'name' }).value == 'test2'
        item2.attributes.find({ it.name == 'name' }).oldValue == 'test1'

        cleanup:

        if (entity != null && entity.getId().get() != null) {
            new QueryRunner(cont.persistence().dataSource).update("delete from TEST_IDENTITY where id = ${entity.getId().get()}")
        }
    }

    def "works for BaseIntIdentityIdEntity"() {

        when:

        IntIdentityEntity entity = null
        Transaction tx = cont.persistence().createTransaction()
        try {
            entity = new IntIdentityEntity(name: 'test1')
            cont.persistence().entityManager.persist(entity)
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        noExceptionThrown()

        def item1 = getEntityLogItems('test$IntIdentityEntity', entity.id.get())[0]
        item1.attributes.find({ it.name == 'name' }).value == 'test1'
        item1.attributes.find({ it.name == 'name' }).oldValue == null

        when:

        cont.persistence().runInTransaction { em ->
            def e = em.find(IntIdentityEntity, entity.id)
            e.name = 'test2'
        }

        tx = cont.persistence().createTransaction()
        try {
            IntIdentityEntity e = cont.persistence().entityManager.find(IntIdentityEntity, entity.id)
            e.name = 'test2'
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        def item2 = getEntityLogItems('test$IntIdentityEntity', entity.id.get())[0]
        item2.attributes.find({ it.name == 'name' }).value == 'test2'
        item2.attributes.find({ it.name == 'name' }).oldValue == 'test1'

        cleanup:

        if (entity != null && entity.getId().get() != null) {
            new QueryRunner(cont.persistence().dataSource).update("delete from TEST_INT_IDENTITY where id = ${entity.getId().get()}")
        }
    }

    def "works with MetaProperty"() {

        when:

        Transaction tx = cont.persistence().createTransaction()
        StringKeyEntity entity = null
        try {
            entity = new StringKeyEntity(code: 'code1', name: 'test1')
            cont.persistence().entityManager.persist(entity)
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        noExceptionThrown()

        when:

        tx = cont.persistence().createTransaction()
        try {
            StringKeyEntity e = cont.persistence().entityManager.find(StringKeyEntity, entity.id)
            e.name = 'test2'
            e.description = 'description2'

            EntityAttributeChanges changes = new EntityAttributeChanges()
            changes.addChanges(e)
            changes.addChange('description', 'description1')
            entityLog.registerModify(e, false, changes)
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        def item2 = getEntityLogItems('test$StringKeyEntity', entity.id)[0]
        item2.attributes.find({ it.name == 'name' }).value == 'test2'
        item2.attributes.find({ it.name == 'name' }).oldValue == 'test1'
        item2.attributes.find({ it.name == 'description' }).value == 'description2'
        item2.attributes.find({ it.name == 'description' }).oldValue == 'description1'

        cleanup:

        if (entity != null && entity.getId() != null) {
            new QueryRunner(cont.persistence().dataSource).update("delete from TEST_STRING_KEY where code = '${entity.getId()}'")
        }
    }
}
