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
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.data.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.DataContextFactory;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class DcScreen2 extends AbstractWindow {

    @Inject
    private TextField<String> textField1;
    @Inject
    private Metadata metadata;
    @Inject
    private DataContextFactory dataContextFactory;
    @Inject
    private ComponentsFactory componentsFactory;
    @Inject
    private DataManager dataManager;

    private List<User> users;
    private int index;
    private CollectionContainer<User> container;
    private boolean reverse;
    private boolean filter;
    private Table<User> table;
    private CollectionLoader<User> loader;
    private DataContext dataContext;

    @Override
    public void init(Map<String, Object> params) {
        dataContext = dataContextFactory.createDataContext();

        table = componentsFactory.createComponent(Table.class);
        table.setWidthFull();
        add(table);
        expand(table);

        MetaClass metaClass = metadata.getClassNN(User.class);
        table.addColumn(new Table.Column(metaClass.getPropertyPath("login")));
        table.addColumn(new Table.Column(metaClass.getPropertyPath("name")));

        container = dataContextFactory.createCollectionContainer(User.class);

        loader = dataContextFactory.createCollectionLoader();
        loader.setDataContext(dataContext);
        loader.setContainer(container);
        loader.setQuery("select u from sec$User u order by u.name");
        loader.setView(View.LOCAL);
        loader.load();

//        users = dataManager.loadList(LoadContext.create(User.class)
//                .setQuery(LoadContext.createQuery("select u from sec$User u order by u.name"))
//                .setView(View.LOCAL));

//        container.setItems(users);

        table.setContainer(container);
        textField1.setValueSource(new ContainerValueSource<>(container, "name"));
    }

    public void sort() {
        reverse = !reverse;
        List<User> items = new ArrayList<>(container.getItems());
        items.sort(Comparator.nullsLast(Comparator.comparing(User::getName)));
        if (reverse)
            Collections.reverse(items);
        container.setItems(items);
    }

    public void filter() {
        filter = !filter;
        if (!filter) {
            loader.load();
        } else {
            List<User> items = container.getItems().stream()
                    .filter(user -> user.getLogin().startsWith("u"))
                    .collect(Collectors.toList());
            container.setItems(items);
        }
    }

    public void nextUser() {
        index = container.getItems().indexOf(container.getItem());
        index++;
        if (index > users.size() - 1)
            index = 0;

        container.setItem(users.get(index));
    }

    public void addUser() {
        Group group = dataManager.load(LoadContext.create(Group.class).setId(UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));

        User user = metadata.create(User.class);
        long millis = System.currentTimeMillis();
        user.setLogin("u-" + millis);
        user.setName("User-" + millis);
        user.setGroup(group);

        user = dataContext.merge(user);
        container.getMutableItems().add(user);

        table.setSelected(user);
    }

    public void removeUser() {
        User user = container.getItem();
        if (user != null) {
            container.getMutableItems().remove(user);
            dataContext.remove(user);
        }
    }

    public void saveChanges() {
        dataContext.commit();
    }
}
