/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @author artamonov
 * @version $Id$
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
        final DateField component = factory.createComponent(DateField.class);

        assertTrue(component.isEditable());

        component.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                component.setEditable(false);
            }
        });

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

        final AtomicInteger counter = new AtomicInteger(0);

        final Date value1 = new SimpleDateFormat("dd.MM.yyyy").parse("12.12.2000");
        ValueListener okListener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertNull(prevValue);
                assertEquals(value1, value);

                counter.addAndGet(1);
            }
        };
        component.addListener(okListener);
        component.setValue(value1);

        assertEquals(1, counter.get());
        component.removeListener(okListener);

        final Date value2 = new SimpleDateFormat("dd.MM.yyyy").parse("10.10.2000");
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

        ValueListener dsLoadListener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertEquals(value2, prevValue);
                assertNull(value);

                counter.addAndGet(1);
            }
        };
        component.addListener(dsLoadListener);
        component.setDatasource(testDs, "createTs");

        assertEquals(2, counter.get());

        component.removeListener(dsLoadListener);

        final Date value3 = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2000");
        ValueListener dsListener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertNull(prevValue);
                assertEquals(value3, value);

                counter.addAndGet(1);
            }
        };
        component.addListener(dsListener);
        testDs.getItem().setCreateTs(value3);

        assertEquals(3, counter.get());
    }
}