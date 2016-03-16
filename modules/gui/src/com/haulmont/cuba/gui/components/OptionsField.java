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

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.List;
import java.util.Map;

/**
 */
public interface OptionsField extends Field {
    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    String getDescriptionProperty();
    void setDescriptionProperty(String descProperty);

    CollectionDatasource getOptionsDatasource();
    void setOptionsDatasource(CollectionDatasource datasource);

    List getOptionsList();
    void setOptionsList(List optionsList);

    Map<String, Object> getOptionsMap();
    void setOptionsMap(Map<String, Object> map);

    Class<? extends EnumClass> getOptionsEnum();
    void setOptionsEnum(Class<? extends EnumClass> optionsEnum);
}
