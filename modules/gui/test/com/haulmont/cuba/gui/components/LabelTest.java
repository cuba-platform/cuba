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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.security.entity.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class LabelTest extends AbstractComponentTest {

    @Test
    public void testValueChangeListener() {
        //noinspection unchecked
        Datasource<User> ds = new DsBuilder()
                .setId("testDs")
                .setJavaClass(User.class)
                .setView(viewRepository.getView(User.class, View.LOCAL))
                .buildDatasource();

        User dsUser = metadata.create(User.class);
        dsUser.setName("testName");
        ds.setItem(dsUser);
        ((DatasourceImpl) ds).valid();

        final Boolean[] valueWasChanged = {false};
        Component.ValueChangeListener listener = e -> valueWasChanged[0] = true;

        Label label = (Label) factory.createComponent(Label.NAME);
        label.addValueChangeListener(listener);
        label.setDatasource(ds, "name");

        dsUser.setName("anotherName");
        Assert.assertEquals(true, valueWasChanged[0]);
    }
}