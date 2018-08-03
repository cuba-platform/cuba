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

import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TransactionalDataManager
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.core.sys.listener.EntityListenerManager
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testmodel.sales.Order
import com.haulmont.cuba.testmodel.sales.OrderLine
import com.haulmont.cuba.testmodel.sales.TestCustomerListenerBean
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import org.springframework.transaction.support.TransactionSynchronizationManager
import spock.lang.Shared
import spock.lang.Specification

class DataManagerTransactionalTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence
    private TransactionalDataManager txDataManager
    private ViewRepository viewRepository
    private View baseView
    private Metadata metadata
    private EntityStates entityStates

    void setup() {
        metadata = cont.metadata()
        persistence = cont.persistence()
        txDataManager = AppBeans.get(TransactionalDataManager)
        viewRepository = AppBeans.get(ViewRepository)
        baseView = viewRepository.getView(Customer, '_base')
        entityStates = AppBeans.get(EntityStates)
    }

    void cleanup() {
    }

    def "create and load in one transaction"() {

        EntityListenerManager entityListenerManager = AppBeans.get(EntityListenerManager)
        entityListenerManager.addListener(Customer, TestCustomerListenerBean)
        TestCustomerListenerBean.events.clear()

        Customer customer1 = metadata.create(Customer)
        customer1.name = 'Smith'

        Transaction tx = persistence.createTransaction()
        TransactionSynchronizationManager.setCurrentTransactionName("tx1")

        when:

        txDataManager.save(customer1)

//        dataManager.commit(new CommitContext(customer1).setJoinTransaction(true))

        then:

        println(">>>" + TestCustomerListenerBean.events)

        TestCustomerListenerBean.events.size() == 2
        TestCustomerListenerBean.events[0] == 'onAfterInsert: tx1'
        TestCustomerListenerBean.events[1] == 'onBeforeDetach: tx1'

        when:

        TestCustomerListenerBean.events.clear()

        Customer customer = txDataManager.load(Customer).id(customer1.id).one()

//        LoadContext<Customer> loadContext = LoadContext.create(Customer).setId(customer1.id)
//                .setJoinTransaction(true)
//        Customer customer = dataManager.load(loadContext)

        tx.commit()
        tx.end()

        then:

        println(">>>" + TestCustomerListenerBean.events)

        TestCustomerListenerBean.events.size() == 1
        TestCustomerListenerBean.events[0] == 'onBeforeDetach: tx1'

        customer == customer1

        cleanup:

        tx.end()
        cont.deleteRecord(customer1)

        entityListenerManager.removeListener(Customer, TestCustomerListenerBean)
    }

    def "create and then rollback transaction"() {
        Customer customer1 = metadata.create(Customer)
        customer1.name = 'Smith'

        when:

        Transaction tx = persistence.createTransaction()
        try {
            txDataManager.save(customer1)

//            dataManager.commit(new CommitContext(customer1).setJoinTransaction(true))
        } finally {
            tx.end()
        }

        then:

        !txDataManager.load(Customer).id(customer1.id).optional().isPresent()
    }

    def "create new returns detached entities"() {
        Customer customer1 = metadata.create(Customer)
        customer1.name = 'Smith'

        Transaction tx = persistence.createTransaction()

        when:

        Customer customer = txDataManager.save(customer1)

        then:

        !BaseEntityInternalAccess.isManaged(customer)
        BaseEntityInternalAccess.isDetached(customer)
        !BaseEntityInternalAccess.isNew(customer)

        cleanup:

        tx.end()
        cont.deleteRecord(customer1)
    }

    def "update returns detached entities"() {
        Customer customer1 = metadata.create(Customer)
        customer1.name = 'Smith'
        Customer customer2 = txDataManager.save(customer1)

        Transaction tx = persistence.createTransaction()

        when:

        customer2.name = 'Johns'
        Customer customer = txDataManager.save(customer2)

        then:

        customer.name == 'Johns'
        !BaseEntityInternalAccess.isManaged(customer)
        BaseEntityInternalAccess.isDetached(customer)
        !BaseEntityInternalAccess.isNew(customer)

        cleanup:

        tx.end()
        cont.deleteRecord(customer1)
    }

    def "load returns detached entities"() {
        Customer customer1 = new Customer(name: 'Smith')
        Order order1 = new Order(customer: customer1, number: '111')
        def orderLine11 = new OrderLine(order: order1, product: 'abc')
        def orderLine12 = new OrderLine(order: order1, product: 'def')
        txDataManager.save(customer1, order1, orderLine11, orderLine12)

        View orderView = new View(Order)
                .addProperty('number')
                .addProperty('customer', new View(Customer).addProperty('name'))
                .addProperty('orderLines', new View(OrderLine).addProperty('product'))

        Transaction tx = persistence.createTransaction()

        when:

        Order order = txDataManager.load(Order).id(order1.id).view(orderView).one()

        then:

        checkObjectGraph(order)

        cleanup:

        tx.end()
        cont.deleteRecord(orderLine11, orderLine12, order1, customer1)
    }

    def "load list returns detached entities"() {
        Customer customer1 = new Customer(name: 'Smith')
        Order order1 = new Order(customer: customer1, number: '111')
        def orderLine11 = new OrderLine(order: order1, product: 'abc')
        def orderLine12 = new OrderLine(order: order1, product: 'def')
        txDataManager.save(customer1, order1, orderLine11, orderLine12)

        View orderView = new View(Order)
                .addProperty('number')
                .addProperty('customer', new View(Customer).addProperty('name'))
                .addProperty('orderLines', new View(OrderLine).addProperty('product'))

        Transaction tx = persistence.createTransaction()

        when:

        List<Order> orders = txDataManager.load(Order)
                .query('select e from test$Order e where e.id = :id').parameter('id', order1.id)
                .view(orderView).list()

        then:

        checkObjectGraph(orders[0])

        cleanup:

        tx.end()
        cont.deleteRecord(orderLine11, orderLine12, order1, customer1)
    }

    private void checkObjectGraph(Order order) {
        assert !BaseEntityInternalAccess.isManaged(order)
        assert BaseEntityInternalAccess.isDetached(order)
        assert !BaseEntityInternalAccess.isNew(order)

        assert !BaseEntityInternalAccess.isManaged(order.customer)
        assert BaseEntityInternalAccess.isDetached(order.customer)
        assert !BaseEntityInternalAccess.isNew(order.customer)

        assert !BaseEntityInternalAccess.isManaged(order.orderLines[0])
        assert BaseEntityInternalAccess.isDetached(order.orderLines[0])
        assert !BaseEntityInternalAccess.isNew(order.orderLines[0])

        assert !BaseEntityInternalAccess.isManaged(order.orderLines[1])
        assert BaseEntityInternalAccess.isDetached(order.orderLines[1])
        assert !BaseEntityInternalAccess.isNew(order.orderLines[1])
    }
}
