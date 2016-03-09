/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.impl.*;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;

/**
 * Datasources builder.
 * <p/>
 * Use setters to provide parameters and then invoke one of the build* methods to obtain the datasource implementation.<br>
 * <p/>
 * Sample usage:
 * <pre>
 * CollectionDatasource usersDs = new DsBuilder(getDsContext())
 *               .setMetaClass(metaClass)
 *               .setId("usersDs")
 *               .setViewName(View.MINIMAL)
 *               .buildCollectionDatasource();</pre>
 *
 * If you set <code>master</code> and <code>property</code> properties you will get a <code>PropertyDatasource</code>
 * implementation.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DsBuilder {

    private DataSupplier dataSupplier;
    private Metadata metadata;
    private ViewRepository viewRepository;
    private PersistenceManagerService persistenceManager;

    private DsContext dsContext;

    private MetaClass metaClass;

    private Class javaClass;

    private String viewName;

    private View view;

    private String id;

    private boolean softDeletion = true;

    private Datasource master;

    private String property;

    private Class<?> dsClass;

    private CollectionDatasource.RefreshMode refreshMode;

    private int maxResults;

    private boolean allowCommit = true;

    public DsBuilder() {
        this(null);
    }

    public DsBuilder(DsContext dsContext) {
        this.dsContext = dsContext;
        this.id = "ds";

        if (dsContext != null)
            this.dataSupplier = dsContext.getDataSupplier();
        else
            this.dataSupplier = new GenericDataSupplier();

        this.metadata = AppBeans.get(Metadata.NAME);
        this.viewRepository = metadata.getViewRepository();
        this.persistenceManager = AppBeans.get(PersistenceManagerClient.NAME);
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

    public DsBuilder setView(View view) {
        this.view = view;
        if (view != null)
            this.viewName = view.getName();
        return this;
    }

    public DsBuilder setViewName(String viewName) {
        if (!ObjectUtils.equals(viewName, this.viewName)) {
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
            metaClass = metadata.getSession().getClass(javaClass);
        }
        if (view == null && viewName != null) {
            view = viewRepository.getView(metaClass, viewName);
        }
    }

    public Datasource buildDatasource() {
        init();
        Datasource datasource;
        try {
            if (master == null && property == null) {
                if (dsClass == null) {
                    datasource = new DatasourceImpl();
                } else {
                    datasource = (Datasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
            } else {
                boolean isEmbedded = false;
                if (master != null) {
                    MetaClass metaClass = master.getMetaClass();
                    MetaProperty metaProperty = metaClass.getProperty(property);
                    MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
                    isEmbedded = metadataTools.isEmbedded(metaProperty);
                }
                if (dsClass == null) {
                    datasource = isEmbedded ? new EmbeddedDatasourceImpl() : new PropertyDatasourceImpl();
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
                    datasource = new CollectionDatasourceImpl();
                } else {
                    datasource = (CollectionDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
                if (maxResults > 0)
                    datasource.setMaxResults(maxResults);
                else if (metaClass != null)
                    datasource.setMaxResults(persistenceManager.getMaxFetchUI(metaClass.getName()));
                if (datasource instanceof AbstractCollectionDatasource)
                    ((AbstractCollectionDatasource) datasource).setRefreshMode(refreshMode);
            } else {
                if (dsClass == null) {
                    datasource = new CollectionPropertyDatasourceImpl();
                } else {
                    datasource = (CollectionDatasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        datasource.setSoftDeletion(softDeletion);
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
                    datasource = new HierarchicalDatasourceImpl();
                } else {
                    datasource = (HierarchicalDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
                if (maxResults > 0)
                    datasource.setMaxResults(maxResults);
                else if (metaClass != null)
                    datasource.setMaxResults(persistenceManager.getMaxFetchUI(metaClass.getName()));
                if (datasource instanceof AbstractCollectionDatasource)
                    ((AbstractCollectionDatasource) datasource).setRefreshMode(refreshMode);
            } else {
                if (dsClass == null) {
                    datasource = new HierarchicalPropertyDatasourceImpl();
                } else {
                    datasource = (HierarchicalDatasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        datasource.setSoftDeletion(softDeletion);
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
                    datasource = new GroupDatasourceImpl();
                } else {
                    datasource = (GroupDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
                if (maxResults > 0)
                    datasource.setMaxResults(maxResults);
                else if (metaClass != null)
                    datasource.setMaxResults(persistenceManager.getMaxFetchUI(metaClass.getName()));
                if (datasource instanceof AbstractCollectionDatasource)
                    ((AbstractCollectionDatasource) datasource).setRefreshMode(refreshMode);
            } else {
                if (dsClass == null) {
                    datasource = new GroupPropertyDatasourceImpl();
                } else {
                    datasource = (GroupDatasource) dsClass.newInstance();
                }
                ((NestedDatasource) datasource).setup(id, master, property);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        datasource.setSoftDeletion(softDeletion);
        datasource.setAllowCommit(allowCommit);
        registerDatasource(datasource);
        return datasource;
    }

    public RuntimePropsDatasource buildRuntimePropsDataSource(String mainDsId, @Nullable MetaClass categorizedEntityClass) {
        init();
        RuntimePropsDatasourceImpl datasource;
        datasource = new RuntimePropsDatasourceImpl(dsContext, dataSupplier, id, mainDsId, categorizedEntityClass);
        registerDatasource(datasource);
        return datasource;
    }

    private void registerDatasource(Datasource datasource) {
        if (dsContext != null && id != null) {
            ((DsContextImplementation) dsContext).register(datasource);
        }
    }
}