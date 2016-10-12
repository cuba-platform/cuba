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
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.*;

public class DynamicAttributesTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected DataManager dataManager;
    protected Metadata metadata;
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    protected Category userCategory, userRoleCategory;
    protected CategoryAttribute userAttribute, userRoleAttribute;
    protected Group group;

    protected User user;
    protected UserRole userRole;
    protected Role role;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
        metadata = AppBeans.get(Metadata.class);
        dynamicAttributesManagerAPI = AppBeans.get(DynamicAttributesManagerAPI.class);

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

            user = metadata.create(User.class);
            user.setName("user");
            user.setLogin("user");
            user.setGroup(group);
            em.persist(user);

            role = metadata.create(Role.class);
            role.setName("role");
            em.persist(role);

            userRole = metadata.create(UserRole.class);
            userRole.setUser(user);
            userRole.setRole(role);
            em.persist(userRole);

            tx.commit();
        }

        dynamicAttributesManagerAPI.loadCache();

        group = metadata.create(Group.class);

        user = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true));
        user.setValue("+userAttribute", "userName");
        dataManager.commit(user);

        userRole = dataManager.load(LoadContext.create(UserRole.class).setId(userRole.getId()).setLoadDynamicAttributes(true));
        userRole.setValue("+userRoleAttribute", "userRole");
        dataManager.commit(userRole);
    }

    @After
    public void tearDown() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_ATTR_VALUE");
        cont.deleteRecord(userRole, role, user, group);
        cont.deleteRecord(userAttribute, userRoleAttribute);
        cont.deleteRecord(userCategory, userRoleCategory);
    }

    @Test
    public void testDynamicAttributes() {
        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true));
        Assert.assertEquals("userName", loadedUser.getValue("+userAttribute"));
    }

    @Test
    public void testDynamicAttributesWithLocalView() {
        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true).setView(View.LOCAL));
        Assert.assertEquals("userName", loadedUser.getValue("+userAttribute"));
    }

    @Test
    public void testDynamicAttributesWithNestedDynamicAttributes() {
        View view = new View(User.class, "testView")
                .addProperty("login")
                .addProperty("userRoles",
                        new View(UserRole.class, "testView"));

        User loadedUser = dataManager.load(LoadContext.create(User.class).setId(user.getId()).setLoadDynamicAttributes(true).setView(view));
        Assert.assertEquals("userName", loadedUser.getValue("+userAttribute"));
        Assert.assertEquals("userRole", loadedUser.getUserRoles().get(0).getValue("+userRoleAttribute"));
    }
}
