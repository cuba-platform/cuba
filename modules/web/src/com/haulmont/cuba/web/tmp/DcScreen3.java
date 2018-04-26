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

package com.haulmont.cuba.web.tmp;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContextFactory;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import javax.inject.Inject;
import java.util.Map;

/**
 *
 */
public class DcScreen3 extends AbstractWindow {

    @Inject
    private TextField<String> textField1;
    @Inject
    private SplitPanel split;
    @Inject
    private Metadata metadata;
    @Inject
    private DataContextFactory dataContextFactory;
    @Inject
    private ComponentsFactory componentsFactory;

    private CollectionContainer<User> usersContainer;
    private CollectionContainer<UserRole> userRolesContainer;

    @Override
    public void init(Map<String, Object> params) {
        Table usersTable = componentsFactory.createComponent(Table.class);
        usersTable.setSizeFull();
        split.add(usersTable);

        Table userRolesTable = componentsFactory.createComponent(Table.class);
        userRolesTable.setSizeFull();
        split.add(userRolesTable);

        MetaClass userMetaClass = metadata.getClassNN(User.class);
        usersTable.addColumn(new Table.Column(userMetaClass.getPropertyPath("login")));
        usersTable.addColumn(new Table.Column(userMetaClass.getPropertyPath("name")));

        MetaClass userRoleMetaClass = metadata.getClassNN(UserRole.class);
        userRolesTable.addColumn(new Table.Column(userRoleMetaClass.getPropertyPath("role.name")));

        usersContainer = dataContextFactory.createCollectionContainer(User.class);
        userRolesContainer = dataContextFactory.createCollectionContainer(UserRole.class);

        CollectionLoader<User> loader = dataContextFactory.createCollectionLoader();
        loader.setContainer(usersContainer);
        loader.setQuery("select u from sec$User u order by u.name");
        loader.setView("user.edit");

        loader.load();

        usersContainer.addItemChangeListener(e -> {
            User user = e.getItem();
            userRolesContainer.setItems(user != null ? user.getUserRoles() : null);
        });

        usersTable.setContainer(usersContainer);
        userRolesTable.setContainer(userRolesContainer);
        
        textField1.setValueSource(new ContainerValueSource<>(usersContainer, "name"));
    }
}
