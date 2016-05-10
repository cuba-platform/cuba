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

import java.util.List;
import java.util.Map;

/**
 */
public interface BulkEditor
        extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon, Component.Focusable {

    String NAME = "bulkEditor";
    String PERMISSION = "cuba.gui.bulkEdit";

    WindowManager.OpenType getOpenType();
    void setOpenType(WindowManager.OpenType openType);

    String getExcludePropertiesRegex();
    void setExcludePropertiesRegex(String excludeRegex);

    ListComponent getListComponent();
    void setListComponent(ListComponent listComponent);

    Map<String, Field.Validator> getFieldValidators();
    void setFieldValidators(Map <String, Field.Validator> fieldValidators);

    List<Field.Validator> getModelValidators();
    void setModelValidators(List<Field.Validator> modelValidators);
}