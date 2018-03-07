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
package spec.cuba.core.entity_listeners

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.core.global.ViewRepository
import com.haulmont.cuba.core.listener.TestUserDetachListener
import com.haulmont.cuba.core.listener.TestUserEntityListener
import com.haulmont.cuba.core.sys.listener.EntityListenerManager
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityListenerTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private dataManager

    void setup() {
        dataManager = AppBeans.get(DataManager)
    }

    def "PL-9350 onBeforeInsert listener fires twice if em.flush() is used"() {

        EntityListenerManager entityListenerManager = AppBeans.get(EntityListenerManager);
        entityListenerManager.addListener(User, TestUserEntityListener)
        def events = TestUserEntityListener.events
        events.clear()

        def user = cont.metadata().create(User)
        user.setLogin("user-" + user.id)
        user.setGroup(cont.persistence().callInTransaction { em -> em.find(Group, TestSupport.COMPANY_GROUP_ID) })

        when:

        cont.persistence().runInTransaction() { em ->
            em.persist(user)
            em.flush()
            user.setName(user.login)
        }

        then:

        events.size() == 4
        events[0].startsWith("onBeforeInsert")
        events[1].startsWith("onAfterInsert")
        events[2].startsWith("onBeforeUpdate")
        events[3].startsWith("onAfterUpdate")

        cleanup:

        events.clear()
        entityListenerManager.removeListener(User, TestUserEntityListener)
        cont.deleteRecord(user)
    }

    def "accessing properties that are not loaded"() {

        def user = cont.metadata().create(User)
        user.setLogin("User-$user.id")
        user.setName('test user')
        user.setGroup(cont.persistence().callInTransaction { em -> em.find(Group, TestSupport.COMPANY_GROUP_ID) })
        cont.persistence().runInTransaction() { em ->
            em.persist(user)
        }

        def view = new View(User).addProperty('name')
        def loadContext = LoadContext.create(User).setId(user.id).setView(view)

        when:

        def loadedUser = dataManager.load(loadContext)
        loadedUser.setName('changed name')
        dataManager.commit(loadedUser)

        loadedUser = cont.persistence().callInTransaction() { em ->
            em.find(User, user.id)
        }

        then:

        noExceptionThrown()
        loadedUser.name == 'changed name'
        loadedUser.login == user.login
        loadedUser.loginLowerCase == user.login.toLowerCase()

        cleanup:

        cont.deleteRecord(user)
    }

    def "accessing not loaded attributes in BeforeDetach"() {

        def user = cont.metadata().create(User)
        user.setLogin("User-$user.id")
        user.setName('test user')

        def group = cont.persistence().callInTransaction { em -> em.find(Group, TestSupport.COMPANY_GROUP_ID) }
        user.setGroup(group)

        cont.persistence().runInTransaction() { em ->
            em.persist(user)
        }

        EntityListenerManager entityListenerManager = AppBeans.get(EntityListenerManager);
        entityListenerManager.addListener(User, TestUserDetachListener)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "loading entity with view"

        def view = new View(User)
                .addProperty('name')
                .addProperty('group', new View(Group)
                    .addProperty('name'))
        dataManager.load(LoadContext.create(User).setId(user.id).setView(view))

        then: "throw exception on access unfetched attribute in DetachListener"

        def exception = thrown(IllegalStateException)
        println(exception)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "loading entity with local view"

        view = AppBeans.get(ViewRepository).getView(User, View.LOCAL)

        def loadedUser = dataManager.load(LoadContext.create(User).setId(user.id).setView(view))

        then: "can fetch reference attributes in DetachListener"

        loadedUser.group == group

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "loading entity without view"

        loadedUser = cont.persistence().callInTransaction { em -> em.find(User, user.id) }

        then: "can fetch reference attributes in DetachListener"

        loadedUser.group == group

        cleanup:

        entityListenerManager.removeListener(User, TestUserDetachListener)
        cont.deleteRecord(user)
    }
}
