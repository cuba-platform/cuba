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

package spec.cuba.core.tx_listeners

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import com.haulmont.cuba.tx_listener.TestBeforeCommitTxListener
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class UsingTransactionsInBeforeCommitTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata
    private Persistence persistence
    private Group companyGroup

    void setup() {
        metadata = cont.metadata()
        persistence = cont.persistence()

        companyGroup = persistence.callInTransaction { em -> em.find(Group, TestSupport.COMPANY_GROUP_ID) }

        AppBeans.get(EntityLog).getLoggedAttributes('sec$User', true)
    }

    void cleanup() {
        QueryRunner runner = new QueryRunner(persistence.getDataSource())
        runner.update("delete from SEC_USER where LOGIN_LC like 'txlstnrtst-%'")
    }

    def "create entity in new transaction"() {
        TestBeforeCommitTxListener.test = "testCreateEntityInNewTransaction"

        User u = metadata.create(User)
        u.setLogin("u-$u.id")
        u.setGroup(companyGroup)

        when:

        Transaction tx = persistence.createTransaction()
        try {
            persistence.getEntityManager().persist(u)
            tx.commit()
        } finally {
            tx.end()
            TestBeforeCommitTxListener.test = null
        }

        then:

        persistence.callInTransaction { em -> em.find(User, TestBeforeCommitTxListener.createdEntityId) } != null
    }

    def "create entity in same transaction"() {
        TestBeforeCommitTxListener.test = "testCreateEntityInSameTransaction"

        User u = metadata.create(User)
        u.setLogin("u-$u.id")
        u.setGroup(companyGroup)

        when:

        Transaction tx = persistence.createTransaction()
        try {
            persistence.getEntityManager().persist(u)
            tx.commit()
        } finally {
            tx.end()
            TestBeforeCommitTxListener.test = null
        }

        then:

        persistence.callInTransaction { em -> em.find(User, TestBeforeCommitTxListener.createdEntityId) } != null
    }

    def "create entity in new transaction and rollback"() {
        TestBeforeCommitTxListener.test = "testCreateEntityInNewTransactionAndRollback"

        User u = metadata.create(User)
        u.setLogin("u-$u.id")
        u.setGroup(companyGroup)

        when:

        Transaction tx = persistence.createTransaction()
        try {
            persistence.getEntityManager().persist(u)
            tx.commit()
        } finally {
            tx.end()
            TestBeforeCommitTxListener.test = null
        }

        then:

        def e = thrown(RuntimeException)
        e.message == 'some error'

        persistence.callInTransaction { em -> em.find(User, TestBeforeCommitTxListener.createdEntityId) } != null
    }

    def "create entity in same transaction and rollback"() {
        TestBeforeCommitTxListener.test = "testCreateEntityInSameTransactionAndRollback"

        User u = metadata.create(User)
        u.setLogin("u-$u.id")
        u.setGroup(companyGroup)

        when:

        Transaction tx = persistence.createTransaction()
        try {
            persistence.getEntityManager().persist(u)
            tx.commit()
        } finally {
            tx.end()
            TestBeforeCommitTxListener.test = null
        }

        then: "entity was not saved"

        def e = thrown(RuntimeException)
        e.message == 'some error'

        persistence.callInTransaction { em -> em.find(User, TestBeforeCommitTxListener.createdEntityId) } == null
    }
}
