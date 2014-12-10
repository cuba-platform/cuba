/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class LookupFieldTest extends CubaClientTestCase {

    protected ComponentsFactory factory;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        initExpectations();

        messages.init();
    }

    protected void initExpectations() {
        new NonStrictExpectations() {
            @Mocked PersistenceManagerService persistenceManagerService;
            {
                AppBeans.get(PersistenceManagerClient.NAME); result = persistenceManagerService;
                AppBeans.get(PersistenceManagerClient.class); result = persistenceManagerService;
                AppBeans.get(PersistenceManagerClient.NAME, PersistenceManagerClient.class); result = persistenceManagerService;

                persistenceManagerService.getMaxFetchUI(anyString); result = 10000;
            }
        };
    }

    @Test
    public void testNew() {
        Component component = factory.createComponent(LookupField.NAME);
        assertNotNull(component);
        assertTrue(component instanceof LookupField);
    }

    @Test
    public void testGetSetValue() {
        LookupField component = factory.createComponent(LookupField.NAME);

        assertNull(component.getValue());

        component.setOptionsList(new ArrayList<>(Arrays.asList("One", "Two", "Three")));
        component.setValue("One");

        assertEquals("One", component.getValue());
    }

    @Test
    public void testSetToReadonly() {
        LookupField component = factory.createComponent(LookupField.NAME);

        component.setEditable(false);
        assertFalse(component.isEditable());

        component.setOptionsList(new ArrayList<>(Arrays.asList("One", "Two", "Three")));
        component.setValue("One");

        assertEquals("One", component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testDatasource() {
        LookupField component = factory.createComponent(LookupField.NAME);

        //noinspection unchecked
        Datasource<User> testDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        testDs.setItem(new User());
        ((DatasourceImpl) testDs).valid();

        assertNull(component.getValue());
        Group g = new Group();
        testDs.getItem().setGroup(g);

        CollectionDatasource<Group, UUID> groupsDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(Group.class)
                .setView(viewRepository.getView(Group.class, View.LOCAL))
                .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                .setAllowCommit(false)
                .buildCollectionDatasource();

        groupsDs.includeItem(g);
        Group g1 = new Group();
        groupsDs.includeItem(g1);
        Group g2 = new Group();
        groupsDs.includeItem(g2);

        component.setOptionsDatasource(groupsDs);

        component.setValue(g2);
        component.setDatasource(testDs, "group");
        assertEquals(g, component.getValue());

        component.setValue(g1);
        assertEquals(g1, testDs.getItem().getGroup());

        testDs.getItem().setGroup(g2);
        assertEquals(g2, component.getValue());
    }

    /*@Test
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
    }*/
}