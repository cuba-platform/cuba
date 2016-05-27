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

package com.haulmont.cuba.web.app.ui.jmxinstance.browse;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;

import javax.inject.Named;
import java.util.Map;

public class JmxInstanceBrowser extends AbstractLookup {

    @Named("jmxInstancesTable.create")
    protected CreateAction createInstanceAction;

    @Named("jmxInstancesTable.edit")
    protected EditAction editInstanceAction;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        createInstanceAction.setOpenType(WindowManager.OpenType.DIALOG);
        editInstanceAction.setOpenType(WindowManager.OpenType.DIALOG);
    }
}