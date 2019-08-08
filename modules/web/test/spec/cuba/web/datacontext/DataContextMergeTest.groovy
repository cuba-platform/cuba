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

package spec.cuba.web.datacontext

import com.haulmont.chile.core.model.Instance
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess
import com.haulmont.cuba.core.entity.SecurityState
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.web.container.CubaTestContainer
import com.haulmont.cuba.web.testmodel.sales.Customer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.Status
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import org.eclipse.persistence.internal.queries.EntityFetchGroup
import org.eclipse.persistence.queries.FetchGroupTracker
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DataContextMergeTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = CubaTestContainer.Common.INSTANCE

    private DataComponents factory
    private EntityStates entityStates
    private Metadata metadata

    void setup() {
        factory = cont.getBean(DataComponents)
        metadata = cont.getBean(Metadata)
        entityStates = cont.getBean(EntityStates)
    }

    void cleanup() {
        TestServiceProxy.clear()
    }

    def "merge equal instances"() throws Exception {
        DataContext context = factory.createDataContext()

        when: "merging instance first time"

        Customer customer1 = new Customer(name: 'c1')
        def trackedCustomer1 = context.merge(customer1)
        def customerInContext1 = context.find(Customer, customer1.id)

        then: "tracked instance is different"

        !trackedCustomer1.is(customer1)
        customerInContext1.is(trackedCustomer1)
        trackedCustomer1.name == 'c1'

        when: "merging another instance with the same id"

        Customer customer11 = new Customer(id: customer1.id, name: 'c11')
        def trackedCustomer11 = context.merge(customer11)
        def customerInContext11 = context.find(Customer, customer11.id)

        then: "returned instance which was already in context"

        trackedCustomer11.is(trackedCustomer1)
        !trackedCustomer11.is(customer11)
        customerInContext11.is(trackedCustomer11)
        trackedCustomer11.name == 'c11'
    }

    def "merge graph 1"() throws Exception {

        // order1
        //   line1
        //     order2 (=order1)

        DataContext context = factory.createDataContext()

        when: "merging graph containing different instances with same ids"

        Order order1 = new Order(number: '1')
        Order order2 = new Order(number: '2', id: order1.id)

        OrderLine line1 = new OrderLine(quantity: 1, order: order2)
        makeDetached(line1)
        order1.orderLines = [line1]

        def mergedOrder = context.merge(order1)

        then: "context contains another instance"

        mergedOrder == order1
        mergedOrder == order2
        !mergedOrder.is(order1)
        !mergedOrder.is(order2)

        and: "merged instance has local attributes of the second object"

        mergedOrder.number == '2'

        and: "second object in the graph is now the same instance"

        mergedOrder.orderLines.size() == 1
        mergedOrder.orderLines[0].order.is(mergedOrder)
    }

    def "merge graph 2"() throws Exception {

        // order1
        //   line1
        //     order1
        //   line2
        //     order2 (=order1)

        DataContext context = factory.createDataContext()

        when: "merging graph containing different instances with same ids"

        Order order1 = new Order(number: '1')
        Order order2 = new Order(number: '2', id: order1.id)

        OrderLine line1 = new OrderLine(quantity: 1, order: order1)
        OrderLine line2 = new OrderLine(quantity: 2, order: order2)
        order1.orderLines = [line1, line2]

        def mergedOrder = context.merge(order1)

        then: "context contains another instance"

        mergedOrder == order1
        mergedOrder == order2
        !mergedOrder.is(order1)
        !mergedOrder.is(order2)

        and: "merged instance has local attributes of the second object"

        mergedOrder.number == '2'

        and: "second object in the graph is now the same instance"

        mergedOrder.orderLines.size() == 2
        mergedOrder.orderLines[0].order.is(mergedOrder)
        mergedOrder.orderLines[1].order.is(mergedOrder)
    }

    def "merge graph 3"() throws Exception {

        // order1
        //   customer1

        // order2 (=order1)
        //   customer2

        DataContext context = factory.createDataContext()

        when:

        Customer customer1 = new Customer(name: 'c1')
        makeDetached(customer1)
        Order order1 = new Order(number: '1', customer: customer1)
        makeDetached(order1)

        Customer customer2 = new Customer(name: 'c2')
        makeDetached(customer2)
        Order order2 = new Order(number: '2', customer: customer2, id: order1.id)
        makeDetached(order2)

        def mergedOrder1 = context.merge(order1)
        def mergedOrder2 = context.merge(order2)

        then:

        mergedOrder2.is(mergedOrder1)
        mergedOrder1 == order1
        mergedOrder1 == order2
        !mergedOrder1.is(order1)
        !mergedOrder1.is(order2)

        and: "merged instance has local attributes of the second object"

        mergedOrder1.number == '2'

        and: "merged instance has reference which is a copy of the reference merged second"

        mergedOrder1.customer == customer2
        !mergedOrder1.customer.is(customer2)
    }

    def "merge graph 4"() throws Exception {

        // order1
        //   line1
        //     order1

        // order2 (=order1)
        //   line21 (=line1)
        //     order2
        //   line22
        //     order2

        DataContext context = factory.createDataContext()

        when: "merging graph containing different instances with same ids"

        Order order1 = new Order(number: '1')
        makeDetached(order1)

        OrderLine line1 = new OrderLine(quantity: 1, order: order1)
        makeDetached(line1)
        order1.orderLines = [line1]

        Order order2 = new Order(number: '2', id: order1.id)
        makeDetached(order2)

        OrderLine line21 = new OrderLine(quantity: 11, order: order2, id: line1.id)
        makeDetached(line21)
        OrderLine line22 = new OrderLine(quantity: 2, order: order2)
        makeDetached(line22)
        order2.orderLines = [line21, line22]

        def mergedOrder1 = context.merge(order1)
        def mergedOrder2 = context.merge(order2)

        then:

        mergedOrder1.is(mergedOrder2)
        mergedOrder1.number == '2'

        and:

        mergedOrder1.orderLines.size() == 2

        def mergedLine1 = mergedOrder1.orderLines[0]
        def mergedLine2 = mergedOrder1.orderLines[1]
        mergedLine1 == line1
        mergedLine1 == line21
        mergedLine2 == line22

        mergedLine1.order.is(mergedOrder1)
        mergedLine2.order.is(mergedOrder1)

        mergedLine1.quantity == 11
        mergedLine2.quantity == 2
    }

    def "merge graph 5"() throws Exception {

        // order1
        //   customer1
        //   line1
        //     order1

        // order2 (=order1)

        DataContext context = factory.createDataContext()

        when:

        Customer customer1 = new Customer(name: 'c1')

        Order order1 = new Order(number: '1', customer: customer1)

        OrderLine line1 = new OrderLine(quantity: 1, order: order1)
        order1.orderLines = [line1]

        Order order2 = new Order(number: '2', id: order1.id)

        def mergedOrder1 = context.merge(order1)
        def mergedOrder2 = context.merge(order2)

        then:

        mergedOrder1.is(mergedOrder2)
        mergedOrder1.number == '2'

        and: "attributes of merged root completely replace previously merged attributes"

        mergedOrder1.customer == null
        mergedOrder1.orderLines == null
    }

    def "merge with existing - locals"() {

        DataContext context = factory.createDataContext()

        def cust1, cust2

        when: "(1) src.new -> dst.new : copy all"

        cust1 = new Customer(name: 'c1')
        cust2 = new Customer(name: 'c2', status: Status.OK, id: cust1.id)

        context.merge(cust1)
        context.merge(cust2)

        then:

        def merged1 = context.find(Customer, cust1.id)
        merged1.name == 'c2'
        merged1.status == Status.OK

        when: "(2) src.new -> dst.det : copy all"

        cust1 = new Customer(name: 'c1')
        makeDetached(cust1)
        cust2 = new Customer(name: 'c2', status: Status.OK, id: cust1.id)

        context.merge(cust1)
        context.merge(cust2)

        then:

        def merged2 = context.find(Customer, cust1.id)
        merged2.name == 'c2'
        merged2.status == Status.OK

        when: "(3) src.det -> dst.new : copy all loaded, make detached"

        cust1 = new Customer(name: 'c1', email: 'c1@aaa.aa', status: Status.NOT_OK)
        cust2 = new Customer(name: 'c2', id: cust1.id)
        makeDetached(cust2)
        ((FetchGroupTracker) cust2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'name', 'email'))

        context.merge(cust1)
        context.merge(cust2)

        then:

        def merged3 = context.find(Customer, cust1.id)
        merged3.name == 'c2'
        merged3.email == null
        merged3.status == Status.NOT_OK
        entityStates.isDetached(merged3)

        when: "(4) src.det -> dst.det : if src.version >= dst.version, copy all loaded"

        cust1 = new Customer(name: 'c1', email: 'c1@aaa.aa', status: Status.NOT_OK, version: 1)
        makeDetached(cust1)
        cust2 = new Customer(name: 'c2', id: cust1.id, version: 2)
        makeDetached(cust2)
        ((FetchGroupTracker) cust2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'name', 'email'))

        context.merge(cust1)
        context.merge(cust2)

        then:

        def merged41 = context.find(Customer, cust1.id)
        merged41.name == 'c2'
        merged41.email == null
        merged41.status == Status.NOT_OK
        merged41.version == 2
    }

    def "merge with existing - to-one refs"() {

        DataContext context = factory.createDataContext()

        def order1, order2, cust1, cust2, user1

        cust1 = new Customer(name: 'c1')
        cust2 = new Customer(name: 'c2', status: Status.OK)
        user1 = new User(login: 'u1')

        when: "(1) src.new -> dst.new : copy all"

        order1 = new Order(customer: cust1, user: user1)
        order2 = new Order(customer: cust2, id: order1.id)

        context.merge(order1)
        context.merge(order2)

        then:

        def merged1 = context.find(Order, order1.id)
        merged1.customer == cust2
        merged1.user == null

        when: "(2) src.new -> dst.det : copy all"

        order1 = new Order(customer: cust1)
        makeDetached(order1)
        order2 = new Order(customer: cust2, user: user1, id: order1.id)

        context.merge(order1)
        context.merge(order2)

        then:

        def merged2 = context.find(Order, order1.id)
        merged2.customer == cust2
        merged2.user == user1

        when: "(3) src.det -> dst.new : copy all loaded, make detached"

        order1 = new Order(customer: cust1, user: user1)
        order2 = new Order(customer: cust2, id: order1.id)
        makeDetached(order2)
        ((FetchGroupTracker) order2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'customer', 'user'))

        context.merge(order1)
        context.merge(order2)

        then:

        def merged3 = context.find(Order, order1.id)
        merged3.customer == cust2
        merged3.user == null
        entityStates.isDetached(merged3)

        when: "(4) src.det -> dst.det : if src.version >= dst.version, copy all loaded"

        order1 = new Order(customer: cust1, user: user1, version: 1)
        makeDetached(order1)
        order2 = new Order(customer: cust2, id: order1.id, version: 2)
        makeDetached(order2)
        ((FetchGroupTracker) order2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'customer', 'user'))

        context.merge(order1)
        context.merge(order2)

        then:

        def merged41 = context.find(Order, order1.id)
        merged41.customer == cust2
        merged41.user == null
        merged41.version == 2
    }

    def "merge with existing - to-many refs"() {

        DataContext context = factory.createDataContext()

        def order1, order2, line1, line2

        when: "(1) src.new -> dst.new : copy all (replace collections)"

        order1 = new Order()
        order2 = new Order(id: order1.id)
        line1 = new OrderLine(order: order1)
        order1.orderLines = [line1]
        line2 = new OrderLine(order: order2)
        order2.orderLines = [line2]

        context.merge(order1)
        context.merge(order2)

        then:

        def merged1 = context.find(Order, order1.id)
        merged1.orderLines.size() == 1
        merged1.orderLines.contains(line2)

        when: "(1) src.new > dst.new : copy all (replace null collection)"

        order1 = new Order()
        order2 = new Order(id: order1.id)
        line1 = new OrderLine(order: order1)
        order1.orderLines = [line1]

        context.merge(order1)
        context.merge(order2)

        then:

        def merged11 = context.find(Order, order1.id)
        merged11.orderLines == null

        when: "(2) src.new -> dst.det : copy all (replace collections)"

        order1 = new Order()
        makeDetached(order1)
        order2 = new Order(id: order1.id)
        line1 = new OrderLine(order: order1)
        order1.orderLines = [line1]
        line2 = new OrderLine(order: order2)
        order2.orderLines = [line2]

        context.merge(order1)
        context.merge(order2)

        then:

        def merged2 = context.find(Order, order1.id)
        merged2.orderLines.size() == 1
        merged2.orderLines.containsAll(line2)

        when: "(3) src.det -> dst.new : copy all loaded, make detached"

        order1 = new Order()
        order2 = new Order(id: order1.id)
        makeDetached(order2)
        ((FetchGroupTracker) order2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'orderLines'))
        line1 = new OrderLine(order: order1)
        order1.orderLines = [line1]
        line2 = new OrderLine(order: order2)
        order2.orderLines = [line2]

        context.merge(order1)
        context.merge(order2)

        then:

        def merged3 = context.find(Order, order1.id)
        merged3.orderLines.size() == 1
        merged3.orderLines[0] == line2
        !merged3.orderLines[0].is(line2)


        when: "(4) src.det -> dst.det : if src.version > dst.version, copy all loaded, replace collections"

        order1 = new Order(version: 1)
        makeDetached(order1)
        order2 = new Order(id: order1.id, version: 2)
        makeDetached(order2)
        ((FetchGroupTracker) order2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'orderLines'))
        line1 = new OrderLine(order: order1)
        order1.orderLines = [line1]
        line2 = new OrderLine(order: order2)
        order2.orderLines = [line2]

        context.merge(order1)
        context.merge(order2)

        then:

        def merged41 = context.find(Order, order1.id)
        merged41.orderLines.size() == 1
        merged41.orderLines[0] == line2
        !merged41.orderLines[0].is(line2)

        when: "(4) src.det -> dst.det : if src.version == dst.version, copy all loaded, join collections"

        order1 = new Order(version: 1)
        makeDetached(order1)
        order2 = new Order(id: order1.id, version: 1)
        makeDetached(order2)
        ((FetchGroupTracker) order2)._persistence_setFetchGroup(
                new EntityFetchGroup('id', 'version', 'deleteTs', 'orderLines'))
        line1 = new OrderLine(order: order1)
        order1.orderLines = [line1]
        line2 = new OrderLine(order: order2)
        order2.orderLines = [line2]

        context.merge(order1)
        context.merge(order2)

        then:

        def merged42 = context.find(Order, order1.id)
        merged42.orderLines.size() == 1
        merged42.orderLines[0] == line2
        !merged42.orderLines[0].is(line2)
    }

    def "property change events on commit"(orderId, line1Id, line2Id) {
        DataContext context = factory.createDataContext()

        Order order1 = new Order(id: orderId, number: '1')
        OrderLine line1 = new OrderLine(id: line1Id, quantity: 1, order: order1)
        OrderLine line2 = new OrderLine(id: line2Id, quantity: 2, order: order1)
        order1.orderLines = [line1, line2]

        def mergedOrder = context.merge(order1)

        Map<String, Integer> events = [:]
        Instance.PropertyChangeListener listener = new Instance.PropertyChangeListener() {
            @Override
            void propertyChanged(Instance.PropertyChangeEvent e) {
                events.compute(e.property, { k, v -> v == null ? 1 : v + 1 })
            }
        }
        mergedOrder.addPropertyChangeListener(listener)

        when:

        context.commit()
        println 'After commit: ' + events

        then:

        def order11 = context.find(Order, order1.id)
        order11.version == 1
        def line11 = context.find(OrderLine, line1.id)
        line11.version == 1
        def line21 = context.find(OrderLine, line2.id)
        line21.version == 1

        events['version'] == 1
        events['createTs'] == 1
        events['number'] == null
        events['orderLines'] == 1

        where:

        orderId << [uuid(0), uuid(1), uuid(2)]
        line1Id << [uuid(1), uuid(0), uuid(1)]
        line2Id << [uuid(2), uuid(2), uuid(0)]
    }

    def "system state should not be merged for non root entities"() {
        DataContext context = factory.createDataContext()

        Order order1 = new Order(amount: 1)
        Order order2 = new Order(id: order1.id)
        OrderLine line1 = new OrderLine(quantity: 1, order: order2)
        order1.orderLines = [line1]

        when: "parent entity has system state and child entity has link to the object without system state"

        def securityState = new SecurityState()
        BaseEntityInternalAccess.setSecurityState(order1, securityState)
        BaseEntityInternalAccess.setNew(order1, true)
        context.merge(order1)

        order2.amount = 4
        BaseEntityInternalAccess.setNew(order2, false)
        context.merge(line1)

        then:

        def orderInContext = context.find(Order, order1.id)
        orderInContext.amount == 4
        BaseEntityInternalAccess.getSecurityState(orderInContext).is(securityState)
        BaseEntityInternalAccess.isNew(orderInContext)

    }

    private UUID uuid(int val) {
        new UUID(val, 0)
    }

    private void makeDetached(def entity) {
        entityStates.makeDetached(entity)
    }
}