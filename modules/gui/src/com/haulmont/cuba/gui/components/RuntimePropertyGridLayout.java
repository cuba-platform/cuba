/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 15.02.2010 16:16:14
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

public interface RuntimePropertyGridLayout extends GridLayout{

    CollectionDatasource getAttributesDs();
    void setAttributesDs(CollectionDatasource ds);

    DateField.Resolution getResolution();
    void setResolution(DateField.Resolution resolution);

    String getAttributeProperty();
    void setAttributeProperty(String value);

    String getInnerComponentWidth();
    void setInnerComponentWidth(String width);

    String getDateFormat();
    void setDateFormat(String dateFormat);
}
