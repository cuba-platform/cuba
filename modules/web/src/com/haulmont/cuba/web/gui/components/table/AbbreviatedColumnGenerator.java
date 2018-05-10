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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.widgets.CubaEnhancedTable;
import com.vaadin.v7.data.Property;
import org.apache.commons.lang.StringUtils;

import static com.haulmont.cuba.web.gui.components.WebAbstractTable.MAX_TEXT_LENGTH_GAP;

public class AbbreviatedColumnGenerator implements SystemTableColumnGenerator,
        CubaEnhancedTable.PlainTextGeneratedColumn {

    protected Table.Column column;
    protected DynamicAttributesTools dynamicAttributesTools;

    public AbbreviatedColumnGenerator(Table.Column column, DynamicAttributesTools dynamicAttributesTools) {
        this.column = column;
        this.dynamicAttributesTools = dynamicAttributesTools;
    }

    @Override
    public Object generateCell(com.vaadin.v7.ui.Table source, Object itemId, Object columnId) {
        Property property = source.getItem(itemId).getItemProperty(columnId);
        Object value = property.getValue();

        if (value == null) {
            return null;
        }

        String stringValue = value.toString();
        if (columnId instanceof MetaPropertyPath) {
            MetaProperty metaProperty = ((MetaPropertyPath) columnId).getMetaProperty();
            if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                stringValue = dynamicAttributesTools.getDynamicAttributeValueAsString(metaProperty, value);
            }
        }
        String cellValue = stringValue;
        boolean isMultiLineCell = StringUtils.contains(stringValue, "\n");
        if (isMultiLineCell) {
            cellValue = StringUtils.replaceChars(cellValue, '\n', ' ');
        }

        int maxTextLength = column.getMaxTextLength();
        if (stringValue.length() > maxTextLength + MAX_TEXT_LENGTH_GAP || isMultiLineCell) {
            return StringUtils.abbreviate(cellValue, maxTextLength);
        } else {
            return cellValue;
        }
    }
}