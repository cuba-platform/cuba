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

package spec.cuba.core.persistence_tools

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.PersistenceTools
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testmodel.sales.Order
import com.haulmont.cuba.testmodel.sales.OrderLine
import com.haulmont.cuba.testmodel.sales.Status
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class GetOldValueTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private PersistenceTools persistenceTools

    private Persistence persistence = cont.persistence()
    private Metadata metadata = cont.metadata()

    private Customer customer1
    private Order order1
    private OrderLine orderLine1

    void setup() {
        persistence.runInTransaction({ em ->
            customer1 = metadata.create(Customer)
            customer1.name = 'a customer'
            em.persist(customer1)

            order1 = metadata.create(Order)
            order1.setNumber('1')
            order1.setCustomer(customer1)
            em.persist(order1)

            orderLine1 = metadata.create(OrderLine)
            orderLine1.product = "prod1"
            orderLine1.quantity = 10
            orderLine1.order = order1
            em.persist(orderLine1)
        })
        persistenceTools = AppBeans.get(PersistenceTools)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_ORDER_LINE')
        runner.update('delete from TEST_ORDER')
        runner.update('delete from TEST_CUSTOMER')
    }

    def "test not changed attribute"() {
        def order
        def oldValue = null

        when:

        persistence.runInTransaction { em ->
            order = persistence.getEntityManager().find(Order, order1.id)

            order.amount = 100
            oldValue = persistenceTools.getOldValue(order, 'number')
        }

        then:

        oldValue == '1'
    }

    def "test local attribute"() {
        def order
        def oldValue

        when:

        persistence.runInTransaction { em ->
            order = persistence.getEntityManager().find(Order, order1.id)

            order.amount = 100
            oldValue = persistenceTools.getOldValue(order, 'amount')
        }

        then:

        oldValue == null

        when:

        persistence.runInTransaction { em ->
            order = persistence.getEntityManager().find(Order, order1.id)

            order.amount = 200
            oldValue = persistenceTools.getOldValue(order, 'amount')
        }

        then:

        oldValue == 100
    }

    def "test local unfetched attribute"() {
        def order
        def oldValue

        def view = new View(Order).addProperty('number').setLoadPartialEntities(true)

        when:

        persistence.runInTransaction { em ->
            order = persistence.getEntityManager().find(Order, order1.id, view)

            order.amount = 100
            oldValue = persistenceTools.getOldValue(order, 'amount')
        }

        then:

        noExceptionThrown()
        oldValue == null

        when:

        persistence.runInTransaction { em ->
            order = persistence.getEntityManager().find(Order, order1.id, view)

            order.amount = 200
            oldValue = persistenceTools.getOldValue(order, 'amount')
        }

        then:

        oldValue == 100
    }

    def "test collection attribute"() {
        def order
        def oldValue

        def view = new View(Order).addProperty('orderLines', new View(OrderLine).addProperty('product'))

        when:

        persistence.runInTransaction { em ->
            order = persistence.getEntityManager().find(Order, order1.id, view)

            def orderLine = metadata.create(OrderLine)
            orderLine.product = "prod2"
            orderLine.order = order
            order.orderLines.add(orderLine)
            em.persist(orderLine)

            oldValue = persistenceTools.getOldValue(order, 'orderLines')
        }

        then:

        oldValue == [orderLine1]
    }

    def "test enum attribute"() {
        def customer
        def oldValue
        def oldEnumValue

        when:

        persistence.runInTransaction { em ->
            customer = persistence.getEntityManager().find(Customer, customer1.id)
            customer.setStatus(Status.OK)

            oldValue = persistenceTools.getOldValue(customer, 'status')
            oldEnumValue = persistenceTools.getOldEnumValue(customer, 'status')
        }

        then:

        oldValue == null
        oldEnumValue == null

        when:

        persistence.runInTransaction { em ->
            customer = persistence.getEntityManager().find(Customer, customer1.id)
            customer.setStatus(Status.NOT_OK)

            oldValue = persistenceTools.getOldValue(customer, 'status')
            oldEnumValue = persistenceTools.getOldEnumValue(customer, 'status')
        }

        then:

        oldValue == 'O'
        oldEnumValue == Status.OK

        when: "using getOldEnumValue for non-enum attribute"

        persistence.runInTransaction { em ->
            customer = persistence.getEntityManager().find(Customer, customer1.id)

            oldEnumValue = persistenceTools.getOldEnumValue(customer, 'name')
        }

        then: "return null"

        oldEnumValue == null
    }
}
