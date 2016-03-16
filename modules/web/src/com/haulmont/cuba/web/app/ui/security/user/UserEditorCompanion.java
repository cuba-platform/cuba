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

package com.haulmont.cuba.web.app.ui.security.user;

import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.auth.WebAuthConfig;

import javax.inject.Inject;

/**
 */
public class UserEditorCompanion implements UserEditor.Companion {

    @Inject
    protected WebAuthConfig config;

    @Override
    public void initPasswordField(PasswordField passwordField) {
        passwordField.setRequired(!config.getExternalAuthentication());
    }

    @Override
    public void refreshUserSubstitutions() {
        App.getInstance().getAppWindow().refreshUserSubstitutions();
    }
}