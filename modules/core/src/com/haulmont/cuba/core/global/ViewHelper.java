/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 15.04.2009 16:32:56
 * $Id$
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetaProperty;

public class ViewHelper {
    public static boolean contains(View view, MetaPropertyPath propertyPath) {
        View currentView = view;
        for (MetaProperty metaProperty : propertyPath.get()) {
            if (currentView == null) return false;

            final ViewProperty property = currentView.getProperty(metaProperty.getName());
            if (property == null) return false;

            currentView = property.getView();
        }
        return true;
    }
}
