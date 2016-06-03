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

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.security.entity.ConstraintOperationType;

public abstract class ItemTrackingAction extends BaseAction {

    protected ConstraintOperationType constraintOperationType;
    protected String constraintCode;

    protected Security security = AppBeans.get(Security.NAME);

    public ItemTrackingAction(String id) {
        this(null, id);
    }

    protected ItemTrackingAction(ListComponent target, String id) {
        super(id, null);

        this.target = target;
    }

    @Override
    protected boolean isApplicable() {
        return target != null && !target.getSelected().isEmpty() && super.isApplicable();
    }

    @Override
    protected boolean isPermitted() {
        if (target == null) {
            return false;
        }

        Entity singleSelected = target.getSingleSelected();
        if (singleSelected == null) {
            return false;
        }

        if (constraintOperationType != null) {
            boolean isPermitted;
            if (constraintCode != null) {
                isPermitted = security.isPermitted(singleSelected, constraintCode);
            } else {
                isPermitted = security.isPermitted(singleSelected, constraintOperationType);
            }
            if (!isPermitted) {
                return false;
            }
        }

        return super.isPermitted();
    }

    public void setConstraintOperationType(ConstraintOperationType constraintOperationType) {
        this.constraintOperationType = constraintOperationType;
    }

    public void setConstraintCode(String constraintCode) {
        this.constraintCode = constraintCode;
    }
}