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
import com.haulmont.cuba.core.entity.KeyValueEntity
import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader
import com.haulmont.cuba.gui.model.KeyValueContainer
import com.haulmont.cuba.web.testmodel.sales.Customer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.WebSpec

class KeyValueContainersTest extends WebSpec {

    private Customer customer1
    private Order order1

    @Override
    void setup() {
        customer1 = new Customer(name: 'customer1')
        order1 = new Order(number: '111', customer: customer1, amount: 100)
    }

    def "load collection"() {

        KeyValueCollectionContainer container = dataContextFactory.createKeyValueCollectionContainer()
        container.addProperty('custName').addProperty('amount')

        KeyValueCollectionLoader loader = dataContextFactory.createKeyValueCollectionLoader()
        loader.setContainer(container)
        loader.setQuery('select o.customer.name, sum(o.amount) from test$Order o group by o.customer.name')

        TestServiceProxy.mock(DataService, Mock(DataService) {
            loadValues(_) >> {
                KeyValueEntity entity = new KeyValueEntity()
                entity.setValue('custName', 'customer1')
                entity.setValue('amount', 100)
                [entity]
            }
        })

        when:

        loader.load()

        then:

        container.items[0].getValue('custName') == 'customer1'
        container.items[0].getValue('amount') == 100

        container.items[0].getMetaClass().getProperty('custName') != null
        container.items[0].getMetaClass().getProperty('amount') != null
    }

    def "binding"() {

        KeyValueContainer container = dataContextFactory.createKeyValueContainer()
        container.addProperty('custName').addProperty('amount')

        TextField field1 = componentsFactory.createComponent(TextField)
        field1.setValueSource(new ContainerValueSource(container, 'custName'))

        TextField field2 = componentsFactory.createComponent(TextField)
        field2.setValueSource(new ContainerValueSource(container, 'custName'))

        KeyValueEntity entity = new KeyValueEntity()
        entity.setValue('custName', 'customer1')
        entity.setValue('amount', 100)

        when:

        container.setItem(entity)

        then:

        field1.getValue() == 'customer1'
        field2.getValue() == 'customer1'

        when:

        field1.setValue('changed')

        then:

        field2.getValue() == 'changed'
        entity.getValue('custName') == 'changed'
    }
}
