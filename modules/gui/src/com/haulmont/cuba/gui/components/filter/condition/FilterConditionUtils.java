/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.condition;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.filter.Param;

import javax.persistence.TemporalType;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class FilterConditionUtils {

    public static String getPropertyLocCaption(MetaClass metaClass, String propertyPath) {
        MessageTools messageTools = AppBeans.get(MessageTools.class);
        MetaPropertyPath mpp = metaClass.getPropertyPath(propertyPath);
        if (mpp == null)
            return propertyPath;
        else {
            MetaProperty[] metaProperties = mpp.getMetaProperties();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < metaProperties.length; i++) {
                sb.append(messageTools.getPropertyCaption(metaProperties[i]));
                if (i < metaProperties.length - 1)
                    sb.append(".");
            }
            return sb.toString();
        }
    }

    public static String formatParamValue(Param param, Object value) {
        Datatype datatype = Datatypes.get(param.getJavaClass());
        MetaProperty property = param.getProperty();
        if (property != null) {
            TemporalType tt = (TemporalType) property.getAnnotations().get("temporal");
            if (tt == TemporalType.DATE) {
                datatype = Datatypes.get(DateDatatype.NAME);
            }
        }
        if (datatype != null) {
            UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
            return datatype.format(value, userSessionSource.getLocale());
        }
        return value.toString();

    }
}
