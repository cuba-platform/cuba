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

    void setup() {
        dataManager = AppBeans.get(DataManager)
    }

    def "usage"() {

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
}
