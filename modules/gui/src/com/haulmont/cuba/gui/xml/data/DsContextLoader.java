/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.data;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.filter.QueryFilter;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class DsContextLoader {

    private DsBuilder builder;
    private DataSupplier dataservice;
    private Scripting scripting;
    private Metadata metadata;

    private DsContextImplementation context;

    public DsContextLoader(DataSupplier dataservice) {
        this.dataservice = dataservice;
        this.scripting = AppBeans.get(Scripting.class);
        this.metadata = AppBeans.get(Metadata.class);
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

        //noinspection unchecked
        elements = element.elements("runtimePropsDatasource");
        for (Element ds : elements) {
            context.register(loadRuntimePropsDataSource(ds));
        }

        context.executeLazyTasks();

        return context;
    }

    protected DsContextImplementation createDsContext(String contextClass, Element element) {
        DsContextImplementation context;

        final Class<Object> aClass = scripting.loadClass(contextClass);
        if (aClass == null)
            throw new IllegalStateException("DsContext class " + contextClass + " is not found");
        try {
            final Constructor<Object> constructor = aClass.getConstructor(DataSupplier.class);
            context = (DsContextImplementation) constructor.newInstance(dataservice);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return context;
    }

    protected Datasource loadHierarchicalDatasource(Element element) {
        MetaClass metaClass = loadMetaClass(element);
        initCollectionDatasourceAttributes(element, metaClass);

        HierarchicalDatasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .setFetchMode(getFetchMode(element))
                .setRefreshMode(getRefreshMode(element))
                .setAllowCommit(getAllowCommit(element))
                .buildHierarchicalDatasource();

        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        loadQuery(element, metaClass, datasource);

        loadDatasources(element, datasource);

        return datasource;
    }

    @Nullable
    private Class<? extends Datasource> getDatasourceClass(Element element) {
        String datasourceClass = element.attributeValue("datasourceClass");
        if (StringUtils.isEmpty(datasourceClass)) {
            Element datasourceClassElement = element.element("datasourceClass");
            if (datasourceClassElement != null)
                datasourceClass = datasourceClassElement.getTextTrim();
        }
        if (StringUtils.isEmpty(datasourceClass)) {
            return null;
        } else {
            Class<HierarchicalDatasource> aClass = scripting.loadClass(datasourceClass);
            if (aClass == null)
                throw new IllegalStateException("Datasource class " + datasourceClass + " is not found");
            return aClass;
        }
    }

    protected Datasource loadGroupDatasource(Element element) {
        MetaClass metaClass = loadMetaClass(element);
        initCollectionDatasourceAttributes(element, metaClass);

        GroupDatasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .setFetchMode(getFetchMode(element))
                .setRefreshMode(getRefreshMode(element))
                .setAllowCommit(getAllowCommit(element))
                .buildGroupDatasource();

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        loadQuery(element, metaClass, datasource);

        loadDatasources(element, datasource);

        return datasource;
    }

    protected Datasource loadDatasource(Element element) {
        initDatasourceAttributes(element);

        Datasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .buildDatasource();

        // TODO implement ContextListeners
//        String item = element.attributeValue("item");
//        if (!StringUtils.isBlank(item)) {
//            final ParametersHelper.ParameterInfo info = ParametersHelper.parse(item);
//            context.registerListener(info, datasource);
//        }

        loadDatasources(element, datasource);

        return datasource;
    }

    private void initDatasourceAttributes(Element element) {
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);
        final String viewName = element.attributeValue("view");

        builder.reset().setMetaClass(metaClass).setId(id).setViewName(viewName);
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

        //noinspection unchecked
        elements = element.elements("runtimePropsDatasource");
        for (Element ds : elements) {
            context.register(loadRuntimePropsDataSource(ds));
        }
    }

    private Datasource loadDatasource(Element element, Datasource ds, String property) {
        initPropertyDatasourceAttributes(element, ds, property);

        Datasource datasource = builder.buildDatasource();

        loadDatasources(element, datasource);

        return datasource;
    }

    private void initPropertyDatasourceAttributes(Element element, Datasource ds, String property) {
        String id = element.attributeValue("id");

        MetaClass metaClass = ds.getMetaClass();
        MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new IllegalStateException(
                    String.format("Can't find property '%s' in datasource '%s'", property, ds.getId()));
        }

        builder.reset().setMetaClass(metaClass).setId(id).setMaster(ds).setProperty(property);
    }

    private Datasource loadCollectionDatasource(Element element, Datasource ds, String property) {
        initPropertyDatasourceAttributes(element, ds, property);

        CollectionDatasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .buildCollectionDatasource();

        loadDatasources(element, datasource);

        return datasource;
    }

    private Datasource loadHierarchicalDatasource(Element element, Datasource ds, String property) {
        initPropertyDatasourceAttributes(element, ds, property);

        HierarchicalDatasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .buildHierarchicalDatasource();

        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }

        loadDatasources(element, datasource);

        return datasource;
    }

    private Datasource loadGroupDatasource(Element element, Datasource ds, String property) {
        initPropertyDatasourceAttributes(element, ds, property);

        GroupDatasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .buildGroupDatasource();

        loadDatasources(element, datasource);

        return datasource;
    }

    private MetaClass loadMetaClass(Element element) {
        final String className = element.attributeValue("class");
        if (className == null)
            return null;

        final Class<?> aClass = ReflectionHelper.getClass(className);
        final MetaClass metaClass = metadata.getSession().getClass(aClass);

        if (metaClass == null)
            throw new IllegalStateException(String.format("Can't find metaClass '%s'", className));

        return metaClass;
    }

    protected CollectionDatasource loadCollectionDatasource(Element element) {
        MetaClass metaClass = loadMetaClass(element);
        initCollectionDatasourceAttributes(element, metaClass);

        CollectionDatasource datasource = builder
                .setDsClass(getDatasourceClass(element))
                .setFetchMode(getFetchMode(element))
                .setRefreshMode(getRefreshMode(element))
                .setAllowCommit(getAllowCommit(element))
                .buildCollectionDatasource();

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        loadQuery(element, metaClass, datasource);

        loadDatasources(element, datasource);

        return datasource;
    }

    private void loadQuery(Element element, MetaClass metaClass, CollectionDatasource datasource) {
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
    }

    private void initCollectionDatasourceAttributes(Element element, MetaClass metaClass) {
        final String id = element.attributeValue("id");
        final String viewName = element.attributeValue("view");

        String deletion = element.attributeValue("softDeletion");
        boolean softDeletion = deletion == null || Boolean.valueOf(deletion);

        builder.reset().setMetaClass(metaClass).setId(id).setViewName(viewName).setSoftDeletion(softDeletion);
    }

    private CollectionDatasource.RefreshMode getRefreshMode(Element element) {
        final String refreshModeName = element.attributeValue("refreshMode");
        CollectionDatasource.RefreshMode refreshMode = CollectionDatasource.RefreshMode.ALWAYS;
        if (StringUtils.isNotEmpty(refreshModeName)) {
            refreshMode = CollectionDatasource.RefreshMode.valueOf(refreshModeName);
        }
        return refreshMode;
    }

    private boolean getAllowCommit(Element element) {
        final String allowCommitStr = element.attributeValue("allowCommit");
        boolean allowCommit = true;
        if (StringUtils.isNotEmpty(allowCommitStr))
            allowCommit = Boolean.valueOf(allowCommitStr);
        return allowCommit;
    }

    protected CollectionDatasource.FetchMode getFetchMode(Element element) {
        CollectionDatasource.FetchMode mode = null;

        final String fetchMode = element.attributeValue("fetchMode");
        if (!StringUtils.isEmpty(fetchMode)) {
            mode = CollectionDatasource.FetchMode.valueOf(fetchMode);
        }
        return mode;
    }

    protected RuntimePropsDatasource loadRuntimePropsDataSource(Element element){
        final String id = element.attributeValue("id");
        final MetaClass metaClass = loadMetaClass(element);

        final String mainDsId = element.attributeValue("mainDs");

        if (mainDsId == null) {
            throw new IllegalStateException("RuntimePropsDs attributes not specified");
        }

        builder.reset().setMetaClass(metaClass).setId(id);

        RuntimePropsDatasource datasource = builder.buildRuntimePropsDataSource(mainDsId);

        loadDatasources(element, datasource);
        return datasource;
    }
}