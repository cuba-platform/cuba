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

package spec.cuba.core.tx_listeners

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserRole
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import com.haulmont.cuba.tx_listener.TestAfterCompleteTxListener
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class AfterCompleteTransactionListenerTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    def "reference CAN be fetched in afterComplete if entity is not partial"() {

        def entityStates = AppBeans.get(EntityStates)
        TestAfterCompleteTxListener.test = 'accessGroup'

        when:

        def user = cont.persistence().callInTransaction { em ->
            em.find(User, TestSupport.ADMIN_USER_ID)
        }

        then:

        entityStates.isLoaded(user, 'login')
        entityStates.isLoaded(user, 'name')
        entityStates.isLoaded(user, 'group')
        user.group != null

        cleanup:

        TestAfterCompleteTxListener.test = null
    }

    def "collection CAN be fetched in afterComplete if entity is not partial"() {

        def entityStates = AppBeans.get(EntityStates)
        TestAfterCompleteTxListener.test = 'accessUserRoles'

        when:

        def user = cont.persistence().callInTransaction { em ->
            em.find(User, TestSupport.ADMIN_USER_ID)
        }

        then:

        entityStates.isLoaded(user, 'login')
        entityStates.isLoaded(user, 'name')
        entityStates.isLoaded(user, 'userRoles')
        user.userRoles.size() == 2

        cleanup:

        TestAfterCompleteTxListener.test = null
    }

    def "local attribute CANNOT be fetched in afterComplete if entity is partial"() {

        def entityStates = AppBeans.get(EntityStates)
        TestAfterCompleteTxListener.test = 'accessName'

        def view = new View(User)
                .addProperty('login')
                .addProperty('group', new View(Group).addProperty('name'))
        view.setLoadPartialEntities(true)

        when:

        def user = cont.persistence().callInTransaction { em ->
            em.find(User, TestSupport.ADMIN_USER_ID, view)
        }

        then:

        entityStates.isLoaded(user, 'login')
        entityStates.isLoaded(user, 'group')
        entityStates.isLoaded(user.group, 'name')
        !entityStates.isLoaded(user, 'name')

        cleanup:

        TestAfterCompleteTxListener.test = null
    }

    def "reference CANNOT be fetched in afterComplete if entity is partial"() {

        def entityStates = AppBeans.get(EntityStates)
        TestAfterCompleteTxListener.test = 'accessGroup'

        def view = new View(User)
                .addProperty('login')
                .addProperty('userRoles', new View(UserRole)
                    .addProperty('role', new View(Role)
                        .addProperty('name')))
        view.setLoadPartialEntities(true)

        when:

        def user = cont.persistence().callInTransaction { em ->
            em.find(User, TestSupport.ADMIN_USER_ID, view)
        }

        then:

        entityStates.isLoaded(user, 'login')
        entityStates.isLoaded(user, 'userRoles')
        !entityStates.isLoaded(user, 'group')

        cleanup:

        TestAfterCompleteTxListener.test = null
    }

    def "collection CANNOT be fetched in afterComplete if entity is partial"() {

        def entityStates = AppBeans.get(EntityStates)
        TestAfterCompleteTxListener.test = 'accessUserRoles'

        def view = new View(User)
                .addProperty('login')
        view.setLoadPartialEntities(true)

        when:

        def user = cont.persistence().callInTransaction { em ->
            em.find(User, TestSupport.ADMIN_USER_ID, view)
        }

        then:

        entityStates.isLoaded(user, 'login')
        !entityStates.isLoaded(user, 'userRoles')

        cleanup:

        TestAfterCompleteTxListener.test = null
    }
}
