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

package spec.cuba.core.entity_log


import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.PersistenceTools
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.security.app.EntityAttributeChanges
import com.haulmont.cuba.testmodel.primary_keys.IdentityEntity
import com.haulmont.cuba.testmodel.primary_keys.IntIdentityEntity
import com.haulmont.cuba.testmodel.primary_keys.StringKeyEntity

class EntityLogDatatypesTest extends AbstractEntityLogTest {

    Entity entity
    def entityId


    void setup() {
        clearTables("SEC_LOGGED_ATTR", "SEC_LOGGED_ENTITY")

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

        saveEntityLogAutoConfFor(em, 'test$IntIdentityEntity', 'name')

        saveEntityLogAutoConfFor(em, 'test$IdentityEntity', 'name')

        saveManualEntityLogAutoConfFor(em, 'test$StringKeyEntity', 'name', 'description')
    }


    void cleanup() {
        clearTables("SEC_LOGGED_ATTR", "SEC_LOGGED_ENTITY")
    }


    def "Logging is working for a creation of a BaseIdentityIdEntity"() {

        when:

        def identityEntity = new IdentityEntity(name: 'test1')
        saveEntity(identityEntity, identityEntity.id.get())

        then:

        def entityLogItem = getLatestEntityLogItem('test$IdentityEntity', entityId)

        loggedValueMatches(entityLogItem, 'name', 'test1')
        loggedOldValueMatches(entityLogItem, 'name', null)

        cleanup:

        clearEntityById(entity, entityId, 'TEST_IDENTITY')
    }


    def "Logging is working for an update of a BaseIdentityIdEntity"() {

        given:
        def identityEntity = new IdentityEntity(name: 'test1')
        saveEntity(identityEntity, identityEntity.id.get())

        when:

        withTransaction {EntityManager em ->
            IdentityEntity e = em.find(IdentityEntity, entity.id)
            e.name = 'test2'
        }

        then:

        def item2 = getLatestEntityLogItem('test$IdentityEntity', entityId)

        loggedValueMatches(item2, 'name', 'test2')
        loggedOldValueMatches(item2, 'name', 'test1')

        cleanup:

        clearEntityById(entity, entityId, 'TEST_IDENTITY')
    }


    def "Logging is working for a creation of a BaseIntIdentityIdEntity"() {

        when:
        def intIdentityEntity = new IntIdentityEntity(name: 'test1')
        saveEntity(intIdentityEntity, intIdentityEntity.id.get())

        then:

        noExceptionThrown()

        def item1 = getLatestEntityLogItem('test$IntIdentityEntity', entityId)


        loggedValueMatches(item1, 'name', 'test1')
        loggedOldValueMatches(item1, 'name', null)

        cleanup:

        clearEntityById(entity, entityId, 'TEST_INT_IDENTITY')
    }

    def "Logging is working for an update of a BaseIntIdentityIdEntity"() {

        given:
        def intIdentityEntity = new IntIdentityEntity(name: 'test1')
        saveEntity(intIdentityEntity, intIdentityEntity.id.get())


        when:

        withTransaction { EntityManager em ->
            def e = em.find(IntIdentityEntity, entity.id)
            e.name = 'test2'
        }

        then:

        def item2 = getLatestEntityLogItem('test$IntIdentityEntity', entityId)

        loggedValueMatches(item2, 'name', 'test2')
        loggedOldValueMatches(item2, 'name', 'test1')


        cleanup:

        clearEntityById(entity, entityId, 'TEST_INT_IDENTITY')
    }

    protected saveEntity(Entity entity, def entityId) {
        withTransaction { EntityManager em ->
            this.entity = entity
            this.entityId = entityId
            em.persist(entity)
        }
    }

    def "Logging is working for a creation of a MetaProperty"() {

        when:

        def stringKeyEntity = new StringKeyEntity(code: 'code1', name: 'test1')
        saveEntity(stringKeyEntity, stringKeyEntity.id)

        then:

        noExceptionThrown()


        cleanup:
        clearEntityByCode(entity, entityId, 'TEST_STRING_KEY')

    }

    def "Logging is working for an update of a MetaProperty"() {

        given:

        withTransaction {EntityManager em ->
            entity = new StringKeyEntity(code: 'code1', name: 'test1')
            entityId = entity.id
            em.persist(entity)
        }

        when:

        withTransaction { EntityManager em ->
            StringKeyEntity e = em.find(StringKeyEntity, entity.id)
            e.name = 'test2'
            e.description = 'description2'

            EntityAttributeChanges changes = new EntityAttributeChanges()
            changes.addChanges(e)
            changes.addChange('description', 'description1')
            entityLog.registerModify(e, false, changes)
        }

        then:

        def item2 = getLatestEntityLogItem('test$StringKeyEntity', entity.id)

        loggedValueMatches(item2, 'name', 'test2')
        loggedOldValueMatches(item2, 'name', 'test1')

        loggedValueMatches(item2, 'description', 'description2')
        loggedOldValueMatches(item2, 'description', 'description1')


        cleanup:
        clearEntityByCode(entity, entityId, 'TEST_STRING_KEY')

    }


    private clearEntityById(Entity  entity, def entityId, String tableName) {
        if (entity && entityId) {
            runSqlUpdate("delete from $tableName where id = ${entityId}")
        }
    }

    private clearEntityByCode(Entity  entity, def entityCode, String tableName) {
        if (entity && entityCode) {
            runSqlUpdate("delete from $tableName where code = '${entityCode}'")
        }
    }


}
