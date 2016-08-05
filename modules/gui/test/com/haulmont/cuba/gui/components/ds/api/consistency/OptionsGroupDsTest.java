/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.ds.api.consistency;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.RoleType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class OptionsGroupDsTest extends DsApiConsistencyTest {

    @Test
    public void testUnsubscribeComponentListener() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        optionsGroup.setDatasource(roleDs, "type");
        optionsGroup.setValue(RoleType.STANDARD);

        optionsGroup.setDatasource(null, null);

        Component.ValueChangeListener listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        optionsGroup.addValueChangeListener(listener);

        roleDs.getItem().setType(RoleType.DENYING);
        assertEquals(RoleType.STANDARD, optionsGroup.getValue());
    }

    @Test
    public void testUnsubscribeDsListener() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        optionsGroup.setDatasource(roleDs, "type");
        optionsGroup.setValue(RoleType.STANDARD);

        optionsGroup.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<Role> listener = e -> {
            throw new RuntimeException("");
        };
        roleDs.addItemPropertyChangeListener(listener);

        optionsGroup.setValue(RoleType.DENYING);
        assertEquals(RoleType.STANDARD, roleDs.getItem().getType());
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        roleDs.getItem().setType(RoleType.DENYING);
        optionsGroup.setDatasource(roleDs, "type");

        optionsGroup.setDatasource(null, null);

        // datasource before listener
        optionsGroup.setDatasource(roleDs, "type");

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        optionsGroup.addValueChangeListener(listener);

        roleDs.getItem().setType(RoleType.READONLY);
        assertEquals(true, valueWasChanged[0]);

        // reset state
        valueWasChanged[0] = false;
        optionsGroup.removeValueChangeListener(listener);
        optionsGroup.setDatasource(null, null);
        optionsGroup.setValue(null);

        // listener before datasource
        optionsGroup.addValueChangeListener(listener);
        optionsGroup.setDatasource(roleDs, "type");
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        roleDs.getItem().setType(RoleType.DENYING);
        optionsGroup.setDatasource(roleDs, "type");

        optionsGroup.setDatasource(null, null);

        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<Role> listener = e -> valueWasChanged[0] = true;
        roleDs.addItemPropertyChangeListener(listener);
        optionsGroup.setDatasource(roleDs, "type");

        optionsGroup.setValue(RoleType.STANDARD);
        assertTrue(valueWasChanged[0]);
    }

    @Test
    public void testValueChangeListener() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        // listener before datasource

        boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        optionsGroup.addValueChangeListener(listener);

        Datasource<Role> roleDs = getTestRoleDatasource();
        roleDs.getItem().setType(RoleType.READONLY);

        optionsGroup.setDatasource(roleDs, "type");
        assertTrue(valueWasChanged[0]);

        // reset state
        valueWasChanged[0] = false;
        optionsGroup.removeValueChangeListener(listener);
        optionsGroup.setDatasource(null, null);

        // datasource before listener
        optionsGroup.setDatasource(roleDs, "type");
        optionsGroup.addValueChangeListener(listener);
        roleDs.getItem().setType(RoleType.DENYING);
        assertTrue(valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        optionsGroup.setDatasource(null, null);
        optionsGroup.setDatasource(null, null);

        Datasource<Role> roleDs1 = getTestRoleDatasource();
        optionsGroup.setValue(RoleType.STANDARD);
        boolean exceptionWasThrown = false;
        try {
            optionsGroup.setDatasource(roleDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;

        try {
            optionsGroup.setDatasource(null, "group");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        optionsGroup.setDatasource(roleDs1, "type");
        optionsGroup.setDatasource(roleDs1, "type");

        Datasource<Role> roleDs2 = getTestRoleDatasource();

        optionsGroup.setDatasource(roleDs2, "type");
        optionsGroup.setValue(RoleType.DENYING);
        Assert.assertEquals(RoleType.STANDARD, roleDs1.getItem().getType());
    }

    @Test
    public void testSetValue() {
        OptionsGroup optionsGroup = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        optionsGroup.setDatasource(roleDs, "type");

        optionsGroup.setValue(RoleType.DENYING);
        assertNotNull(optionsGroup.getValue());
    }
}
