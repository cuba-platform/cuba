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
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.testmodel.primary_keys.IdentityUuidEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IdentityUuidTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;

    @Before
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
}
