/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.10.2010 17:49:04
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.impl.*;
import org.apache.commons.lang.ObjectUtils;

import java.lang.reflect.Constructor;

public class DsBuilder {

    private GenericDataService gds = new GenericDataService();
    private ViewRepository viewRepository = MetadataProvider.getViewRepository();

    private DsContext dsContext;

    private MetaClass metaClass;

    private String viewName;

    private View view;

    private String id;

    private boolean softDeletion = true;

    private Datasource master;

    private String property;

    private Class<? extends Datasource> dsClass;

    private CollectionDatasource.FetchMode fetchMode;

    public DsBuilder(DsContext dsContext) {
        this.dsContext = dsContext;
        this.id = "ds";
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
        master = null;
        property = null;
        softDeletion = true;
        fetchMode = null;
        dsClass = null;
        return this;
    }

    private void initView() {
        if (view == null && viewName != null) {
            view = viewRepository.getView(metaClass, viewName);
        }
    }

    public Datasource buildDatasource() {
        initView();
        Datasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                datasource = new DatasourceImpl(dsContext, gds, id, metaClass, view);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (Datasource) constructor.newInstance(
                            dsContext, gds, id, metaClass, view.getName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (dsClass == null) {
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
        initView();
        CollectionDatasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                if (CollectionDatasource.FetchMode.LAZY.equals(resolvedFetchMode()))
                    datasource = new LazyCollectionDatasource(dsContext, gds, id, metaClass, view, softDeletion);
                else
                    datasource = new CollectionDatasourceImpl(dsContext, gds, id, metaClass, view, softDeletion);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (CollectionDatasource) constructor.newInstance(
                            dsContext, gds, id, metaClass, view.getName());
                    datasource.setSoftDeletion(softDeletion);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
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
        if (fetchMode != null)
            return fetchMode;

        if (metaClass == null)
            throw new IllegalStateException("MetaClass is not set");

        PersistenceManagerService pms = ServiceLocator.lookup(PersistenceManagerService.NAME);
        boolean lazy = pms.useLazyCollection(metaClass.getName());
        return lazy ? CollectionDatasource.FetchMode.LAZY : CollectionDatasource.FetchMode.ALL;
    }

    public HierarchicalDatasource buildHierarchicalDatasource() {
        initView();
        HierarchicalDatasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                datasource = new HierarchicalDatasourceImpl(dsContext, gds, id, metaClass, view, softDeletion);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (HierarchicalDatasource) constructor.newInstance(
                            dsContext, gds, id, metaClass, view.getName());
                    datasource.setSoftDeletion(softDeletion);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new UnsupportedOperationException("HierarchicalDatasource can not be PropertyDatasource");
        }
        return datasource;
    }

    public GroupDatasource buildGroupDatasource() {
        initView();
        GroupDatasource datasource;
        if (master == null && property == null) {
            if (dsClass == null) {
                datasource = new GroupDatasourceImpl(dsContext, gds, id, metaClass, view, softDeletion);
            } else {
                try {
                    Constructor constructor = dsClass.getConstructor(
                            DsContext.class, DataService.class, String.class, MetaClass.class, String.class);
                    datasource = (GroupDatasource) constructor.newInstance(
                            dsContext, gds, id, metaClass, view.getName());
                    datasource.setSoftDeletion(softDeletion);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
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

}
