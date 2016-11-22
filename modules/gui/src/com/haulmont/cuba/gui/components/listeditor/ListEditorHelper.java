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

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.gui.components.ListEditor;

/**
 */
public class ListEditorHelper {

    public static String getValueCaption(Object v, ListEditor.ItemType itemType) {
        if (v == null)
            return null;
        switch (itemType) {
            case ENTITY:
                if (v instanceof Instance)
                    return ((Instance) v).getInstanceName();
                else
                    return v.toString();
            case STRING:
                return (String) v;
            case DATE:
                return Datatypes.get(DateDatatype.NAME).format(v);
            case DATETIME:
                return Datatypes.get(DateTimeDatatype.NAME).format(v);
            case INTEGER:
                return Datatypes.get(IntegerDatatype.NAME).format(v);
            case LONG:
                return Datatypes.get(LongDatatype.NAME).format(v);
            case BIGDECIMAL:
                return Datatypes.get(BigDecimalDatatype.NAME).format(v);
            case DOUBLE:
                return Datatypes.get(DoubleDatatype.NAME).format(v);
            default:
                throw new IllegalStateException("Unknown item type");
        }
    }

}
