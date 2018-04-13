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

package spec.cuba.core.statistics_counter

import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.jmx.StatisticsCounterMBean
import com.haulmont.cuba.core.sys.AppContext
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class StatisticsCounterTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Customer customer1
    private StatisticsCounterMBean statCounter
    private Persistence persistence

    void setup() {
        persistence = cont.persistence()
        statCounter = AppBeans.get(StatisticsCounterMBean)
        AppBeans.get(MiddlewareStatisticsAccumulator).reset()
        AppContext.setProperty('cuba.entityLog.enabled', 'false')
    }

    void cleanup() {
        cont.deleteRecord(customer1)
        AppContext.setProperty('cuba.entityLog.enabled', 'true')
    }

    def "loading entities"() {

        def before, inside, after

        when:

        before = statCounter.activeTransactionsCount

        persistence.runInTransaction { em ->
            em.createQuery('select c from test$Customer c').getResultList()

            inside = statCounter.activeTransactionsCount
        }

        after = statCounter.activeTransactionsCount

        then:

        before == 0
        inside == 1
        after == 0
    }

    def "updating entities"() {

        def before, inside, after

        when:

        before = statCounter.activeTransactionsCount

        persistence.runInTransaction({ em ->
            customer1 = cont.metadata().create(Customer)
            customer1.name = 'a customer'
            em.persist(customer1)

            inside = statCounter.activeTransactionsCount
        })

        after = statCounter.activeTransactionsCount

        then:

        before == 0
        inside == 1
        after == 0
    }

    def "rollback transaction"() {

        def before, inside, after

        when:

        before = statCounter.activeTransactionsCount

        def tx = persistence.createTransaction()
        try {
            def em = persistence.getEntityManager()
            def customer = cont.metadata().create(Customer)
            customer.name = 'a customer'
            em.persist(customer)

            inside = statCounter.activeTransactionsCount
            // no commit
        } finally {
            tx.end()
        }

        after = statCounter.activeTransactionsCount

        then:

        before == 0
        inside == 1
        after == 0
        statCounter.rolledBackTransactionsCount == 1
    }
}
