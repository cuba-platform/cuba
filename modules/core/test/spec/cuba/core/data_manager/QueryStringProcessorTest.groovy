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

package spec.cuba.core.data_manager

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.QueryStringProcessor
import com.haulmont.cuba.testmodel.sales.Order
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class QueryStringProcessorTest extends  Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    QueryStringProcessor processor

    void setup() {
        processor = AppBeans.get(QueryStringProcessor)
    }

    def "test from"() {
        def query

        when:
        query = processor.process('from test$Order o left join o.orderLines l where l.product = :product', Order)
        then:
        query == 'select o from test$Order o left join o.orderLines l where l.product = :product'

        when:
        query = processor.process('from test$Customer c, test$Order o where o.customer = c', Order)
        then:
        query == 'select o from test$Customer c, test$Order o where o.customer = c'

        when:
        processor.process('from test$OrderLine l join l.order o', Order)
        then: "not supported, use full query syntax"
        thrown(RuntimeException)
    }

    def "test where"() {
        def query

        when:
        query = processor.process('where e.number = :num', Order)
        then:
        query == 'select e from test$Order e where e.number = :num'
    }

    def "test order by"() {
        def query

        when:
        query = processor.process('order by e.number', Order)
        then:
        query == 'select e from test$Order e order by e.number'
    }

    def "test property conditions"() {
        def query

        when:
        query = processor.process('e.number = :num and e.date = :date', Order)
        then:
        query == 'select e from test$Order e where e.number = :num and e.date = :date'
    }
}
