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

package com.haulmont.cuba.gui.components;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Supplier;

/**
 * The component is used for displaying and editing a collection of values.
 *
 * WARNING: for internal use only!
 */
public interface ListEditor<V> extends Field<List<V>>, Component.Focusable {

    String NAME = "listEditor";

    boolean isUseLookupField();

    /**
     * Sets whether a lookup field must be used for selecting an entity. PickerField is used by default.
     */
    void setUseLookupField(boolean useLookupField);

    String getLookupScreen();

    /**
     * Sets the entity lookup screen ID
     */
    void setLookupScreen(String lookupScreen);

    String getEntityName();

    /**
     * Sets the entity name. Edited collection must be of this entity type
     */
    void setEntityName(String entityName);

    List<?> getOptionsList();

    /**
     * Set the list of available collection items values
     */
    void setOptionsList(List<?> optionsList);

    Map<String, Object> getOptionsMap();

    void setOptionsMap(Map<String, Object> optionsMap);

    String getEntityJoinClause();

    void setEntityJoinClause(String entityJoinClause);

    String getEntityWhereClause();

    void setEntityWhereClause(String entityWhereClause);

    Class<? extends Enum> getEnumClass();

    void setEnumClass(Class<? extends Enum> enumClass);

    void setClearButtonVisible(boolean visible);

    boolean isClearButtonVisible();

    /**
     * Sets the window id of editor screen
     *
     * @param windowId alias of screen in the app-screens.xml
     */
    void setEditorWindowId(String windowId);

    /**
     * @return window id of editor screen
     */
    String getEditorWindowId();

    /**
     * Adds {@link com.haulmont.cuba.gui.components.ListEditor.EditorCloseListener} that invoked after editor window
     * closing.
     *
     * @param listener listener instance
     */
    void addEditorCloseListener(EditorCloseListener listener);

    /**
     * @param listener listener to be removed
     */
    void removeEditorCloseListener(EditorCloseListener listener);

    /**
     * @param paramsSupplier additional params map for editor screen.
     */
    void setEditorParamsSupplier(Supplier<Map<String, Object>> paramsSupplier);

    Supplier<Map<String, Object>> getEditorParamsSupplier();

    /**
     *
     * @param timeZone - for DateTime fields and date formatting
     */
    void setTimeZone(TimeZone timeZone);

    TimeZone getTimeZone();

    enum ItemType {
        STRING,
        DATE,
        DATETIME,
        DOUBLE,
        BIGDECIMAL,
        INTEGER,
        LONG,
        BOOLEAN,
        UUID,
        ENUM,
        ENTITY
    }

    /**
     * Sets the type of elements of editable collection
     */
    void setItemType(ItemType itemType);

    ItemType getItemType();

    /**
     * Listener that will be notified when editor window is closed.
     */
    @FunctionalInterface
    interface EditorCloseListener {

        /**
         * Called when a editor screen is closed.
         *
         * @param closeEvent event instance
         */
        void editorClosed(EditorCloseEvent closeEvent);
    }

    class EditorCloseEvent {

        protected String actionId;
        protected Window window;

        public EditorCloseEvent(String actionId, Window window) {
            this.actionId = actionId;
            this.window = window;
        }

        public String getActionId() {
            return actionId;
        }

        public Window getWindow() {
            return window;
        }
    }
}
