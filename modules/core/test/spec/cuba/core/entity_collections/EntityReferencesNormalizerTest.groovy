/*
 * Copyright (c) 2008-2020 Haulmont.
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

package spec.cuba.core.entity_collections

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.sys.EntityReferencesNormalizer
import com.haulmont.cuba.testmodel.sales_1.Customer
import com.haulmont.cuba.testmodel.sales_1.Order
import com.haulmont.cuba.testmodel.sales_1.OrderLineA
import com.haulmont.cuba.testmodel.sales_1.Product
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityReferencesNormalizerTest extends Specification {

    @Shared @ClassRule
    TestContainer cont = TestContainer.Common.INSTANCE

    private EntityReferencesNormalizer normalizer

    void setup() {
        normalizer = AppBeans.get(EntityReferencesNormalizer)
    }

    def "update immediate to-one references"() {
        def customer1 = new Customer(name: 'cust')
        def customer2 = new Customer(id: customer1.id, name: 'cust')

        def order = new Order(number: '1', customer: customer2)

        def collection = [order, customer1]

        when:
        normalizer.updateReferences(collection)

        then:
        order.customer.is(customer1)
    }

    def "update deep to-one references"() {
        def product1 = new Product(name: 'product')
        def product2 = new Product(id: product1.id, name: 'product')

        def order = new Order(number: '1')
        def orderLineA = new OrderLineA(order: order, product: product2)
        order.orderLines = [orderLineA]

        def collection = [order, orderLineA, product1]

        when:
        normalizer.updateReferences(collection)

        then:
        order.orderLines[0].product.is(product1)
    }
}
