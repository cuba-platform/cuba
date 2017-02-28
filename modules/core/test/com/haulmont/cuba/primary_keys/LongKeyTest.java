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

package com.haulmont.cuba.primary_keys;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.testmodel.primary_keys.JoinedLongIdBar;
import com.haulmont.cuba.testmodel.primary_keys.JoinedLongIdFoo;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class LongKeyTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Persistence persistence;
    private Metadata metadata;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();
    }

    @Test
    public void testJoinedInheritance() throws Exception {
        JoinedLongIdFoo foo = metadata.create(JoinedLongIdFoo.class);
        JoinedLongIdBar bar = metadata.create(JoinedLongIdBar.class);
        assertNotEquals(foo.getId(), bar.getId());
        try (Transaction tx = persistence.createTransaction()) {
            persistence.getEntityManager().persist(foo);
            persistence.getEntityManager().persist(bar);
            tx.commit();
        }
    }
}
