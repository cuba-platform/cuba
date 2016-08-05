/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.ds.api.consistency;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SearchField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class SearchFieldDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeComponentListener() {
        SearchField searchField = factory.createComponent(SearchField.class);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        searchField.setOptionsDatasource(groupsDs);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = groupsDs.getItems().iterator().next();
        User user = userDs.getItem();
        user.setGroup(group);
        searchField.setDatasource(userDs, "group");

        // unbind
        searchField.setDatasource(null, null);

        Component.ValueChangeListener listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        searchField.addValueChangeListener(listener);

        user.setGroup(metadata.create(Group.class));
    }

    @Test
    public void testUnsubscribeDsListener() {
        SearchField searchField = factory.createComponent(SearchField.class);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        searchField.setOptionsDatasource(groupsDs);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = groupsDs.getItems().iterator().next();
        userDs.getItem().setGroup(group);
        searchField.setDatasource(userDs, "group");

        // unbind
        searchField.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<User> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(listener);

        searchField.setValue(metadata.create(Group.class));
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        SearchField searchField = factory.createComponent(SearchField.class);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        searchField.setOptionsDatasource(groupsDs);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = groupsDs.getItems().iterator().next();
        User user = userDs.getItem();
        user.setGroup(group);
        searchField.setDatasource(userDs, "group");

        // unbind
        searchField.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;

        // datasource before listener
        searchField.setDatasource(userDs, "group");
        searchField.addValueChangeListener(listener);

        user.setGroup(metadata.create(Group.class));
        assertEquals(true, valueWasChanged[0]);

        // reset state

        valueWasChanged[0] = false;
        searchField.setDatasource(null, null);
        searchField.removeValueChangeListener(listener);
        searchField.setValue(null);

        // listener before datasource

        searchField.addValueChangeListener(listener);
        searchField.setDatasource(userDs, "group");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        SearchField searchField = factory.createComponent(SearchField.class);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        searchField.setOptionsDatasource(groupsDs);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = groupsDs.getItems().iterator().next();
        userDs.getItem().setGroup(group);
        searchField.setDatasource(userDs, "group");

        // unbind
        searchField.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> listener = e -> valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(listener);

        searchField.setDatasource(userDs, "group");
        searchField.setValue(null);
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testValueChangeListener() {
        SearchField searchField = factory.createComponent(SearchField.class);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        searchField.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setGroup(groups.get(0));

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        searchField.addValueChangeListener(listener);

        searchField.setDatasource(userDs, "group");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        SearchField searchField = (SearchField) factory.createComponent(SearchField.NAME);

        searchField.setDatasource(null, null);
        searchField.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        boolean exceptionWasThrown = false;
        try {
            searchField.setDatasource(userDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;

        try {
            searchField.setDatasource(null, "name");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        searchField.setDatasource(userDs1, "name");
        searchField.setDatasource(userDs1, "name");

        userDs1.getItem().setName("Test name");
        searchField.setDatasource(userDs1, "name");

        Datasource<User> userDs2 = getTestUserDatasource();
        searchField.setDatasource(userDs2, "name");

        searchField.setValue(null);
        assertEquals("Test name", userDs1.getItem().getName());
    }
}
