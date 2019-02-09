/*
 * Copyright (c) 2008-2019 Haulmont.
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

package spec.cuba.web.datacontext


import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.web.testmodel.datacontext.TestStringIdEntity
import com.haulmont.cuba.web.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DataContextNullIdTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataComponents factory

    void setup() {
        factory = cont.getBean(DataComponents)
    }

    def "can contain only 1 instance with null id"() {
        TestStringIdEntity entity1 = new TestStringIdEntity(name: "111")
        TestStringIdEntity entity2 = new TestStringIdEntity(name: "222")

        def dataContext = factory.createDataContext()
        def merged1 = dataContext.merge(entity1)

        when:

        def merged2 = dataContext.merge(entity2)

        then:

        thrown(IllegalStateException)
    }

    def "merged instance with null id"() {
        TestStringIdEntity entity = new TestStringIdEntity(name: "111")

        def dataContext = factory.createDataContext()

        when:

        def merged = dataContext.merge(entity)

        then:

        !merged.is(entity)
        dataContext.contains(entity)
        dataContext.find(TestStringIdEntity, null).is(merged)
    }

    def "merged instance changes id"() {
        TestStringIdEntity entity = new TestStringIdEntity(name: "111")

        def dataContext = factory.createDataContext()

        when:

        def merged = dataContext.merge(entity)
        merged.setCode('1')

        then:

        dataContext.find(TestStringIdEntity, '1').is(merged)

        when:

        merged.setCode('2')

        then:

        dataContext.find(TestStringIdEntity, '1') == null
        dataContext.find(TestStringIdEntity, '2').is(merged)
    }
}
