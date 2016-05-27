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
 *
 */
package com.haulmont.cuba.core;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.utils.PrintUtils;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.security.entity.EntityLogItem;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class MetadataTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void test() {
        Session session = AppBeans.get(Metadata.class).getSession();
        assertNotNull(session);

        Collection<MetaModel> models = session.getModels();
        for (MetaModel model : models) {
            System.out.println("Model: " + model.getName());
            System.out.println(PrintUtils.printClassHierarchy(model));
        }
    }

    @Test
    public void testPersistentAndTransientProperties() throws Exception {
        MetadataTools tools = cont.metadata().getTools();

        // User
        MetaClass metaClass = cont.metadata().getSession().getClassNN(User.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("login")));
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("group")));
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("userRoles")));

        // EntityLogItem
        metaClass = cont.metadata().getSession().getClassNN(EntityLogItem.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("user")));
        assertFalse(tools.isPersistent(metaClass.getPropertyNN("attributes")));
        assertTrue(tools.isTransient(metaClass.getPropertyNN("attributes")));

        // Folder
        metaClass = cont.metadata().getSession().getClassNN(Folder.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("name")));
        assertTrue(tools.isTransient(new Folder(), "itemStyle"));

        // UserSessionEntity
        metaClass = cont.metadata().getSession().getClassNN(UserSessionEntity.class);
        assertTrue(tools.isTransient(metaClass.getPropertyNN("login")));
    }

    @Test
    public void testSystemLevel() throws Exception {
        MetadataTools tools = cont.metadata().getTools();

        assertTrue(tools.isSystemLevel(cont.metadata().getSession().getClassNN(UserRole.class)));

        MetaClass metaClass = cont.metadata().getSession().getClassNN(User.class);
        assertTrue(tools.isSystemLevel(metaClass.getPropertyNN("password")));
    }
}