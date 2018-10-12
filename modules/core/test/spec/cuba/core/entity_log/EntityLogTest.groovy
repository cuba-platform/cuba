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
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import spock.lang.Issue

class EntityLogTest extends AbstractEntityLogTest {

    private UUID user1Id, user2Id
    private UUID roleId


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

        saveEntityLogAutoConfFor(em, 'sec$User', 'name', 'email')

        saveEntityLogAutoConfFor(em, 'sec$Role', 'type')

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

        withTransaction { EntityManager em ->
            def user1 = em.find(User, user1Id)
            user1.setEmail('email1')

            em.reload(findCompanyGroup(), View.BASE)

            def user2 = em.find(User, user2Id)
            user2.setEmail('email1')
        }

        then: 'those attribute changes result in Entity log items'

        getEntityLogItems('sec$User', user1Id).size() == 2
        getEntityLogItems('sec$User', user2Id).size() == 2
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

        withTransaction { EntityManager em ->
            def user1 = em.find(User, user1Id)

            user1.setEmail(secondEmail)
            user1.setName(secondName)

            persistenceTools.getDirtyFields(user1)

            em.reload(group, View.BASE)

            user1 = em.find(User, user1Id)
            user1.setEmail(thirdEmail)
        }

        then:

        getEntityLogItems('sec$User', user1Id).size() == 2
        def item = getLatestEntityLogItem('sec$User', user1Id)

        loggedValueMatches(item, 'email', thirdEmail)
        loggedOldValueMatches(item, 'email', firstEmail)

        loggedValueMatches(item, 'name', secondName)
        loggedOldValueMatches(item, 'name', firstName)

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
