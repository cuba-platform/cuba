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

package com.haulmont.cuba.fetchjoin;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.FetchMode;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.testmodel.fetchjoin.*;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FetchJoinTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected JoinC joinC;
    protected JoinD joinD;
    protected JoinE joinE;
    protected JoinF joinF;
    protected JoinB joinB;
    protected JoinA joinA;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Metadata metadata = cont.metadata();

            joinF = metadata.create(JoinF.class);
            joinF.setName("joinF");
            em.persist(joinF);

            joinD = metadata.create(JoinD.class);
            joinD.setName("joinD");
            em.persist(joinD);

            joinE = metadata.create(JoinE.class);
            joinE.setName("joinE");
            joinE.setF(joinF);
            em.persist(joinE);

            joinC = metadata.create(JoinC.class);
            joinC.setName("joinC");
            joinC.setD(joinD);
            joinC.setE(joinE);
            em.persist(joinC);

            joinB = metadata.create(JoinB.class);
            joinB.setName("joinB");
            joinB.setC(joinC);
            em.persist(joinB);

            joinA = metadata.create(JoinA.class);
            joinA.setName("joinA");
            joinA.setB(joinB);
            em.persist(joinA);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(joinA, joinB, joinC, joinD, joinE, joinF);
    }

    @Test
    public void testNotLoadingJoinB() throws Exception {
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

            View fView = new View(JoinF.class).addProperty("name");
            View eView = new View(JoinE.class).addProperty("name").addProperty("f", fView, FetchMode.JOIN);
            View dView = new View(JoinD.class).addProperty("name");
            View cView = new View(JoinC.class).addProperty("name")
                    .addProperty("d", dView, FetchMode.JOIN)
                    .addProperty("e", eView, FetchMode.JOIN);
            View bView = new View(JoinB.class).addProperty("name")
                    .addProperty("c", cView, FetchMode.JOIN);
            View aView = new View(JoinA.class).addProperty("name")
                    .addProperty("b", bView, FetchMode.JOIN);

            JoinA loadedA = em.find(JoinA.class, joinA.getId(), aView);
            assertNotNull(loadedA);
            assertNotNull(loadedA.getB().getC().getD());
            assertNotNull(loadedA.getB().getC().getE());
            assertNotNull(loadedA.getB().getC().getE().getF());
            tx.commit();
        }
    }
}
