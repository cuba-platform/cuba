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

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 */
@Ignore
public abstract class DateFieldTest extends AbstractComponentTest {

    @Test
    public void testNew() {
        Component component = factory.createComponent(DateField.NAME);
        assertNotNull(component);
        assertTrue(component instanceof DateField);
    }

    @Test
    public void testGetSetValue() throws ParseException {
        DateField component = factory.createComponent(DateField.class);

        assertNull(component.getValue());

        Date value = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        component.setValue(value);

        assertEquals(value, component.getValue());
    }

    @Test
    public void testSetToReadonly() throws ParseException {
        DateField component = factory.createComponent(DateField.class);

        component.setEditable(false);
        assertFalse(component.isEditable());

        Date value = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        component.setValue(value);

        assertEquals(value, component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testSetToReadonlyFromValueListener() throws ParseException {
        DateField component = factory.createComponent(DateField.class);

        assertTrue(component.isEditable());

        component.addValueChangeListener(e -> component.setEditable(false));

        Date value = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        component.setValue(value);

        assertEquals(value, component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testDatasource() throws ParseException {
        DateField component = factory.createComponent(DateField.class);

        //noinspection unchecked
        Datasource<User> testDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        testDs.setItem(new User());
        ((DatasourceImpl) testDs).valid();

        assertNull(component.getValue());

        Date dsInitValue = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        testDs.getItem().setCreateTs(dsInitValue);

        component.setValue(new Date());
        component.setDatasource(testDs, "createTs");
        assertEquals(dsInitValue, component.getValue());

        Date componentValue = new SimpleDateFormat("dd.MM.yyyy").parse("06.06.2000");
        component.setValue(componentValue);
        assertEquals(componentValue, testDs.getItem().getCreateTs());

        Date dsValue = new SimpleDateFormat("dd.MM.yyyy").parse("08.08.2000");
        testDs.getItem().setCreateTs(dsValue);
        assertEquals(dsValue, component.getValue());
    }

    @Test
    public void testValueChangeListener() throws ParseException {
        DateField component = factory.createComponent(DateField.class);

        AtomicInteger counter = new AtomicInteger(0);

        Date value1 = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        Component.ValueChangeListener okListener = e -> {
            assertNull(e.getPrevValue());
            assertEquals(value1, e.getValue());

            counter.addAndGet(1);
        };
        component.addValueChangeListener(okListener);
        component.setValue(value1);

        assertEquals(1, counter.get());
        component.removeValueChangeListener(okListener);

        Date value2 = new SimpleDateFormat("dd.MM.yyyy").parse("10.10.2000");
        component.setValue(value2);
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
            assertEquals(value2, e.getPrevValue());
            assertNull(e.getValue());

            counter.addAndGet(1);
        };
        component.addValueChangeListener(dsLoadListener);
        component.setDatasource(testDs, "createTs");

        assertEquals(2, counter.get());

        component.removeValueChangeListener(dsLoadListener);

        Date value3 = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2000");
        Component.ValueChangeListener dsListener = e -> {
            assertNull(e.getPrevValue());
            assertEquals(value3, e.getValue());

            counter.addAndGet(1);
        };
        component.addValueChangeListener(dsListener);
        testDs.getItem().setCreateTs(value3);

        assertEquals(3, counter.get());
    }
}