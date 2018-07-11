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

package spec.cuba.core.data_events

import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TransactionalDataManager
import com.haulmont.cuba.core.app.events.EntityChangedEvent
import com.haulmont.cuba.core.entity.contracts.Id
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.testmodel.sales_1.Order
import com.haulmont.cuba.testmodel.sales_1.OrderLine
import com.haulmont.cuba.testmodel.sales_1.Product
import com.haulmont.cuba.testmodel.sales_1.TestEntityChangedEventListener
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityChangedEventTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private TestEntityChangedEventListener listener
    private Events events
    private DataManager dataManager
    private TransactionalDataManager txDataManager
    private Metadata metadata
    private EntityStates entityStates

    void setup() {
        listener = AppBeans.get(TestEntityChangedEventListener)
        listener.entityChangedEvents.clear()

        metadata = cont.metadata()
        events = AppBeans.get(Events)
        dataManager = AppBeans.get(DataManager)
        txDataManager = AppBeans.get(TransactionalDataManager)
        entityStates = AppBeans.get(EntityStates)

        AppBeans.get(EntityLog)

        listener.clear()
    }

    void cleanup() {
        listener.clear()
    }

    def "create/update/delete entity"() {

        Order order = metadata.create(Order)
        order.setNumber('111')
        order.setAmount(10)

        when:

        Order order1 = dataManager.commit(order)

        then:

        listener.entityChangedEvents.size() == 2

        !listener.entityChangedEvents[0].committedToDb
        listener.entityChangedEvents[1].committedToDb

        listener.entityChangedEvents[0].event.getEntityId().value == order.id
        listener.entityChangedEvents[1].event.getEntityId().value == order.id

        listener.entityChangedEvents[0].event.getType() == EntityChangedEvent.Type.CREATED
        listener.entityChangedEvents[1].event.getType() == EntityChangedEvent.Type.CREATED

        when:

        listener.clear()

        order1.setAmount(20)
        Order order2 = dataManager.commit(order1)

        then:

        listener.entityChangedEvents.size() == 2

        listener.entityChangedEvents[0].event.getEntityId().value == order.id
        listener.entityChangedEvents[1].event.getEntityId().value == order.id

        listener.entityChangedEvents[0].event.getType() == EntityChangedEvent.Type.UPDATED
        listener.entityChangedEvents[1].event.getType() == EntityChangedEvent.Type.UPDATED

        listener.entityChangedEvents[0].event.getChanges().attributes.contains('amount')
        listener.entityChangedEvents[0].event.getChanges().getOldValue('amount') == 10
        listener.entityChangedEvents[1].event.getChanges().attributes.contains('amount')
        listener.entityChangedEvents[1].event.getChanges().getOldValue('amount') == 10

        when:

        listener.clear()

        Order order3 = dataManager.remove(order2)

        then:

        listener.entityChangedEvents.size() == 2

        listener.entityChangedEvents[0].event.getEntityId().value == order.id
        listener.entityChangedEvents[1].event.getEntityId().value == order.id

        listener.entityChangedEvents[0].event.getType() == EntityChangedEvent.Type.DELETED
        listener.entityChangedEvents[1].event.getType() == EntityChangedEvent.Type.DELETED

        cleanup:

        cont.deleteRecord(order)
    }

    def "old value of collection attribute"() {

        Order order1 = new Order(number: '111', amount: 100)
        Product product1 = new Product(name: 'abc', quantity: 1000)
        Product product2 = new Product(name: 'def', quantity: 1000)
        OrderLine orderLine11 = new OrderLine(order: order1, product: product1, quantity: 10)
        OrderLine orderLine12 = new OrderLine(order: order1, product: product2, quantity: 20)

        EntitySet committed = dataManager.commit(order1, orderLine11, orderLine12, product1, product2)
        Order order2 = committed.get(order1)
        OrderLine orderLine2 = committed.get(orderLine11)

        listener.clear()

        when:

        order2.orderLines.remove(orderLine2)
        Transaction tx = txDataManager.transactions().create()
        try {
            txDataManager.save(order2)
            txDataManager.remove(orderLine2)
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        listener.entityChangedEvents[0].event.getEntityId().value == order2.id

        Collection oldLines = listener.entityChangedEvents[0].event.changes.getOldValue('orderLines')
        oldLines.containsAll([Id.of(orderLine11), Id.of(orderLine12)])

        cleanup:

        cont.deleteRecord(orderLine11, orderLine12, product1, product2, order1)
    }
}
