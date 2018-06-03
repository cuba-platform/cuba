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

import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.gui.model.CollectionContainer
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.gui.model.InstanceContainer
import com.haulmont.cuba.gui.model.InstanceLoader
import com.haulmont.cuba.web.testmodel.sales.Customer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.Product
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.WebSpec

import static com.haulmont.cuba.client.testsupport.TestSupport.reserialize

class CompositionTest extends WebSpec {

    private Customer customer1
    private Order order1
    private Product product11, product12
    private OrderLine orderLine11, orderLine12

    class OrderScreen {
        DataContext dataContext
        InstanceContainer<Order> orderCnt
        CollectionContainer<OrderLine> linesCnt

        def open(Order order) {
            mockLoad(order)

            dataContext = dataContextFactory.createDataContext()
            orderCnt = dataContextFactory.createInstanceContainer(Order)
            linesCnt = dataContextFactory.createCollectionContainer(OrderLine)
            orderCnt.addItemChangeListener { e ->
                linesCnt.setItems(e.item.orderLines)
            }

            InstanceLoader orderLdr = dataContextFactory.createInstanceLoader()
            orderLdr.setContainer(orderCnt)
            orderLdr.setDataContext(dataContext)

            orderLdr.entityId = order.id
            orderLdr.load()

            TestServiceProxy.clear()
        }
    }

    class LineScreen {
        DataContext dataContext
        InstanceContainer<OrderLine> lineCnt

        def open(OrderLine orderLine, DataContext parentContext) {
            mockLoad(orderLine)

            dataContext = dataContextFactory.createDataContext()
            if (parentContext != null)
                dataContext.setParent(parentContext)
            lineCnt = dataContextFactory.createInstanceContainer(OrderLine)

            if (!dataContext.contains(orderLine)) {
                InstanceLoader loader = dataContextFactory.createInstanceLoader()
                loader.setContainer(lineCnt)
                loader.setDataContext(dataContext)
                loader.setEntityId(orderLine.id)
                loader.load()
            } else {
                lineCnt.item = dataContext.find(OrderLine, orderLine.id)
            }

            TestServiceProxy.clear()
        }
    }

    class ProductScreen {
        DataContext dataContext
        InstanceContainer<Product> productCnt

        def open(Product product, DataContext parentContext) {
            mockLoad(product)

            dataContext = dataContextFactory.createDataContext()
            if (parentContext != null)
                dataContext.setParent(parentContext)
            productCnt = dataContextFactory.createInstanceContainer(Product)

            if (!dataContext.contains(product)) {
                InstanceLoader loader = dataContextFactory.createInstanceLoader()
                loader.setContainer(productCnt)
                loader.setDataContext(dataContext)
                loader.setEntityId(product.id)
                loader.load()
            } else {
                productCnt.item = dataContext.find(Product, product.id)
            }

            TestServiceProxy.clear()
        }
    }

    @Override
    void setup() {
        customer1 = makeSaved(new Customer(name: "customer-1"))

        order1 = makeSaved(new Order(number: "111", orderLines: []))
        order1.customer = customer1

        product11 = makeSaved(new Product(name: "product-11", price: 100))

        product12 = makeSaved(new Product(name: "product-12", price: 200))

        orderLine11 = makeSaved(new OrderLine(quantity: 10))
        orderLine11.order = order1
        orderLine11.product = product11

        orderLine12 = makeSaved(new OrderLine(quantity: 20))
        orderLine12.order = order1
        orderLine12.product = product11

        order1.orderLines.addAll([orderLine11, orderLine12])
    }

    private mockLoad(Entity loadedEntity) {
        TestServiceProxy.mock(DataService, Mock(DataService) {
            load(_) >> reserialize(loadedEntity)
        })
    }

