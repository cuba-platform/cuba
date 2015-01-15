/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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