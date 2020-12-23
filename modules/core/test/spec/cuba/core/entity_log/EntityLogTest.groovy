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

import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.PersistenceTools
import com.haulmont.cuba.core.app.importexport.EntityImportExportAPI
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.entity.EntityLogItem
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testmodel.entity_log.EntityLogA
import com.haulmont.cuba.testmodel.entity_log.EntityLogB
import spock.lang.Issue

class EntityLogTest extends AbstractEntityLogTest {

    private UUID user1Id, user2Id
    private UUID roleId

    private EntityImportExportAPI entityImportExport
    protected EntityImportViewBuilderAPI entityImportViewBuilderAPI
    protected Metadata metadata

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
        metadataTools = AppBeans.get(MetadataTools.class)
        entityImportExport = AppBeans.get(EntityImportExportAPI.class)
        entityImportViewBuilderAPI = AppBeans.get(EntityImportViewBuilderAPI.class)
        metadata = AppBeans.get(Metadata.class)
    }


    protected void initEntityLogConfiguration(EntityManager em) {

        saveEntityLogAutoConfFor(em, 'sec$User', 'name', 'email', 'group')

        saveEntityLogAutoConfFor(em, 'sec$Role', 'type')

        saveEntityLogAutoConfFor(em, 'test_EntityLogA', 'name', 'entityLogB')
    }


    void cleanup() {
        clearTables("SEC_LOGGED_ATTR", "SEC_LOGGED_ENTITY")

        if (user1Id != null)
            cont.deleteRecord("SEC_USER", user1Id)
        if (user2Id != null)
            cont.deleteRecord("SEC_USER", user2Id)

        if (roleId != null)
            cont.deleteRecord("SEC_ROLE", roleId)
    }


    @Issue("PL-10005")
    def "missed Entity Log items because of implicit flush"() {

        given:

        withTransaction { EntityManager em ->
            user1Id = createAndSaveUser(em, [login: "test", name: 'test-name'])
            user2Id = createAndSaveUser(em, [login: "test2", name: 'test2-name'])
        }

        and:

        getEntityLogItems('sec$User', user1Id).size() == 1
        getEntityLogItems('sec$User', user2Id).size() == 1


        when: 'instance attributes are changed an implicit flush is executed'

        def instanceName1 = ''
        def instanceName2 = ''

        withTransaction { EntityManager em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email1')
            instanceName1 = metadataTools.getInstanceName(user1)

            em.reload(findCompanyGroup(), View.BASE)

            def user2 = em.find(User, user2Id)
            user2.setEmail('email1')
            instanceName2 = metadataTools.getInstanceName(user2)
        }

        then: 'those attribute changes result in Entity log items'

        getEntityLogItems('sec$User', user1Id).size() == 2
        getEntityLogItems('sec$User', user2Id).size() == 2
        getLatestEntityLogItem('sec$User', user1Id).entityInstanceName == instanceName1
        getLatestEntityLogItem('sec$User', user2Id).entityInstanceName == instanceName2
    }

    def "correct old value in case of flush in the middle"() {

        given:

        Group group = findCompanyGroup()

        and:
        def firstEmail = 'email1'
        def firstName = 'name1'

        and:

        withTransaction { EntityManager em ->
            user1Id = createAndSaveUser(em, [login: 'test', name: firstName, email: firstEmail])
        }

        when:
        def secondEmail = 'email11'
        def secondName = 'name11'
        def thirdEmail = 'email111'
        def instanceName = ''

        withTransaction { EntityManager em ->
            def user1 = em.find(User, user1Id)

            user1.setEmail(secondEmail)
            user1.setName(secondName)

            persistenceTools.getDirtyFields(user1)

            em.reload(group, View.BASE)

            user1 = em.find(User, user1Id)
            user1.setEmail(thirdEmail)
            instanceName = metadataTools.getInstanceName(user1)
        }

        then:

        getEntityLogItems('sec$User', user1Id).size() == 2
        def item = getLatestEntityLogItem('sec$User', user1Id)

        loggedValueMatches(item, 'email', thirdEmail)
        loggedOldValueMatches(item, 'email', firstEmail)

        loggedValueMatches(item, 'name', secondName)
        loggedOldValueMatches(item, 'name', firstName)

        getLatestEntityLogItem('sec$User', user1Id).entityInstanceName == instanceName
    }

    def "instance name with reference"() {
        when:

        EntityLogB entityLogB

        withTransaction { EntityManager em ->
            entityLogB = cont.metadata().create(EntityLogB)
            entityLogB.setName('nameB')

            em.persist(entityLogB)
        }

        UUID aId = null
        EntityLogA entityLogA

        withTransaction { EntityManager em ->
            em = cont.persistence().getEntityManager()
            entityLogA = cont.metadata().create(EntityLogA)
            entityLogA.setName('nameA')
            entityLogA.setEntityLogB(entityLogB)

            em.persist(entityLogA)
            aId = entityLogA.getId()
        }

        withTransaction { EntityManager em ->
            em = cont.persistence().getEntityManager()
            entityLogA = em.reload(entityLogA, '_local')
            entityLogA.setName('edited_nameA')

            em.persist(entityLogA)
        }

        then: 'those attribute changes result in Entity log items'

        getEntityLogItems('test_EntityLogA', aId).size() == 2
        getLatestEntityLogItem('test_EntityLogA', aId).entityInstanceName == 'edited_nameA nameB'
    }

    def "field value with empty instance name set up via dataManager.getReference() in entity log"() {
        when:

        EntityLogB entityLogB

        DataManager dataManager = AppBeans.get(DataManager)

        withTransaction { EntityManager em ->
            entityLogB = cont.metadata().create(EntityLogB)

            em.persist(entityLogB)
        }

        UUID aId = null
        EntityLogA entityLogA

        withTransaction { EntityManager em ->
            em = cont.persistence().getEntityManager()
            entityLogA = cont.metadata().create(EntityLogA)
            entityLogA.setName('nameA')

            entityLogA.setEntityLogB(dataManager.getReference(EntityLogB, entityLogB.id))

            em.persist(entityLogA)
            aId = entityLogA.getId()
        }

        then: 'those attribute changes result in Entity log items'

        getEntityLogItems('test_EntityLogA', aId).size() == 1
        def item = getLatestEntityLogItem('test_EntityLogA', aId)

        loggedValueMatches(item, 'entityLogB', "")
    }

    def "Entity import logged successfully"() {
        when: "New entity with reference to existing one imported"
        user1Id = UUID.randomUUID()
        String json = "{\n" +
                '  "_entityName": "sec$User",\n' +
                '  "id": "' + user1Id + '",\n' +
                '  "login": "testImport",\n' +
                '  "group": {\n' +
                '    "_entityName": "sec$Group",\n' +
                '    "id": "' + findCompanyGroup().id + '"\n' +
                '  },\n' +
                '  "name": "test-import-name"\n' +
                '}\n'
        String jsonArray = "[$json]"

        entityImportExport.importEntitiesFromJson(jsonArray,
                entityImportViewBuilderAPI.buildFromJson(json, metadata.getClass('sec$User')))

        then: "Reference entity loaded properly and isn't caused an unfetched attribute exception at entity log creation"

        EntityLogItem logItem = getLatestEntityLogItem('sec$User', user1Id)
        logItem.type == EntityLogItem.Type.CREATE
        loggedValueMatches(logItem, 'group', 'Company')
    }

    protected def createAndSaveUser(EntityManager em, Map params) {
        User user = cont.metadata().create(User)

        params.each { k, v ->
            user[k] = v
        }
        user.setGroup(findCompanyGroup())
        em.persist(user)
        user.getId()
    }

}
