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

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.value.CollectionContainerTableSource;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContextFactory;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class DcScreen4 extends AbstractWindow {

    @Inject
    private Metadata metadata;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private ScreenData screenData;

    private CollectionContainer<User> usersContainer;

    @Inject
    private Filter usersFilter;

    @Inject
    private TextField<String> loginField;

    @Inject
    private CheckBox activeField;

    @Override
    public void init(Map<String, Object> params) {
        screenData.load(getXmlDescriptor().element("data"));

        Table<User> usersTable = componentsFactory.createComponent(Table.class);
        usersTable.setSizeFull();
        add(usersTable);
        expand(usersTable);

        MetaClass userMetaClass = metadata.getClassNN(User.class);
        usersTable.addColumn(new Table.Column<>(userMetaClass.getPropertyPath("login")));
        usersTable.addColumn(new Table.Column<>(userMetaClass.getPropertyPath("name")));

        CollectionLoader<User> usersLoader = screenData.getLoader("usersLoader");
        usersLoader.setParameter("groupId", UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

        usersContainer = screenData.getContainer("usersCont");
        usersTable.setTableSource(new CollectionContainerTableSource<>(usersContainer));

        loginField.addValueChangeListener(e -> {
            usersLoader.setParameter("login", Strings.isNullOrEmpty((String) e.getValue()) ? null : "(?i)%" + e.getValue() + "%");
            usersLoader.load();
        });

        activeField.addValueChangeListener(e -> {
            usersLoader.setParameter("active", Boolean.TRUE.equals(e.getValue()) ? true : null);
            usersLoader.load();
        });

        usersFilter.setDataLoader(usersLoader);
    }
}
