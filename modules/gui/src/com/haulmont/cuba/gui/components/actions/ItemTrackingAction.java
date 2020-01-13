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
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.security.entity.ConstraintOperationType;

import javax.annotation.Nullable;

/**
 * Standard action that changes enabled property depending on selection of a bound {@link ListComponent}.
 * <br>
 * You can use fluent API to create instances of ItemTrackingAction and assign handlers to them:
 * <pre>{@code
 *     Action action = new ItemTrackingAction("moveToTrash")
 *             .withCaption("Move to trash")
 *             .withIcon("icons/trash.png")
 *             .withHandler(event -> {
 *                 // action logic here
 *             });
 *     docsTable.addAction(action);
 * }</pre>
 */
public class ItemTrackingAction extends ListAction implements Action.HasSecurityConstraint {

    protected ConstraintOperationType constraintOperationType;
    protected String constraintCode;

    protected Security security = AppBeans.get(Security.NAME);

    public ItemTrackingAction(String id) {
        this(null, id);
    }

    public ItemTrackingAction(@Nullable ListComponent target, String id) {
        super(id, null);

        this.target = target;
    }

    @Override
    protected boolean isApplicable() {
        return target != null
                && target.getSingleSelected() != null;
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

        return true;
    }

    @Override
    public void setConstraintOperationType(ConstraintOperationType constraintOperationType) {
        this.constraintOperationType = constraintOperationType;
    }

    @Override
    public ConstraintOperationType getConstraintOperationType() {
        return constraintOperationType;
    }

    @Override
    public void setConstraintCode(String constraintCode) {
        this.constraintCode = constraintCode;
    }

    @Override
    public String getConstraintCode() {
        return constraintCode;
    }
}