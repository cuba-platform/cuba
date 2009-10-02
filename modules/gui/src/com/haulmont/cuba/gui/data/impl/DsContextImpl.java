/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:18:21
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;

import java.util.*;

import org.apache.commons.lang.ObjectUtils;

public class DsContextImpl implements DsContextImplementation {
    private WindowContext windowContext;
    private DataService dataservice;

    private DsContext parent;
    private List<DsContext> children = new ArrayList<DsContext>();

    private Map<String, Datasource> datasourceMap =
            new HashMap<String, Datasource>();

    private Map<String, Collection<Datasource>> contextListeners =
            new HashMap<String, Collection<Datasource>>();

    protected List<LazyTask> lazyTasks = new ArrayList<LazyTask>();

    private Set<CommitListener> commitListeners = new LinkedHashSet<CommitListener>();

    public DsContextImpl(DataService dataservice) {
        this.dataservice = dataservice;
    }

    public void addLazyTask(LazyTask lazyTask) {
        if (!lazyTasks.contains(lazyTask)) lazyTasks.add(lazyTask);
    }

    public void executeLazyTasks() {
        for (LazyTask lazyTask : lazyTasks) {
            lazyTask.execute(this);
        }
    }

    public WindowContext getWindowContext() {
        return windowContext;
    }

