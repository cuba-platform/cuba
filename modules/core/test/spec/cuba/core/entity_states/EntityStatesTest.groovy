/*
 * Copyright (c) 2008-2020 Haulmont.
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

package spec.cuba.core.entity_states

import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testmodel.sales_1.Order
import com.haulmont.cuba.testmodel.sales_1.OrderLineA
import com.haulmont.cuba.testmodel.sales_1.OrderLineB
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityStatesTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    DataManager dataManager
    EntityStates entityStates

    void setup() {
        dataManager = AppBeans.get(DataManager)
        entityStates = AppBeans.get(EntityStates)
    }

    def "test getCurrentView for object graph"() {
        given:
        User user = dataManager.load(LoadContext.create(User)
                .setId(UUID.fromString('60885987-1b61-4247-94c7-dff348347f93'))
                .setView('user.edit'))

        when:
        View view = entityStates.getCurrentView(user)

        User user1 = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString('60885987-1b61-4247-94c7-dff348347f93'))
                .setView(view))

        then:
        entityStates.isLoadedWithView(user1, 'user.edit')
    }

    def "test getCurrentView for object graph with inheritance"() {
        given:
        def order = new Order(number: '1')
        def lineA = new OrderLineA(order: order, quantity: 1, param1: 'p1')
        def lineB = new OrderLineB(order: order, quantity: 1, param2: 'p2')
        order.orderLines = [lineA, lineB]

        def orderView = entityStates.getCurrentView(order)
        def lineAView = entityStates.getCurrentView(lineA)
        def lineBView = entityStates.getCurrentView(lineB)
        def commitContext = new CommitContext()
                .addInstanceToCommit(order, orderView).addInstanceToCommit(lineA, lineAView).addInstanceToCommit(lineB, lineBView)

        when:
        def committedOrder = dataManager.commit(commitContext).get(order)

        then:
        def committedLineA = committedOrder.orderLines.find { it == lineA }
        def committedLineB = committedOrder.orderLines.find { it == lineB }

        (committedLineA as OrderLineA).param1 == 'p1'
        (committedLineB as OrderLineB).param2 == 'p2'
        committedLineA.product == null
        committedLineB.product == null
    }
}
