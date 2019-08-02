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

package com.haulmont.cuba.gui.components.filter.condition;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.filter.Param;

import javax.persistence.TemporalType;

public class FilterConditionUtils {

    public static String getPropertyLocCaption(MetaClass metaClass, String propertyPath) {
        MessageTools messageTools = AppBeans.get(MessageTools.class);
        MetaPropertyPath mpp = metaClass.getPropertyPath(propertyPath);
        if (mpp == null) {
            return propertyPath;
        } else {
            MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

            MetaProperty[] metaProperties = mpp.getMetaProperties();
            StringBuilder sb = new StringBuilder();

            MetaPropertyPath parentMpp = null;
            MetaClass tempMetaClass;

            for (int i = 0; i < metaProperties.length; i++) {
                if (i == 0) {
                    parentMpp = new MetaPropertyPath(metaClass, metaProperties[i]);
                    tempMetaClass = metaClass;
                } else {
                    parentMpp = new MetaPropertyPath(parentMpp, metaProperties[i]);
                    tempMetaClass = metadataTools.getPropertyEnclosingMetaClass(parentMpp);
                }

                sb.append(messageTools.getPropertyCaption(tempMetaClass, metaProperties[i].getName()));
                if (i < metaProperties.length - 1) {
                    sb.append(".");
                }
            }
            return sb.toString();
        }
    }

    public static String formatParamValue(Param param, Object value) {
        //noinspection unchecked
        Datatype datatype = Datatypes.get(param.getJavaClass());
        MetaProperty property = param.getProperty();
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get(MetadataTools.TEMPORAL_ANN_NAME);
            if (tt == TemporalType.DATE) {
                datatype = Datatypes.getNN(java.sql.Date.class);
            }
        }
        if (datatype != null) {
            UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
            return datatype.format(value, userSessionSource.getLocale());
        }
        return value.toString();
    }
}