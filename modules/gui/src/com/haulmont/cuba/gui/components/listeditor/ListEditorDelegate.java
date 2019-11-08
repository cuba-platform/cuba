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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.data.Options;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The implementation of this class contains a logic for creation the {@link ListEditor} component, that is used by
 * components in client and desktop client. They delegate many invocations to the {@link ListEditorDelegate}.
 */
public interface ListEditorDelegate<V> {
    String NAME = "cuba_ListEditorDelegate";

    void setActualField(Field actualField);

    HBoxLayout getLayout();

    TextField getDisplayValuesField();

    List<V> getValue();
    void setValue(List<V> newValue);

    ListEditor.ItemType getItemType();

    void setItemType(ListEditor.ItemType itemType);

    String getEntityName();
    void setEntityName(String entityName);

    String getLookupScreen();
    void setLookupScreen(String lookupScreen);

    boolean isUseLookupField();
    void setUseLookupField(boolean useLookupField);

    Class<? extends Enum> getEnumClass();
    void setEnumClass(Class<? extends Enum> enumClass);

    void setDisplayDescription(boolean displayDescription);

    String getEntityJoinClause();
    void setEntityJoinClause(String entityJoinClause);

    String getEntityWhereClause();
    void setEntityWhereClause(String entityWhereClause);

    boolean isEditable();
    void setEditable(boolean editable);

    void setClearButtonVisible(boolean visible);
    boolean isClearButtonVisible();

    void setEditorWindowId(String windowId);
    String getEditorWindowId();

    Subscription addEditorCloseListener(Consumer<ListEditor.EditorCloseEvent> listener);

    @Deprecated
    void removeEditorCloseListener(Consumer<ListEditor.EditorCloseEvent> listener);

    void setEditorParamsSupplier(Supplier<Map<String, Object>> paramsSupplier);
    Supplier<Map<String, Object>> getEditorParamsSupplier();

    void setTimeZone(TimeZone timeZone);
    TimeZone getTimeZone();

    void setOptions(Options<V> options);
    Options<V> getOptions();

    void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider);
    Function<? super V, String> getOptionCaptionProvider();

    void addListItemValidator(Consumer<? super V> validator);
    List<Consumer<? super V>> getListItemValidators();

    boolean isDisplayValuesFieldEditable();
    void setDisplayValuesFieldEditable(boolean displayValuesFieldEditable);
}