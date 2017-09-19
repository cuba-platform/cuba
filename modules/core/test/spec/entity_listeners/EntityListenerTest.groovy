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

package spec.entity_listeners

import com.haulmont.cuba.core.global.AppBeans
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
    public TestContainer cont = TestContainer.Common.INSTANCE;

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
}
