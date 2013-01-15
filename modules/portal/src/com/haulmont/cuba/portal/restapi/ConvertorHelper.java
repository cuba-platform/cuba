/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 20.04.2011
 * Time: 18:13:08
 */
public class ConvertorHelper {
    public static final Comparator<MetaProperty> PROPERTY_COMPARATOR = new Comparator<MetaProperty>() {
        public int compare(MetaProperty p1, MetaProperty p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

    public static List<MetaProperty> getOrderedProperties(MetaClass metaClass) {
        List<MetaProperty> result = new ArrayList<MetaProperty>(metaClass.getProperties());
        Collections.sort(result, PROPERTY_COMPARATOR);
        return result;
    }
}