    public void setWindowContext(WindowContext windowContext) {
        this.windowContext = windowContext;
        for (Map.Entry<String, Collection<Datasource>> entry : contextListeners.entrySet()) {
            final String property = entry.getKey();
            final Object value = windowContext.getValue(property);

            if (value != null) {
                final Collection<Datasource> datasources = entry.getValue();
                for (Datasource datasource : datasources) {
                    datasource.setItem((Entity) value);
                }
            }
        }

        windowContext.addValueListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                for (Map.Entry<String, Collection<Datasource>> entry : contextListeners.entrySet()) {
                    if (entry.getKey().equals(property)) {
                        final Collection<Datasource> datasources = entry.getValue();
                        for (Datasource datasource : datasources) {
                            datasource.setItem((Entity) value);
                        }
                    }
                }
            }
        });
    }

    public void commit() {
        for (DsContext childDsContext : children) {
            for (Datasource datasource : childDsContext.getAll()) {
                if (Datasource.CommitMode.PARENT.equals(datasource.getCommitMode())) {
                    datasource.commit();
                }
            }
        }
        for (Datasource datasource : datasourceMap.values()) {
            if (Datasource.CommitMode.PARENT.equals(datasource.getCommitMode())) {
                datasource.commit();
            }
        }

        final Map<DataService, Collection<Datasource<Entity>>> commitData = collectCommitData();

        if (commitData.isEmpty()) return;

        final DataService dataservice = getDataService();
        final Set<DataService> services = commitData.keySet();

        if (services.size() == 1 &&
                ObjectUtils.equals(services.iterator().next(), dataservice))
        {
            final CommitContext<Entity> context = createCommitContext(dataservice, commitData);

            fireBeforeCommit(context);

            final Map<Entity, Entity> map = dataservice.commit(context);

            fireAfterCommit(context, map);

            for (Datasource<Entity> datasource : commitData.get(dataservice)) {
                ((DatasourceImplementation) datasource).commited(map);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void fireBeforeCommit(CommitContext<Entity> context) {
        for (CommitListener commitListener : commitListeners) {
            commitListener.beforeCommit(context);
        }
    }

    private void fireAfterCommit(CommitContext<Entity> context, Map<Entity, Entity> result) {
        for (CommitListener commitListener : commitListeners) {
            commitListener.afterCommit(context, result);
        }
    }

    protected CommitContext<Entity> createCommitContext(DataService dataservice, Map<DataService, Collection<Datasource<Entity>>> commitData) {

        final CommitContext<Entity> context =
                new CommitContext<Entity>();

        for (Datasource<Entity> datasource : commitData.get(dataservice)) {
            final DatasourceImplementation<Entity> implementation = (DatasourceImplementation) datasource;

            for (Entity entity : implementation.getItemsToCreate()) {
                context.getCommitInstances().add(entity);
                context.getViews().put(entity, datasource.getView());
            }
            for (Entity entity : implementation.getItemsToUpdate()) {
                context.getCommitInstances().add(entity);
                context.getViews().put(entity, datasource.getView());
            }
            for (Entity entity : implementation.getItemsToDelete()) {
                context.getRemoveInstances().add(entity);
                context.getViews().put(entity, datasource.getView());
            }
        }
        return context;
    }

    protected Map<DataService, Collection<Datasource<Entity>>> collectCommitData() {
        Collection<Datasource> datasources = new ArrayList<Datasource>();

        for (DsContext childDsContext : children) {
            datasources.addAll(childDsContext.getAll());
        }
        datasources.addAll(datasourceMap.values());

        final Map<DataService,Collection<Datasource<Entity>>> commitDatasources =
                new HashMap<DataService,Collection<Datasource<Entity>>>();

        for (Datasource datasource : datasources) {
            if (Datasource.CommitMode.DATASTORE.equals(datasource.getCommitMode()) &&
                    datasource.isModified())
            {
                final DataService dataservice = datasource.getDataService();
                Collection<Datasource<Entity>> collection = commitDatasources.get(dataservice);
                if (collection == null) {
                    collection = new ArrayList<Datasource<Entity>>();
                    commitDatasources.put(dataservice, collection);
                }
                collection.add(datasource);
            }
        }
        return commitDatasources;
    }

    protected Map<Datasource, Datasource> dependencies = new HashMap<Datasource, Datasource>();

    public void registerDependency(final Datasource datasource, final Datasource dependFrom, final String propertyName) {
        if (dependencies.containsKey(datasource)) throw new UnsupportedOperationException();

        final DatasourceListener listener = new CollectionDatasourceListener<Entity>() {
            public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                if (Datasource.State.VALID.equals(datasource.getState()))
                    datasource.refresh();
            }

            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {}

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (propertyName != null && ObjectUtils.equals(propertyName, property)) {
                    final Entity item = Datasource.State.VALID.equals(dependFrom.getState()) ? dependFrom.getItem() : null;
                    if (ObjectUtils.equals(item, source)) {
                        datasource.refresh();
                    }
                }
            }

            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                if (Operation.REFRESH.equals(operation)) {
                    datasource.refresh();
                }
            }
        };

        dependFrom.addListener(listener);
        dependencies.put(datasource, dependFrom);
    }

    public void addListener(CommitListener listener) {
        commitListeners.add(listener);
    }

    public void removeListener(CommitListener listener) {
        commitListeners.remove(listener);
    }

    public DataService getDataService() {
        return dataservice;
    }

    public <T extends Datasource> T get(String id) {
        Datasource ds = datasourceMap.get(id);
        if (ds == null && parent != null) {
            ds = parent.get(id);
        }
        return (T) ds;
    }

    public Collection<Datasource> getAll() {
        return datasourceMap.values();
    }

    public boolean isModified() {
        for (Datasource datasource : datasourceMap.values()) {
            if (datasource.isModified()) {
                return true;
            }
        }
        return false;
    }

    public void refresh() {
        final Collection<Datasource> datasources = datasourceMap.values();
        for (Datasource datasource : datasources) {
            if (dependencies.containsKey(datasource)) continue;
            datasource.refresh();
        }
    }

    public void register(Datasource datasource) {
        datasourceMap.put(datasource.getId(), datasource);
    }

    public void registerListener(ParametersHelper.ParameterInfo item, Datasource datasource) {
        if (ParametersHelper.ParameterInfo.Type.PARAM.equals(item.getType())) {
            Collection<Datasource> collection = contextListeners.get(item.getPath());
            if (collection == null) {
                collection = new ArrayList<Datasource>();
                contextListeners.put(item.getPath(), collection);
            }
            collection.add(datasource);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public DsContext getParent() {
        return parent;
    }

    public void setParent(DsContext parent) {
        this.parent = parent;
        if (!parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
    }

    public List<DsContext> getChildren() {
        return children;
    }
}
