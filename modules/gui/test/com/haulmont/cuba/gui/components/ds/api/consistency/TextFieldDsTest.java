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
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class TextFieldDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeComponentListener() {
        TextField textField = (TextField) factory.createComponent(TextField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        textField.setDatasource(userDs, "name");

        // unbind
        textField.setDatasource(null, null);

        Component.ValueChangeListener listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        textField.addValueChangeListener(listener);
        user.setName("anotherName");
    }

    @Test
    public void testUnsubscribeDsListener() {
        TextField textField = (TextField) factory.createComponent(TextField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        userDs.getItem().setName("testName");
        textField.setDatasource(userDs, "name");

        // unbind
        textField.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<User> propertyChangeListener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(propertyChangeListener);

        textField.setValue("anotherName");
    }

    @Test
    public void testValueChangeListener() {
        TextField textField = (TextField) factory.createComponent(TextField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        userDs.getItem().setName("testName");

        // listener before datasource

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        textField.addValueChangeListener(listener);

        textField.setDatasource(userDs, "name");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        valueWasChanged[0] = false;
        textField.removeValueChangeListener(listener);
        textField.setDatasource(null, null);

        // datasource before listener
        textField.setDatasource(userDs, "name");
        textField.addValueChangeListener(listener);
        userDs.getItem().setName("anotherName");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        TextField textField = (TextField) factory.createComponent(TextField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        textField.setDatasource(userDs, "name");

        // unbind
        textField.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;

        // datasource before listener

        textField.setDatasource(userDs, "name");
        textField.addValueChangeListener(listener);
        user.setName("anotherName");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        textField.removeValueChangeListener(listener);
        textField.setDatasource(null, null);
        valueWasChanged[0] = false;
        textField.setValue("testName");

        // listener before datasource
        textField.addValueChangeListener(listener);
        textField.setDatasource(userDs, "name");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        TextField textField = (TextField) factory.createComponent(TextField.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        userDs.getItem().setName("testName");
        textField.setDatasource(userDs, "name");

        // unbind
        textField.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> listener = e -> valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(listener);

        textField.setDatasource(userDs, "name");
        textField.setValue("anotherName");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        TextField textField = (TextField) factory.createComponent(TextField.NAME);

        textField.setDatasource(null, null);
        textField.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        boolean exceptionWasThrown = false;
        try {
            textField.setDatasource(userDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;

        try {
            textField.setDatasource(null, "name");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        textField.setDatasource(userDs1, "name");
        textField.setDatasource(userDs1, "name");

        userDs1.getItem().setName("Test name");
        textField.setDatasource(userDs1, "name");

        Datasource<User> userDs2 = getTestUserDatasource();
        textField.setDatasource(userDs2, "name");

        textField.setValue(false);
        assertEquals("Test name", userDs1.getItem().getName());
    }
}