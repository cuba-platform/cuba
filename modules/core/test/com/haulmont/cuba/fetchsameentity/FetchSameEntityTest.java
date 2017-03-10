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

package com.haulmont.cuba.fetchsameentity;


import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.testmodel.fetchsameentity.FetchSameLinkAEntity;
import com.haulmont.cuba.testmodel.fetchsameentity.FetchSameLinkBEntity;
import com.haulmont.cuba.testmodel.fetchsameentity.FetchSameMainEntity;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class FetchSameEntityTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected FetchSameMainEntity mainEntity;
    protected FetchSameLinkBEntity linkB1, linkB2;
    protected FetchSameLinkAEntity linkA1, linkA2;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            Metadata metadata = cont.metadata();

            mainEntity = metadata.create(FetchSameMainEntity.class);
            mainEntity.setName("mainEntity");
            mainEntity.setDescription("mainDescription");
            em.persist(mainEntity);

            linkB1 = metadata.create(FetchSameLinkBEntity.class);
            linkB1.setName("linkB1");
            linkB1.setMainEntity(mainEntity);
            em.persist(linkB1);

            linkB2 = metadata.create(FetchSameLinkBEntity.class);
            linkB2.setName("linkB2");
            linkB2.setMainEntity(mainEntity);
            em.persist(linkB2);

            linkA1 = metadata.create(FetchSameLinkAEntity.class);
            linkA1.setName("linkA1");
            linkA1.setDescription("descriptionA1");
            linkA1.setMainEntity(mainEntity);
            em.persist(linkA1);

            linkA2 = metadata.create(FetchSameLinkAEntity.class);
            linkA2.setName("linkA2");
            linkA2.setDescription("descriptionA2");
            em.persist(linkA2);

            linkB1.setLinkAEntity(linkA1);
            linkB2.setLinkAEntity(linkA2);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(linkB1, linkB2, linkA1, linkA2, mainEntity);
    }

    @Test
    public void testUnFetched() throws Exception {
        DataManager dataManager = AppBeans.get(DataManager.class);

        View a1View = new View(FetchSameLinkAEntity.class)
                .addProperty("name")
                .addProperty("mainEntity", new View(FetchSameMainEntity.class));

        View a2View = new View(FetchSameLinkAEntity.class)
                .addProperty("description");

        View bView = new View(FetchSameLinkBEntity.class)
                .addProperty("name")
                .addProperty("linkAEntity", a2View);

        View mainView = new View(FetchSameMainEntity.class)
                .addProperty("description")
                .addProperty("linkAEntities", a1View)
                .addProperty("linkBEntities", bView);


        LoadContext<FetchSameMainEntity> lc = new LoadContext<>(FetchSameMainEntity.class)
                .setId(mainEntity.getId()).setView(mainView);

        FetchSameMainEntity reloaded = dataManager.load(lc);
        reloaded.getLinkAEntities().size();
    }
}
