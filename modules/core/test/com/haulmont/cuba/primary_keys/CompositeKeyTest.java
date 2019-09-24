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

package com.haulmont.cuba.primary_keys;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.testmodel.primary_keys.CompositeKeyEntity;
import com.haulmont.cuba.testmodel.primary_keys.EntityKey;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompositeKeyTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;

    @BeforeEach
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from TEST_COMPOSITE_KEY");

        metadata = cont.metadata();
        persistence = cont.persistence();
    }

    @Test
    public void testOperations() throws Exception {
        CompositeKeyEntity foo = metadata.create(CompositeKeyEntity.class);

        EntityKey entityKey = metadata.create(EntityKey.class);
        entityKey.setTenant(1);
        entityKey.setEntityId(10L);

        foo.setId(entityKey);
        foo.setName("foo");
        foo.setEmail("foo@mail.com");

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            tx.commit();
        }

        CompositeKeyEntity loadedFoo;
        try (Transaction tx = persistence.createTransaction()) {
            loadedFoo = persistence.getEntityManager().find(CompositeKeyEntity.class, entityKey);
            tx.commit();
        }
        assertNotNull(loadedFoo);
        assertEquals(foo, loadedFoo);

        loadedFoo.setName("bar");
        CompositeKeyEntity bar;
        try (Transaction tx = persistence.createTransaction()) {
            bar = persistence.getEntityManager().merge(loadedFoo);
            tx.commit();
        }
        assertEquals(foo, bar);

        CompositeKeyEntity loadedBar;
        try (Transaction tx = persistence.createTransaction()) {
            loadedBar = persistence.getEntityManager().find(CompositeKeyEntity.class, entityKey);
            tx.commit();
        }
        assertNotNull(loadedBar);
        assertEquals("bar", loadedBar.getName());

        try (Transaction tx = persistence.createTransaction()) {
            loadedBar = persistence.getEntityManager().find(CompositeKeyEntity.class, entityKey);
            persistence.getEntityManager().remove(loadedBar);
            tx.commit();
        }
        assertTrue(BaseEntityInternalAccess.isRemoved(loadedBar));

        try (Transaction tx = persistence.createTransaction()) {
            loadedBar = persistence.getEntityManager().find(CompositeKeyEntity.class, entityKey);
            assertNull(loadedBar);
            tx.commit();
        }
    }

    @Test
    public void testListParameter() throws Exception {
        CompositeKeyEntity foo1 = metadata.create(CompositeKeyEntity.class);

        EntityKey entityKey1 = metadata.create(EntityKey.class);
        entityKey1.setTenant(1);
        entityKey1.setEntityId(10L);

        foo1.setId(entityKey1);
        foo1.setName("foo1");
        foo1.setEmail("foo1@mail.com");

        CompositeKeyEntity foo2 = metadata.create(CompositeKeyEntity.class);

        EntityKey entityKey2 = metadata.create(EntityKey.class);
        entityKey2.setTenant(2);
        entityKey2.setEntityId(20L);

        foo2.setId(entityKey2);
        foo2.setName("foo2");
        foo2.setEmail("foo2@mail.com");

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo1);
            persistence.getEntityManager().persist(foo2);
            tx.commit();
        }

        try (Transaction tx = persistence.createTransaction()) {
            Query query = persistence.getEntityManager().createQuery("select e from test$CompositeKeyEntity e " +
                    "where e.id.tenant in :list1 and e.id.entityId in :list2");
            query.setParameter("list1", Arrays.asList(entityKey1.getTenant(), entityKey2.getTenant()));
            query.setParameter("list2", Arrays.asList(entityKey1.getEntityId(), entityKey2.getEntityId()));
            List resultList = query.getResultList();
            assertTrue(resultList.contains(foo1) && resultList.contains(foo2));

            tx.commit();
        }

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().remove(foo1);
            persistence.getEntityManager().remove(foo2);
            tx.commit();
        }
    }

    @Test
    public void testMetadata() throws Exception {
        MetaClass metaClass = metadata.getClassNN(CompositeKeyEntity.class);

        String primaryKeyName = metadata.getTools().getPrimaryKeyName(metaClass);
        assertEquals("id", primaryKeyName);

        MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
        assertNotNull(primaryKeyProperty);
        assertEquals("id", primaryKeyProperty.getName());

        assertTrue(metadata.getTools().isEmbedded(metaClass.getPropertyNN("id")));
    }
}
