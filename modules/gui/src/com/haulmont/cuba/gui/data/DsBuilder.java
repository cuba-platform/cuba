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

/**
 * Datasources builder.
 * <p/>
 * Use setters to provide parameters and then invoke one of build... mehods to obtain the datasource implementation.<br>
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

    private Class<? extends Datasource> dsClass;

    private CollectionDatasource.FetchMode fetchMode;

    private CollectionDatasource.RefreshMode refreshMode;

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

    @Deprecated
    public CollectionDatasource.FetchMode getFetchMode() {
        return fetchMode;
    }

    @Deprecated
    public DsBuilder setFetchMode(CollectionDatasource.FetchMode fetchMode) {
        this.fetchMode = fetchMode;
        return this;
    }

    public CollectionDatasource.RefreshMode getRefreshMode() {
        return refreshMode;
    }

    public DsBuilder setRefreshMode(CollectionDatasource.RefreshMode refreshMode) {
        this.refreshMode = refreshMode;
        return this;
    }

    public Class<? extends Datasource> getDsClass() {
        return dsClass;
    }

    public DsBuilder setDsClass(Class<? extends Datasource> dsClass) {
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
        fetchMode = null;
        refreshMode = null;
        dsClass = null;
        return this;
    }

    private void init() {
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
                    datasource = dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
            } else {
                boolean isEmbedded = false;
                if (master != null) {
                    MetaClass metaClass = master.getMetaClass();
                    MetaProperty metaProperty = metaClass.getProperty(property);
                    isEmbedded = AppBeans.get(MetadataTools.class).isEmbedded(metaProperty);
                }
                if (dsClass == null) {
                    datasource = isEmbedded ? new EmbeddedDatasourceImpl() : new PropertyDatasourceImpl();
                } else {
                    datasource = dsClass.newInstance();
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

    public <T extends CollectionDatasource> T buildCollectionDatasource() {
        init();
        CollectionDatasource datasource;
        try {
            if (master == null && property == null) {
                if (dsClass == null) {
                    datasource = CollectionDatasource.FetchMode.LAZY.equals(resolvedFetchMode()) ?
                            new LazyCollectionDatasource() : new CollectionDatasourceImpl();
                } else {
                    datasource = (CollectionDatasource) dsClass.newInstance();
                }
                datasource.setup(dsContext, dataSupplier, id, metaClass, view);
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
        return (T) datasource;
    }

    private CollectionDatasource.FetchMode resolvedFetchMode() {
        CollectionDatasource.FetchMode fm;
        if (fetchMode == null) {
            fm = CollectionDatasource.FetchMode.ALL;
        } else if (CollectionDatasource.FetchMode.AUTO.equals(fetchMode)) {
            if (metaClass == null)
                throw new IllegalStateException("MetaClass is not set");

            boolean lazy = persistenceManager.useLazyCollection(metaClass.getName());
            fm = lazy ? CollectionDatasource.FetchMode.LAZY : CollectionDatasource.FetchMode.ALL;
        } else {
            fm = fetchMode;
        }
        return fm;
    }

    public <T extends HierarchicalDatasource> T buildHierarchicalDatasource() {
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
        return (T) datasource;
    }

    public <T extends GroupDatasource> T buildGroupDatasource() {
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
        return (T) datasource;
    }

    public <T extends RuntimePropsDatasource> T buildRuntimePropsDataSource(String mainDsId) {
        init();
        RuntimePropsDatasourceImpl datasource;
        datasource = new RuntimePropsDatasourceImpl(dsContext, dataSupplier, id, mainDsId);
        registerDatasource(datasource);
        return (T) datasource;
    }

    private void registerDatasource(Datasource datasource) {
        if (dsContext != null && id != null) {
            ((DsContextImplementation) dsContext).register(datasource);
        }
    }
}