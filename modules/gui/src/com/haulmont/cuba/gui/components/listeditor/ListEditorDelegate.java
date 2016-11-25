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
 */

package com.haulmont.cuba.gui.components.listeditor;

import com.haulmont.cuba.gui.components.*;

import java.util.List;

/**
 * The implementation of this class contains a logic for creation the {@link ListEditor} component, that is used by
 * components in client and desktop client. They delegate many invocations to the {@link ListEditorDelegate}.
 */
public interface ListEditorDelegate {
    String NAME = "cuba_ListEditorDelegate";

    void setActualField(Field actualField);

    HBoxLayout getLayout();

    List getValue();

    void setValue(List newValue);

    ListEditor.ItemType getItemType();

    void setItemType(ListEditor.ItemType itemType);

    String getEntityName();

    void setEntityName(String entityName);

    String getLookupScreen();

    void setLookupScreen(String lookupScreen);

    boolean isUseLookupField();

    void setUseLookupField(boolean useLookupField);

    List<Object> getOptionsList();

    void setOptionsList(List<Object> optionsList);

    void setDisplayDescription(boolean displayDescription);

    String getEntityJoinClause();

    void setEntityJoinClause(String entityJoinClause);

    String getEntityWhereClause();

    void setEntityWhereClause(String entityWhereClause);
}
