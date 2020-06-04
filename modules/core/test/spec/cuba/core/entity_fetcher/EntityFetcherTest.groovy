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

package spec.cuba.core.entity_fetcher

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.entity.contracts.Id
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.core.global.ViewBuilder
import com.haulmont.cuba.core.sys.EntityFetcher
import com.haulmont.cuba.core.sys.persistence.CubaEntityFetchGroup
import com.haulmont.cuba.testmodel.many2many.Many2ManyA
import com.haulmont.cuba.testmodel.many2many.Many2ManyB
import com.haulmont.cuba.testmodel.many2many.Many2ManyRef
import com.haulmont.cuba.testmodel.not_persistent.CustomerWithNonPersistentRef
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testmodel.sales.Status
import com.haulmont.cuba.testsupport.TestContainer
import org.eclipse.persistence.queries.FetchGroupTracker
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityFetcherTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataManager dataManager
    private EntityFetcher entityFetcher
    private Persistence persistence
    private EntityStates entityStates

    void setup() {
        dataManager = AppBeans.get(DataManager)
        entityFetcher = AppBeans.get(EntityFetcher)
        entityStates = AppBeans.get(EntityStates)
        persistence = cont.persistence()
    }

    def "fetching entity with many-to-many collection containing detached instances"() {
        def ref = new Many2ManyRef(name: 'ref1')
        def b1 = new Many2ManyB()
        def a1 = new Many2ManyA(ref: ref, collectionOfB: [b1])
        dataManager.commit(ref, b1, a1)

        def loadedB1 = dataManager.load(Id.of(b1)).view { vb -> vb.addAll('collectionOfA')}.one()
        println(loadedB1)

        when:
        def a2 = new Many2ManyA(collectionOfB: [loadedB1])

        persistence.callInTransaction { em ->
            em.persist(a2)

            entityFetcher.fetch(a2, ViewBuilder.of(Many2ManyA).addAll('collectionOfB.collectionOfA.ref').build())
            return a2
        }

        then:
        noExceptionThrown()

        cleanup:
        QueryRunner runner = new QueryRunner(persistence.getDataSource())
        runner.update('delete from TEST_MANY2MANY_AB_LINK where A_ID = ?', a1.id)
        runner.update('delete from TEST_MANY2MANY_AB_LINK where A_ID = ?', a2.id)

        cont.deleteRecord(a1, a2, b1, ref)
    }

    def "fetching entity with non-persistent reference"() {
        // setup the entity like it is stored in a custom datastore and linked as transient property
        def npCustomer = new Customer(status: Status.OK)
        entityStates.makeDetached(npCustomer)
        ((FetchGroupTracker) npCustomer)._persistence_setFetchGroup(new CubaEntityFetchGroup(['status']))

        def entity = new CustomerWithNonPersistentRef(
                name: 'c',
                customer: npCustomer
        )
        def view = ViewBuilder.of(CustomerWithNonPersistentRef).addAll('name', 'customer.name').build()

        when:
        def committed = dataManager.commit(entity, view)

        then:
        noExceptionThrown()
        committed == entity

        cleanup:
        cont.deleteRecord(entity)
    }
}
