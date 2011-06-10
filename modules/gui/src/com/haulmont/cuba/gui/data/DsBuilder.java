/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 * Author: Konstantin Krivopustov
 * Created: 01.10.2010 17:49:04
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.impl.*;
import org.apache.commons.lang.ObjectUtils;

import java.lang.reflect.Constructor;

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
 *               .setViewName("_minimal")
 *               .buildCollectionDatasource();</pre>
 *
 * If you set <code>master</code> and <code>property</code> properties you will get a <code>PropertyDatasource</code>
 * implementation.
 *
 * If you don't set <code>fetchMode</code> explicitly, lazy implementation may be chosen based on <code>PersistenceManager</code>
 * statistics.
 */
public class DsBuilder {

    private DataService dataService;
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

    public DsBuilder() {
        this(null);
    }

    public DsBuilder(DsContext dsContext) {
        this.dsContext = dsContext;
        this.id = "ds";

        if (dsContext != null)
            this.dataService = dsContext.getDataService();
        else
            this.dataService = new GenericDataService();

        this.viewRepository = MetadataProvider.getViewRepository();
        this.persistenceManager = ServiceLocator.lookup(PersistenceManagerService.NAME);
    }

    public DsContext getDsContext() {
        return dsContext;
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

    public CollectionDatasource.FetchMode getFetchMode() {
        return fetchMode;
    }

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
        fetchMode = null;
        dsClass = null;
        return this;
    }

    private void init() {
        if (metaClass == null && javaClass != null) {
            metaClass = MetadataProvider.getSession().getClass(javaClass);
        }
        if (view == null && viewName != null) {
            view = viewRepository.getView(metaClass, viewName);
        }
    }

    public Datasource buildDatasource() {
        init();
        Datasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                datasource = new DatasourceImpl(dsContext, dataService, id, metaClass, view);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (Datasource) constructor.newInstance(
                            dsContext, dataService, id, metaClass, view == null ? null : view.getName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (dsClass == null) {
                boolean isEmbedded = false;
                if (master != null) {
                    MetaClass metaClass = master.getMetaClass();
                    MetaProperty metaProperty = metaClass.getProperty(property);
                    isEmbedded = MetadataHelper.isEmbedded(metaProperty);
                }
                if (isEmbedded)
                    datasource = new EmbeddedDatasourceImpl(id, master, property);
                else
                    datasource = new PropertyDatasourceImpl(id, master, property);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            String.class, Datasource.class, String.class);
                    datasource = (Datasource) constructor.newInstance(
                            id, master, property);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return datasource;
    }

    public CollectionDatasource buildCollectionDatasource() {
        init();
        CollectionDatasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                if (CollectionDatasource.FetchMode.LAZY.equals(resolvedFetchMode()))
                    datasource = new LazyCollectionDatasource(dsContext, dataService, id, metaClass, view, softDeletion);
                else {
                    datasource = new CollectionDatasourceImpl(dsContext, dataService, id, metaClass, view, softDeletion);
                    ((CollectionDatasourceImpl) datasource).setRefreshMode(refreshMode);
                }
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (CollectionDatasource) constructor.newInstance(
                            dsContext, dataService, id, metaClass, view == null ? null : view.getName());
                    datasource.setSoftDeletion(softDeletion);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            datasource.setMaxResults(persistenceManager.getMaxFetchUI(metaClass.getName()));
        } else {
            if (dsClass == null) {
                datasource = new CollectionPropertyDatasourceImpl(id, master, property);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            String.class, Datasource.class, String.class);
                    datasource = (CollectionDatasource) constructor.newInstance(
                            id, master, property);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return datasource;
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

    public HierarchicalDatasource buildHierarchicalDatasource() {
        init();
        HierarchicalDatasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                datasource = new HierarchicalDatasourceImpl(dsContext, dataService, id, metaClass, view, softDeletion);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (HierarchicalDatasource) constructor.newInstance(
                            dsContext, dataService, id, metaClass, view == null ? null : view.getName());
                    datasource.setSoftDeletion(softDeletion);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            datasource.setMaxResults(persistenceManager.getMaxFetchUI(metaClass.getName()));
        } else {
            if (dsClass == null) {
                datasource = new HierarchicalPropertyDatasourceImpl(id, master, property);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            String.class, Datasource.class, String.class);
                    datasource = (HierarchicalDatasource) constructor.newInstance(
                            id, master, property);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return datasource;
    }

    public GroupDatasource buildGroupDatasource() {
        init();
        GroupDatasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                datasource = new GroupDatasourceImpl(dsContext, dataService, id, metaClass, view, softDeletion);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (GroupDatasource) constructor.newInstance(
                            dsContext, dataService, id, metaClass, view == null ? null : view.getName());
                    datasource.setSoftDeletion(softDeletion);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            datasource.setMaxResults(persistenceManager.getMaxFetchUI(metaClass.getName()));
        } else {
            if (dsClass == null) {
                datasource = new GroupPropertyDatasourceImpl(id, master, property);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            String.class, Datasource.class, String.class);
                    datasource = (GroupDatasource) constructor.newInstance(
                            id, master, property);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return datasource;
    }

    public RuntimePropsDatasource buildRuntimePropsDataSource(String mainDsId){
        init();
        RuntimePropsDatasourceImpl datasource;
        datasource = new RuntimePropsDatasourceImpl(dsContext, dataService, id, viewName, mainDsId);
        return datasource;
    }
}
