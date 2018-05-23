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

package com.haulmont.cuba.gui.components.ds.api.consistency;

import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.AbstractComponentTestCase;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import mockit.Mocked;
import mockit.Expectations;
import org.junit.Ignore;

import java.util.UUID;

public class DsApiConsistencyTestCase extends AbstractComponentTestCase {

    @Mocked
    PersistenceManagerService persistenceManagerService;

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new Expectations() {
            {
                AppBeans.get(PersistenceManagerClient.NAME); result = persistenceManagerService; minTimes = 0;
                AppBeans.get(PersistenceManagerClient.class); result = persistenceManagerService; minTimes = 0;
                AppBeans.get(PersistenceManagerClient.NAME, PersistenceManagerClient.class); result = persistenceManagerService; minTimes = 0;

                persistenceManagerService.getMaxFetchUI(anyString); result = 10000; minTimes = 0;

                clientConfig.getPickerShortcutModifiers(); result = "CTRL-ALT"; minTimes = 0;
            }
        };
    }

    protected Datasource<User> getTestUserDatasource() {
        // noinspection unchecked
        Datasource<User> datasource = new DsBuilder()
                .setId("userDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        User user = metadata.create(User.class);
        datasource.setItem(user);
        ((DatasourceImpl) datasource).valid();

        return datasource;
    }

    protected Datasource<Role> getTestRoleDatasource() {
        // noinspection unchecked
        Datasource<Role> roleDs = (Datasource<Role>) new DsBuilder()
                .setId("roleDs")
                .setJavaClass(Role.class)
                .setView(viewRepository.getView(Role.class, View.LOCAL))
                .buildDatasource();
        roleDs.refresh();

        Role role = metadata.create(Role.class);
        roleDs.setItem(role);
        ((DatasourceImpl) roleDs).valid();

        return roleDs;
    }

    protected CollectionDatasource<Group, UUID> getTestCollectionDatasource() {
        // noinspection unchecked
        CollectionDatasource<Group, UUID> collectionDatasource = new DsBuilder()
                .setId("testDs")
                .setJavaClass(Group.class)
                .setView(viewRepository.getView(Group.class, View.LOCAL))
                .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                .setAllowCommit(false)
                .buildCollectionDatasource();

        for (int i = 0; i < 3; i++) {
            Group group = metadata.create(Group.class);
            group.setName("Group #" + (i + 1));

            Group parentGroup = metadata.create(Group.class);
            parentGroup.setName("Parent group #" + (i + 1));
            group.setParent(parentGroup);

            collectionDatasource.addItem(group);
        }
        ((CollectionDatasourceImpl) collectionDatasource).valid();

        return collectionDatasource;
    }
}