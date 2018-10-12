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

import com.haulmont.cuba.gui.model.CollectionPropertyContainer
import com.haulmont.cuba.gui.model.InstanceContainer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import spec.cuba.web.WebSpec

class CollectionPropertyContainerTest extends WebSpec {

    private Order order
    private OrderLine orderLine1
    private OrderLine orderLine2

    private InstanceContainer<Order> orderCt
    private CollectionPropertyContainer<OrderLine> linesCt

    @Override
    void setup() {
        orderCt = dataContextFactory.createInstanceContainer(Order)
        linesCt = dataContextFactory.createCollectionContainer(OrderLine, orderCt, 'orderLines')

        order = new Order(number: '111', orderLines: [])
        orderLine1 = new OrderLine(order: this.order, quantity: 1)
        orderLine2 = new OrderLine(order: this.order, quantity: 2)
        this.order.orderLines.addAll([this.orderLine1, this.orderLine2])
    }

    def "property reflects changes"() {

        orderCt.setItem(order)
        linesCt.setItems(order.orderLines)

        when:

        linesCt.getMutableItems().remove(orderLine2)

        then:

        order.orderLines.contains(orderLine1)
        !order.orderLines.contains(orderLine2)
    }

    def "property does not reflect changes"() {

        orderCt.setItem(order)
        linesCt.setItems(order.orderLines)

        when:

        linesCt.getDisconnectedItems().remove(orderLine2)

        then:

        order.orderLines.containsAll([orderLine1, orderLine2])
    }
}
