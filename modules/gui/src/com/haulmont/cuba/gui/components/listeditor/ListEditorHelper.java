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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.TimeZoneAwareDatatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Function;

import static com.haulmont.cuba.gui.components.ListEditor.ItemType.*;

/**
 */
public class ListEditorHelper {

    public static String getValueCaption(Object v, ListEditor.ItemType itemType, TimeZone timeZone,
                                         @Nullable Function<Object, String> captionProvider) {
        if (captionProvider == null) {
            return getValueCaption(v, itemType, timeZone);
        } else {
            return captionProvider.apply(v);
        }
    }

    @Nullable
    public static String getValueCaption(@Nullable Object v, ListEditor.ItemType itemType, @Nullable TimeZone timeZone) {
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
                return Datatypes.getNN(java.sql.Date.class).format(v);
            case DATETIME:
                UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
                UserSession userSession = userSessionSource.getUserSession();
                if (timeZone != null) {
                    return ((TimeZoneAwareDatatype)Datatypes.getNN(Date.class)).format(v, userSession.getLocale(), timeZone);
                } else {
                    return Datatypes.getNN(Date.class).format(v, userSession.getLocale());
                }
            case INTEGER:
                return Datatypes.getNN(Integer.class).format(v);
            case LONG:
                return Datatypes.getNN(Long.class).format(v);
            case BIGDECIMAL:
                return Datatypes.getNN(BigDecimal.class).format(v);
            case DOUBLE:
                return Datatypes.getNN(Double.class).format(v);
            case ENUM:
                return AppBeans.get(Messages.class).getMessage((Enum) v);
            case UUID:
                return Datatypes.getNN(java.util.UUID.class).format(v);
            default:
                throw new IllegalStateException("Unknown item type");
        }
    }

    public static ListEditor.ItemType itemTypeFromDatatype(Datatype datatype) {
        Class type = datatype.getJavaClass();
        if (type.equals(String.class)) {
            return STRING;
        } else if (type.equals(Integer.class)) {
            return INTEGER;
        } else if (type.equals(BigDecimal.class)) {
            return BIGDECIMAL;
        } else if (type.equals(Double.class)) {
            return DOUBLE;
        } else if (type.equals(Long.class)) {
            return LONG;
        } else if (type.equals(java.sql.Date.class)) {
            return DATE;
        } else if (type.equals(Date.class)) {
            return DATETIME;
        } else if (type.equals(Boolean.class)) {
            return BOOLEAN;
        } else if (type.equals(java.util.UUID.class)) {
            return UUID;
        } else {
            throw new IllegalArgumentException("Datatype " + datatype + " is not supported");
        }
    }

}
