/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:14:58
 * $Id$
 */
package com.haulmont.cuba.gui.xml.data;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.List;

public class DsContextLoader {
    private DatasourceFactory factory;
    private DataService dataservice;
    private DsContextImpl datasources;

    public DsContextLoader(DatasourceFactory factory, DataService dataservice) {
        this.factory = factory;
        this.dataservice = dataservice;
    }

    public DsContext loadDatasources(Element element) {
        datasources = new DsContextImpl(dataservice);

        //noinspection unchecked
        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            datasources.register(loadDatasource(ds));
        }

        //noinspection unchecked
        elements = element.elements("hierarchicalDatasource");
        for (Element ds : elements) {
            datasources.register(loadHierarchicalDatasource(ds));
        }

        //noinspection unchecked
        elements = element.elements("collectionDatasource");
        for (Element ds : elements) {
            datasources.register(loadCollectionDatasource(ds));
        }

        datasources.executeLazyTasks();

        return datasources;
    }

    protected Datasource loadHierarchicalDatasource(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");
        final String hierarchyProperty = element.attributeValue("hierarchyProperty");

        final Element datasourceClassElement = element.element("datasourceClass");

        final HierarchicalDatasource datasource;
        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass)) throw new IllegalStateException("Datasource class is not specified");

            try {
                final Class<HierarchicalDatasource> aClass = ReflectionHelper.getClass(datasourceClass);
                final Constructor<HierarchicalDatasource> constructor =
                        aClass.getConstructor(
                                DsContext.class, DataService.class,
                                    String.class, MetaClass.class, String.class);
                datasource = constructor.newInstance(datasources, dataservice, id, metaClass, viewName);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else
            datasource = factory.createHierarchicalDatasource(datasources, dataservice, id, metaClass, viewName);

        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }

        final String query = element.elementText("query");
        if (!StringUtils.isBlank(query)) {
            datasource.setQuery(query);
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    protected Datasource loadDatasource(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");

        final Element datasourceClassElement = element.element("datasourceClass");

        final Datasource datasource;
        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass)) throw new IllegalStateException("Datasource class is not specified");

            try {
                final Class<Datasource> aClass = ReflectionHelper.getClass(datasourceClass);
                final Constructor<Datasource> constructor =
                        aClass.getConstructor(
                                DsContext.class, DataService.class,
                                    String.class, MetaClass.class, String.class);
                datasource = constructor.newInstance(datasources, dataservice, id, metaClass, viewName);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else
            datasource = factory.createDatasource(datasources, dataservice, id, metaClass, viewName);

        String item = element.attributeValue("item");
        if (!StringUtils.isBlank(item)) {
            final ParametersHelper.ParameterInfo info = ParametersHelper.parse(item);
            datasources.registerListener(info, datasource);
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    private void loadDatasources(Element element, Datasource datasource) {
        //noinspection unchecked
        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            datasources.register(loadDatasource(ds, datasource, property));
        }

        //noinspection unchecked
        elements = element.elements("collectionDatasource");
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

    private MetaClass loadMetaClass(Element element) {
        final String className = element.attributeValue("class");
        final Class<?> aClass = ReflectionHelper.getClass(className);
        final MetaClass metaClass = MetadataProvider.getSession().getClass(aClass);

        if (metaClass == null) 
            throw new IllegalStateException(String.format("Can't find metaClass '%s'", className));

        return metaClass;
    }

    protected CollectionDatasource loadCollectionDatasource(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");

        final Element datasourceClassElement = element.element("datasourceClass");

        final CollectionDatasource datasource;
        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass)) throw new IllegalStateException("Datasource class is not specified");

            try {
                final Class<CollectionDatasource> aClass = ReflectionHelper.getClass(datasourceClass);
                final Constructor<CollectionDatasource> constructor =
                        aClass.getConstructor(
                                DsContext.class, DataService.class,
                                    String.class, MetaClass.class, String.class);
                datasource = constructor.newInstance(datasources, dataservice, id, metaClass, viewName);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else
            datasource = factory.createCollectionDatasource(datasources, dataservice, id, metaClass, viewName);

        final String query = element.elementText("query");
        if (!StringUtils.isBlank(query)) {
            datasource.setQuery(query);
        }

        loadDatasources(element, datasource);

        return datasource;
    }
}
