/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.charts;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.charts.CategoryChart;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.data.CategoryChartDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.toolkit.ui.charts.CategoryChartComponent;
import com.haulmont.cuba.web.toolkit.ui.charts.ChartComponent;
import com.vaadin.data.util.IndexedContainer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public abstract class WebAbstractCategoryChart<T extends CategoryChartComponent>
        extends WebAbstractChart<T>
        implements CategoryChart {

    private static final long serialVersionUID = 8120229914290249619L;

    protected CollectionDatasource datasource;

    public CollectionDatasource getCollectionDatasource() {
        return datasource;
    }

    public void setCollectionDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;

        if (datasource instanceof CategoryChartDatasource) {
            final CategoryChartDatasource categoryChartDatasource = (CategoryChartDatasource) datasource;

            component.setContainerDataSource(new IndexedContainer());

            Object rowCaptionPropertyId = new Object();
            component.addProperty(rowCaptionPropertyId);
            component.setRowCaptionPropertyId(rowCaptionPropertyId);

            for (final Object categoryId : categoryChartDatasource.getCategoryIds()) {
                component.addProperty(categoryId);
                component.addCategory(categoryId, categoryChartDatasource.getCategoryCaption(categoryId));
            }

            for (final Object rowId : categoryChartDatasource.getRowIds()) {
                component.addRow(rowId, categoryChartDatasource.getRowCaption(rowId));

                for (final Object categoryId : categoryChartDatasource.getCategoryIds()) {
                    component.setValue(rowId, categoryId, categoryChartDatasource.getValue(rowId, categoryId));
                }
            }

        } else {
            DsManager dsManager = new DsManager(datasource, this);

            CollectionDsWrapper dsWrapper = new CollectionDsWrapper(datasource, true, dsManager);

            component.setContainerDataSource(dsWrapper);
        }
    }



    public String getRowCaption(Object id) {
        return component.getRowCaption(id);
    }

    public Object getRowCaptionProperty() {
        return component.getRowCaptionPropertyId();
    }

    public void setRowCaptionProperty(Object property) {
        component.setRowCaptionPropertyId(property);
    }



    public Collection<?> getCategoryProperties() {
        return component.getCategoryPropertyIds();
    }

    public void addCategory(Object property, String caption) {
        component.addCategory(property, caption);
    }

    public String getCategoryCaption(Object property) {
        return component.getCategoryCaption(property);
    }
}
