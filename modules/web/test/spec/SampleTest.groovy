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

package spec

import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserSetting
import com.haulmont.cuba.web.testmodel.sample.Sample
import com.haulmont.cuba.web.testmodel.sample.SampleNonPersistent
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class SampleTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata
    private DataManager dataManager

    void setup() {
        metadata = cont.getBean(Metadata)
        dataManager = cont.getBean(DataManager)
    }

    def "test metadata"() {

        expect:

        metadata != null
        metadata.getClassNN(User).getJavaClass() == User
        metadata.getClassNN(User).getPropertyNN('login').range.asDatatype().javaClass == String
    }

    def "DataManager returns null if not mocked"() {

        when:

        LoadContext<User> loadContext = LoadContext.create(User).setId(UUID.randomUUID())
        User user = dataManager.load(loadContext)

        then:

        noExceptionThrown()
        user == null
    }

    def "DataManager returns correct instances from commit if not mocked"() {

        def entityStates = cont.getBean(EntityStates)
        User user = new User(login: 'user1')
        UserSetting userSetting = new UserSetting(user: user)

        when:

        def user1 = dataManager.commit(user)

        then:

        !user1.is(user)
        user1 == user
        user1.login == 'user1'

        !entityStates.isNew(user1)
        entityStates.isDetached(user1)
        user1.version == 1
        user1.createTs != null
        user1.createdBy != null
        user1.updateTs != null
        user1.updatedBy == null

        when:

        Thread.sleep(10)
        def user2 = dataManager.commit(user1)

        then:

        user2.version == 2
        user2.updateTs > user1.updateTs
        user2.updatedBy != null

        when:

        def cc = new CommitContext().addInstanceToRemove(user2)
        Set<Entity> committed = dataManager.commit(cc)

        then:

        committed.size() == 1
        User user3 = committed[0]
        user3.version == 3
        user3.deleteTs != null
        user3.deletedBy != null

        when:

        UserSetting userSetting1 = dataManager.commit(userSetting)
        def cc1 = new CommitContext().addInstanceToRemove(userSetting1)
        def committed1 = dataManager.commit(cc1)

        then:

        UserSetting userSetting2 = committed1[0]
        BaseEntityInternalAccess.isRemoved(userSetting2)
    }

    def "DataManager can return a value"() {

        def user = new User()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            load(_) >> user
            loadList(_) >> [user]
        })

        when:

        LoadContext<User> loadContext = LoadContext.create(User).setId(user.getId())
        User user1 = dataManager.load(loadContext)

        then:

        noExceptionThrown()
        user1 == user

        when:

        List<User> users = dataManager.loadList(loadContext)

        then:

        noExceptionThrown()
        users[0] == user

        cleanup:

        TestServiceProxy.clear()
    }

    def "persistent entity from test model"() {

        def sample = new Sample()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            load(_) >> sample
        })

        when:

        LoadContext<Sample> loadContext = LoadContext.create(Sample).setId(sample.getId())
        def sample1 = dataManager.load(loadContext)

        then:

        noExceptionThrown()
        sample1 == sample

        cleanup:

        TestServiceProxy.clear()
    }

    def "non-persistent entity from test model"() {

        def sample = new SampleNonPersistent()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            load(_) >> sample
        })

        when:

        LoadContext<SampleNonPersistent> loadContext = LoadContext.create(SampleNonPersistent).setId(sample.getId())
        def sample1 = dataManager.load(loadContext)

        then:

        noExceptionThrown()
        sample1 == sample

        cleanup:

        TestServiceProxy.clear()
    }

    def "combination of default behavior and mock"() {

        def sample = new Sample()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            load(_) >> sample
            commit(_) >> { CommitContext cc -> TestServiceProxy.getDefault(DataService).commit(cc) }
        })

        when:

        LoadContext<Sample> loadContext = LoadContext.create(Sample).setId(sample.getId())
        def sample1 = dataManager.load(loadContext)

        then:

        noExceptionThrown()
        sample1 == sample

        when:

        def sample2 = dataManager.commit(sample)

        then:

        cont.getBean(EntityStates).isDetached(sample2)

        cleanup:

        TestServiceProxy.clear()

    }
}
