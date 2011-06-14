/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.charts;

import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public interface CategoryChart extends Chart {

    CollectionDatasource getCollectionDatasource();
    void setCollectionDatasource(CollectionDatasource datasource);


    String getRowCaption(Object id);

    Object getRowCaptionProperty();
    void setRowCaptionProperty(Object property);



    Collection<?> getCategoryProperties();
    void addCategory(Object property, String caption);

    String getCategoryCaption(Object property);


}