    private Map<String, List> mockCommit() {
        def updated = []
        def removed = []
        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> { CommitContext cc ->
                updated.addAll(cc.commitInstances)
                removed.addAll(cc.removeInstances)
                TestServiceProxy.getDefault(DataService).commit(cc)
            }
        })
        [upd: updated, rem: removed]
    }

    private static <T> T makeSaved(T entity) {
        TestServiceProxy.getDefault(DataService).commit(new CommitContext().addInstanceToCommit(entity))[0] as T
    }

    def "zero composition"() {

        def orderScreen = new OrderScreen()

        when:

        orderScreen.open(order1)

        then:

        orderScreen.orderCnt.item == order1

        when:

        orderScreen.orderCnt.item.number = '222'
        def committed = mockCommit()
        orderScreen.dataContext.commit()

        then:

        committed.upd.size() == 1
    }

    def "one level of composition"() {

        EntitySerializationAPI entitySerialization = cont.getBean(EntitySerializationAPI.NAME, EntitySerializationAPI)

        def orderScreen = new OrderScreen()
        def orderLineScreen = new LineScreen()

        when:

        orderScreen.open(order1)

        then:

        orderScreen.orderCnt.item == order1
        orderScreen.linesCnt.items.size() == 2
        orderScreen.linesCnt.item == null

        when: "open edit screen for orderLine11"

        orderScreen.linesCnt.item = orderLine11
        orderLineScreen.open(orderScreen.linesCnt.item, orderScreen.dataContext)

        then:

        !orderScreen.dataContext.hasChanges()
        orderLineScreen.lineCnt.item == orderLine11
        orderLineScreen.lineCnt.item.quantity == 10

        when: "change orderLine11.quantity and commit child context"

        orderLineScreen.lineCnt.item.quantity = 11

        def childContextStateBeforeCommit = entitySerialization.toJson(orderLineScreen.dataContext.getAll())

        def committed = mockCommit()
        def modified = []
        orderLineScreen.dataContext.addPreCommitListener { e ->
            modified.addAll(e.modifiedInstances)
        }
        orderLineScreen.dataContext.commit()

        def childContextStateAfterCommit = entitySerialization.toJson(orderLineScreen.dataContext.getAll())

        then: "child context committed orderLine11 to parent"

        modified.contains(orderLine11)
        committed.upd.isEmpty()
        orderScreen.linesCnt.item.quantity == 11

        and: "child context has no changes anymore"

        !orderLineScreen.dataContext.hasChanges()

        and: "child context is exactly the same as before commit"

        childContextStateAfterCommit == childContextStateBeforeCommit

        when: "commit parent context"

        orderScreen.dataContext.commit()

        then: "orderLine11 committed to DataService"

        committed.upd.size() == 1
        committed.upd.contains(orderLine11)
        committed.upd.find { it == orderLine11 }.quantity == 11
    }

    def "two levels of composition"() {

        def orderScreen = new OrderScreen()
        def orderLineScreen = new LineScreen()
        def productScreen = new ProductScreen()

        when:

        orderScreen.open(order1)

        orderScreen.linesCnt.item = orderLine11
        orderLineScreen.open(orderScreen.linesCnt.item, orderScreen.dataContext)

        productScreen.open(orderScreen.linesCnt.item.product, orderLineScreen.dataContext)

        then:

        productScreen.productCnt.item == product11

        when:

        productScreen.productCnt.item.price = 101

        def committed = mockCommit()
        productScreen.dataContext.commit()

        then:

        committed.upd.isEmpty()

        when:

        orderLineScreen.lineCnt.item.quantity = 11
        orderLineScreen.dataContext.commit()

        then:

        committed.upd.isEmpty()

        when:

        orderScreen.dataContext.commit()

        then:

        committed.upd.size() == 2
        committed.upd.contains(product11)
        committed.upd.contains(orderLine11)
        committed.upd.find { it == product11}.price == 101
        committed.upd.find { it == orderLine11}.quantity == 11
    }

    def "one level of composition - repetitive edit"() {

        def orderScreen = new OrderScreen()
        def orderLineScreen = new LineScreen()

        orderScreen.open(order1)

        orderScreen.linesCnt.item = orderLine11
        orderLineScreen.open(orderScreen.linesCnt.item, orderScreen.dataContext)
        orderLineScreen.lineCnt.item.quantity = 11
        orderLineScreen.dataContext.commit()

        when: "open orderLineScreen second time"

        orderLineScreen.open(orderScreen.linesCnt.item, orderScreen.dataContext)

        then:

        orderLineScreen.lineCnt.item.quantity == 11

        when:

        orderLineScreen.lineCnt.item.quantity = 12

        def committed = mockCommit()
        orderLineScreen.dataContext.commit()
        orderScreen.dataContext.commit()

        then:

        committed.upd.size() == 1
        committed.upd.find { it == orderLine11}.quantity == 12
    }

    def "one level of composition - remove item"() {

        def orderScreen = new OrderScreen()
        def lineScreen = new LineScreen()

        orderScreen.open(order1)

        orderScreen.linesCnt.item = orderLine11
        lineScreen.open(orderScreen.linesCnt.item, orderScreen.dataContext)

        def committed = mockCommit()

        when: "remove OrderLine in lineScreen"

        lineScreen.dataContext.remove(lineScreen.lineCnt.item)
        lineScreen.dataContext.commit()

        then:

        committed.upd.isEmpty()
        committed.rem.isEmpty()

        when:

        orderScreen.dataContext.commit()

        then:

        committed.upd.isEmpty()
        committed.rem.size() == 1
        committed.rem.contains(orderLine11)
    }
}
