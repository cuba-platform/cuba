/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:14:58
 * $Id$
 */
package com.haulmont.cuba.gui.xml.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class DsContextLoader {
    private DatasourceFactory factory;
    private DsContextImpl datasources;

    public DsContextLoader(DatasourceFactory factory) {
        this.factory = factory;
    }

    public DsContext loadDatasources(Element element) {
        datasources = new DsContextImpl();

        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            datasources.register(loadDatasource(ds));
        }

        elements = element.elements("collection-datasource");
        for (Element ds : elements) {
            datasources.register(loadCollectionDatasource(ds));
        }

        return datasources;
    }

    protected Datasource loadDatasource(Element element) {
        try {
            final String id = element.attributeValue("id");
            final MetaClass metaClass = loadMetaClass(element);
            final String viewName = element.attributeValue("view");

            final Datasource datasource =
                    factory.createDatasource(datasources, id, metaClass, viewName);

            String item = element.attributeValue("item");
            if (!StringUtils.isBlank(item)) {
                final ParametersHelper.ParameterInfo info = ParametersHelper.parse(item);
                datasources.registerListener(info, datasource);
            }

            loadDatasources(element, datasource);

            return datasource;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDatasources(Element element, Datasource datasource) {
        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            datasources.register(loadDatasource(ds, datasource, property));
        }

        elements = element.elements("collection-datasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            datasources.register(loadCollectionDatasource(ds, datasource, property));
        }
    }

    private Datasource loadDatasource(Element element, Datasource ds, String property) {
        final String id = element.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new IllegalStateException(
                    String.format("Can't find property '%s' in datasource '%s'", property, ds.getId()));
        }

        final Datasource datasource =
                factory.createDatasource(id, ds, property);

        loadDatasources(element, datasource);

        return datasource;
    }

    private Datasource loadCollectionDatasource(Element element, Datasource ds, String property) {
        final String id = element.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new IllegalStateException(
                    String.format("Can't find property '%s' in datasource '%s'", property, ds.getId()));
        }

        final CollectionDatasource datasource =
                factory.createCollectionDatasource(id, ds, property);

        loadDatasources(element, datasource);

        return datasource;
    }

    private MetaClass loadMetaClass(Element element) throws ClassNotFoundException {
        final String className = element.attributeValue("class");
        final Class<?> aClass = Class.forName(className);
        final MetaClass metaClass = MetadataProvider.getSession().getClass(aClass);

        if (metaClass == null) 
            throw new IllegalStateException(String.format("Can't find metaClass '%s'", className));

        return metaClass;
    }

    protected CollectionDatasource loadCollectionDatasource(Element element) {
        final String id = element.attributeValue("id");
        try {
            final MetaClass metaClass = loadMetaClass(element);
            final String viewName = element.attributeValue("view");

            final CollectionDatasource datasource =
                    factory.createCollectionDatasource(datasources, id, metaClass, viewName);

            final String query = element.elementText("query");
            if (!StringUtils.isBlank(query)) {
                datasource.setQuery(query);
            }

            loadDatasources(element, datasource);

            return datasource;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
