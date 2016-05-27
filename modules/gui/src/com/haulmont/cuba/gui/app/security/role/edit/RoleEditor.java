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

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.security.entity.Role;

import javax.inject.Inject;
import javax.inject.Named;

public class RoleEditor extends AbstractEditor<Role> {

    @Inject
    protected ScreenPermissionsFrame screensTabFrame;

    @Named("name")
    protected TextField nameField;

    @Inject
    protected TextField locName;

    @Override
    protected void postInit() {
        setCaption(PersistenceHelper.isNew(getItem()) ?
                getMessage("createCaption") : formatMessage("editCaption", getItem().getName()));

        screensTabFrame.loadPermissions();
    }
}