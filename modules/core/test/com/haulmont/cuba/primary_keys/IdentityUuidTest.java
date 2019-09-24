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
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseIdentityIdEntity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.testmodel.primary_keys.IdentityUuidEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IdentityUuidTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;

    @BeforeEach
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from TEST_IDENTITY_UUID");

        metadata = cont.metadata();
        persistence = cont.persistence();
    }

    @Test
    public void test() throws Exception {
        IdentityUuidEntity foo = metadata.create(IdentityUuidEntity.class);
        foo.setName("foo");

        UUID uuid = foo.getUuid();
        assertEquals(uuid, foo.getId().getUuid());

        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            tx.commit();
        }
        assertNotNull(foo.getId().get());
        assertEquals(uuid, foo.getUuid());

        IdentityUuidEntity loaded;
        try (Transaction tx = persistence.createTransaction()) {
            loaded = persistence.getEntityManager().find(IdentityUuidEntity.class, foo.getId());
            tx.commit();
        }
        assertNotNull(loaded);
        assertEquals(foo, loaded);
        assertEquals(uuid, loaded.getUuid());
        assertEquals(uuid, loaded.getId().getUuid());

        loaded.setName("bar");
        IdentityUuidEntity merged;
        try (Transaction tx = persistence.createTransaction()) {
            merged = persistence.getEntityManager().merge(loaded);
            tx.commit();
        }
        assertEquals("bar", merged.getName());
        assertEquals(foo, merged);
        assertEquals(uuid, merged.getUuid());
        assertEquals(uuid, merged.getId().getUuid());
    }

    @Test
    public void testEquality() throws Exception {
        IdentityUuidEntity e1 = metadata.create(IdentityUuidEntity.class);
        assertNotNull(e1.getId());
        assertNotNull(e1.getId().getUuid());

        IdentityUuidEntity e2 = metadata.create(IdentityUuidEntity.class);
        assertNotEquals(e1, e2);

        Field idField = BaseIdentityIdEntity.class.getDeclaredField("id");
        idField.setAccessible(true);

        // e1 & e3 are different instances with the same UUID
        IdentityUuidEntity e3 = TestSupport.reserialize(e1);
        // one of them has an Id, other has not - this is the case when a newly committed instance returns from
        // middleware to the client
        idField.set(e3, 100L);
        // they should be equal and with the same hashCode
        assertEquals(e1, e3);
        assertTrue(e1.hashCode() == e3.hashCode());

        // e1 & e3 are different instances with the same Id
        e1 = metadata.create(IdentityUuidEntity.class);
        idField.set(e1, 100L);
        e2 = metadata.create(IdentityUuidEntity.class);
        idField.set(e2, 100L);
        // UUIDs are equal as if loaded from DB
        e2.setUuid(e1.getUuid());
        // they should be equal and with the same hashCode
        assertEquals(e1, e2);
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    @Test
    public void testSavingInHashTables() throws Exception {
        Field idField = BaseIdentityIdEntity.class.getDeclaredField("id");
        idField.setAccessible(true);

        IdentityUuidEntity e1 = metadata.create(IdentityUuidEntity.class);
        idField.set(e1, 100L);

        Map<Object, IdentityUuidEntity> map = new HashMap<>();
        map.put(e1.getId(), e1);

        IdProxy<Long> id = IdProxy.of(100L, e1.getUuid());

        IdentityUuidEntity entity = map.get(id);
        assertSame(e1, entity);
    }

}
