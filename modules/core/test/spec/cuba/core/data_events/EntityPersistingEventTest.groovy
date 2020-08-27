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

import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.testmodel.sales.Status
import com.haulmont.cuba.testmodel.sales_1.Customer
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityPersistingEventTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata
    private Persistence persistence
    private DataManager dataManager
    private Customer customer

    void setup() {
        metadata = cont.metadata()
        persistence = cont.persistence()
        dataManager = AppBeans.get(DataManager)
    }

    void cleanup() {
        if (customer != null) {
            cont.deleteRecord(customer)
        }
    }

    def "EntityPersistingEvent while merge entity"() {
        customer = metadata.create(Customer)
        customer.name = 'customer1'

        when:

        def tx = persistence.createTransaction()
        try {
            def em = persistence.entityManager

            em.merge(customer)

            tx.commit()
        } finally {
            tx.end()
        }

        Customer reloadedCustomer = dataManager.load(Customer)
                .id(customer.id)
                .optional()
                .orElse(null)

        then:

        reloadedCustomer.status == Status.OK
    }
}
