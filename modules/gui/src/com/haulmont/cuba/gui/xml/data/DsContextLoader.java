/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.xml.data;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.StringDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.*;
import com.haulmont.cuba.core.global.filter.QueryFilter;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

public class DsContextLoader {

    protected DsBuilder builder;
    private DataSupplier dataservice;
    private Scripting scripting;
    private Metadata metadata;

    protected DsContextImplementation context;

    public DsContextLoader(DataSupplier dataservice) {
        this.dataservice = dataservice;
        this.scripting = AppBeans.get(Scripting.NAME);
        this.metadata = AppBeans.get(Metadata.NAME);
    }

    public DsContext loadDatasources(@Nullable Element element,@Nullable DsContext parent) {
        if (element == null) {
            context = new DsContextImpl(dataservice);
            if (parent != null)
                context.setParent(parent);
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
            Datasource datasource = loadDatasource(ds);
            setLoadDynamicAttributes(ds, datasource);
        }

        //noinspection unchecked
        elements = element.elements("hierarchicalDatasource");
        for (Element ds : elements) {
            Datasource datasource = loadHierarchicalDatasource(ds);
            setLoadDynamicAttributes(ds, datasource);
        }

        //noinspection unchecked
        elements = element.elements("collectionDatasource");
        for (Element ds : elements) {
            CollectionDatasource datasource = loadCollectionDatasource(ds);
            setLoadDynamicAttributes(ds, datasource);
        }

        //noinspection unchecked
        elements = element.elements("groupDatasource");
        for (Element ds : elements) {
            Datasource datasource = loadGroupDatasource(ds);
            setLoadDynamicAttributes(ds, datasource);
        }

        //noinspection unchecked
        elements = element.elements("runtimePropsDatasource");
        for (Element ds : elements) {
            loadRuntimePropsDatasource(ds);
        }

        //noinspection unchecked
        elements = element.elements("valueCollectionDatasource");
        for (Element ds : elements) {
            loadValueCollectionDatasource(ds);
        }

        //noinspection unchecked
        elements = element.elements("valueGroupDatasource");
        for (Element ds : elements) {
            loadValueGroupDatasource(ds);
        }

        //noinspection unchecked
        elements = element.elements("valueHierarchicalDatasource");
        for (Element ds : elements) {
            loadValueHierarchicalDatasource(ds);
        }

        context.executeLazyTasks();

        return context;
    }

    protected void setLoadDynamicAttributes(Element element, Datasource datasource) {
        datasource.setLoadDynamicAttributes("true".equals(element.attributeValue("loadDynamicAttributes")));
    }

