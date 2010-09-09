/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 16:53:23
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.charts;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.charts.ChartComponent;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class WebAbstractChart<T extends ChartComponent>
        extends WebAbstractComponent<T>
        implements Chart
{
    private CollectionDatasource datasource;

    protected Map<MetaPropertyPath, String> columns = new LinkedHashMap<MetaPropertyPath, String>();

    public void addColumn(MetaPropertyPath propertyId, String caption) {
        columns.put(propertyId, caption);
    }

    public CollectionDatasource getCollectionDatasource() {
        return datasource;
    }

    public void setCollectionDatasource(CollectionDatasource datasource) {

        Collection<MetaPropertyPath> props;
        if (columns.isEmpty()) {
            props = null;
        } else {
            props = new LinkedHashSet<MetaPropertyPath>(columns.keySet());
        }

        if (props == null) {
            throw new IllegalStateException("Properties cannot be NULL");
        }

        if (getRowCaptionPropertyId() != null) {
            props.add(getRowCaptionPropertyId());
        }

        CollectionDsWrapper dsWrapper = createContainerDatasource(datasource, props);

        this.datasource = datasource;

        component.setContainerDataSource(dsWrapper);

        for (final Map.Entry<MetaPropertyPath, String> entry : columns.entrySet()) {
            component.setColumnCaption(entry.getKey(), entry.getValue());
        }
    }

    protected CollectionDsWrapper createContainerDatasource(
            CollectionDatasource datasource,
            Collection<MetaPropertyPath> props
    ) {
        return new CollectionDsWrapper(datasource, props, true);
    }

    public MetaPropertyPath getRowCaptionPropertyId() {
        return (MetaPropertyPath) component.getRowCaptionPropertyId();
    }

    public void setRowCaptionPropertyId(MetaPropertyPath propertyId) {
        component.setRowCaptionPropertyId(propertyId);
    }

    public String getColumnAxisLabel() {
        return component.getColumnAxisLabel();
    }

    public void setColumnAxisLabel(String label) {
        component.setColumnAxisLabel(label);
    }

    public String getValueAxisLabel() {
        return component.getValueAxisLabel();
    }

    public void setValueAxisLabel(String label) {
        component.setValueAxisLabel(label);
    }

    public boolean isLegend() {
        return component.isLegend();
    }

    public void setLegend(boolean needLegend) {
        component.setLegend(needLegend);
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    @Override
    public void setExpandable(boolean expandable) {
        //ignore
    }

    @Override
    public boolean isExpandable() {
        return false;
    }

    @Override
    public void setWidth(String width) {
        try {
            component.setChartWidth(Integer.parseInt(width));
        } catch (NumberFormatException e) {
            //do nothing
        }
    }

    @Override
    public void setHeight(String height) {
        try {
            component.setChartHeight(Integer.parseInt(height));
        } catch (NumberFormatException e) {
            //do nothing
        }
    }
}
