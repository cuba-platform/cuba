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

package spec.cuba.core.data_manager

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.ValueLoadContext
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DataManagerTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataManager dataManager

    void setup() {
        dataManager = AppBeans.get(DataManager)
    }

    def "loadList query parameter without implicit conversion"() {
        def group = dataManager.load(LoadContext.create(Group).setId(TestSupport.COMPANY_GROUP_ID))

        LoadContext.Query query

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "condition by reference object, no implicit conversion"

        query = LoadContext.createQuery('select u from sec$User u where u.group = :group')
        query.setParameter('group', group, false) // no implicit conversion
        def users = dataManager.loadList(LoadContext.create(User).setQuery(query).setView('user.browse'))

        then: "ok"

        users.size() > 0

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "condition by reference object, implicit conversion"

        query = LoadContext.createQuery('select u from sec$User u where u.group = :group')
        query.setParameter('group', group)
        dataManager.loadList(LoadContext.create(User).setQuery(query).setView('user.browse'))

        then: "fail"

        thrown(IllegalArgumentException)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "condition by reference id, implicit conversion"

        query = LoadContext.createQuery('select u from sec$User u where u.group.id = :group')
        query.setParameter('group', group)
        users = dataManager.loadList(LoadContext.create(User).setQuery(query).setView('user.browse'))

        then: "ok"

        users.size() > 0
    }

    def "loadValues query parameter without implicit conversion"() {
        def group = dataManager.load(LoadContext.create(Group).setId(TestSupport.COMPANY_GROUP_ID))

        ValueLoadContext.Query query

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "condition by reference object, no implicit conversion"

        query = ValueLoadContext.createQuery('select u.id, u.login from sec$User u where u.group = :group')
        query.setParameter('group', group, false) // no implicit conversion
        def list = dataManager.loadValues(ValueLoadContext.create().setQuery(query).addProperty('id').addProperty('login'))

        then: "ok"

        list.size() > 0

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "condition by reference object, implicit conversion"

        query = ValueLoadContext.createQuery('select u.id, u.login from sec$User u where u.group = :group')
        query.setParameter('group', group)
        dataManager.loadValues(ValueLoadContext.create().setQuery(query).addProperty('id').addProperty('login'))

        then: "fail"

        thrown(IllegalArgumentException)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when: "condition by reference id, implicit conversion"

        query = ValueLoadContext.createQuery('select u.id, u.login from sec$User u where u.group.id = :group')
        query.setParameter('group', group)
        list = dataManager.loadValues(ValueLoadContext.create().setQuery(query).addProperty('id').addProperty('login'))

        then: "ok"

        list.size() > 0
    }
}
