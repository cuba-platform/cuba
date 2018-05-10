/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.gui.components.table;

import com.vaadin.ui.CssLayout;

public class TableComposition extends CssLayout {
    protected com.vaadin.v7.ui.Table table;

    public com.vaadin.v7.ui.Table getTable() {
        return table;
    }

    public void setTable(com.vaadin.v7.ui.Table table) {
        this.table = table;
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (getHeight() < 0) {
            table.setHeightUndefined();
        } else {
            table.setHeight(100, Unit.PERCENTAGE);
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (getWidth() < 0) {
            table.setWidthUndefined();
        } else {
            table.setWidth(100, Unit.PERCENTAGE);
        }
    }
}