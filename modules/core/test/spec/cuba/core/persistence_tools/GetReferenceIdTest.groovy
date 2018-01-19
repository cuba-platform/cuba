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

package spec.cuba.core.persistence_tools

import com.haulmont.cuba.core.PersistenceTools
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.core.global.ViewRepository
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testmodel.sales.Order
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class GetReferenceIdTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private PersistenceTools persistenceTools

    private Customer customer1
    private Order order1
    private Order order2

    void setup() {
        cont.persistence().runInTransaction({ em ->
            customer1 = cont.metadata().create(Customer)
            customer1.name = 'a customer'
            em.persist(customer1)

            order1 = cont.metadata().create(Order)
            order1.setNumber('1')
            order1.setCustomer(customer1)
            em.persist(order1)

            order2 = cont.metadata().create(Order)
            order2.setNumber('2')
            em.persist(order2)
        })
        persistenceTools = AppBeans.get(PersistenceTools)
    }

    void cleanup() {
        cont.deleteRecord(order1, order2, customer1)
    }

    def "get existing reference id"() {
        def order
        def refId = null

        when:

        def tx = cont.persistence().createTransaction()
        try {
            order = cont.persistence().getEntityManager().find(Order, order1.id)
            refId = persistenceTools.getReferenceId(order, 'customer')
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        refId.loaded
        refId.value == customer1.id
    }

    def "get existing not loaded reference id"() {
        def order
        def refId = null

        when:

        def tx = cont.persistence().createTransaction()
        try {
            def view = AppBeans.get(ViewRepository).getView(Order, View.LOCAL)
            view.setLoadPartialEntities(true)

            order = cont.persistence().getEntityManager().find(Order, order1.id, view)
            refId = persistenceTools.getReferenceId(order, 'customer')
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        !refId.loaded

        when:

        refId.value

        then:

        thrown(IllegalStateException)
    }

    def "get null reference id"() {
        def order
        def refId = null

        when:

        def tx = cont.persistence().createTransaction()
        try {
            order = cont.persistence().getEntityManager().find(Order, order2.id)
            refId = persistenceTools.getReferenceId(order, 'customer')
            tx.commit()
        } finally {
            tx.end()
        }

        then:

        refId.loaded
        refId.value == null
    }
}
