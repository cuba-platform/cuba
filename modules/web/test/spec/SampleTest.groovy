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
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.security.entity.User
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

    def "DataManager can return a value"() {

        def user = new User()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            load(_) >> user
        })

        when:

        LoadContext<User> loadContext = LoadContext.create(User).setId(user.getId())
        User user1 = dataManager.load(loadContext)

        then:

        noExceptionThrown()
        user1 == user

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
}
