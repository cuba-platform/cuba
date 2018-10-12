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

class EntityLogDynamicAttributesTest extends AbstractEntityLogTest {


    private UUID userId, categoryId, categoryAttributeId
    private DataManager dataManager
    private DynamicAttributesManagerAPI dynamicAttributesManagerAPI

    private final String DYNAMIC_ATTRIBUTE_NAME = '+userAttribute'


    void setup() {

        clearEntityLogTables()

        withTransaction { EntityManager em ->
            clearTable(em, "SEC_ENTITY_LOG")
            clearTable(em, "SYS_ATTR_VALUE")

            initEntityLogConfiguration(em)
            initDynamicAttributeConfiguration(em)
        }
        initBeans()
    }

    protected void initEntityLogConfiguration(EntityManager em) {
        LoggedEntity le = new LoggedEntity(name: 'sec$User', auto: true)
        em.persist(le)

        em.persist(new LoggedAttribute(entity: le, name: 'name'))
        em.persist(new LoggedAttribute(entity: le, name: DYNAMIC_ATTRIBUTE_NAME))
    }

    protected void initBeans() {
        entityLog = AppBeans.get(EntityLogAPI.class) as EntityLog
        entityLog.invalidateCache()
        dynamicAttributesManagerAPI = AppBeans.get(DynamicAttributesManagerAPI.class)
        dynamicAttributesManagerAPI.loadCache()
        dataManager = AppBeans.get(DataManager.class)
    }

    protected void initDynamicAttributeConfiguration(EntityManager em) {
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


    void cleanup() {

        clearEntityLogTables()

        if (userId != null)
            cont.deleteRecord("SEC_USER", userId)

        cont.deleteRecord("SYS_CATEGORY_ATTR", categoryAttributeId)
        cont.deleteRecord("SYS_CATEGORY", categoryId)
    }

    private void clearEntityLogTables() throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource())
        runner.update("delete from SEC_LOGGED_ATTR")
        runner.update("delete from SEC_LOGGED_ENTITY")
        runner.update("delete from SYS_ATTR_VALUE")
    }


    def "EntityLog logs the creation of a dynamic attribute"() {

        given:

        User user = createUser()

        when:

        saveUserWithDynamicAttributeValue(user, 'userName')

        def log = latestEntityLogItem(user)

        then:

        isCreateType(log)

        and:

        loggedValueMatches(log, 'userName')

    }

    def "EntityLog logs the update of a dynamic attribute"() {

        given:

        User user = createAndSaveUser('oldUserName')

        when:

        saveUserWithDynamicAttributeValue(user, 'updatedUserName')

        def log = latestEntityLogItem(user)

        then:

        isModifyType(log)

        and:

        loggedValueMatches(log, 'updatedUserName')
        loggedOldValueMatches(log, 'oldUserName')

    }

    def "EntityLog logs the deletion of a dynamic attribute"() {

        given:

        User user = createAndSaveUser('oldUserName')

        when:

        saveUserWithDynamicAttributeValue(user, null)

        def log = latestEntityLogItem(user)

        then:

        isModifyType(log)

        and:

        loggedValueMatches(log, '')
        loggedOldValueMatches(log, 'oldUserName')

    }

    protected boolean isModifyType(EntityLogItem entityLogItem) {
        entityLogItem.type == EntityLogItem.Type.MODIFY
    }

    protected boolean isCreateType(EntityLogItem entityLogItem) {
        entityLogItem.type == EntityLogItem.Type.CREATE
    }

    boolean loggedOldValueMatches(EntityLogItem entityLogItem, String oldValue) {
        dynamicAttributeEntityLog(entityLogItem).oldValue == oldValue
    }

    boolean loggedValueMatches(EntityLogItem entityLogItem, String value) {
        dynamicAttributeEntityLog(entityLogItem).value == value
    }

    private EntityLogItem latestEntityLogItem(User user) {
        getLatestEntityLogItem('sec$User', user.id)
    }

    private EntityLogAttr dynamicAttributeEntityLog(EntityLogItem entityLogItem) {
        entityLogItem.attributes.find { it.name == DYNAMIC_ATTRIBUTE_NAME }
    }


    private User createUser() {
        User user = cont.metadata().create(User)
        userId = user.id
        user.group = findCompanyGroup()
        user.login = "test"
        user.name = 'test-name'

        // the dynamic attribute has to be loaded explicitly in order to work with it further down the road
        user.getValue(DYNAMIC_ATTRIBUTE_NAME)


        user
    }

    private User saveUserWithDynamicAttributeValue(User user, String newDynamicAttributeValue) {
        user.setValue(DYNAMIC_ATTRIBUTE_NAME, newDynamicAttributeValue)
        dataManager.commit(user)
    }

    private User createAndSaveUser(String dynamicAttributeValue) {
        User user = createUser()
        saveUserWithDynamicAttributeValue(user, dynamicAttributeValue)

        LoadContext loadContext = new LoadContext(User.class)
                .setId(user)
                .setView(View.LOCAL)
                .setLoadDynamicAttributes(true)
        dataManager.load(loadContext)

    }


}
