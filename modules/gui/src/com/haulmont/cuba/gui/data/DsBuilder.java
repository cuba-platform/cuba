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
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.impl.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Datasources builder.
 * <p>
 * Use setters to provide parameters and then invoke one of the build* methods to obtain the datasource implementation.<br>
 * <p>
 * Sample usage:
 * <pre>
 * CollectionDatasource usersDs = DsBuilder.create(getDsContext())
 *               .setMetaClass(metaClass)
 *               .setId("usersDs")
 *               .setViewName(View.MINIMAL)
 *               .buildCollectionDatasource();</pre>
 *
 * If you set <code>master</code> and <code>property</code> properties you will get a <code>PropertyDatasource</code>
 * implementation.
 * <p>
 * In order to provide your own implementations of standard datasources, create a subclass, override corresponding
 * {@code createXYZDatasource()} methods and register the subclass in {@code spring.xml}, for example:
 * <pre>
 *     &lt;bean id="cuba_DsBuilder" class="com.company.sample.gui.MyDsBuilder" scope="prototype"/&gt;
 * </pre>
 */
@Component(DsBuilder.NAME)
@Scope("prototype")
public class DsBuilder {

    public static final String NAME = "cuba_DsBuilder";

    private DataSupplier dataSupplier;

    private DsContext dsContext;

    private MetaClass metaClass;

    private Class javaClass;

    private String viewName;

    private View view;

    private String id;

    private boolean softDeletion = true;

    private boolean cacheable;

    private Datasource master;

    private String property;

    private Class<?> dsClass;

    private CollectionDatasource.RefreshMode refreshMode;

    private int maxResults;

    private boolean allowCommit = true;

    /**
     * Creates DsBuilder for building datasources not belonging to a {@link DsContext}.
     */
    public static DsBuilder create() {
        return AppBeans.getPrototype(NAME);
    }

    /**
     * Creates DsBuilder for building datasources for the given {@link DsContext}.
     *
     * @param dsContext context instance or null
     */
    public static DsBuilder create(@Nullable DsContext dsContext) {
        return AppBeans.getPrototype(NAME, dsContext);
    }

    /**
     * INTERNAL
     */
    public DsBuilder() {
        this(null);
    }

    /**
     * INTERNAL
     */
    public DsBuilder(@Nullable DsContext dsContext) {
        this.dsContext = dsContext;
        this.id = "ds";

        if (dsContext != null)
            this.dataSupplier = dsContext.getDataSupplier();
        else
            this.dataSupplier = new GenericDataSupplier();
    }

    protected Datasource createDatasource() {
        return new DatasourceImpl();
    }

    protected NestedDatasource createPropertyDatasource() {
        return new PropertyDatasourceImpl();
    }

    protected NestedDatasource createEmbeddedDatasource() {
        return new EmbeddedDatasourceImpl();
    }

    protected CollectionDatasource createCollectionDatasource() {
        return new CollectionDatasourceImpl();
    }

    protected CollectionDatasource createCollectionPropertyDatasource() {
        return new CollectionPropertyDatasourceImpl();
    }

    protected GroupDatasource createGroupDatasource() {
        return new GroupDatasourceImpl();
    }

    protected GroupDatasource createGroupPropertyDatasource() {
        return new GroupPropertyDatasourceImpl();
    }

    protected HierarchicalDatasource createHierarchicalDatasource() {
        return new HierarchicalDatasourceImpl();
    }

    protected HierarchicalDatasource createHierarchicalPropertyDatasource() {
        return new HierarchicalPropertyDatasourceImpl();
    }

    protected ValueCollectionDatasourceImpl createValueCollectionDatasource() {
        return new ValueCollectionDatasourceImpl();
    }

    protected ValueGroupDatasourceImpl createValueGroupDatasource() {
        return new ValueGroupDatasourceImpl();
    }

    protected ValueHierarchicalDatasourceImpl createValueHierarchicalDatasource() {
        return new ValueHierarchicalDatasourceImpl();
    }

    protected PersistenceManagerService getPersistenceManager() {
        return AppBeans.get(PersistenceManagerClient.NAME);
    }

    protected ViewRepository getViewRepository() {
        return AppBeans.get(ViewRepository.NAME);
    }

    protected Metadata getMetadata() {
        return AppBeans.get(Metadata.NAME);
    }

    public DsContext getDsContext() {
        return dsContext;
    }

    public DataSupplier getDataSupplier() {
        return dataSupplier;
    }

    public DsBuilder setDataSupplier(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
        return this;
    }

    public String getId() {
        return id;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public DsBuilder setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
        return this;
    }

    public boolean isAllowCommit() {
        return allowCommit;
    }

    public DsBuilder setAllowCommit(boolean allowCommit) {
        this.allowCommit = allowCommit;
        return this;
    }

    public View getView() {
        return view;
    }

    public String getViewName() {
        return viewName;
    }

