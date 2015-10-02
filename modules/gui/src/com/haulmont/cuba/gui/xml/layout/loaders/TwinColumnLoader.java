/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.TwinColumn;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author Gorodnov
 * @version $Id$
 */
public class TwinColumnLoader extends AbstractFieldLoader<TwinColumn> {
    @Override
    public void createComponent() {
        resultComponent = (TwinColumn) factory.createComponent(TwinColumn.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            resultComponent.setCaptionMode(CaptionMode.PROPERTY);
            resultComponent.setCaptionProperty(captionProperty);
        }

        String columns = element.attributeValue("columns");
        if (!StringUtils.isEmpty(columns)) {
            resultComponent.setColumns(Integer.parseInt(columns));
        }

        String rows = element.attributeValue("rows");
        if (!StringUtils.isEmpty(rows)) {
            resultComponent.setRows(Integer.parseInt(rows));
        }

        String addBtnEnabled = element.attributeValue("addAllBtnEnabled");
        if (!StringUtils.isEmpty(addBtnEnabled)) {
            resultComponent.setAddAllBtnEnabled(Boolean.valueOf(addBtnEnabled));
        }
    }

    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            Datasource ds = context.getDsContext().get(datasource);
            ((TwinColumn) component).setOptionsDatasource((CollectionDatasource) ds);
        }

        super.loadDatasource(component, element);
    }
}