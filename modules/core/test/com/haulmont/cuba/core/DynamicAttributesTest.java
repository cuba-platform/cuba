/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.core;

import com.google.common.collect.Lists;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testmodel.primary_keys.CompositeKeyEntity;
import com.haulmont.cuba.testmodel.primary_keys.EntityKey;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DynamicAttributesTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected DataManager dataManager;
    protected Metadata metadata;
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    protected Category userCategory, userRoleCategory;
    protected CategoryAttribute userAttribute, userRoleAttribute, userGroupAttribute, userGroupCollectionAttribute, userIntCollectionAttribute;
    protected Group group, group2;

    protected User user, user2;
    protected UserRole userRole;
    protected Role role;
    protected CompositeKeyEntity compositeKeyEntity;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
        metadata = AppBeans.get(Metadata.class);
        dynamicAttributesManagerAPI = AppBeans.get(DynamicAttributesManagerAPI.class);

        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from TEST_COMPOSITE_KEY");

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.entityManager();
            userCategory = metadata.create(Category.class);
            userCategory.setName("user");
            userCategory.setEntityType("sec$User");
            em.persist(userCategory);

            userAttribute = metadata.create(CategoryAttribute.class);
            userAttribute.setName("userAttribute");
            userAttribute.setCode("userAttribute");
            userAttribute.setCategory(userCategory);
            userAttribute.setCategoryEntityType("sec$User");
            userAttribute.setDataType(PropertyType.STRING);
            em.persist(userAttribute);

            userRoleCategory = metadata.create(Category.class);
            userRoleCategory.setName("userRole");
            userRoleCategory.setEntityType("sec$UserRole");
            em.persist(userRoleCategory);

            userRoleAttribute = metadata.create(CategoryAttribute.class);
            userRoleAttribute.setName("userRoleAttribute");
            userRoleAttribute.setCode("userRoleAttribute");
            userRoleAttribute.setCategory(userRoleCategory);
            userRoleAttribute.setCategoryEntityType("sec$UserRole");
            userRoleAttribute.setDataType(PropertyType.STRING);
            em.persist(userRoleAttribute);

            group = metadata.create(Group.class);
            group.setName("group");
            em.persist(group);

            group2 = metadata.create(Group.class);
            group2.setName("group2");
            em.persist(group2);

            userGroupAttribute = metadata.create(CategoryAttribute.class);
            userGroupAttribute.setName("userGroupAttribute");
            userGroupAttribute.setCode("userGroupAttribute");
            userGroupAttribute.setCategory(userCategory);
            userGroupAttribute.setCategoryEntityType("sec$User");
            userGroupAttribute.setDataType(PropertyType.ENTITY);
            userGroupAttribute.setEntityClass("com.haulmont.cuba.security.entity.Group");
            em.persist(userGroupAttribute);

            userGroupCollectionAttribute = metadata.create(CategoryAttribute.class);
            userGroupCollectionAttribute.setName("userGroupCollectionAttribute");
            userGroupCollectionAttribute.setCode("userGroupCollectionAttribute");
            userGroupCollectionAttribute.setCategory(userCategory);
            userGroupCollectionAttribute.setCategoryEntityType("sec$User");
            userGroupCollectionAttribute.setDataType(PropertyType.ENTITY);
            userGroupCollectionAttribute.setEntityClass("com.haulmont.cuba.security.entity.Group");
            userGroupCollectionAttribute.setIsCollection(true);
            em.persist(userGroupCollectionAttribute);

            userIntCollectionAttribute = metadata.create(CategoryAttribute.class);
            userIntCollectionAttribute.setName("userIntCollectionAttribute");
            userIntCollectionAttribute.setCode("userIntCollectionAttribute");
            userIntCollectionAttribute.setCategory(userCategory);
            userIntCollectionAttribute.setCategoryEntityType("sec$User");
            userIntCollectionAttribute.setDataType(PropertyType.INTEGER);
            userIntCollectionAttribute.setIsCollection(true);
            em.persist(userIntCollectionAttribute);

            user = metadata.create(User.class);
            user.setName("user");
            user.setLogin("user");
            user.setGroup(group);
            em.persist(user);

            user2 = metadata.create(User.class);
            user2.setName("user2");
            user2.setLogin("user2");
            user2.setGroup(group);
            em.persist(user2);

            role = metadata.create(Role.class);
            role.setName("role");
            em.persist(role);

            userRole = metadata.create(UserRole.class);
            userRole.setUser(user);
            userRole.setRole(role);
            em.persist(userRole);

            compositeKeyEntity = metadata.create(CompositeKeyEntity.class);
            EntityKey entityKey = metadata.create(EntityKey.class);
            entityKey.setTenant(1);
            entityKey.setEntityId(10L);
            compositeKeyEntity.setId(entityKey);
            compositeKeyEntity.setName("foo");
            compositeKeyEntity.setEmail("foo@mail.com");
            em.persist(compositeKeyEntity);

            tx.commit();
        }

        dynamicAttributesManagerAPI.loadCache();

        user = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true));
        user.setValue("+userAttribute", "userName");
        user.setValue("+userGroupAttribute", group);
        user.setValue("+userGroupCollectionAttribute", Lists.newArrayList(group, group2));
        user.setValue("+userIntCollectionAttribute", Lists.newArrayList(1, 2));
        dataManager.commit(user);

        user2 = dataManager.load(LoadContext.create(User.class).setId(user2.getId()).setLoadDynamicAttributes(true));
        user2.setValue("+userAttribute", "userName");
        user2.setValue("+userGroupAttribute", group);
        dataManager.commit(user2);

        userRole = dataManager.load(LoadContext.create(UserRole.class).setId(userRole.getId()).setLoadDynamicAttributes(true));
        userRole.setValue("+userRoleAttribute", "userRole");
        dataManager.commit(userRole);
    }

    @After
    public void tearDown() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_ATTR_VALUE");
        runner.update("delete from TEST_COMPOSITE_KEY");
        cont.deleteRecord(userRole, role, user, user2, group, group2);
        cont.deleteRecord(userAttribute, userRoleAttribute, userGroupAttribute, userGroupCollectionAttribute, userIntCollectionAttribute);
        cont.deleteRecord(userCategory, userRoleCategory);
    }

    @Test
    public void testDynamicAttributes() {
        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true));
        assertEquals("userName", loadedUser.getValue("+userAttribute"));
    }

    @Test
    public void testDynamicAttributesWithLocalView() {
        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true).setView(View.LOCAL));
        assertEquals("userName", loadedUser.getValue("+userAttribute"));
    }

    @Test
    public void testDynamicAttributesWithNestedDynamicAttributes() {
        View view = new View(User.class, "testView")
                .addProperty("login")
                .addProperty("userRoles",
                        new View(UserRole.class, "testView"));

        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true).setView(view));
        assertEquals("userName", loadedUser.getValue("+userAttribute"));
        assertEquals("userRole", loadedUser.getUserRoles().get(0).getValue("+userRoleAttribute"));
    }

    @Test
    public void testLoadEntityAttribute() {
        LoadContext<User> ctx = LoadContext.create(User.class).setLoadDynamicAttributes(true);
        ctx.setQueryString("select u from sec$User u where u.login like 'user%' order by u.login");
        List<User> users = dataManager.loadList(ctx);
        User user = users.get(0);
        User user2 = users.get(1);
        assertEquals(group, user.getValue("+userGroupAttribute"));
        assertEquals(group, user2.getValue("+userGroupAttribute"));
    }

    @Test
    public void testCollectionOfEntitiesAttribute() {
        LoadContext<User> loadContext = LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true);
        User loadedUser = dataManager.load(loadContext);
        List<Group> groupsCollection = loadedUser.getValue("+userGroupCollectionAttribute");
        assertEquals(2, groupsCollection.size());

        loadedUser.setValue("+userGroupCollectionAttribute", Lists.newArrayList(group));
        dataManager.commit(loadedUser);

        loadedUser = dataManager.load(loadContext);
        groupsCollection = loadedUser.getValue("+userGroupCollectionAttribute");
        assertEquals(1, groupsCollection.size());
        assertEquals(group, groupsCollection.get(0));

        loadedUser.setValue("+userGroupCollectionAttribute", Lists.newArrayList(group, group2));
        dataManager.commit(loadedUser);
        groupsCollection = loadedUser.getValue("+userGroupCollectionAttribute");
        assertEquals(2, groupsCollection.size());
    }

    @Test
    public void testCollectionOfIntAttribute() {
        LoadContext<User> loadContext = LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true);
        User loadedUser = dataManager.load(loadContext);
        List<Integer> intCollection = loadedUser.getValue("+userIntCollectionAttribute");
        assertEquals(2, intCollection.size());

        loadedUser.setValue("+userIntCollectionAttribute", Lists.newArrayList(1));
        dataManager.commit(loadedUser);

        loadedUser = dataManager.load(loadContext);
        intCollection = loadedUser.getValue("+userIntCollectionAttribute");
        assertEquals(1, intCollection.size());
        assertEquals(1, (int)intCollection.get(0));

        loadedUser.setValue("+userIntCollectionAttribute", Lists.newArrayList(1, 3));
        dataManager.commit(loadedUser);
        intCollection = loadedUser.getValue("+userIntCollectionAttribute");
        assertEquals(2, intCollection.size());
        assertTrue(intCollection.contains(1));
        assertTrue(intCollection.contains(3));
    }

    @Test
    public void testLoadDynamicAttributesForCompositeKeyEntity() {
        LoadContext<CompositeKeyEntity> loadContext = LoadContext.create(CompositeKeyEntity.class).setLoadDynamicAttributes(true);
        loadContext.setQueryString("select e from test$CompositeKeyEntity e");
        List<CompositeKeyEntity> result = dataManager.loadList(loadContext);
        assertEquals(1, result.size());
    }

    @Test
    public void testPropertyChangeListener() {
        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true));
        assertEquals("userName", loadedUser.getValue("+userAttribute"));
        loadedUser.addPropertyChangeListener(e -> {
            assertEquals("userName", e.getPrevValue());
            assertEquals("newName", e.getValue());
        });
        loadedUser.setValue("+userAttribute", "newName");
    }
}