    public DsBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public DsBuilder setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
        return this;
    }

    public DsBuilder setView(@Nullable View view) {
        this.view = view;
        if (view != null)
            this.viewName = view.getName();
        return this;
    }

    public DsBuilder setViewName(String viewName) {
        if (!Objects.equals(viewName, this.viewName)) {
            this.viewName = viewName;
            this.view = null;
        }
        return this;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public DsBuilder setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        return this;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public DsBuilder setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    public Datasource getMaster() {
        return master;
    }

    public DsBuilder setMaster(Datasource master) {
        this.master = master;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public DsBuilder setProperty(String property) {
        this.property = property;
        return this;
    }

    public CollectionDatasource.RefreshMode getRefreshMode() {
        return refreshMode;
    }

    public DsBuilder setRefreshMode(CollectionDatasource.RefreshMode refreshMode) {
        this.refreshMode = refreshMode;
        return this;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public DsBuilder setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public Class<?> getDsClass() {
        return dsClass;
    }

    /**
     * Set datasource implementation class. If not specified, a standard implementation is used.
     */
    public DsBuilder setDsClass(Class<?> dsClass) {
        this.dsClass = dsClass;
        return this;
    }

    public DsBuilder reset() {
        view = null;
        viewName = null;
        metaClass = null;
        javaClass = null;
        master = null;
        property = null;
        softDeletion = true;
        allowCommit = true;
        refreshMode = null;
        maxResults = 0;
        dsClass = null;
        return this;
    }

    protected void init() {
        if (metaClass == null && javaClass != null) {
            metaClass = getMetadata().getSession().getClass(javaClass);
        }
        if (view == null && viewName != null) {
            view = getViewRepository().getView(metaClass, viewName);
        }
    }

    public Datasource buildDatasource() {
        init();
        Datasource datasource;
        try {
            if (master == null && property == null) {
                if (dsClass == null) {
                    datasource = createDatasource();
                } else {
                    datasource = (Datasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
            } else {
                boolean isEmbedded = false;
                if (master != null) {
                    MetaClass metaClass = master.getMetaClass();
                    MetaProperty metaProperty = metaClass.getPropertyNN(property);
                    MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
                    isEmbedded = metadataTools.isEmbedded(metaProperty);
                }
                if (dsClass == null) {
                    datasource = isEmbedded ? createEmbeddedDatasource() : createPropertyDatasource();
                } else {
                    datasource = (Datasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        datasource.setAllowCommit(allowCommit);
        registerDatasource(datasource);
        return datasource;
    }

    /**
     * Build a {@link CollectionDatasource} with the specified implementation class.
     */
    @SuppressWarnings("unchecked")
    public <T> T buildCollectionDatasource(Class<T> datasourceClass) {
        setDsClass(datasourceClass);
        return (T) buildCollectionDatasource();
    }

    /**
     * Build a {@link CollectionDatasource} with the standard implementation.
     */
    public CollectionDatasource buildCollectionDatasource() {
        init();
        CollectionDatasource datasource;
        try {
            if (master == null && property == null) {
                if (dsClass == null) {
                    datasource = createCollectionDatasource();
                } else {
                    datasource = (CollectionDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
                if (maxResults > 0)
                    datasource.setMaxResults(maxResults);
                else if (metaClass != null)
                    datasource.setMaxResults(getPersistenceManager().getMaxFetchUI(metaClass.getName()));
                if (datasource instanceof AbstractCollectionDatasource)
                    ((AbstractCollectionDatasource) datasource).setRefreshMode(refreshMode);
            } else {
                if (dsClass == null) {
                    datasource = createCollectionPropertyDatasource();
                } else {
                    datasource = (CollectionDatasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        datasource.setSoftDeletion(softDeletion);
        datasource.setCacheable(cacheable);
        datasource.setAllowCommit(allowCommit);
        registerDatasource(datasource);
        return datasource;
    }

    /**
     * Build a {@link HierarchicalDatasource} with the specified implementation class.
     */
    @SuppressWarnings("unchecked")
    public <T> T buildHierarchicalDatasource(Class<T> datasourceClass) {
        setDsClass(datasourceClass);
        return (T) buildHierarchicalDatasource();
    }

    /**
     * Build a {@link HierarchicalDatasource} with the standard implementation.
     */
    public HierarchicalDatasource buildHierarchicalDatasource() {
        init();
        HierarchicalDatasource datasource;
        try {
            if (master == null && property == null) {
                if (dsClass == null) {
                    datasource = createHierarchicalDatasource();
                } else {
                    datasource = (HierarchicalDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
                if (maxResults > 0)
                    datasource.setMaxResults(maxResults);
                else if (metaClass != null)
                    datasource.setMaxResults(getPersistenceManager().getMaxFetchUI(metaClass.getName()));
                if (datasource instanceof AbstractCollectionDatasource)
                    ((AbstractCollectionDatasource) datasource).setRefreshMode(refreshMode);
            } else {
                if (dsClass == null) {
                    datasource = createHierarchicalPropertyDatasource();
                } else {
                    datasource = (HierarchicalDatasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        datasource.setSoftDeletion(softDeletion);
        datasource.setCacheable(cacheable);
        datasource.setAllowCommit(allowCommit);
        registerDatasource(datasource);
        return datasource;
    }

    /**
     * Build a {@link GroupDatasource} with the specified implementation class.
     */
    @SuppressWarnings("unchecked")
    public <T> T buildGroupDatasource(Class<T> datasourceClass) {
        setDsClass(datasourceClass);
        return (T) buildGroupDatasource();
    }

    /**
     * Build a {@link GroupDatasource} with the standard implementation.
     */
    public GroupDatasource buildGroupDatasource() {
        init();
        GroupDatasource datasource;
        try {
            if (master == null && property == null) {
                if (dsClass == null) {
                    datasource = createGroupDatasource();
                } else {
                    datasource = (GroupDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
                if (maxResults > 0)
                    datasource.setMaxResults(maxResults);
                else if (metaClass != null)
                    datasource.setMaxResults(getPersistenceManager().getMaxFetchUI(metaClass.getName()));
                if (datasource instanceof AbstractCollectionDatasource)
                    ((AbstractCollectionDatasource) datasource).setRefreshMode(refreshMode);
            } else {
                if (dsClass == null) {
                    datasource = createGroupPropertyDatasource();
                } else {
                    datasource = (GroupDatasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        datasource.setSoftDeletion(softDeletion);
        datasource.setCacheable(cacheable);
        datasource.setAllowCommit(allowCommit);
        registerDatasource(datasource);
        return datasource;
    }

    public RuntimePropsDatasource buildRuntimePropsDatasource(String mainDsId, @Nullable MetaClass categorizedEntityClass) {
        init();
        RuntimePropsDatasourceImpl datasource;
        datasource = new RuntimePropsDatasourceImpl(dsContext, dataSupplier, id, mainDsId, categorizedEntityClass);
        registerDatasource(datasource);
        return datasource;
    }

    /**
     * Build a {@link ValueCollectionDatasourceImpl} with the standard implementation.
     */
    public ValueCollectionDatasourceImpl buildValuesCollectionDatasource() {
        try {
            ValueCollectionDatasourceImpl datasource = dsClass == null ?
                    createValueCollectionDatasource() : (ValueCollectionDatasourceImpl) dsClass.newInstance();
            datasource.setup(dsContext, dataSupplier, id, metaClass, null);
            if (maxResults > 0)
                datasource.setMaxResults(maxResults);
            datasource.setSoftDeletion(softDeletion);
            registerDatasource(datasource);
            return datasource;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Build a {@link ValueCollectionDatasourceImpl} with the specified implementation class.
     */
    @SuppressWarnings("unchecked")
    public <T> T buildValuesCollectionDatasource(Class<T> datasourceClass) {
        setDsClass(datasourceClass);
        return (T) buildValuesCollectionDatasource();
    }

    /**
     * Build a {@link ValueGroupDatasourceImpl} with the standard implementation.
     */
    public ValueGroupDatasourceImpl buildValuesGroupDatasource() {
        try {
            ValueGroupDatasourceImpl datasource = dsClass == null ?
                    createValueGroupDatasource() : (ValueGroupDatasourceImpl) dsClass.newInstance();
            datasource.setup(dsContext, dataSupplier, id, metaClass, null);
            if (maxResults > 0)
                datasource.setMaxResults(maxResults);
            datasource.setSoftDeletion(softDeletion);
            registerDatasource(datasource);
            return datasource;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Build a {@link ValueGroupDatasourceImpl} with the specified implementation class.
     */
    @SuppressWarnings("unchecked")
    public <T> T buildValuesGroupDatasource(Class<T> datasourceClass) {
        setDsClass(datasourceClass);
        return (T) buildValuesGroupDatasource();
    }

    /**
     * Build a {@link ValueHierarchicalDatasourceImpl} with the standard implementation.
     */
    public ValueHierarchicalDatasourceImpl buildValuesHierarchicalDatasource() {
        try {
            ValueHierarchicalDatasourceImpl datasource = dsClass == null ?
                    createValueHierarchicalDatasource() : (ValueHierarchicalDatasourceImpl) dsClass.newInstance();
            datasource.setup(dsContext, dataSupplier, id, metaClass, null);
            if (maxResults > 0)
                datasource.setMaxResults(maxResults);
            datasource.setSoftDeletion(softDeletion);
            registerDatasource(datasource);
            return datasource;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Build a {@link ValueHierarchicalDatasourceImpl} with the specified implementation class.
     */
    @SuppressWarnings("unchecked")
    public <T> T buildValuesHierarchicalDatasource(Class<T> datasourceClass) {
        setDsClass(datasourceClass);
        return (T) buildValuesHierarchicalDatasource();
    }

    private void registerDatasource(Datasource datasource) {
        if (dsContext != null && id != null) {
            ((DsContextImplementation) dsContext).register(datasource);
        }
    }
}