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
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class PickerFieldDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeWithComponentListener() {
        PickerField pickerField = (PickerField) factory.createComponent(PickerField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = metadata.create(Group.class);
        group.setName("Test group");
        userDs.getItem().setGroup(group);
        pickerField.setDatasource(userDs, "group");

        // unbind
        pickerField.setDatasource(null, null);

        Component.ValueChangeListener listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        pickerField.addValueChangeListener(listener);
        userDs.getItem().setGroup(metadata.create(Group.class));
    }

    @Test
    public void testUnsubscribeWithDsListener() {
        PickerField pickerField = (PickerField) factory.createComponent(PickerField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = metadata.create(Group.class);
        group.setName("Test group");
        userDs.getItem().setGroup(group);
        pickerField.setDatasource(userDs, "group");

        // unbind
        pickerField.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<User> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(listener);
        pickerField.setValue(null);
    }

    @Test
    public void testValueChangeListener() {
        PickerField pickerField = (PickerField) factory.createComponent(PickerField.NAME);

        Datasource<User> userDs = getTestUserDatasource();

        User user = userDs.getItem();
        Group group = metadata.create(Group.class);
        group.setName("Test group");
        user.setGroup(group);

        // datasource before listener

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        pickerField.addValueChangeListener(listener);

        pickerField.setDatasource(userDs, "group");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        pickerField.setDatasource(null, null);
        pickerField.removeValueChangeListener(listener);
        valueWasChanged[0] = false;
        pickerField.setValue(null);

        // listener before datasource
        pickerField.addValueChangeListener(listener);
        pickerField.setDatasource(userDs, "group");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        PickerField pickerField = (PickerField) factory.createComponent(PickerField.NAME);

        Datasource<User> userDs = getTestUserDatasource();

        User user = userDs.getItem();
        Group group = metadata.create(Group.class);
        group.setName("Test group");
        user.setGroup(group);

        pickerField.setDatasource(userDs, "group");

        // unbind
        pickerField.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;

        // datasource before listener

        pickerField.setDatasource(userDs, "group");
        pickerField.addValueChangeListener(listener);

        user.setGroup(null);
        assertEquals(true, valueWasChanged[0]);

        // reset state
        pickerField.setDatasource(null, null);
        pickerField.removeValueChangeListener(listener);
        valueWasChanged[0] = false;
        user.setGroup(metadata.create(Group.class));

        // listener before datasource
        pickerField.addValueChangeListener(listener);
        pickerField.setDatasource(userDs, "group");

        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        PickerField pickerField = (PickerField) factory.createComponent(PickerField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        Group group = metadata.create(Group.class);
        group.setName("Test group");
        userDs.getItem().setGroup(group);
        pickerField.setDatasource(userDs, "group");

        // unbind
        pickerField.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> listener = e -> valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(listener);
        pickerField.setDatasource(userDs, "group");

        pickerField.setValue(null);
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        PickerField pickerField = (PickerField) factory.createComponent(PickerField.NAME);

        pickerField.setDatasource(null, null);
        pickerField.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        boolean exceptionWasThrown = false;
        try {
            pickerField.setDatasource(userDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;

        try {
            pickerField.setDatasource(null, "group");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        pickerField.setDatasource(userDs1, "group");
        pickerField.setDatasource(userDs1, "group");

        userDs1.getItem().setGroup(metadata.create(Group.class));
        pickerField.setDatasource(userDs1, "group");

        Datasource<User> userDs2 = getTestUserDatasource();
        pickerField.setDatasource(userDs2, "group");

        pickerField.setValue(null);
        assertNotNull(userDs1.getItem().getGroup());
    }
}
