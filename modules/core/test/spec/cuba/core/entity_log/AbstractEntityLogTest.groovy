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

package spec.cuba.core.entity_log


import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.PersistenceTools
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TypedQuery
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.security.app.EntityLogAPI
import com.haulmont.cuba.security.entity.EntityLogItem
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.LoggedAttribute
import com.haulmont.cuba.security.entity.LoggedEntity
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

abstract class AbstractEntityLogTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE


    protected EntityLogAPI entityLog
    protected PersistenceTools persistenceTools

    protected void saveEntityLogAutoConfFor(EntityManager em, String entityName, String... attributes) {

        LoggedEntity le = new LoggedEntity(name: entityName, auto: true)
        em.persist(le)

        attributes.each {
            LoggedAttribute la = new LoggedAttribute(entity: le, name: it)
            em.persist(la)
        }

    }

    protected void saveManualEntityLogAutoConfFor(EntityManager em, String entityName, String... attributes) {

        LoggedEntity le = new LoggedEntity(name: entityName, auto: false, manual: true)
        em.persist(le)

        attributes.each {
            LoggedAttribute la = new LoggedAttribute(entity: le, name: it)
            em.persist(la)
        }

    }

    protected void initEntityLogAPI() {
        entityLog = AppBeans.get(EntityLogAPI.class)
        entityLog.invalidateCache()
    }

    protected List<EntityLogItem> getEntityLogItems(String entityName, def entityId) {
        Transaction tx
        List<EntityLogItem> items
        tx = cont.persistence().createTransaction()
        try {
            EntityManager em = cont.persistence().getEntityManager()
            String entityIdField
            if (entityId instanceof Integer) entityIdField = 'intEntityId'
            else if (entityId instanceof Long) entityIdField = 'longEntityId'
            else if (entityId instanceof String) entityIdField = 'stringEntityId'
            else entityIdField = 'entityId'

            TypedQuery<EntityLogItem> query = em.createQuery(
                    "select i from sec\$EntityLog i where i.entity = ?1 and i.entityRef.$entityIdField = ?2 order by i.eventTs desc", EntityLogItem.class)
            query.setParameter(1, entityName)
            query.setParameter(2, entityId)
            items = query.getResultList()

            tx.commit()
        } finally {
            tx.end()
        }
        return items
    }

    protected boolean loggedValueMatches(EntityLogItem entityLogItem, String attributeName, String value) {
        entityLogItem.attributes.find { it.name == attributeName }.value == value
    }

    protected boolean loggedOldValueMatches(EntityLogItem entityLogItem, String attributeName, String oldValue) {
        entityLogItem.attributes.find { it.name == attributeName }.oldValue == oldValue
    }

    protected EntityLogItem getLatestEntityLogItem(String entityName, def entityId) {
        getEntityLogItems(entityName, entityId).first()
    }

    protected void clearTable(EntityManager em, String tableName) {
        em.createNativeQuery("delete from " + tableName).executeUpdate()
    }


    protected withTransaction(Closure run) {

        cont.persistence().runInTransaction { em ->
            run.call(em)
        }
    }

    protected Group findCompanyGroup() {
        cont.persistence().callInTransaction { em ->
            em.find(Group.class, TestSupport.COMPANY_GROUP_ID)
        }
    }
}
