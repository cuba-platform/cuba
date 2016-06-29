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

import com.haulmont.cuba.core.entity.Entity;

public interface TwinColumn extends OptionsField {

    String NAME = "twinColumn";

    int getColumns();
    void setColumns(int columns);

    int getRows();
    void setRows(int rows);

    void setStyleProvider(StyleProvider styleProvider);

    void setAddAllBtnEnabled(boolean enabled);
    boolean isAddAllBtnEnabled();

    /**
     * Set caption for the left column.
     *
     * @param leftColumnCaption
     */
    void setLeftColumnCaption(String leftColumnCaption);
    /**
     * Return caption of the left column.
     *
     * @return caption text or null if not set.
     */
    String getLeftColumnCaption();

    /**
     * Set caption for the right column.
     *
     * @param rightColumnCaption
     */
    void setRightColumnCaption(String rightColumnCaption);
    /**
     * Return caption of the right column.
     *
     * @return caption text or null if not set.
     */
    String getRightColumnCaption();

    interface StyleProvider {
        String getStyleName(Entity item, Object property, boolean selected);
        String getItemIcon(Entity item, boolean selected);
    }
}