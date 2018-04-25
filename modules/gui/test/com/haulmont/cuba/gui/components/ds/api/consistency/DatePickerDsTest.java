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
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class DatePickerDsTest extends DsApiConsistencyTestCase {

    @Test
    public void testUnsubscribeComponentListener() {
        DatePicker datePicker = (DatePicker) factory.createComponent(DatePicker.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();

        Date updateTs = new Date();
        user.setUpdateTs(updateTs);
        datePicker.setDatasource(userDs, "updateTs");

        // unbind
        datePicker.setDatasource(null, null);

        HasValue.ValueChangeListener valueChangeListener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        datePicker.addValueChangeListener(valueChangeListener);

        user.setUpdateTs(new Date());
        assertEquals(updateTs, datePicker.getValue());
    }

    @Test
    public void testUnsubscribeDsListener() {
        DatePicker datePicker = (DatePicker) factory.createComponent(DatePicker.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        Date updateTs = new Date();
        user.setUpdateTs(updateTs);
        datePicker.setDatasource(userDs, "updateTs");

        // unbind
        datePicker.setDatasource(null, null);

        Datasource.ItemPropertyChangeListener<User> propertyChangeListener = e -> {
            throw new RuntimeException("Value was changed externally");
        };
        userDs.addItemPropertyChangeListener(propertyChangeListener);

        datePicker.setValue(new Date());
        assertEquals(updateTs, user.getUpdateTs());
    }

    @Test
    public void testUnsubscribeSubscribeComponentListener() {
        DatePicker datePicker = (DatePicker) factory.createComponent(DatePicker.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        Date updateTs = new Date();
        user.setUpdateTs(updateTs);
        datePicker.setDatasource(userDs, "updateTs");

        // unbind
        datePicker.setDatasource(null, null);

        // datasource before listener
        datePicker.setDatasource(userDs, "updateTs");
        assertEquals(updateTs, datePicker.getValue());

        boolean[] valueWasChanged = {false};
        HasValue.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        datePicker.addValueChangeListener(listener);

        Date updateTs2 = new Date();
        user.setUpdateTs(updateTs2);
        assertEquals(true, valueWasChanged[0]);
        assertEquals(updateTs2, datePicker.getValue());

        // reset state
        datePicker.removeValueChangeListener(listener);
        datePicker.setDatasource(null, null);
        valueWasChanged[0] = false;
        datePicker.setValue(updateTs);

        // listener before datasource

        datePicker.addValueChangeListener(listener);
        datePicker.setDatasource(userDs, "updateTs");
        assertEquals(true, valueWasChanged[0]);
        assertEquals(updateTs2, datePicker.getValue());
    }

    @Test
    public void testUnsubscribeSubscribeDsListener() {
        DatePicker datePicker = (DatePicker) factory.createComponent(DatePicker.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        Date updateTs = new Date();
        user.setUpdateTs(updateTs);
        datePicker.setDatasource(userDs, "updateTs");

        // unbind
        datePicker.setDatasource(null, null);

        // setup
        boolean[] valueWasChanged = {false};
        Datasource.ItemPropertyChangeListener<User> listener = e ->
                valueWasChanged[0] = true;
        userDs.addItemPropertyChangeListener(listener);
        datePicker.setDatasource(userDs, "updateTs");

        Date updateTs2 = new Date();
        datePicker.setValue(updateTs2);
        assertEquals(true, valueWasChanged[0]);
        assertEquals(updateTs2, user.getUpdateTs());
    }

    @Test
    public void testValueChangeListener() {
        DatePicker datePicker = (DatePicker) factory.createComponent(DatePicker.NAME);

        Datasource<User> userDs = getTestUserDatasource();
        User user = userDs.getItem();
        user.setUpdateTs(new Date());

        // listener before datasource
        Boolean[] valueWasChanged = {false};
        HasValue.ValueChangeListener listener = e -> valueWasChanged[0] = true;
        datePicker.addValueChangeListener(listener);

        datePicker.setDatasource(userDs, "updateTs");
        assertEquals(true, valueWasChanged[0]);

        // reset state
        datePicker.setDatasource(null, null);
        datePicker.removeValueChangeListener(listener);
        valueWasChanged[0] = false;

        // datasource before listener
        datePicker.setDatasource(userDs, "updateTs");
        datePicker.addValueChangeListener(listener);

        user.setUpdateTs(new Date());
        assertEquals(true, valueWasChanged[0]);
    }

    @Test
    public void testDatasourceRepeatableAssign() {
        DatePicker datePicker = (DatePicker) factory.createComponent(DatePicker.NAME);

        datePicker.setDatasource(null, null);
        datePicker.setDatasource(null, null);

        Datasource<User> userDs1 = getTestUserDatasource();
        datePicker.setDatasource(userDs1, "updateTs");
        datePicker.setDatasource(userDs1, "updateTs");

        boolean exceptionWasThrown = false;
        try {
            datePicker.setDatasource(userDs1, null);
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        exceptionWasThrown = false;
        try {
            datePicker.setDatasource(null, "updateTs");
        } catch (Exception e) {
            exceptionWasThrown = true;
        }
        assertTrue(exceptionWasThrown);

        Date updateTs = new Date();
        userDs1.getItem().setUpdateTs(updateTs);
        datePicker.setDatasource(userDs1, "updateTs");

        Datasource<User> userDs2 = getTestUserDatasource();
        datePicker.setDatasource(userDs2, "updateTs");

        datePicker.setValue(new Date());
        assertEquals(updateTs, userDs1.getItem().getUpdateTs());
    }
}