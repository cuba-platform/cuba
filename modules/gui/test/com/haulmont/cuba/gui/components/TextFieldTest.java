/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @author artamonov
 * @version $Id$
 */
@Ignore
public abstract class TextFieldTest extends CubaClientTestCase {

    protected ComponentsFactory factory;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        initExpectations();

        messages.init();
    }

    protected void initExpectations() {
    }

    @Test
    public void testNew() {
        Component component = factory.createComponent(TextField.NAME);
        assertNotNull(component);
        assertTrue(component instanceof TextField);
    }

    @Test
    public void testGetSetValue() {
        TextField component = factory.createComponent(TextField.NAME);

        assertNull(component.getValue());

        component.setValue("Test");

        assertEquals("Test", component.getValue());
    }

    @Test
    public void testGetSetInteger() {
        TextField component = factory.createComponent(TextField.NAME);

        assertNull(component.getValue());

        component.setDatatype(Datatypes.getNN(Integer.class));
        component.setValue(10);

        assertEquals(10, component.getValue());
    }

    @Test
    public void testSetToReadonly() {
        TextField component = factory.createComponent(TextField.NAME);

        component.setEditable(false);
        assertFalse(component.isEditable());

        component.setValue("OK");

        assertEquals("OK", component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testDatasource() {
        TextField component = factory.createComponent(TextField.NAME);

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
        TextField component = factory.createComponent(TextField.NAME);

        final AtomicInteger counter = new AtomicInteger(0);

        ValueListener okListener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertNull(prevValue);
                assertEquals("OK", value);

                counter.addAndGet(1);
            }
        };
        component.addListener(okListener);
        component.setValue("OK");

        assertEquals(1, counter.get());
        component.removeListener(okListener);

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

        ValueListener dsLoadListener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertEquals("Test", prevValue);
                assertNull(value);

                counter.addAndGet(1);
            }
        };
        component.addListener(dsLoadListener);
        component.setDatasource(testDs, "login");

        assertEquals(2, counter.get());

        component.removeListener(dsLoadListener);

        ValueListener dsListener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertNull(prevValue);
                assertEquals("dsValue", value);

                counter.addAndGet(1);
            }
        };
        component.addListener(dsListener);
        testDs.getItem().setLogin("dsValue");

        assertEquals(3, counter.get());
    }
}