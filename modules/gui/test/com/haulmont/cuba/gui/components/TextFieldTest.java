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
 *
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 */
@Ignore
public abstract class TextFieldTest extends AbstractComponentTest {

    @Test
    public void testNew() {
        Component component = factory.createComponent(TextField.NAME);
        assertNotNull(component);
        assertTrue(component instanceof TextField);
    }

    @Test
    public void testGetSetValue() {
        TextField component = factory.createComponent(TextField.class);

        assertNull(component.getValue());

        component.setValue("Test");

        assertEquals("Test", component.getValue());
    }

    @Test
    public void testGetSetInteger() {
        TextField component = factory.createComponent(TextField.class);

        assertNull(component.getValue());

        component.setDatatype(Datatypes.getNN(Integer.class));
        component.setValue(10);

        assertEquals(10, (int) component.getValue());
    }

    @Test
    public void testSetToReadonly() {
        TextField component = factory.createComponent(TextField.class);

        component.setEditable(false);
        assertFalse(component.isEditable());

        component.setValue("OK");

        assertEquals("OK", component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testSetToReadonlyFromValueListener() {
        final TextField component = factory.createComponent(TextField.class);

        assertTrue(component.isEditable());

        component.addValueChangeListener(e -> component.setEditable(false));

        component.setValue("OK");

        assertEquals("OK", component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testDatasource() {
        TextField component = factory.createComponent(TextField.class);

        //noinspection unchecked
        Datasource<User> testDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        testDs.setItem(new User());
        ((DatasourceImpl) testDs).valid();

        assertNull(component.getValue());
        testDs.getItem().setLogin("Ok");

        component.setValue("none");
        component.setDatasource(testDs, "login");
        assertEquals("Ok", component.getValue());

        component.setValue("user");
        assertEquals("user", testDs.getItem().getLogin());

        testDs.getItem().setLogin("login");
        assertEquals("login", component.getValue());
    }

    @Test
    public void testValueChangeListener() {
        TextField component = factory.createComponent(TextField.class);

        AtomicInteger counter = new AtomicInteger(0);

        Component.ValueChangeListener okListener = e -> {
            assertNull(e.getPrevValue());
            assertEquals("OK", e.getValue());

            counter.addAndGet(1);
        };
        component.addValueChangeListener(okListener);
        component.setValue("OK");

        assertEquals(1, counter.get());
        component.removeValueChangeListener(okListener);

        component.setValue("Test");
        assertEquals(1, counter.get());

        //noinspection unchecked
        Datasource<User> testDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        testDs.setItem(new User());
        ((DatasourceImpl) testDs).valid();

        Component.ValueChangeListener dsLoadListener = e -> {
            assertEquals("Test", e.getPrevValue());
            assertNull(e.getValue());

            counter.addAndGet(1);
        };
        component.addValueChangeListener(dsLoadListener);
        component.setDatasource(testDs, "login");

        assertEquals(2, counter.get());

        component.removeValueChangeListener(dsLoadListener);

        Component.ValueChangeListener dsListener = e -> {
            assertNull(e.getPrevValue());
            assertEquals("dsValue", e.getValue());

            counter.addAndGet(1);
        };
        component.addValueChangeListener(dsListener);
        testDs.getItem().setLogin("dsValue");

        assertEquals(3, counter.get());
    }
}