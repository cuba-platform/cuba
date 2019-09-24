/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.ds.api.consistency;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.OptionsList;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unchecked")
@Disabled
public class OptionsListDsTest extends DsApiConsistencyTestCase {

    @Test
    public void testUnsubscribeComponentListener() {
        OptionsList optionsList = uiComponents.create(OptionsList.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        optionsList.setDatasource(roleDs, "type");
        optionsList.setValue(RoleType.DENYING);

        optionsList.setDatasource(null, null);

        Consumer<HasValue.ValueChangeEvent> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        optionsList.addValueChangeListener(listener);


        roleDs.getItem().setType(RoleType.READONLY);
        assertEquals(RoleType.DENYING, optionsList.getValue());
    }

    @Test
    public void testUnsubscribeDsListener() {
        OptionsList optionsList = uiComponents.create(OptionsList.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        optionsList.setDatasource(roleDs, "type");
        optionsList.setValue(RoleType.DENYING);

        optionsList.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<Role> listener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        roleDs.addItemPropertyChangeListener(listener);

        optionsList.setValue(RoleType.STANDARD);
        assertEquals(RoleType.DENYING, roleDs.getItem().getType());
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        OptionsList optionsList = uiComponents.create(OptionsList.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        roleDs.getItem().setType(RoleType.DENYING);
        optionsList.setDatasource(roleDs, "type");

        optionsList.setDatasource(null, null);

        // datasource before listener
        optionsList.setDatasource(roleDs, "type");

        boolean[] valueWasChanged = {false};
        Consumer<HasValue.ValueChangeEvent> listener = e -> valueWasChanged[0] = true;
        optionsList.addValueChangeListener(listener);

        roleDs.getItem().setType(RoleType.READONLY);
        Assertions.assertEquals(true, valueWasChanged[0]);

        // reset state
        valueWasChanged[0] = false;
        optionsList.removeValueChangeListener(listener);
        optionsList.setDatasource(null, null);
        optionsList.setValue(null);

        // listener before datasource
        optionsList.addValueChangeListener(listener);
        optionsList.setDatasource(roleDs, "type");
        Assertions.assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        OptionsList optionsList = uiComponents.create(OptionsList.NAME);

        Datasource<Role> roleDs = getTestRoleDatasource();
        roleDs.getItem().setType(RoleType.DENYING);
        optionsList.setDatasource(roleDs, "type");

        optionsList.setDatasource(null, null);

        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<Role> listener = e -> valueWasChanged[0] = true;
        roleDs.addItemPropertyChangeListener(listener);
        optionsList.setDatasource(roleDs, "type");

        optionsList.setValue(RoleType.STANDARD);
        assertTrue(valueWasChanged[0]);
    }

    @Test
    public void testValueChangeListener() {
        OptionsList optionsList = uiComponents.create(OptionsList.NAME);

        // listener before datasource

        boolean[] valueWasChanged = {false};
        Consumer<HasValue.ValueChangeEvent> listener = e -> valueWasChanged[0] = true;
        optionsList.addValueChangeListener(listener);

        Datasource<Role> roleDs = getTestRoleDatasource();
        roleDs.getItem().setType(RoleType.READONLY);

        optionsList.setDatasource(roleDs, "type");
        assertTrue(valueWasChanged[0]);

        // reset state
        valueWasChanged[0] = false;
        optionsList.removeValueChangeListener(listener);
        optionsList.setDatasource(null, null);

        // datasource before listener
        optionsList.setDatasource(roleDs, "type");
        optionsList.addValueChangeListener(listener);
        roleDs.getItem().setType(RoleType.DENYING);
        assertTrue(valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        OptionsList optionsList = uiComponents.create(OptionsList.NAME);

        optionsList.setDatasource(null, null);
        optionsList.setDatasource(null, null);

        Datasource<Role> roleDs1 = getTestRoleDatasource();
        optionsList.setValue(RoleType.STANDARD);
        boolean exceptionWasThrown = false;
        try {
            optionsList.setDatasource(roleDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);
        exceptionWasThrown = false;

        try {
            optionsList.setDatasource(null, "group");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        optionsList.setDatasource(roleDs1, "type");
        optionsList.setDatasource(roleDs1, "type");

        Datasource<Role> roleDs2 = getTestRoleDatasource();

        optionsList.setDatasource(roleDs2, "type");
        optionsList.setValue(RoleType.DENYING);
        Assertions.assertEquals(RoleType.STANDARD, roleDs1.getItem().getType());
    }
}
