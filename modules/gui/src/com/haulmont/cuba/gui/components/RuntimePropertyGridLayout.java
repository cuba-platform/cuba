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
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

public interface RuntimePropertyGridLayout extends GridLayout, Component.HasCaption{

    Datasource getMainDs();
    void setMainDs(Datasource ds);

    String getAttributeProperty();
    void setAttributeProperty(String value);

    String getAttributePropertyOrder();
    void setAttributePropertyOrder(String value);

    String getInnerComponentWidth();
    void setInnerComponentWidth(String width);

    String getDateFormat();
    void setDateFormat(String dateFormat);

    String getAttributeValueProperty();
    void setAttributeValueProperty(String attributeValueProperty);

    String getTypeProperty();
    void setTypeProperty(String typeProperty);

    Boolean getCheckNewAttributes();
    void setCheckNewAttributes(Boolean checkNewAttributes);
}
