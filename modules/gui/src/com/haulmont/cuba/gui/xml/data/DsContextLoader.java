/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.data;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.filter.QueryFilter;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.persistence.Embedded;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

public class DsContextLoader {

    private DsBuilder builder;
    private DataService dataservice;

    private DsContextImplementation context;

    public DsContextLoader(DataService dataservice) {
        this.dataservice = dataservice;
    }

    public DsContext loadDatasources(Element element, DsContext parent) {
        if (element == null) {
            context = new DsContextImpl(dataservice);
            return context;
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

        builder = new DsBuilder(context);

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

        //noinspection unchecked
        elements = element.elements("groupDatasource");
        for (Element ds : elements) {
            context.register(loadGroupDatasource(ds));
        }

        context.executeLazyTasks();

        return context;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected DsContextImplementation createDsContext(String contextClass, Element element) {
        DsContextImplementation context;

        final Class<Object> aClass = ScriptingProvider.loadClass(contextClass);
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
        boolean softDeletion = deletion == null || Boolean.valueOf(deletion);

        builder.reset().setMetaClass(metaClass).setId(id).setViewName(viewName).setSoftDeletion(softDeletion);

        final HierarchicalDatasource datasource;

        final Element datasourceClassElement = element.element("datasourceClass");

        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            Class<HierarchicalDatasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildHierarchicalDatasource();
        } else {
            CollectionDatasource.FetchMode fetchMode = getFetchMode(element);
            datasource = builder.setFetchMode(fetchMode).buildHierarchicalDatasource();
        }

        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        final String query = element.elementText("query");
        if (!StringUtils.isBlank(query)) {
            datasource.setQuery(query);
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    protected Datasource loadGroupDatasource(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");
        String deletion = element.attributeValue("softDeletion");
        boolean softDeletion = deletion == null || Boolean.valueOf(deletion);

        builder.reset().setMetaClass(metaClass).setId(id).setViewName(viewName).setSoftDeletion(softDeletion);

        final GroupDatasource datasource;

        final Element datasourceClassElement = element.element("datasourceClass");

        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            final Class<GroupDatasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildGroupDatasource();
        } else {
            final CollectionDatasource.FetchMode fetchMode = getFetchMode(element);
            datasource = builder.setFetchMode(fetchMode).buildGroupDatasource();
        }

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

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

    protected Datasource loadDatasource(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");

        builder.reset().setMetaClass(metaClass).setId(id).setViewName(viewName);

        final Datasource datasource;

        final Element datasourceClassElement = element.element("datasourceClass");

        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            final Class<Datasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildDatasource();
        } else {
            datasource = builder.buildDatasource();
        }

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

        //noinspection unchecked
        elements = element.elements("groupDatasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            context.register(loadGroupDatasource(ds, datasource, property));
        }

        //noinspection unchecked
        elements = element.elements("hierarchicalDatasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            context.register(loadHierarchicalDatasource(ds, datasource, property));
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

        Datasource datasource = builder.reset()
                .setId(id)
                .setMaster(ds)
                .setProperty(property)
                .buildDatasource();

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

        builder.reset().setMetaClass(metaClass).setId(id).setMaster(ds).setProperty(property);

        final CollectionDatasource datasource;

        final Element datasourceClassElement = element.element("datasourceClass");

        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            final Class<CollectionDatasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildCollectionDatasource();
        } else {
            datasource = builder.buildCollectionDatasource();
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    private Datasource loadHierarchicalDatasource(Element element, Datasource ds, String property) {
        final String id = element.attributeValue("id");
        final String hierarchyProperty = element.attributeValue("hierarchyProperty");
        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new IllegalStateException(
                    String.format("Can't find property '%s' in datasource '%s'", property, ds.getId()));
        }

        builder.reset().setMetaClass(metaClass).setId(id).setMaster(ds).setProperty(property);

        final HierarchicalDatasource datasource;

        final Element datasourceClassElement = element.element("datasourceClass");

        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            final Class<HierarchicalDatasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildHierarchicalDatasource();
        } else {
            datasource = builder.buildHierarchicalDatasource();
        }

        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }
        loadDatasources(element, datasource);

        return datasource;
    }

    private Datasource loadGroupDatasource(Element element, Datasource ds, String property) {
        final String id = element.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new IllegalStateException(
                    String.format("Can't find property '%s' in datasource '%s'", property, ds.getId()));
        }

        builder.reset().setMetaClass(metaClass).setId(id).setMaster(ds).setProperty(property);

        final Element datasourceClassElement = element.element("datasourceClass");
        GroupDatasource datasource;
        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            final Class<GroupDatasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildGroupDatasource();
        } else {
            datasource = builder.buildGroupDatasource();
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    private MetaClass loadMetaClass(Element element) {
        final String className = element.attributeValue("class");
        if (className == null)
            return null;

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
        boolean softDeletion = deletion == null || Boolean.valueOf(deletion);

        builder.reset().setMetaClass(metaClass).setId(id).setViewName(viewName).setSoftDeletion(softDeletion);

        final Element datasourceClassElement = element.element("datasourceClass");

        final CollectionDatasource datasource;
        if (datasourceClassElement != null) {
            final String datasourceClass = datasourceClassElement.getText();
            if (StringUtils.isEmpty(datasourceClass))
                throw new IllegalStateException("Datasource class is not specified");

            final Class<CollectionDatasource> aClass = ScriptingProvider.loadClass(datasourceClass);
            datasource = builder.setDsClass(aClass).buildCollectionDatasource();
        } else {
            final CollectionDatasource.FetchMode fetchMode = getFetchMode(element);
            final CollectionDatasource.RefreshMode refreshMode = getRefreshMode(element);

            datasource = builder
                    .setFetchMode(fetchMode)
                    .setRefreshMode(refreshMode)
                    .buildCollectionDatasource();
        }

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

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

    private CollectionDatasource.RefreshMode getRefreshMode(Element element) {
        final String refreshModeName = element.attributeValue("refreshMode");
        CollectionDatasource.RefreshMode refreshMode = CollectionDatasource.RefreshMode.ALWAYS;
        if (StringUtils.isNotEmpty(refreshModeName)) {
            refreshMode = CollectionDatasource.RefreshMode.valueOf(refreshModeName);
        }
        return refreshMode;
    }

    protected CollectionDatasource.FetchMode getFetchMode(Element element) {
        CollectionDatasource.FetchMode mode = null;

        final String fetchMode = element.attributeValue("fetchMode");
        if (!StringUtils.isEmpty(fetchMode)) {
            mode = CollectionDatasource.FetchMode.valueOf(fetchMode);
        }
        return mode;
    }
}
