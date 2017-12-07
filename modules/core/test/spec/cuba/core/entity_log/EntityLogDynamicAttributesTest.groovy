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
import com.haulmont.cuba.core.Query
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

    private UUID user1Id, categoryId, categoryAttributeId
    private def entityLog
    private DataManager dataManager
    private def dynamicAttributesManagerAPI


    void setup() {
        _cleanup()

        cont.persistence().runInTransaction { em ->
            Query q
            q = em.createNativeQuery("delete from SEC_ENTITY_LOG")
            q.executeUpdate()

            q = em.createNativeQuery("delete from SYS_ATTR_VALUE")
            q.executeUpdate()

            LoggedEntity le = new LoggedEntity()
            le.setName('sec$User')
            le.setAuto(true)
            em.persist(le)

            LoggedAttribute la = new LoggedAttribute()
            la.setEntity(le)
            la.setName('name')
            em.persist(la)

            la = new LoggedAttribute()
            la.setEntity(le)
            la.setName('+userAttribute')
            em.persist(la)

            Category category = new Category()
            category.setName("user")
            category.setEntityType("sec\$User")
            categoryId = category.getId()
            em.persist(category)

            CategoryAttribute categoryAttribute = new CategoryAttribute()
            categoryAttribute.setName("userAttribute")
            categoryAttribute.setCode("userAttribute")
            categoryAttribute.setCategory(category)
            categoryAttribute.setCategoryEntityType("sec\$User")
            categoryAttribute.setDataType(PropertyType.STRING)
            categoryAttribute.setDefaultEntity(new ReferenceToEntity())
            categoryAttributeId = categoryAttribute.getId()
            em.persist(categoryAttribute)

        }
        entityLog = AppBeans.get(EntityLogAPI.class)
        entityLog.invalidateCache()
        dynamicAttributesManagerAPI = AppBeans.get(DynamicAttributesManagerAPI.class)
        dynamicAttributesManagerAPI.loadCache()
        dataManager = AppBeans.get(DataManager.class)
    }

    void cleanup() {
        _cleanup()
        if (user1Id != null)
            cont.deleteRecord("SEC_USER", user1Id)

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
        Transaction tx
        List<EntityLogItem> items
        tx = cont.persistence().createTransaction()
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
        return items
    }

    def "Entity Log: create/update/delete entity with dynamic attribute"() {
        Group group = cont.persistence().callInTransaction({ em ->
            em.find(Group.class, TestSupport.COMPANY_GROUP_ID)
        })

        when:

        User user1 = cont.metadata().create(User)
        user1Id = user1.getId()
        user1.setGroup(group)
        user1.setLogin("test")
        user1.setName("test-name")
        user1.getValue("+userAttribute")
        user1.setValue("+userAttribute", "userName")

        dataManager.commit(user1)

        then:

        def items = getEntityLogItems(user1Id)
        items.size() == 1
        def item = items[0]

        item.type == EntityLogItem.Type.CREATE
        item.attributes.find({ it.name == 'name' }).value == 'test-name'
        item.attributes.find({ it.name == '+userAttribute' }).value == 'userName'

        when:

        LoadContext<User> loadContext = new LoadContext(User.class)
                .setId(user1).setView(View.LOCAL).setLoadDynamicAttributes(true)
        user1 = dataManager.load(loadContext)
        user1Id = user1.getId()
        user1.setValue("+userAttribute", "userName1")

        dataManager.commit(user1)

        then:

        def items1 = getEntityLogItems(user1Id)
        items1.size() == 2
        def item1 = items1[0]

        item1.type == EntityLogItem.Type.MODIFY
        item1.attributes.find({ it.name == '+userAttribute' }).oldValue == 'userName'
        item1.attributes.find({ it.name == '+userAttribute' }).value == 'userName1'

        when:

        loadContext = new LoadContext(User.class)
                .setId(user1).setView(View.LOCAL).setLoadDynamicAttributes(true)
        user1 = (User) dataManager.load(loadContext)
        user1Id = user1.getId()
        user1.setValue("+userAttribute", null)

        dataManager.commit(user1)

        then:

        def items2 = getEntityLogItems(user1Id)
        items2.size() == 3
        def item2 = items2[0]

        item2.type == EntityLogItem.Type.MODIFY
        item2.attributes.find({ it.name == '+userAttribute' }).value == ""
        item2.attributes.find({ it.name == '+userAttribute' }).oldValue == 'userName1'
    }
}
