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

package com.haulmont.cuba.severalfetchgroups;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.testmodel.severalfetchgroups.SeveralFetchGroups_Tariff;
import com.haulmont.cuba.testmodel.severalfetchgroups.SeveralFetchGroups_TariffVersion;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SeveralFetchGroupsTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;
    private DataManager dataManager;
    private UUID tariffId1, tariffId2_1, tariffId3_1, tariffId4_2;
    private UUID tariffVersionId1, tariffVersionId2, tariffVersionId3;

    @BeforeEach
    public void setUp() {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            SeveralFetchGroups_Tariff tariff1 = metadata.create(SeveralFetchGroups_Tariff.class);
            tariffId1 = tariff1.getId();
            tariff1.setName("tariff1");
            em.persist(tariff1);

            SeveralFetchGroups_Tariff tariff2_1 = metadata.create(SeveralFetchGroups_Tariff.class);
            tariffId2_1 = tariff2_1.getId();
            tariff2_1.setName("tariff2_1");
            tariff2_1.setParent(tariff1);
            em.persist(tariff2_1);

            SeveralFetchGroups_Tariff tariff3_1 = metadata.create(SeveralFetchGroups_Tariff.class);
            tariffId3_1 = tariff3_1.getId();
            tariff3_1.setName("tariff3_1");
            tariff3_1.setParent(tariff1);
            em.persist(tariff3_1);

            SeveralFetchGroups_Tariff tariff4_2 = metadata.create(SeveralFetchGroups_Tariff.class);
            tariffId4_2 = tariff4_2.getId();
            tariff4_2.setName("tariff4");
            tariff4_2.setParent(tariff2_1);
            em.persist(tariff4_2);

            SeveralFetchGroups_TariffVersion tariffVersion1 =
                    metadata.create(SeveralFetchGroups_TariffVersion.class);
            tariffVersionId1 = tariffVersion1.getId();
            tariffVersion1.setName("1");
            tariffVersion1.setDescription("tariffVersionDescription1");
            tariffVersion1.setParent(tariff1);
            tariff1.setActiveVersion(tariffVersion1);
            em.persist(tariffVersion1);

            SeveralFetchGroups_TariffVersion tariffVersion2 =
                    metadata.create(SeveralFetchGroups_TariffVersion.class);
            tariffVersionId2 = tariffVersion2.getId();
            tariffVersion2.setName("2");
            tariffVersion2.setDescription("tariffVersionDescription2");
            tariffVersion2.setParent(tariff4_2);
            tariff4_2.setActiveVersion(tariffVersion2);
            em.persist(tariffVersion2);

            SeveralFetchGroups_TariffVersion tariffVersion3 =
                    metadata.create(SeveralFetchGroups_TariffVersion.class);
            tariffVersionId3 = tariffVersion3.getId();
            tariffVersion3.setName("3");
            tariffVersion3.setDescription("tariffVersionDescription3");
            tariffVersion3.setParent(tariff2_1);
            tariff2_1.setActiveVersion(tariffVersion3);
            em.persist(tariffVersion3);

            tx.commit();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("update TEST_SEVERAL_FETCH_GROUPS_TARIFF set ACTIVE_VERSION_ID = null");
        cont.deleteRecord("TEST_SEVERAL_FETCH_GROUPS_TARIFF_VERSION", tariffVersionId3, tariffVersionId2, tariffVersionId1);
        cont.deleteRecord("TEST_SEVERAL_FETCH_GROUPS_TARIFF", tariffId4_2, tariffId3_1, tariffId2_1, tariffId1);
    }

    @Test
    public void testLoadTariffVersions() {
        LoadContext<SeveralFetchGroups_TariffVersion> loadContext = new LoadContext<>(SeveralFetchGroups_TariffVersion.class);
        loadContext.setQueryString("select e from test$SeveralFetchGroups_TariffVersion e order by e.name asc");
        loadContext.setView("tariffVersion.withParent");
        List<SeveralFetchGroups_TariffVersion> result = dataManager.loadList(loadContext);
        for (SeveralFetchGroups_TariffVersion version : result) {
            Assertions.assertNotNull(version.getParent());
        }
    }
}