    protected DsContextImplementation createDsContext(String contextClass, Element element) {
        DsContextImplementation context;

        final Class<?> aClass = scripting.loadClass(contextClass);
        if (aClass == null)
            throw new IllegalStateException("DsContext class " + contextClass + " is not found");
        try {
            final Constructor<?> constructor = aClass.getConstructor(DataSupplier.class);
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
                .buildHierarchicalDatasource();

        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        loadQuery(element, datasource);

        loadDatasources(element, datasource);

        return datasource;
    }

    @Nullable
    private Class<?> getDatasourceClass(Element element) {
        String datasourceClass = element.attributeValue("datasourceClass");
        if (StringUtils.isEmpty(datasourceClass)) {
            Element datasourceClassElement = element.element("datasourceClass");
            if (datasourceClassElement != null)
                datasourceClass = datasourceClassElement.getTextTrim();
        }
        if (StringUtils.isEmpty(datasourceClass)) {
            return null;
        } else {
            Class<?> aClass = scripting.loadClass(datasourceClass);
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
                .buildGroupDatasource();

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        loadQuery(element, datasource);

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
        String id = getDatasourceId(element);
        MetaClass metaClass = loadMetaClass(element);
        String viewName = element.attributeValue("view");

        builder.reset()
                .setMetaClass(metaClass)
                .setId(id)
                .setViewName(viewName)
                .setAllowCommit(getAllowCommit(element));
    }

    protected void loadDatasources(Element element, Datasource datasource) {
        //noinspection unchecked
        List<Element> elements = element.elements("datasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            loadDatasource(ds, datasource, property);
        }

        //noinspection unchecked
        elements = element.elements("collectionDatasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            loadCollectionDatasource(ds, datasource, property);
        }

        //noinspection unchecked
        elements = element.elements("groupDatasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            loadGroupDatasource(ds, datasource, property);
        }

        //noinspection unchecked
        elements = element.elements("hierarchicalDatasource");
        for (Element ds : elements) {
            final String property = ds.attributeValue("property");
            loadHierarchicalDatasource(ds, datasource, property);
        }

        //noinspection unchecked
        elements = element.elements("runtimePropsDatasource");
        for (Element ds : elements) {
            loadRuntimePropsDatasource(ds);
        }
    }

    private Datasource loadDatasource(Element element, Datasource ds, String property) {
        initPropertyDatasourceAttributes(element, ds, property);

        Datasource datasource = builder.buildDatasource();

        loadDatasources(element, datasource);

        return datasource;
    }

    private void initPropertyDatasourceAttributes(Element element, Datasource ds, String property) {
        String id = getDatasourceId(element);
        MetaClass metaClass = ds.getMetaClass();
        metaClass.getPropertyNN(property); // check property existense

        builder.reset()
                .setMetaClass(metaClass)
                .setId(id)
                .setMaster(ds)
                .setProperty(property)
                .setAllowCommit(getAllowCommit(element));
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

    protected MetaClass loadMetaClass(Element element) {
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
                .buildCollectionDatasource();

        String maxResults = element.attributeValue("maxResults");
        if (!StringUtils.isEmpty(maxResults))
            datasource.setMaxResults(Integer.parseInt(maxResults));

        if (datasource instanceof CollectionDatasource.Suspendable)
            ((CollectionDatasource.Suspendable) datasource).setSuspended(true);

        loadQuery(element, datasource);

        loadDatasources(element, datasource);

        return datasource;
    }

    private void loadQuery(Element element, CollectionDatasource datasource) {
        Element queryElem = element.element("query");
        if (queryElem != null) {
            Element filterElem = queryElem.element("filter");

            String query = queryElem.getText();
            if (!StringUtils.isBlank(query)) {
                if (filterElem != null)
                    datasource.setQuery(query, new QueryFilter(filterElem));
                else
                    datasource.setQuery(query);
            }
        }
    }

    private void initCollectionDatasourceAttributes(Element element, MetaClass metaClass) {
        String id = getDatasourceId(element);
        String viewName = element.attributeValue("view");

        String deletion = element.attributeValue("softDeletion");
        boolean softDeletion = deletion == null || Boolean.parseBoolean(deletion);
        boolean cacheable = Boolean.parseBoolean(element.attributeValue("cacheable"));

        builder.reset()
                .setMetaClass(metaClass)
                .setId(id)
                .setViewName(viewName)
                .setSoftDeletion(softDeletion)
                .setCacheable(cacheable)
                .setRefreshMode(getRefreshMode(element))
                .setMaxResults(getMaxResults(element))
                .setAllowCommit(getAllowCommit(element));
    }

    private int getMaxResults(Element element) {
        String maxResults = element.attributeValue("maxResults");
        return StringUtils.isEmpty(maxResults) ? 0 : Integer.parseInt(maxResults);
    }

    private CollectionDatasource.RefreshMode getRefreshMode(Element element) {
        final String refreshModeName = element.attributeValue("refreshMode");
        return StringUtils.isEmpty(refreshModeName) ?
                CollectionDatasource.RefreshMode.ALWAYS : CollectionDatasource.RefreshMode.valueOf(refreshModeName);
    }

    private boolean getAllowCommit(Element element) {
        final String allowCommitStr = element.attributeValue("allowCommit");
        return StringUtils.isEmpty(allowCommitStr) || Boolean.parseBoolean(allowCommitStr);
    }

    protected RuntimePropsDatasource loadRuntimePropsDatasource(Element element){
        String id = getDatasourceId(element);
        MetaClass metaClass = loadMetaClass(element);

        String mainDsId = element.attributeValue("mainDs");
        if (mainDsId == null) {
            throw new IllegalStateException("RuntimePropsDs attributes not specified");
        }

        String categorizedEntityClassName = element.attributeValue("categorizedEntityClass");
        MetaClass categorizedEntityMetaClass = null;
        if (StringUtils.isNotBlank(categorizedEntityClassName)) {
            final Class<?> aClass = ReflectionHelper.getClass(categorizedEntityClassName);
            categorizedEntityMetaClass = metadata.getSession().getClass(aClass);
        }

        builder.reset().setMetaClass(metaClass).setId(id);

        RuntimePropsDatasource datasource = builder.buildRuntimePropsDatasource(mainDsId, categorizedEntityMetaClass);

        loadDatasources(element, datasource);
        return datasource;
    }

    private ValueCollectionDatasourceImpl loadValueCollectionDatasource(Element element) {
        String id = getDatasourceId(element);
        builder.reset().setMetaClass(metadata.getClassNN(KeyValueEntity.class)).setId(id);

        ValueCollectionDatasourceImpl datasource = builder.buildValuesCollectionDatasource();

        String maxResults = element.attributeValue("maxResults");
        if (!StringUtils.isEmpty(maxResults))
            datasource.setMaxResults(Integer.parseInt(maxResults));

        datasource.setSuspended(true);

        loadQuery(element, datasource);

        loadProperties(element, datasource);

        datasource.setStoreName(element.attributeValue("store"));

        return datasource;
    }

    private ValueGroupDatasourceImpl loadValueGroupDatasource(Element element) {
        String id = getDatasourceId(element);
        builder.reset().setMetaClass(metadata.getClassNN(KeyValueEntity.class)).setId(id);

        ValueGroupDatasourceImpl datasource = builder.buildValuesGroupDatasource();

        String maxResults = element.attributeValue("maxResults");
        if (!StringUtils.isEmpty(maxResults))
            datasource.setMaxResults(Integer.parseInt(maxResults));

        datasource.setSuspended(true);

        loadQuery(element, datasource);

        loadProperties(element, datasource);

        datasource.setStoreName(element.attributeValue("store"));

        return datasource;
    }

    private ValueHierarchicalDatasourceImpl loadValueHierarchicalDatasource(Element element) {
        String id = getDatasourceId(element);
        builder.reset().setMetaClass(metadata.getClassNN(KeyValueEntity.class)).setId(id);

        ValueHierarchicalDatasourceImpl datasource = builder.buildValuesHierarchicalDatasourceImpl();

        String maxResults = element.attributeValue("maxResults");
        if (!StringUtils.isEmpty(maxResults))
            datasource.setMaxResults(Integer.parseInt(maxResults));

        datasource.setSuspended(true);

        loadQuery(element, datasource);

        loadProperties(element, datasource);

        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (!StringUtils.isEmpty(hierarchyProperty)) {
            datasource.setHierarchyPropertyName(hierarchyProperty);
        }

        datasource.setStoreName(element.attributeValue("store"));

        return datasource;
    }

    private void loadProperties(Element element, ValueDatasource datasource) {
        Element propsEl = element.element("properties");
        if (propsEl != null) {
            for (Element propEl : Dom4j.elements(propsEl)) {
                String name = propEl.attributeValue("name");
                String className = propEl.attributeValue("class");
                if (className != null) {
                    datasource.addProperty(name, ReflectionHelper.getClass(className));
                } else {
                    String typeName = propEl.attributeValue("type");
                    Datatype datatype = typeName == null ? Datatypes.get(StringDatatype.NAME) : Datatypes.get(typeName);
                    datasource.addProperty(name, datatype);
                }
            }
            String idName = propsEl.attributeValue("id");
            if (idName != null) {
                if (datasource.getMetaClass().getProperty(idName) == null)
                    throw new DevelopmentException(String.format("Property '%s' is not defined", idName));
                datasource.setIdName(idName);
            }
        }
    }

    protected String getDatasourceId(Element element) {
        String id = element.attributeValue("id");
        for (Datasource datasource : context.getAll()) {
            if (Objects.equals(datasource.getId(), id))
                throw new DevelopmentException("Duplicated datasource id: " + id);
        }
        return id;
    }
}