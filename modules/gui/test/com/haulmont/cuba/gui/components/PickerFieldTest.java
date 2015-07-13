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
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import mockit.NonStrictExpectations;
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
public abstract class PickerFieldTest extends AbstractComponentTest {

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new NonStrictExpectations() {
            {
                clientConfig.getPickerShortcutModifiers(); result = "CTRL-ALT";
            }
        };
    }

    @Test
    public void testNew() {
        Component component = factory.createComponent(PickerField.NAME);
        assertNotNull(component);
        assertTrue(component instanceof PickerField);
    }

    @Test
    public void testGetSetValue() {
        PickerField component = factory.createComponent(PickerField.NAME);

        assertNull(component.getValue());

        boolean throwed = false;
        try {
            component.setValue("One");
        } catch (IllegalStateException e) {
            throwed = true;
        }
        assertTrue(throwed);

        assertNull(component.getValue());

        User user = new User();
        user.setLogin("admin");

        component.setMetaClass(metadata.getClass(User.class));
        component.setValue(user);

        assertEquals(user, component.getValue());
    }

    @Test
    public void testSetToReadonly() {
        PickerField component = factory.createComponent(PickerField.NAME);

        component.setEditable(false);
        component.setMetaClass(metadata.getClass(User.class));
        assertFalse(component.isEditable());

        User user = new User();
        user.setLogin("admin");
        component.setValue(user);

        assertEquals(user, component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testSetToReadonlyFromValueListener() {
        final PickerField component = factory.createComponent(PickerField.NAME);

        component.setMetaClass(metadata.getClass(User.class));
        assertTrue(component.isEditable());

        component.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                component.setEditable(false);
            }
        });

        User user = new User();
        user.setLogin("admin");
        component.setValue(user);

        assertEquals(user, component.getValue());
        assertFalse(component.isEditable());
    }

    @Test
    public void testDatasource() {
        PickerField component = factory.createComponent(PickerField.NAME);

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

        Group g1 = new Group();
        Group g2 = new Group();

        component.setMetaClass(metadata.getClass(Group.class));

        component.setValue(g2);
        component.setDatasource(testDs, "group");
        assertEquals(g, component.getValue());

        component.setValue(g1);
        assertEquals(g1, testDs.getItem().getGroup());

        testDs.getItem().setGroup(g2);
        assertEquals(g2, component.getValue());
    }

    @Test
    public void testValueChangeListener() {
        PickerField component = factory.createComponent(PickerField.NAME);

        final AtomicInteger counter = new AtomicInteger(0);

        //noinspection unchecked
        Datasource<User> testDs = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        testDs.setItem(new User());
        ((DatasourceImpl) testDs).valid();

        assertNull(component.getValue());
        final Group g = new Group();
        testDs.getItem().setGroup(g);

        final Group g1 = new Group();
        final Group g2 = new Group();

        ValueListener listener1 = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertNull(prevValue);
                assertEquals(g2, value);

                counter.addAndGet(1);
            }
        };
        component.addListener(listener1);

        component.setMetaClass(metadata.getClass(Group.class));
        component.setValue(g2);

        component.removeListener(listener1);
        assertEquals(1, counter.get());

        ValueListener listener2 = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertEquals(g2, prevValue);
                assertEquals(g, value);

                counter.addAndGet(1);
            }
        };

        component.addListener(listener2);

        component.setDatasource(testDs, "group");
        assertEquals(g, component.getValue());

        assertEquals(2, counter.get());

        component.removeListener(listener2);
        component.setValue(g1);
        assertEquals(g1, testDs.getItem().getGroup());

        assertEquals(2, counter.get());

        ValueListener listener3 = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property,
                                     @Nullable Object prevValue, @Nullable Object value) {
                assertEquals(g1, prevValue);
                assertEquals(g2, value);

                counter.addAndGet(1);
            }
        };

        component.addListener(listener3);
        testDs.getItem().setGroup(g2);
        assertEquals(g2, component.getValue());

        assertEquals(3, counter.get());
    }
}