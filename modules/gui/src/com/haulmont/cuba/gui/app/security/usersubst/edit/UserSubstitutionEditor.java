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
package com.haulmont.cuba.gui.app.security.usersubst.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.security.entity.UserSubstitution;

import javax.inject.Inject;
import java.util.Map;

/**
 */
public class UserSubstitutionEditor extends AbstractEditor<UserSubstitution> {

    @Inject
    protected DateField startDateField;

    @Inject
    protected LookupField substUser;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidthAuto();
    }

    @Override
    protected void postInit() {
        super.postInit();

        if (!PersistenceHelper.isNew(getItem())) {
            substUser.setEditable(false);
        }
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        super.postValidate(errors);

        UserSubstitution substitution = getItem();
        if (substitution.getStartDate() != null && substitution.getEndDate() != null) {
            if (substitution.getStartDate().getTime() > substitution.getEndDate().getTime()) {
                errors.add(startDateField, getMessage("dateOrderError"));
            }
        }
    }
}