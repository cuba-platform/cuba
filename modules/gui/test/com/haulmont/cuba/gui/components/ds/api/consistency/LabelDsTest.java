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
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class LabelDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeComponentListener() {
        Label label = (Label) factory.createComponent(Label.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        label.setDatasource(userDs, "name");

        // unbind
        label.setDatasource(null, null);
        assertNotNull(label.getValue());

        Component.ValueChangeListener listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        label.addValueChangeListener(listener);

        user.setName("anotherName");
        assertEquals("testName", label.getValue());
    }

    @Test
    public void testUnsubscribeDsListener() {
        Label label = (Label) factory.createComponent(Label.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        label.setDatasource(userDs, "name");

        // unbind
        label.setDatasource(null, null);
        assertNotNull(label.getValue());

        Datasource.ItemPropertyChangeListener<User> propertyChangeListener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(propertyChangeListener);

        label.setValue("anotherName");
        assertEquals("testName", user.getName());
    }

    @Test
    public void testValueChangeListener() {
        Label label = (Label) factory.createComponent(Label.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        label.setDatasource(userDs, "name");

        // listener after datasource
        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        label.addValueChangeListener(listener);

        user.setName("anotherName");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        label.removeValueChangeListener(listener);
        label.setDatasource(null, null);
        valueWasChanged[0] = false;
        label.setValue("testName");

        // datasource after listener
        label.addValueChangeListener(listener);
        label.setDatasource(userDs, "name");

        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        Label label = (Label) factory.createComponent(Label.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        label.setDatasource(userDs, "name");

        label.setDatasource(null, null);

        // datasource before listener
        label.setDatasource(userDs, "name");
        assertEquals("testName", label.getValue());

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        label.addValueChangeListener(listener);

        user.setName("anotherName");
        assertEquals(true, valueWasChanged[0]);
        assertEquals("anotherName", label.getValue());

        // reset state
        label.removeValueChangeListener(listener);
        label.setDatasource(null, null);
        valueWasChanged[0] = false;
        label.setValue("testName");

        // listener before datasource
        label.addValueChangeListener(listener);
        label.setDatasource(userDs, "name");
        assertEquals(true, valueWasChanged[0]);
        assertEquals("anotherName", label.getValue());
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        Label label = (Label) factory.createComponent(Label.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setName("testName");
        label.setDatasource(userDs, "name");

        label.setDatasource(null, null);
        label.setDatasource(userDs, "name");

        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> propertyChangeListener = e -> valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(propertyChangeListener);

        label.setValue("anotherName");
        assertEquals(true, valueWasChanged[0]);
        assertEquals("anotherName", user.getName());
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        Label label = (Label) factory.createComponent(Label.NAME);

        label.setDatasource(null, null);
        label.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        boolean exceptionWasThrown = false;
        try {
            label.setDatasource(userDs1, null);
        } catch (Exception ignored) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;

        try {
            label.setDatasource(null, "name");
        } catch (Exception ignored) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        label.setDatasource(userDs1, "name");
        label.setDatasource(userDs1, "name");

        userDs1.getItem().setName("Test name");
        label.setDatasource(userDs1, "name");

        Datasource<User> userDs2 = getTestUserDatasource();
        label.setDatasource(userDs2, "name");

        label.setValue("Another name");
        assertEquals("Test name", userDs1.getItem().getName());
    }
}