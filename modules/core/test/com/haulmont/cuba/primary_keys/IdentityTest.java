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
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseIdentityIdEntity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.testmodel.primary_keys.IdentityEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.*;

public class IdentityTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;

    @Before
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from TEST_IDENTITY");

        metadata = cont.metadata();
        persistence = cont.persistence();
    }

    @Test
    public void testEquality() throws Exception {
        IdentityEntity e1 = metadata.create(IdentityEntity.class);
        assertNotNull(e1.getId());
        assertNotNull(e1.getId().getUuid());

        IdentityEntity e2 = metadata.create(IdentityEntity.class);
        assertNotEquals(e1, e2);

//        e2.setUuid(e1.getUuid());
//        assertEquals(e1, e2);
//        assertTrue(e1.hashCode() == e2.hashCode());

        Field idField = BaseIdentityIdEntity.class.getDeclaredField("id");
        idField.setAccessible(true);

        // e1 & e3 are different instances with the same UUID
        IdentityEntity e3 = TestSupport.reserialize(e1);
        // one of them has an Id, other has not - this is the case when a newly committed instance returns from
        // middleware to the client
        idField.set(e3, 100L);
        // they should be equal and with the same hashCode
        assertEquals(e1, e3);
        assertTrue(e1.hashCode() == e3.hashCode());

        // e1 & e3 are different instances with the same Id
        e1 = metadata.create(IdentityEntity.class);
        idField.set(e1, 100L);
        e2 = metadata.create(IdentityEntity.class);
        idField.set(e2, 100L);
        // they should be equal and with the same hashCode
        assertEquals(e1, e2);
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    @Test
    public void testPersistAndMerge() throws Exception {
        IdentityEntity foo = metadata.create(IdentityEntity.class);
        foo.setName("foo");
        foo.setEmail("foo@mail.com");

        UUID uuid = foo.getId().getUuid();
        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            tx.commit();
        }
        assertNotNull(foo.getId().get());
        assertEquals(uuid, foo.getId().getUuid());

        foo.setName("bar");
        IdentityEntity bar;
        try (Transaction tx = persistence.createTransaction()) {
            bar = persistence.getEntityManager().merge(foo);
            tx.commit();
        }
        assertEquals(uuid, foo.getId().getUuid());
        assertEquals(foo, bar);

        bar.setName("baz");
        IdentityEntity baz;
        try (Transaction tx = persistence.createTransaction()) {
            baz = persistence.getEntityManager().merge(foo);
            tx.commit();
        }
        assertEquals(uuid, foo.getId().getUuid());
        assertEquals(foo, baz);
    }

    @Test
    public void testFindAndDelete() throws Exception {
        IdentityEntity foo = metadata.create(IdentityEntity.class);
        foo.setName("foo");
        foo.setEmail("foo@mail.com");

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            tx.commit();
        }

        IdentityEntity loaded;
        try (Transaction tx = persistence.createTransaction()) {
            loaded = persistence.getEntityManager().find(IdentityEntity.class, foo.getId());
            tx.commit();
        }
        assertEquals(foo, loaded);

        try (Transaction tx = persistence.createTransaction()) {
            loaded = persistence.getEntityManager().find(IdentityEntity.class, loaded.getId());
            persistence.getEntityManager().remove(loaded);
            tx.commit();
        }
        assertTrue(BaseEntityInternalAccess.isRemoved(loaded));

        try (Transaction tx = persistence.createTransaction()) {
            loaded = persistence.getEntityManager().find(IdentityEntity.class, loaded.getId());
            assertNull(loaded);
            tx.commit();
        }
    }

    @Test
    public void testQueryById() throws Exception {
        IdentityEntity foo = metadata.create(IdentityEntity.class);
        foo.setName("foo");
        foo.setEmail("foo@mail.com");

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            tx.commit();
        }

        IdentityEntity loaded;
        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<IdentityEntity> query = persistence.getEntityManager().createQuery(
                    "select e from test$IdentityEntity e where e.id = ?1", IdentityEntity.class);
            query.setParameter(1, foo.getId());
            loaded = query.getSingleResult();
            tx.commit();
        }
        assertEquals(foo, loaded);

        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<IdentityEntity> query = persistence.getEntityManager().createQuery(
                    "select e from test$IdentityEntity e where e.id = :id", IdentityEntity.class);
            query.setParameter("id", foo.getId());
            loaded = query.getSingleResult();
            tx.commit();
        }
        assertEquals(foo, loaded);
    }

    @Test
    public void testLoadByIdValue() throws Exception {
        IdentityEntity foo = metadata.create(IdentityEntity.class);
        foo.setName("foo");
        foo.setEmail("foo@mail.com");

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            tx.commit();
        }
        Long idVal = foo.getId().get();

        IdentityEntity loaded;
        try (Transaction tx = persistence.createTransaction()) {
            loaded = persistence.getEntityManager().find(IdentityEntity.class, IdProxy.of(idVal));
            tx.commit();
        }
        assertEquals(foo, loaded);

        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<IdentityEntity> query = persistence.getEntityManager().createQuery(
                    "select e from test$IdentityEntity e where e.id = :id", IdentityEntity.class);
            query.setParameter("id", idVal);
            loaded = query.getSingleResult();
            tx.commit();
        }
        assertEquals(foo, loaded);

        try (Transaction tx = persistence.createTransaction()) {
            TypedQuery<IdentityEntity> query = persistence.getEntityManager().createQuery(
                    "select e from test$IdentityEntity e where e.id = :id", IdentityEntity.class);
            query.setParameter("id", IdProxy.of(idVal));
            loaded = query.getSingleResult();
            tx.commit();
        }
        assertEquals(foo, loaded);
    }
}
