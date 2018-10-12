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
import com.haulmont.cuba.security.entity.*
import com.haulmont.cuba.testmodel.primary_keys.IdentityEntity
import com.haulmont.cuba.testmodel.primary_keys.IntIdentityEntity
import com.haulmont.cuba.testmodel.primary_keys.StringKeyEntity

import java.sql.SQLException

class EntityLogTest extends AbstractEntityLogTest {

    private UUID user1Id, user2Id
    private UUID roleId


    void setup() {
        _cleanup()

        withTransaction { EntityManager em ->
            clearTable(em, "SEC_ENTITY_LOG")
            initEntityLogConfiguration(em)
        }

        initBeans()
    }

    protected void initBeans() {
        initEntityLogAPI()
        persistenceTools = AppBeans.get(PersistenceTools.class)
    }


    protected void initEntityLogConfiguration(EntityManager em) {

        saveEntityLogAutoConfFor(em, 'sec$User', 'name', 'email')

        saveEntityLogAutoConfFor(em, 'sec$Role', 'type')

        saveEntityLogAutoConfFor(em, 'test$IntIdentityEntity', 'name')

        saveEntityLogAutoConfFor(em, 'test$IdentityEntity', 'name')

        saveManualEntityLogAutoConfFor(em, 'test$StringKeyEntity', 'name', 'description')
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

    def "PL-10005 missed Entity Log items because of implicit flush"() {

        when:

        withTransaction { EntityManager em ->
            user1Id = createAndSaveUser(em, [login: "test", name : 'test-name'])
            user2Id = createAndSaveUser(em, [login: "test2", name: 'test2-name'])
        }

        then:

        getEntityLogItems('sec$User', user1Id).size() == 1
        getEntityLogItems('sec$User', user2Id).size() == 1

        when:

        withTransaction{ EntityManager em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email1')

            em.reload(findCompanyGroup(), View.BASE)

            def user2 = em.find(User, user2Id)
            user2.setEmail('email1')
        }

        then:

        getEntityLogItems('sec$User', user1Id).size() == 2
        getEntityLogItems('sec$User', user2Id).size() == 2
    }

    protected def createAndSaveUser(EntityManager em, Map params) {
        User user = cont.metadata().create(User)

        params.each {k,v ->
            user[k] = v
        }
        user.setGroup(findCompanyGroup())
        em.persist(user)
        user.getId()
    }

    def "correct old value in case of flush in the middle"() {

        given:

        Group group = findCompanyGroup()

        and:

        withTransaction { EntityManager em ->
            user1Id = createAndSaveUser(em, [login: 'test', name: 'name1', email: 'email1'])
        }

        when:

        withTransaction { EntityManager em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email11')
            user1.setName('name11')

            persistenceTools.getDirtyFields(user1)

            em.reload(group, View.BASE)

            user1 = em.find(User, user1Id)
            user1.setEmail('email111')
        }

        then:

        getEntityLogItems('sec$User', user1Id).size() == 2
        def item = getLatestEntityLogItem('sec$User', user1Id) // latest

        loggedValueMatches(item, 'email', 'email111')
        loggedOldValueMatches(item, 'email', 'email1')

        loggedValueMatches(item, 'name', 'name11')
        loggedOldValueMatches(item, 'name', 'name1')

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

        def item1 = getLatestEntityLogItem('test$IdentityEntity', entity.id.get())



        loggedValueMatches(item1, 'name', 'test1')
        loggedOldValueMatches(item1, 'name', null)


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

        def item2 = getLatestEntityLogItem('test$IdentityEntity', entity.id.get())

        loggedValueMatches(item2, 'name', 'test2')
        loggedOldValueMatches(item2, 'name', 'test1')

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

        def item1 = getLatestEntityLogItem('test$IntIdentityEntity', entity.id.get())


        loggedValueMatches(item1, 'name', 'test1')
        loggedOldValueMatches(item1, 'name', null)


        when:

        withTransaction { EntityManager em ->
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

        def item2 = getLatestEntityLogItem('test$IntIdentityEntity', entity.id.get())

        loggedValueMatches(item2, 'name', 'test2')
        loggedOldValueMatches(item2, 'name', 'test1')


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

        def item2 = getLatestEntityLogItem('test$StringKeyEntity', entity.id)

        loggedValueMatches(item2, 'name', 'test2')
        loggedOldValueMatches(item2, 'name', 'test1')

        loggedValueMatches(item2, 'description', 'description2')
        loggedOldValueMatches(item2, 'description', 'description1')


        cleanup:

        if (entity != null && entity.getId() != null) {
            new QueryRunner(cont.persistence().dataSource).update("delete from TEST_STRING_KEY where code = '${entity.getId()}'")
        }
    }
}
