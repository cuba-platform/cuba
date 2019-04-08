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

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TransactionalDataManager
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType
import com.haulmont.cuba.core.app.events.EntityChangedEvent
import com.haulmont.cuba.core.entity.Category
import com.haulmont.cuba.core.entity.CategoryAttribute
import com.haulmont.cuba.core.entity.ReferenceToEntity
import com.haulmont.cuba.core.entity.contracts.Id
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.testmodel.sales_1.*
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.LocalDate

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

    private TestEntityChangedEventListener.Info beforeCommit() {
        return listener.entityChangedEvents[0]
    }

    private TestEntityChangedEventListener.Info afterCommit() {
        return listener.entityChangedEvents[1]
    }

    def "create/update/delete entity"() {

        Order order = metadata.create(Order)
        order.setNumber('111')
        order.setAmount(10)

        when:

        Order order1 = dataManager.commit(order)

        then:

        listener.entityChangedEvents.size() == 2

        !beforeCommit().committedToDb
        afterCommit().committedToDb

        beforeCommit().event.getEntityId().value == order.id
        afterCommit().event.getEntityId().value == order.id

        beforeCommit().event.getType() == EntityChangedEvent.Type.CREATED
        afterCommit().event.getType() == EntityChangedEvent.Type.CREATED

        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.isChanged('amount')
        !beforeCommit().event.changes.isChanged('date')
        !beforeCommit().event.changes.isChanged('customer')

        beforeCommit().event.changes.getOldValue('amount') == null

        when:

        listener.clear()

        order1.setAmount(20)
        Order order2 = dataManager.commit(order1)

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.getEntityId().value == order.id
        afterCommit().event.getEntityId().value == order.id

        beforeCommit().event.getType() == EntityChangedEvent.Type.UPDATED
        afterCommit().event.getType() == EntityChangedEvent.Type.UPDATED

        beforeCommit().event.getChanges().attributes.contains('amount')
        beforeCommit().event.getChanges().getOldValue('amount') == 10
        afterCommit().event.getChanges().attributes.contains('amount')
        afterCommit().event.getChanges().getOldValue('amount') == 10

        !beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.isChanged('amount')

        beforeCommit().event.changes.getOldValue('amount') == 10

        when:

        listener.clear()

        Order order3 = dataManager.remove(order2)

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.getEntityId().value == order.id
        afterCommit().event.getEntityId().value == order.id

        beforeCommit().event.getType() == EntityChangedEvent.Type.DELETED
        afterCommit().event.getType() == EntityChangedEvent.Type.DELETED

        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.isChanged('amount')
        beforeCommit().event.changes.isChanged('date')
        beforeCommit().event.changes.isChanged('customer')

        beforeCommit().event.changes.getOldValue('number') == '111'
        beforeCommit().event.changes.getOldValue('amount') == 20
        beforeCommit().event.changes.getOldValue('date') == null
        beforeCommit().event.changes.getOldValue('customer') == null

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

    def "dynamic attributes"() {

        Category category = new Category(name: 'order', entityType: 'sales1$Order')
        CategoryAttribute ca1 = new CategoryAttribute(name: 'dynAttr1', code: 'dynAttr1', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.STRING, defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca2 = new CategoryAttribute(name: 'dynAttr2', code: 'dynAttr2', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.INTEGER, defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca3 = new CategoryAttribute(name: 'dynAttr3', code: 'dynAttr3', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.DOUBLE, defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca4 = new CategoryAttribute(name: 'dynAttr4', code: 'dynAttr4', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.BOOLEAN, defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca5 = new CategoryAttribute(name: 'dynAttr5', code: 'dynAttr5', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.DATE, defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca6 = new CategoryAttribute(name: 'dynAttr6', code: 'dynAttr6', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.ENUMERATION, defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca7 = new CategoryAttribute(name: 'dynAttr7', code: 'dynAttr7', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.ENTITY, entityClass: 'com.haulmont.cuba.testmodel.sales_1.Customer', defaultEntity: new ReferenceToEntity())
        CategoryAttribute ca8 = new CategoryAttribute(name: 'dynAttr8', code: 'dynAttr8', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.DATE_WITHOUT_TIME, defaultEntity: new ReferenceToEntity())
        dataManager.commit(category, ca1, ca2, ca3, ca4, ca5, ca6, ca7, ca8)

        AppBeans.get(DynamicAttributesManagerAPI).loadCache()

        Order order = metadata.create(Order)
        order.setNumber('111')
        dataManager.commit(order)

        Customer cust1 = new Customer(name: 'cust1')
        Customer cust2 = new Customer(name: 'cust2')
        dataManager.commit(cust1, cust2)

        listener.clear()

        when:

        Order order1 = dataManager.load(Order).id(order.id).dynamicAttributes(true).one()
        order1.setNumber('222')
        order1.setValue('+dynAttr1', 'val1')
        order1.setValue('+dynAttr2', 10)
        order1.setValue('+dynAttr3', (double) 123.456)
        order1.setValue('+dynAttr4', true)
        order1.setValue('+dynAttr5', new SimpleDateFormat('yyyy-MM-dd').parse('2018-08-03'))
        order1.setValue('+dynAttr6', 'enumVal1')
        order1.setValue('+dynAttr7', cust1)
        order1.setValue('+dynAttr8', LocalDate.of(2018, 8, 3))
        dataManager.commit(order1)

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.getEntityId().value == order.id
        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.isChanged('+dynAttr1')
        beforeCommit().event.changes.getOldValue('+dynAttr1') == null
        beforeCommit().event.changes.isChanged('+dynAttr2')
        beforeCommit().event.changes.getOldValue('+dynAttr2') == null
        beforeCommit().event.changes.isChanged('+dynAttr3')
        beforeCommit().event.changes.getOldValue('+dynAttr3') == null
        beforeCommit().event.changes.isChanged('+dynAttr4')
        beforeCommit().event.changes.getOldValue('+dynAttr4') == null
        beforeCommit().event.changes.isChanged('+dynAttr5')
        beforeCommit().event.changes.getOldValue('+dynAttr5') == null
        beforeCommit().event.changes.isChanged('+dynAttr6')
        beforeCommit().event.changes.getOldValue('+dynAttr6') == null
        beforeCommit().event.changes.isChanged('+dynAttr7')
        beforeCommit().event.changes.getOldValue('+dynAttr7') == null
        beforeCommit().event.changes.isChanged('+dynAttr8')
        beforeCommit().event.changes.getOldValue('+dynAttr8') == null

        listener.clear()

        when:

        Order order2 = dataManager.load(Order).id(order.id).dynamicAttributes(true).one()
        order2.setNumber('333')
        order2.setValue('+dynAttr1', 'val2')
        order2.setValue('+dynAttr2', 20)
        order2.setValue('+dynAttr3', (double) 7.89)
        order2.setValue('+dynAttr4', false)
        order2.setValue('+dynAttr5', new SimpleDateFormat('yyyy-MM-dd').parse('2018-08-04'))
        order2.setValue('+dynAttr6', 'enumVal2')
        order2.setValue('+dynAttr7', cust2)
        order2.setValue('+dynAttr8', LocalDate.of(2018, 8, 4))
        dataManager.commit(order2)

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.getEntityId().value == order.id
        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.getOldValue('+dynAttr1') == 'val1'
        beforeCommit().event.changes.getOldValue('+dynAttr2') == 10
        beforeCommit().event.changes.getOldValue('+dynAttr3') == 123.456
        beforeCommit().event.changes.getOldValue('+dynAttr4') == true
        beforeCommit().event.changes.getOldValue('+dynAttr5') == new SimpleDateFormat('yyyy-MM-dd').parse('2018-08-03')
        beforeCommit().event.changes.getOldValue('+dynAttr6') == 'enumVal1'
        beforeCommit().event.changes.getOldValue('+dynAttr7') == Id.of(cust1)
        beforeCommit().event.changes.getOldValue('+dynAttr8') == LocalDate.of(2018, 8, 3)

        listener.clear()

        when:

        Order order3 = dataManager.load(Order).id(order.id).dynamicAttributes(true).one()
        dataManager.remove(order3)

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.getEntityId().value == order.id
        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.getOldValue('+dynAttr1') == 'val2'
        beforeCommit().event.changes.getOldValue('+dynAttr2') == 20
        beforeCommit().event.changes.getOldValue('+dynAttr3') == 7.89
        beforeCommit().event.changes.getOldValue('+dynAttr4') == false
        beforeCommit().event.changes.getOldValue('+dynAttr5') == new SimpleDateFormat('yyyy-MM-dd').parse('2018-08-04')
        beforeCommit().event.changes.getOldValue('+dynAttr6') == 'enumVal2'
        beforeCommit().event.changes.getOldValue('+dynAttr7') == Id.of(cust2)
        beforeCommit().event.changes.getOldValue('+dynAttr8') == LocalDate.of(2018, 8, 4)

        cleanup:

        new QueryRunner(cont.persistence().getDataSource()).update("delete from SYS_ATTR_VALUE")
        cont.deleteRecord(ca1, ca2, ca3, ca4, ca5, ca6, ca7, ca8, category, cust1, cust2, order)
    }

    def "dynamic attributes in TransactionalDataManager"() {

        Category category = new Category(name: 'order', entityType: 'sales1$Order')
        CategoryAttribute ca1 = new CategoryAttribute(name: 'dynAttr1', code: 'dynAttr1', category: category, categoryEntityType: 'sales1$Order', dataType: PropertyType.STRING, defaultEntity: new ReferenceToEntity())
        dataManager.commit(category, ca1)

        AppBeans.get(DynamicAttributesManagerAPI).loadCache()

        Order order = metadata.create(Order)
        order.setNumber('111')
        dataManager.commit(order)

        listener.clear()

        when: "set initial value to dynamic attributes"

        def tx = txDataManager.transactions().create()
        try {
            Order order1 = txDataManager.load(Order).id(order.id).dynamicAttributes(true).one()
            order1.setNumber('222')
            order1.setValue('+dynAttr1', 'val1')
            txDataManager.save(order1)
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.getEntityId().value == order.id
        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.isChanged('+dynAttr1')
        beforeCommit().event.changes.getOldValue('+dynAttr1') == null

        listener.clear()

        when: "update dynamic attributes"

        def tx2 = txDataManager.transactions().create()
        try {
            Order order2 = txDataManager.load(Order).id(order.id).dynamicAttributes(true).one()
            order2.setNumber('333')
            order2.setValue('+dynAttr1', 'val2')
            txDataManager.save(order2)
            tx2.commit()
        } finally {
            tx2.end()
        }

        then:

        listener.entityChangedEvents.size() == 2

        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.getOldValue('+dynAttr1') == 'val1'

        listener.clear()

        when: "update dynamic attributes 2 times"

        def tx3 = txDataManager.transactions().create()
        try {
            Order order3 = txDataManager.load(Order).id(order.id).dynamicAttributes(true).one()
            order3.setNumber('3331')
            order3.setValue('+dynAttr1', 'val31')
            order3 = txDataManager.save(order3)

            order3.setNumber('3332')
            order3.setValue('+dynAttr1', 'val32')
            txDataManager.save(order3)

            tx3.commit()
        } finally {
            tx3.end()
        }

        then:

        listener.entityChangedEvents.size() == 4

        // before commit
        listener.entityChangedEvents[0].event.changes.getOldValue('+dynAttr1') == 'val2'
        listener.entityChangedEvents[1].event.changes.getOldValue('+dynAttr1') == 'val31'

        // after commit
        listener.entityChangedEvents[2].event.changes.getOldValue('+dynAttr1') == 'val2'
        listener.entityChangedEvents[3].event.changes.getOldValue('+dynAttr1') == 'val31'

        listener.clear()

        when: "remove entity"

        def tx4 = txDataManager.transactions().create()
        try {
            Order order4 = dataManager.load(Order).id(order.id).dynamicAttributes(true).one()
            txDataManager.remove(order4)
            tx4.commit()
        } finally {
            tx4.end()
        }

        then:

        listener.entityChangedEvents.size() == 2
        beforeCommit().event.changes.isChanged('number')
        beforeCommit().event.changes.getOldValue('+dynAttr1') == 'val32'

        listener.clear()

        cleanup:

        new QueryRunner(cont.persistence().getDataSource()).update("delete from SYS_ATTR_VALUE")
        cont.deleteRecord(ca1, category, order)
    }
}
