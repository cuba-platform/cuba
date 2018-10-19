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

package spec.cuba.web.components

import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource
import com.haulmont.cuba.gui.model.InstanceContainer
import com.haulmont.cuba.web.testmodel.sales.Customer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.Status
import spec.cuba.web.WebSpec

class TextFieldBindingToEntityTest extends WebSpec {

    private InstanceContainer<Customer> customerCt
    private InstanceContainer<Order> orderCt
    private customer
    private order
    private OrderLine orderLine1, orderLine2

    @Override
    void setup() {
        customer = new Customer(name: 'cust1', status: Status.OK)
        order = new Order(number: '111', customer: this.customer)
        orderLine1 = new OrderLine(order: order, quantity: 1)
        orderLine2 = new OrderLine(order: order, quantity: 2)
        order.orderLines = [orderLine1, orderLine2]
        customerCt = dataContextFactory.createInstanceContainer(Customer)
        orderCt = dataContextFactory.createInstanceContainer(Order)
    }

    def "single reference is displayed as its InstanceName"() {

        TextField textField = uiComponents.create(TextField)
        textField.setValueSource(new ContainerValueSource(orderCt, 'customer'))

        when:

        orderCt.setItem(order)

        then:

        textField.unwrap(com.vaadin.ui.TextField).getValue() == 'cust1'
        !textField.isEditable()

        when:

        order.customer = null

        then:

        textField.unwrap(com.vaadin.ui.TextField).getValue() == ''
        !textField.isEditable()
    }

    def "collection reference is displayed as comma-separated list of items InstanceName"() {

        TextField textField = uiComponents.create(TextField)
        textField.setValueSource(new ContainerValueSource(orderCt, 'orderLines'))

        when:

        orderCt.setItem(order)

        then:

        textField.unwrap(com.vaadin.ui.TextField).getValue() == metadataTools.getInstanceName(orderLine1) + ', ' + metadataTools.getInstanceName(orderLine2)
        !textField.isEditable()
    }

    def "enum attribute is displayed as its localized name"() {

        TextField textField = uiComponents.create(TextField)
        textField.setValueSource(new ContainerValueSource(customerCt, 'status'))

        when:

        customerCt.setItem(customer)

        then:

        textField.unwrap(com.vaadin.ui.TextField).getValue() == 'Okay'
        !textField.isEditable()

        when:

        customer.setStatus(Status.NOT_OK)

        then:

        textField.unwrap(com.vaadin.ui.TextField).getValue() == 'Not Okay'
    }
}
