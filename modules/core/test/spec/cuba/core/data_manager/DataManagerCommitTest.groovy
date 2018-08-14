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

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.EntitySet
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testmodel.sales.Order
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DataManagerCommitTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataManager dataManager
    private EntityStates entityStates

    void setup() {
        dataManager = AppBeans.get(DataManager)
        entityStates = AppBeans.get(EntityStates)
    }

    def "usage of returned entities"() {

        Customer customer = new Customer(name: 'Smith')
        Order order = new Order(number: '111', customer: customer)

        def cc = new CommitContext().addInstanceToCommit(customer).addInstanceToCommit(order)
        expect:

        EntitySet committed = dataManager.commit(cc)

        def customer1 = committed.get(Customer, customer.id)
        customer1 == customer

        def customer2 = dataManager.commit(cc).get(customer)
        customer2 == customer

        cleanup:

        cont.deleteRecord(order, customer)
    }

    def "usage of patch object as reference"() {

        Customer customer = new Customer(name: 'Smith')
        Order order = new Order(number: '111')

        Order order1 = dataManager.commit(customer, order).get(order)

        when:

        entityStates.makePatch(customer)
        order1.customer = customer
        Order order2 = dataManager.commit(order1)

        then:

        order2.customer == customer
        order2.customer.version > 0
        order2.customer.name == customer.name

        cleanup:

        cont.deleteRecord(order, customer)
    }

    def "usage of patch object to remove by id"() {

        Customer customer = new Customer(name: 'Smith')
        dataManager.commit(customer)

        when:

        Customer customer1 = new Customer(id: customer.id)
        entityStates.makePatch(customer1)

        dataManager.remove(customer1)

        Customer customer2 = dataManager.load(Customer).id(customer.id).softDeletion(false).one()

        then:

        customer2.isDeleted()
        customer2.name == customer.name

        cleanup:

        cont.deleteRecord(customer)
    }
}
