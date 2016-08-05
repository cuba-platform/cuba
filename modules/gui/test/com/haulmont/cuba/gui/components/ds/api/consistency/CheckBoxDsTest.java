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

import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class CheckBoxDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeComponentListener() {
        CheckBox checkBox = (CheckBox) factory.createComponent(CheckBox.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setActive(true);
        checkBox.setDatasource(userDs, "active");

        // unbind
        checkBox.setDatasource(null, null);

        Component.ValueChangeListener valueChangeListener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        checkBox.addValueChangeListener(valueChangeListener);

        user.setActive(false);
        assertEquals(true, checkBox.getValue());
    }

    @Test
    public void testUnsubscribeDsListener() {
        CheckBox checkBox = (CheckBox) factory.createComponent(CheckBox.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setActive(true);
        checkBox.setDatasource(userDs, "active");

        // unbind
        checkBox.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<User> propertyChangeListener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(propertyChangeListener);

        checkBox.setValue(false);
        assertEquals(true, user.getActive());
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        CheckBox checkBox = (CheckBox) factory.createComponent(CheckBox.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setActive(true);
        checkBox.setDatasource(userDs, "active");

        // unbind
        checkBox.setDatasource(null, null);

        // datasource before listener
        checkBox.setDatasource(userDs, "active");
        assertEquals(true, checkBox.getValue());

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        checkBox.addValueChangeListener(listener);

        user.setActive(false);
        assertEquals(true, valueWasChanged[0]);
        assertEquals(false, checkBox.getValue());

        // reset state
        checkBox.removeValueChangeListener(listener);
        checkBox.setDatasource(null, null);
        valueWasChanged[0] = false;
        checkBox.setValue(true);

        // listener before datasource

        checkBox.addValueChangeListener(listener);
        checkBox.setDatasource(userDs, "active");
        assertEquals(true, valueWasChanged[0]);
        assertEquals(false, checkBox.getValue());
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        CheckBox checkBox = (CheckBox) factory.createComponent(CheckBox.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setActive(true);
        checkBox.setDatasource(userDs, "active");

        // unbind
        checkBox.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> listener = e -> valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(listener);
        checkBox.setDatasource(userDs, "active");

        checkBox.setValue(false);
        assertEquals(true, valueWasChanged[0]);
        assertEquals(false, user.getActive());
    }

    @Test
    public void testValueChangeListener() {
        CheckBox checkBox = (CheckBox) factory.createComponent(CheckBox.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setActive(true);

        // listener before datasource
        Boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        checkBox.addValueChangeListener(listener);

        checkBox.setDatasource(userDs, "active");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        checkBox.setDatasource(null, null);
        checkBox.removeValueChangeListener(listener);
        valueWasChanged[0] = false;

        // datasource before listener
        checkBox.setDatasource(userDs, "active");
        checkBox.addValueChangeListener(listener);

        user.setActive(false);
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        CheckBox checkBox = (CheckBox) factory.createComponent(CheckBox.NAME);

        checkBox.setDatasource(null, null);
        checkBox.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        checkBox.setDatasource(userDs1, "active");
        checkBox.setDatasource(userDs1, "active");

        boolean exceptionWasThrown = false;
        try {
            checkBox.setDatasource(userDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        exceptionWasThrown = false;
        try {
            checkBox.setDatasource(null, "active");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        userDs1.getItem().setActive(true);
        checkBox.setDatasource(userDs1, "active");

        Datasource<User> userDs2 = getTestUserDatasource();
        checkBox.setDatasource(userDs2, "active");

        checkBox.setValue(false);
        assertEquals(true, userDs1.getItem().getActive());
    }
}
