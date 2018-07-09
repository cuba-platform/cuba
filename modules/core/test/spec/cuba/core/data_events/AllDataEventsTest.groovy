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

import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TransactionalDataManager
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.testmodel.sales_1.Order
import com.haulmont.cuba.testmodel.sales_1.TestEntityChangedEventListener
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class AllDataEventsTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private TestEntityChangedEventListener listener
    private Events events
    private DataManager dataManager
    private TransactionalDataManager txDataManager
    private Metadata metadata
    private EntityStates entityStates
    private Order toDiscard

    void setup() {
        listener = AppBeans.get(TestEntityChangedEventListener)
        listener.entityChangedEvents.clear()

        metadata = cont.metadata()
        events = AppBeans.get(Events)
        dataManager = AppBeans.get(DataManager)
        txDataManager = AppBeans.get(TransactionalDataManager)
        entityStates = AppBeans.get(EntityStates)

        // warm up EntityLog, etc.
        toDiscard = metadata.create(Order)
        dataManager.commit(toDiscard)

        listener.clear()
    }

    void cleanup() {
        cont.deleteRecord(toDiscard)
        listener.clear()
    }

    def "create sequence"() {

        when:

        Order order = metadata.create(Order)
        order.number = '111'
        order.amount = 10
        dataManager.commit(order)

        then:

        int i = 0
        listener.allEvents[i++].message == 'JPA PrePersist'
        listener.allEvents[i++].message == 'BeforeInsertEntityListener'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'BeforeCommitTransactionListener'
        listener.allEvents[i++].message == 'AfterInsertEntityListener'
        listener.allEvents[i++].message == 'JPA PostPersist'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, CREATED'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, CREATED'
        listener.allEvents[i++].message == 'AfterCompleteTransactionListener'

        cleanup:

        cont.deleteRecord(order)
    }

    def "update sequence"() {

        Order order = metadata.create(Order)
        order.number = '111'
        order.amount = 10
        Order order1 = dataManager.commit(order)
        listener.clear()

        when:

        order1.setAmount(100)
        dataManager.commit(order1)

        then:

        int i = 0
        listener.allEvents[i++].message == 'BeforeAttachEntityListener'
        listener.allEvents[i++].message == 'JPA PostLoad'
        listener.allEvents[i++].message == 'BeforeUpdateEntityListener'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'BeforeCommitTransactionListener'
        listener.allEvents[i++].message == 'JPA PreUpdate'
        listener.allEvents[i++].message == 'AfterUpdateEntityListener'
        listener.allEvents[i++].message == 'JPA PostUpdate'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, UPDATED'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, UPDATED'
        listener.allEvents[i++].message == 'AfterCompleteTransactionListener'

        cleanup:

        cont.deleteRecord(order)
    }

    def "delete sequence"() {

        Order order = metadata.create(Order)
        order.number = '111'
        order.amount = 10
        Order order1 = dataManager.commit(order)
        listener.clear()

        when:

        dataManager.remove(order1)

        then:

        int i = 0
        listener.allEvents[i++].message == 'BeforeAttachEntityListener'
        listener.allEvents[i++].message == 'JPA PostLoad'
        listener.allEvents[i++].message == 'BeforeDeleteEntityListener'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'BeforeCommitTransactionListener'
        listener.allEvents[i++].message == 'JPA PreUpdate'
        listener.allEvents[i++].message == 'AfterDeleteEntityListener'
        listener.allEvents[i++].message == 'JPA PostUpdate'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, DELETED'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, DELETED'
        listener.allEvents[i++].message == 'AfterCompleteTransactionListener'

        cleanup:

        cont.deleteRecord(order)
    }

    def "create/update/delete in one transaction sequence"() {

        int i = 0
        Transaction tx = cont.persistence().createTransaction()

        when:

        Order order = metadata.create(Order)
        order.number = '111'
        order.amount = 10
        Order order1 = txDataManager.save(order)
        i = 0

        then:

        listener.allEvents[i++].message == 'JPA PrePersist'
        listener.allEvents[i++].message == 'BeforeInsertEntityListener'
        listener.allEvents[i++].message == 'AfterInsertEntityListener'
        listener.allEvents[i++].message == 'JPA PostPersist'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, CREATED'

        when:

        listener.clear()
        order1.setAmount(100)
        Order order2 = txDataManager.save(order1)
        i = 0

        then:

        listener.allEvents[i++].message == 'BeforeAttachEntityListener'
        listener.allEvents[i++].message == 'JPA PostLoad'
        listener.allEvents[i++].message == 'BeforeUpdateEntityListener'
        listener.allEvents[i++].message == 'JPA PreUpdate'
        listener.allEvents[i++].message == 'AfterUpdateEntityListener'
        listener.allEvents[i++].message == 'JPA PostUpdate'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, UPDATED'

        when:

        listener.clear()
        txDataManager.remove(order2)
        i = 0

        then:

        listener.allEvents[i++].message == 'BeforeAttachEntityListener'
        listener.allEvents[i++].message == 'JPA PostLoad'
        listener.allEvents[i++].message == 'BeforeDeleteEntityListener'
        listener.allEvents[i++].message == 'JPA PreUpdate'
        listener.allEvents[i++].message == 'AfterDeleteEntityListener'
        listener.allEvents[i++].message == 'JPA PostUpdate'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, DELETED'

        when:

        listener.clear()
        tx.commit()
        i = 0

        then:

        listener.allEvents[i++].message == 'BeforeCommitTransactionListener'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, CREATED'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, UPDATED'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, DELETED'
        listener.allEvents[i++].message == 'AfterCompleteTransactionListener'

        cleanup:

        tx.end()
        cont.deleteRecord(order)
    }

    def "load/update in one transaction sequence"() {

        int i = 0
        Transaction tx = cont.persistence().createTransaction()

        Order order = metadata.create(Order)
        order.number = '111'
        order.amount = 10
        dataManager.commit(order)
        listener.clear()

        when:

        Order order1 = txDataManager.load(Order).id(order.id).one()
        i = 0

        then:

        listener.allEvents[i++].message == 'JPA PostLoad'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'

        when:

        listener.clear()
        order1.setAmount(100)
        txDataManager.save(order1)
        i = 0

        then:

        listener.allEvents[i++].message == 'BeforeAttachEntityListener'
        listener.allEvents[i++].message == 'JPA PostLoad'
        listener.allEvents[i++].message == 'BeforeUpdateEntityListener'
        listener.allEvents[i++].message == 'JPA PreUpdate'
        listener.allEvents[i++].message == 'AfterUpdateEntityListener'
        listener.allEvents[i++].message == 'JPA PostUpdate'
        listener.allEvents[i++].message == 'BeforeDetachEntityListener'
        listener.allEvents[i++].message == 'EntityChangedEvent: beforeCommit, UPDATED'

        when:

        listener.clear()
        tx.commit()
        i = 0

        then:

        listener.allEvents[i++].message == 'BeforeCommitTransactionListener'
        listener.allEvents[i++].message == 'EntityChangedEvent: afterCommit, UPDATED'
        listener.allEvents[i++].message == 'AfterCompleteTransactionListener'

        cleanup:

        tx.end()
        cont.deleteRecord(order)
    }

}
