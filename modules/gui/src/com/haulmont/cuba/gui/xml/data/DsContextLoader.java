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
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.gui.filter.QueryFilter;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.List;

public class DsContextLoader {
    private DatasourceFactory factory;
    private DataService dataservice;

    private DsContextImplementation context;

    public DsContextLoader(DatasourceFactory factory, DataService dataservice) {
        this.factory = factory;
        this.dataservice = dataservice;
    }

    public DsContext loadDatasources(Element element, DsContext parent) {
        if (element == null) {
            // throw an informational exception
            throw new RuntimeException("Datasource element not specified in template");
        }
        String contextClass = element.attributeValue("class");
        if (StringUtils.isEmpty(contextClass)) {
            final Element contextClassElement = element.element("class");
            if (contextClassElement != null) {
                contextClass = contextClassElement.getText();
                if (StringUtils.isEmpty(contextClass)) {
                    throw new IllegalStateException("Can't find dsContext class name");
                }
                context = createDsContext(contextClass, contextClassElement);
            } else {
                context = new DsContextImpl(dataservice);
            }
        } else {
            context = createDsContext(contextClass, null);
        }
        if (parent != null) {
            context.setParent(parent);
        }

        //noinspection unchecked
        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            context.register(loadDatasource(ds));
        }

        //noinspection unchecked
        elements = element.elements("hierarchicalDatasource");
        for (Element ds : elements) {
            context.register(loadHierarchicalDatasource(ds));
        }

        //noinspection unchecked
        elements = element.elements("collectionDatasource");
        for (Element ds : elements) {
            context.register(loadCollectionDatasource(ds));
        }

        context.executeLazyTasks();

        return context;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected DsContextImplementation createDsContext(String contextClass, Element element) {
        DsContextImplementation context;

        final Class<Object> aClass = ReflectionHelper.getClass(contextClass);
        try {
            final Constructor<Object> constructor = aClass.getConstructor(DataService.class);
            context = (DsContextImplementation) constructor.newInstance(dataservice);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return context;
    }

    protected Datasource loadHierarchicalDatasource(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");
        final String hierarchyProperty = element.attributeValue("hierarchyProperty");
        String deletion = element.attributeValue("softDeletion");
        boolean softDeletion = deletion == null || "true".equals(deletion);

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
                datasource = constructor.newInstance(context, dataservice, id, metaClass, viewName);
                datasource.setSoftDeletion(softDeletion);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            final CollectionDatasource.FetchMode mode = getFetchMode(element);
            datasource = factory.createHierarchicalDatasource(context, dataservice, id, metaClass, viewName, mode, softDeletion);
        }

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
                datasource = constructor.newInstance(context, dataservice, id, metaClass, viewName);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else
            datasource = factory.createDatasource(context, dataservice, id, metaClass, viewName);

        // TODO implement ContextListeners
//        String item = element.attributeValue("item");
//        if (!StringUtils.isBlank(item)) {
//            final ParametersHelper.ParameterInfo info = ParametersHelper.parse(item);
//            context.registerListener(info, datasource);
//        }

        loadDatasources(element, datasource);

        return datasource;
    }

    private void loadDatasources(Element element, Datasource datasource) {
        //noinspection unchecked
        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            context.register(loadDatasource(ds, datasource, property));
        }

        //noinspection unchecked
        elements = element.elements("collectionDatasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            context.register(loadCollectionDatasource(ds, datasource, property));
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

        final Element datasourceClassElement = element.element("datasourceClass");
        CollectionDatasource datasource;
        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");
            try {
                final Class<CollectionDatasource> aClass = ReflectionHelper.getClass(datasourceClass);
                final Constructor<CollectionDatasource> constructor =
                        aClass.getConstructor(String.class, Datasource.class, String.class);
                datasource = constructor.newInstance(id, ds, property);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            datasource = factory.createCollectionDatasource(id, ds, property);
        }

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
        String deletion = element.attributeValue("softDeletion");
        boolean softDeletion = deletion == null || "true".equals(deletion);

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
                datasource = constructor.newInstance(context, dataservice, id, metaClass, viewName);
                datasource.setSoftDeletion(softDeletion);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            final CollectionDatasource.FetchMode mode = getFetchMode(element);
            datasource = factory.createCollectionDatasource(context, dataservice, id, metaClass, viewName, mode, softDeletion);
        }

        Element queryElem = element.element("query");
        if (queryElem != null) {
            Element filterElem = queryElem.element("filter");

            String query = queryElem.getText();
            if (!StringUtils.isBlank(query)) {
                if (filterElem != null)
                    datasource.setQuery(query, new QueryFilter(filterElem, metaClass.getName()));
                else
                    datasource.setQuery(query);
            }
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    protected CollectionDatasource.FetchMode getFetchMode(Element element) {
        final CollectionDatasource.FetchMode mode;

        final String fetchMode = element.attributeValue("fetchMode");
        if (!StringUtils.isEmpty(fetchMode)) {
            mode = CollectionDatasource.FetchMode.valueOf(fetchMode);
        } else {
            mode = CollectionDatasource.FetchMode.ALL;
        }
        return mode;
    }
}
