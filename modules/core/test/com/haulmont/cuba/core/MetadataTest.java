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
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.testmodel.not_persistent.TestNotPersistentEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class MetadataTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
    }

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
    public void testPersistentAndTransientEntities() throws Exception {
        MetadataTools metadataTools = cont.metadata().getTools();

        assertTrue(metadataTools.isPersistent(User.class));
        assertFalse(metadataTools.isTransient(User.class));

        assertFalse(metadataTools.isPersistent(LockInfo.class));
        assertTrue(metadataTools.isTransient(LockInfo.class));

        assertFalse(metadataTools.isPersistent(TestNotPersistentEntity.class));
        assertTrue(metadataTools.isNotPersistent(TestNotPersistentEntity.class));
    }

    @Test
    public void testPersistentAndTransientProperties() throws Exception {
        MetadataTools tools = cont.metadata().getTools();

        // User
        MetaClass metaClass = cont.metadata().getSession().getClassNN(User.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("id")));
        assertTrue(tools.isPersistent(metaClass, metaClass.getPropertyNN("id")));
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("createTs")));
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("login")));
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("group")));
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("userRoles")));

        // EntityLogItem
        metaClass = cont.metadata().getSession().getClassNN(EntityLogItem.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("user")));
        assertFalse(tools.isPersistent(metaClass.getPropertyNN("attributes")));
        assertTrue(tools.isNotPersistent(metaClass.getPropertyNN("attributes")));

        // Folder
        metaClass = cont.metadata().getSession().getClassNN(Folder.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("name")));
        assertTrue(tools.isNotPersistent(new Folder(), "itemStyle"));

        // UserSessionEntity
        metaClass = cont.metadata().getSession().getClassNN(UserSessionEntity.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("id"))); // see JavaDocs on isPersistent() for the reason
        assertTrue(tools.isNotPersistent(metaClass, metaClass.getPropertyNN("id")));
        assertTrue(tools.isNotPersistent(metadata.create(metaClass), "id"));
        assertTrue(tools.isNotPersistent(metaClass.getPropertyNN("login")));
        assertTrue(tools.isNotPersistent(metaClass, metaClass.getPropertyNN("login")));

        // TestTransientEntity
        metaClass = cont.metadata().getSession().getClassNN(TestNotPersistentEntity.class);
        assertTrue(tools.isPersistent(metaClass.getPropertyNN("id"))); // see JavaDocs on isPersistent() for the reason
        assertTrue(tools.isNotPersistent(metaClass, metaClass.getPropertyNN("id")));
        assertTrue(tools.isNotPersistent(metadata.create(metaClass), "id"));
        assertTrue(tools.isNotPersistent(metaClass.getPropertyNN("name")));
        assertTrue(tools.isNotPersistent(metaClass.getPropertyNN("info")));
        assertTrue(tools.isNotPersistent(metaClass.getPropertyNN("embeddedRef")));
        assertFalse(tools.isEmbedded(metaClass.getPropertyNN("embeddedRef")));
    }

    @Test
    public void testSystemLevel() throws Exception {
        MetadataTools tools = metadata.getTools();

        assertTrue(tools.isSystemLevel(metadata.getClassNN(UserRole.class)));

        assertTrue(tools.isSystemLevel(metadata.getClassNN(AbstractNotPersistentEntity.class)));
        assertFalse(tools.isSystemLevel(metadata.getClassNN(TestNotPersistentEntity.class)));

        MetaClass metaClass = metadata.getClassNN(User.class);
        assertTrue(tools.isSystemLevel(metaClass.getPropertyNN("password")));

        assertTrue(tools.isSystemLevel(metadata.getClassNN(SearchFolder.class)));
    }
}