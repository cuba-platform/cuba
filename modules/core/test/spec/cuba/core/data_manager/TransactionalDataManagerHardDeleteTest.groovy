/*
 * Copyright (c) 2008-2019 Haulmont.
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


import com.haulmont.cuba.core.TransactionalDataManager
import com.haulmont.cuba.core.entity.contracts.Id
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class TransactionalDataManagerHardDeleteTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    DataManager dm
    TransactionalDataManager tdm

    void setup() {
        dm = AppBeans.get(DataManager)
        tdm = AppBeans.get(TransactionalDataManager)
        AppBeans.get(EntityLog)
    }

    def "hard delete of soft-deleted entity"() {

        def customer = dm.create(Customer)
        def customer1  = dm.commit(customer)

        boolean sdBefore = true, sdAfter = true

        when:

        def tx = tdm.transactions().create()
        try {
            sdBefore = cont.persistence().getEntityManager().isSoftDeletion()

            tdm.remove(customer1, false)

            sdAfter = cont.persistence().getEntityManager().isSoftDeletion()

            tx.commit()
        } finally {
            tx.end()
        }

        then:

        !dm.load(Id.of(customer1)).softDeletion(false).optional().isPresent()
        sdBefore
        sdAfter
    }
}
