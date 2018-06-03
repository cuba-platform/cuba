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

import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource
import com.haulmont.cuba.gui.model.InstanceContainer
import com.haulmont.cuba.web.testmodel.sales.Customer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import spec.cuba.web.WebSpec

class ContainerBindingToPathTest extends WebSpec {

    private Customer customer1
    private Customer customer2
    private Order order1
    private Order order2
    private OrderLine line1
    private OrderLine line2
    private InstanceContainer<Order> orderCont
    private InstanceContainer<OrderLine> lineCont
    private TextField field1
    private TextField field2

    @Override
    void setup() {
        customer1 = new Customer(name: 'customer1')
        customer2 = new Customer(name: 'customer2')
        order1 = new Order(number: '111', customer: customer1)
        order2 = new Order(number: '222', customer: customer2)
        line1 = new OrderLine(order: order1, quantity: 10)
        line2 = new OrderLine(order: order2, quantity: 20)

        orderCont = dataContextFactory.createInstanceContainer(Order)
        lineCont = dataContextFactory.createInstanceContainer(OrderLine)

        field1 = componentsFactory.createComponent(TextField)
        field2 = componentsFactory.createComponent(TextField)
    }

    def "binding to property path"() {

        field1.setValueSource(new ContainerValueSource(orderCont, 'customer.name'))
        field2.setValueSource(new ContainerValueSource(orderCont, 'customer.name'))


        when:

        orderCont.item = order1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        field1.value = 'customer11'

        then:

        customer1.name == 'customer11'

        and:

        field2.value == 'customer11'

        when:

        customer1.name = 'customer111'

        then:

        field1.value == 'customer111'
        field2.value == 'customer111'
    }

    def "binding to property path - change root item"() {

        field1.setValueSource(new ContainerValueSource(orderCont, 'customer.name'))
        field2.setValueSource(new ContainerValueSource(orderCont, 'customer.name'))

        when:

        orderCont.item = order1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        orderCont.item = order2

        then:

        field1.value == 'customer2'
        field2.value == 'customer2'
    }

    def "binding to property path - change leaf item"() {

        field1.setValueSource(new ContainerValueSource(orderCont, 'customer.name'))
        field2.setValueSource(new ContainerValueSource(orderCont, 'customer.name'))

        when:

        orderCont.item = order1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        order1.customer = customer2

        then:

        field1.value == 'customer2'
        field2.value == 'customer2'
    }

    def "binding to deep property path"() {

        field1.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))
        field2.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))

        when:

        lineCont.item = line1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        field1.value = 'customer11'

        then:

        customer1.name == 'customer11'

        and:

        field2.value == 'customer11'

        when:

        customer1.name = 'customer111'

        then:

        field1.value == 'customer111'
        field2.value == 'customer111'

        when: "change root item"

        lineCont.item = line2

        then:

        field1.value == 'customer2'
        field2.value == 'customer2'

        when: "change intermediate item"

        line2.order = order1

        then:

        field1.value == 'customer111'
        field2.value == 'customer111'

        when: "change leaf item"

        order1.customer = customer2

        then:

        field1.value == 'customer2'
        field2.value == 'customer2'

    }

    def "binding to deep property path - set leaf item null"() {

        field1.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))
        field2.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))

        when:

        lineCont.item = line1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        order1.customer = null

        then:

        field1.value == null
        field2.value == null
    }

    def "binding to deep property path - set intermediate item null"() {

        field1.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))
        field2.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))

        when:

        lineCont.item = line1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        line1.order = null

        then:

        field1.value == null
        field2.value == null
    }

    def "binding to deep property path - set root item null"() {

        field1.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))
        field2.setValueSource(new ContainerValueSource(this.lineCont, 'order.customer.name'))

        when:

        lineCont.item = line1

        then:

        field1.value == 'customer1'
        field2.value == 'customer1'

        when:

        lineCont.item = null

        then:

        field1.value == null
        field2.value == null
    }
}
