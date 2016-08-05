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

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupPickerField;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class LookupPickerFieldDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeComponentListener() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setGroup(groups.get(0));

        component.setDatasource(userDs, "group");

        // unbind
        component.setDatasource(null, null);

        Component.ValueChangeListener listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        component.addValueChangeListener(listener);

        user.setGroup(metadata.create(Group.class));
    }

    @Test
    public void testUnsubscribeDsListener() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        userDs.getItem().setGroup(groups.get(0));

        component.setDatasource(userDs, "group");

        // unbind
        component.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<User> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(listener);

        component.setValue(metadata.create(Group.class));
    }

    @Test
    public void testOptionsDsUnsubscribe() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setGroup(groups.get(0));

        component.setDatasource(userDs, "group");

        //unbind
        component.setOptionsDatasource(null);

        Datasource.ItemChangeListener<Group> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        groupsDs.addItemChangeListener(listener);

        component.setValue(groups.get(1));
    }

    @Test
    public void testSetValueWithoutOptions() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        Datasource<User> userDs = getTestUserDatasource();

        User user = userDs.getItem();
        Group group = metadata.create(Group.class);
        group.setName("group #0");
        user.setGroup(group);

        component.setDatasource(userDs, "group");

        assertNotNull(component.getValue());
    }

    @Test
    public void testValueChangeListener() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setGroup(groups.get(0));

        // listener before datasource

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        component.addValueChangeListener(listener);

        component.setDatasource(userDs, "group");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        valueWasChanged[0] = false;
        component.removeValueChangeListener(listener);
        component.setDatasource(null, null);

        // datasource before listener

        component.setDatasource(userDs, "group");
        component.addValueChangeListener(listener);
        user.setGroup(null);
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setGroup(groups.get(0));

        component.setDatasource(userDs, "group");

        // unbind
        component.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;

        // datasource before listener

        component.setDatasource(userDs, "group");
        component.addValueChangeListener(listener);
        user.setGroup(metadata.create(Group.class));
        assertEquals(true, valueWasChanged[0]);

        // reset state
        component.setDatasource(null, null);
        component.removeValueChangeListener(listener);
        valueWasChanged[0] = false;
        component.setValue(null);

        // listener before datasource
        component.addValueChangeListener(listener);
        component.setDatasource(userDs, "group");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        userDs.getItem().setGroup(groups.get(0));

        component.setDatasource(userDs, "group");

        // unbind
        component.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> listener = e -> valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(listener);
        component.setDatasource(userDs, "group");

        component.setValue(null);
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeOptions() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        CollectionDatasource<Group, UUID> groupsDs = getTestCollectionDatasource();
        component.setOptionsDatasource(groupsDs);
        List<Group> groups = new ArrayList<>(groupsDs.getItems());

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setGroup(groups.get(0));

        component.setDatasource(userDs, "group");

        //unbind
        component.setOptionsDatasource(null);

        Datasource.ItemChangeListener<Group> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        groupsDs.addItemChangeListener(listener);

        component.setValue(groups.get(1));

        //bind
        component.setOptionsDatasource(groupsDs);

        groupsDs.removeItemChangeListener(listener);
        boolean[] valueWasChanged = {false};
        listener = e -> valueWasChanged[0] = true;
        groupsDs.addItemChangeListener(listener);

        component.setValue(groups.get(2));
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        LookupPickerField component = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);

        component.setDatasource(null, null);
        component.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        boolean exceptionWasThrown = false;
        try {
            component.setDatasource(userDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;
        try {
            component.setDatasource(null, "group");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        component.setDatasource(userDs1, "group");
        component.setDatasource(userDs1, "group");

        userDs1.getItem().setGroup(metadata.create(Group.class));
        component.setDatasource(userDs1, "group");

        Datasource<User> userDs2 = getTestUserDatasource();
        component.setDatasource(userDs2, "group");

        component.setValue(null);
        assertNotNull(userDs1.getItem().getGroup());
    }
}