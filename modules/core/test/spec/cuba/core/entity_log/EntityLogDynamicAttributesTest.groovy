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
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType
import com.haulmont.cuba.core.entity.Category
import com.haulmont.cuba.core.entity.CategoryAttribute
import com.haulmont.cuba.core.entity.ReferenceToEntity
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.security.app.EntityLogAPI
import com.haulmont.cuba.security.entity.EntityLogItem
import com.haulmont.cuba.security.entity.LoggedAttribute
import com.haulmont.cuba.security.entity.LoggedEntity
import com.haulmont.cuba.security.entity.User

import java.sql.SQLException

class EntityLogDynamicAttributesTest extends AbstractEntityLogTest {


    private UUID userId, categoryId, categoryAttributeId, secondCategoryAttributeId
    private DataManager dataManager
    private DynamicAttributesManagerAPI dynamicAttributesManagerAPI

    private final String DYNAMIC_ATTRIBUTE_NAME = '+userAttribute'
    private final String SECOND_DYNAMIC_ATTRIBUTE_NAME = '+userSecondAttribute'


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
        em.persist(new LoggedAttribute(entity: le, name: SECOND_DYNAMIC_ATTRIBUTE_NAME))
    }

    protected void initBeans() {
        entityLog = AppBeans.get(EntityLogAPI.class) as EntityLog
        entityLog.invalidateCache()
        dynamicAttributesManagerAPI = AppBeans.get(DynamicAttributesManagerAPI.class)
        dynamicAttributesManagerAPI.loadCache()
        dataManager = AppBeans.get(DataManager.class)
        metadataTools = AppBeans.get(MetadataTools.class)
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

        CategoryAttribute secondCategoryAttribute = new CategoryAttribute(
                name: "userSecondAttribute",
                code: "userSecondAttribute",
                category: category,
                categoryEntityType: 'sec$User',
                dataType: PropertyType.STRING,
                defaultEntity: new ReferenceToEntity()
        )
        secondCategoryAttributeId = secondCategoryAttribute.id
        em.persist(secondCategoryAttribute)
    }


    void cleanup() {

        clearEntityLogTables()

        if (userId != null)
            cont.deleteRecord("SEC_USER", userId)

        cont.deleteRecord("SYS_CATEGORY_ATTR", categoryAttributeId)
        cont.deleteRecord("SYS_CATEGORY_ATTR", secondCategoryAttributeId)
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
        log.entityInstanceName == metadataTools.getInstanceName(user)

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
        log.entityInstanceName == metadataTools.getInstanceName(user)

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
        log.entityInstanceName == metadataTools.getInstanceName(user)

    }

    def "EntityLog logs the creation of entity with two dynamic attributes (#1533 case 1)"() {
        given:

        User user = createUser()

        when:

        changeValuesAndSaveUser(user,'new-test-name', 'firstAttrValue', 'secondAttrValue')

        def log = latestEntityLogItem(user)

        then:

        isCreateType(log)

        and:

        loggedValueMatches(log, DYNAMIC_ATTRIBUTE_NAME, 'firstAttrValue')
        loggedValueMatches(log, SECOND_DYNAMIC_ATTRIBUTE_NAME, 'secondAttrValue')
        loggedValueMatches(log, 'name', 'new-test-name')
        log.entityInstanceName == metadataTools.getInstanceName(user)
    }

    def "Update entity with dynamic attributes (#1533 case 2)"() {

        given:

        User user = createAndSaveUserWithTwoDynamicAttributes('test-name', 'oldFirstAttrValue', 'oldSecondAttrValue')

        when:

        changeValuesAndSaveUser(user, 'new-test-name', 'updatedFirstAttrValue', 'updatedSecondAttrValue')

        def log = latestEntityLogItem(user)

        then:

        isModifyType(log)

        and:

        loggedValueMatches(log, DYNAMIC_ATTRIBUTE_NAME, 'updatedFirstAttrValue')
        loggedValueMatches(log, SECOND_DYNAMIC_ATTRIBUTE_NAME, 'updatedSecondAttrValue')
        loggedValueMatches(log, 'name', 'new-test-name')
        loggedOldValueMatches(log, DYNAMIC_ATTRIBUTE_NAME, 'oldFirstAttrValue')
        loggedOldValueMatches(log, SECOND_DYNAMIC_ATTRIBUTE_NAME, 'oldSecondAttrValue')
        loggedOldValueMatches(log, 'name', 'test-name')
        log.entityInstanceName == metadataTools.getInstanceName(user)

    }

    protected boolean isModifyType(EntityLogItem entityLogItem) {
        entityLogItem.type == EntityLogItem.Type.MODIFY
    }

    protected boolean isCreateType(EntityLogItem entityLogItem) {
        entityLogItem.type == EntityLogItem.Type.CREATE
    }

    boolean loggedOldValueMatches(EntityLogItem entityLogItem, String oldValue) {
        loggedOldValueMatches(entityLogItem, DYNAMIC_ATTRIBUTE_NAME, oldValue)
    }

    boolean loggedValueMatches(EntityLogItem entityLogItem, String value) {
        loggedValueMatches(entityLogItem, DYNAMIC_ATTRIBUTE_NAME, value)
    }

    private EntityLogItem latestEntityLogItem(User user) {
        getLatestEntityLogItem('sec$User', user.id)
    }


    private User createUser() {
        User user = cont.metadata().create(User)
        userId = user.id
        user.group = findCompanyGroup()
        user.login = "test"
        user.name = 'test-name'

        // the dynamic attribute has to be loaded explicitly in order to work with it further down the road
        user.getValue(DYNAMIC_ATTRIBUTE_NAME)
        user.getValue(SECOND_DYNAMIC_ATTRIBUTE_NAME)


        user
    }

    private User saveUserWithDynamicAttributeValue(User user, String newDynamicAttributeValue) {
        user.setValue(DYNAMIC_ATTRIBUTE_NAME, newDynamicAttributeValue)
        dataManager.commit(user)
    }

    private User changeValuesAndSaveUser(User user,
                                         String newName,
                                         String newDynamicAttributeValue,
                                         String newSecondDynamicAttributeValue) {
        user.setValue("name", newName)
        user.setValue(DYNAMIC_ATTRIBUTE_NAME, newDynamicAttributeValue)
        user.setValue(SECOND_DYNAMIC_ATTRIBUTE_NAME, newSecondDynamicAttributeValue)
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

    private User createAndSaveUserWithTwoDynamicAttributes(String userName, String dynamicAttrValue, String secondDynamicAttrValue) {
        User user = createUser()
        changeValuesAndSaveUser(user, userName, dynamicAttrValue, secondDynamicAttrValue)

        LoadContext loadContext = new LoadContext(User.class)
                .setId(user)
                .setView(View.LOCAL)
                .setLoadDynamicAttributes(true)
        dataManager.load(loadContext)
    }


}
