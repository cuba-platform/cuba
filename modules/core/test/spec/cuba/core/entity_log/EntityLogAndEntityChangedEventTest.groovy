/*
 * Copyright (c) 2008-2017 Haulmont.
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

package spec.cuba.core.entity_log

import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.PersistenceTools
import com.haulmont.cuba.core.app.importexport.EntityImportExportAPI
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.entity.EntityLogItem
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testmodel.entity_log.EntityLogA
import com.haulmont.cuba.testmodel.entity_log.EntityLogB
import com.haulmont.cuba.testmodel.entity_log.TestEntityLogChangedEventListener
import spock.lang.Issue

class EntityLogAndEntityChangedEventTest extends AbstractEntityLogTest {

    private UUID entityId

    protected TestEntityLogChangedEventListener entityLogListener
    protected Metadata metadata

    void setup() {
        clearTables("SEC_LOGGED_ATTR", "SEC_LOGGED_ENTITY")

        withTransaction { EntityManager em ->
            clearTable(em, "SEC_ENTITY_LOG")
            initEntityLogConfiguration(em)
        }

        initBeans()
    }

    protected void initBeans() {
        initEntityLogAPI()
        persistenceTools = AppBeans.get(PersistenceTools.class)
        metadata = AppBeans.get(Metadata.class)
        entityLogListener = AppBeans.get(TestEntityLogChangedEventListener.class)

        entityLogListener.enabled = true
    }


    protected void initEntityLogConfiguration(EntityManager em) {
        saveEntityLogAutoConfFor(em, 'test_EntityLogA', 'name', 'description')
    }


    void cleanup() {
        entityLogListener.enabled = false

        clearTables("SEC_LOGGED_ATTR", "SEC_LOGGED_ENTITY")

        if (entityId != null)
            cont.deleteRecord("TEST_ENTITY_LOG_A", entityId)
    }


    def "entity log saving for when entity is changed in EntityChangedEvent listener"() {
        given:

        withTransaction { EntityManager em ->
            EntityLogA entity = metadata.create(EntityLogA)

            entityId = entity.id
            entity.name = 'Entity#1'

            em.persist(entity)
        }

        and:

        getEntityLogItems('test_EntityLogA', entityId).size() == 1
        def logItem = getLatestEntityLogItem('test_EntityLogA', entityId)

        loggedValueMatches(logItem, 'name','Entity#1')
        loggedValueMatches(logItem, 'description','Entity#1')
    }
}
