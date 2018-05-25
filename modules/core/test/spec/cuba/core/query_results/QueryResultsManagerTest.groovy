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

package spec.cuba.core.query_results

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.app.queryresults.QueryResultsManager
import com.haulmont.cuba.core.entity.QueryResult
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.security.app.UserSessions
import com.haulmont.cuba.security.global.UserSession
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class QueryResultsManagerTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence
    private Metadata metadata
    private QueryResultsManager queryResultsManager

    void setup() {
        persistence = cont.persistence()
        metadata = cont.metadata()
        queryResultsManager = AppBeans.get(QueryResultsManager)
    }

    void cleanup() {
        new QueryRunner(persistence.getDataSource()).update("delete from SYS_QUERY_RESULT")
    }

    def "test deleteForInactiveSessions - empty table"() {

        when:

        queryResultsManager.internalDeleteForInactiveSessions()

        then:

        def list = persistence.callInTransaction { em -> em.createQuery('select e from sys$QueryResult e').resultList }
        list.isEmpty()
    }

    def "test deleteForInactiveSessions - table with values"() {
        def userSessionSource = AppBeans.get(UserSessionSource)
        def userSessions = AppBeans.get(UserSessions)
        def user = userSessionSource.userSession.user
        def session1 = new UserSession(UUID.randomUUID(), user, Collections.emptyList(), Locale.ENGLISH, false)
        userSessions.add(session1)
        def session2 = new UserSession(UUID.randomUUID(), user, Collections.emptyList(), Locale.ENGLISH, false)
        userSessions.add(session2)

        persistence.callInTransaction { em ->
            for (i in 0..255) {
                def entity = metadata.create(QueryResult)
                entity.setSessionId(UUID.randomUUID())
                entity.setQueryKey(i)
                em.persist(entity)
            }
            def entity = metadata.create(QueryResult)
            entity.setSessionId(session1.id)
            entity.setQueryKey(1000)
            em.persist(entity)
        }

        when:

        queryResultsManager.internalDeleteForInactiveSessions()

        then:

        def list = persistence.callInTransaction { em -> em.createQuery('select e from sys$QueryResult e').resultList }
        list.size() == 1
        list[0].sessionId == session1.id

        cleanup:

        userSessions.remove(session1)
        userSessions.remove(session2)
    }
}
