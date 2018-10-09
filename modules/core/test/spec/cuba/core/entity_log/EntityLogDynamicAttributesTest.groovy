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

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.TypedQuery
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType
import com.haulmont.cuba.core.entity.Category
import com.haulmont.cuba.core.entity.CategoryAttribute
import com.haulmont.cuba.core.entity.ReferenceToEntity
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.security.app.EntityLogAPI
import com.haulmont.cuba.security.entity.*
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.sql.SQLException

class EntityLogDynamicAttributesTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private UUID userId, categoryId, categoryAttributeId
    private EntityLog entityLog
    private DataManager dataManager
    private DynamicAttributesManagerAPI dynamicAttributesManagerAPI

    private final String DYNAMIC_ATTRIBUTE_NAME = '+userAttribute'


    void setup() {
        _cleanup()

        cont.persistence().runInTransaction { em ->
            clearTable(em, "SEC_ENTITY_LOG")
            clearTable(em, "SYS_ATTR_VALUE")

            LoggedEntity le = new LoggedEntity(name: 'sec$User', auto: true)
            em.persist(le)

            createLoggedAttributeWithName(em, le, 'name')
            createLoggedAttributeWithName(em, le, DYNAMIC_ATTRIBUTE_NAME)

            Category category = new Category(name: 'user', entityType: 'sec$User')
            categoryId = category.id
            em.persist(category)

            CategoryAttribute categoryAttribute = new CategoryAttribute(
                    name: "userAttribute",
                    code: "userAttribute",
                    category: category,
                    categoryEntityType: 'sec$User',
                    dataType: PropertyType.STRING,
                    defaultEntity: new ReferenceToEntity()   
            )
            categoryAttributeId = categoryAttribute.id
            em.persist(categoryAttribute)

        }
        entityLog = AppBeans.get(EntityLogAPI.class) as EntityLog
        entityLog.invalidateCache()
        dynamicAttributesManagerAPI = AppBeans.get(DynamicAttributesManagerAPI.class)
        dynamicAttributesManagerAPI.loadCache()
        dataManager = AppBeans.get(DataManager.class)
    }

    protected void createLoggedAttributeWithName(EntityManager em, LoggedEntity le, String name) {
        em.persist(new LoggedAttribute(entity: le, name: name))
    }

    protected void clearTable(EntityManager em, String table) {
        em.createNativeQuery("delete from " + table).executeUpdate()
    }

    void cleanup() {
        _cleanup()
        if (userId != null)
            cont.deleteRecord("SEC_USER", userId)

        cont.deleteRecord("SYS_CATEGORY_ATTR", categoryAttributeId)
        cont.deleteRecord("SYS_CATEGORY", categoryId)
    }

    private void _cleanup() throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update("delete from SEC_LOGGED_ATTR")
        runner.update("delete from SEC_LOGGED_ENTITY")
        runner.update("delete from SYS_ATTR_VALUE")
    }

    private List<EntityLogItem> getEntityLogItems(def userId) {
        Transaction tx = cont.persistence().createTransaction()
        List<EntityLogItem> items
        try {
            EntityManager em = cont.persistence().getEntityManager()
            TypedQuery<EntityLogItem> query = em.createQuery(
                    'select i from sec$EntityLog i where i.entity = ?1 and i.entityRef.entityId = ?2 order by i.eventTs desc', EntityLogItem.class)
            query.setParameter(1, 'sec$User')
            query.setParameter(2, userId)
            items = query.getResultList()

            tx.commit()
        } finally {
            tx.end()
        }

        items
    }

    def "Entity Log: create/update/delete entity with dynamic attribute"() {

        given:

        Group group = findCompanyGroup()

        and:

        def expectedRegularAttributeValue = 'test-name'
        def expectedDynamicAttributeValue = "userName"

        and:

        User user = cont.metadata().create(User)
        userId = user.getId()
        user.group = group
        user.login = "test"
        user.name = expectedRegularAttributeValue

        and: 'dynamic attribute needs to be loaded explicitly in order to set it'

        user.getValue(DYNAMIC_ATTRIBUTE_NAME)

        and:

        user.setValue(DYNAMIC_ATTRIBUTE_NAME, expectedDynamicAttributeValue)

        when:

        dataManager.commit(user)

        then:

        def entityLogItemsAfterCreate = getEntityLogItems(userId)
        entityLogItemsAfterCreate.size() == 1

        and:

        def createdEntityLogItem = entityLogItemsAfterCreate[0]

        createdEntityLogItem.type == EntityLogItem.Type.CREATE
        createdEntityLogItem.attributes.find { it.name == 'name' }.value == expectedRegularAttributeValue

        and:

        entityLogAttributeForDynamicAttribute(createdEntityLogItem).value == expectedDynamicAttributeValue

        when:

        user = reloadUserFromDbWithDynamicAttributes(user)
        userId = user.id
        def updatedDynamicAttributeValue = "userName1"
        user.setValue(DYNAMIC_ATTRIBUTE_NAME, updatedDynamicAttributeValue)

        dataManager.commit(user)

        then:

        def entityLogItemsAfterModify = getEntityLogItems(userId)
        entityLogItemsAfterModify.size() == 2
        def modifiedEntityLogItem = entityLogItemsAfterModify[0]

        modifiedEntityLogItem.type == EntityLogItem.Type.MODIFY

        and:

        entityLogAttributeForDynamicAttribute(modifiedEntityLogItem).oldValue == expectedDynamicAttributeValue
        entityLogAttributeForDynamicAttribute(modifiedEntityLogItem).value == updatedDynamicAttributeValue

        when:

        user = reloadUserFromDbWithDynamicAttributes(user)
        userId = user.id
        user.setValue(DYNAMIC_ATTRIBUTE_NAME, null)

        dataManager.commit(user)

        then:

        def entityLogItemsAfterDelete = getEntityLogItems(userId)
        entityLogItemsAfterDelete.size() == 3
        def deletedEntityLogItem = entityLogItemsAfterDelete[0]

        deletedEntityLogItem.type == EntityLogItem.Type.MODIFY

        entityLogAttributeForDynamicAttribute(deletedEntityLogItem).value == ""
        entityLogAttributeForDynamicAttribute(deletedEntityLogItem).oldValue == updatedDynamicAttributeValue
    }

    protected EntityLogAttr entityLogAttributeForDynamicAttribute(EntityLogItem entityLogItem) {
        entityLogItem.attributes.find { it.name == DYNAMIC_ATTRIBUTE_NAME }
    }

    protected Group findCompanyGroup() {
        cont.persistence().callInTransaction { em ->
            em.find(Group.class, TestSupport.COMPANY_GROUP_ID)
        }
    }


    protected User reloadUserFromDbWithDynamicAttributes(User user) {
        LoadContext loadContext = new LoadContext(User.class)
                .setId(user)
                .setView(View.LOCAL)
                .setLoadDynamicAttributes(true)
        dataManager.load(loadContext)
    }
}
