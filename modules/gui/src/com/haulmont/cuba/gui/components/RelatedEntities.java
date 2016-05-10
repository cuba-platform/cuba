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

import javax.annotation.Nullable;

/**
 */
public interface RelatedEntities extends Component, Component.HasCaption, Component.BelongToFrame, Component.Focusable {

    String NAME = "relatedEntities";

    WindowManager.OpenType getOpenType();
    void setOpenType(WindowManager.OpenType openType);

    String getExcludePropertiesRegex();
    void setExcludePropertiesRegex(String excludeRegex);

    void addPropertyOption(String property, @Nullable String screen, @Nullable String caption, @Nullable String filterCaption);
    void removePropertyOption(String property);

    ListComponent getListComponent();
    void setListComponent(ListComponent listComponent);
}