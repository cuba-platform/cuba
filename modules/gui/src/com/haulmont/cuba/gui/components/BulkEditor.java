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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.bulk.ColumnsMode;
import com.haulmont.cuba.security.entity.ConstraintOperationType;

import java.util.List;
import java.util.Map;

/**
 * Use {@link com.haulmont.cuba.gui.actions.list.BulkEditAction} in new screens instead.
 *
 * @see com.haulmont.cuba.gui.BulkEditors
 */
public interface BulkEditor extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon,
                                    Component.Focusable, HasHtmlCaption, HasHtmlDescription {

    String NAME = "bulkEditor";
    String PERMISSION = "cuba.gui.bulkEdit";

    WindowManager.OpenType getOpenType();
    void setOpenType(WindowManager.OpenType openType);

    String getExcludePropertiesRegex();
    void setExcludePropertiesRegex(String excludeRegex);

    /**
     * @return list of entity's attributes
     */
    List<String> getIncludeProperties();

    /**
     * Sets entity's attributes to the bulk editor dialog. Unspecified attributes will not be shown.
     *
     * @param includeProperties list of entity's attributes
     */
    void setIncludeProperties(List<String> includeProperties);

    ListComponent getListComponent();
    void setListComponent(ListComponent listComponent);

    Map<String, Field.Validator> getFieldValidators();
    void setFieldValidators(Map <String, Field.Validator> fieldValidators);

    List<Field.Validator> getModelValidators();
    void setModelValidators(List<Field.Validator> modelValidators);

    void setLoadDynamicAttributes(boolean loadDynamicAttributes);
    boolean isLoadDynamicAttributes();

    /**
     * @return columns mode which defines the number of editor columns
     */
    ColumnsMode getColumnsMode();

    /**
     * Sets the columns mode which defines the number of editor columns.
     *
     * @param columnsMode columns mode
     * @see ColumnsMode#ONE_COLUMN
     * @see ColumnsMode#TWO_COLUMNS
     */
    void setColumnsMode(ColumnsMode columnsMode);

    /**
     * Sets the given <code>constraintOperationType</code> to the BulkEditAction.
     *
     * @param constraintOperationType constraintOperationType
     */
    void setConstraintOperationType(ConstraintOperationType constraintOperationType);

    /**
     * @return {@link ConstraintOperationType} of the BulkEditAction
     */
    ConstraintOperationType getConstraintOperationType();

    /**
     * Set useConfirmDialog property to false if you want to disable confirm dialog and commit changes immediately.
     *
     * @param useConfirmDialog useConfirmDialog option
     */
    void setUseConfirmDialog(boolean useConfirmDialog);

    /**
     * @return true if confirm dialog should be enabled
     */
    boolean getUseConfirmDialog();
}