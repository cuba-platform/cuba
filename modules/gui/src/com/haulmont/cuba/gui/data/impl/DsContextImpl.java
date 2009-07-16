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
import com.haulmont.cuba.core.global.DataServiceRemote;

import java.util.*;

import org.apache.commons.lang.ObjectUtils;

public class DsContextImpl implements DsContextImplementation {
    private WindowContext windowContext;
    private DataService dataservice;

    private Map<String, Datasource> datasourceMap =
            new HashMap<String, Datasource>();

    private Map<String, Collection<Datasource>> contextListeners =
            new HashMap<String, Collection<Datasource>>();

    protected List<LazyTask> lazyTasks = new ArrayList<LazyTask>();

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
        final Map<DataService, Collection<Datasource<Entity>>> commitData = collectCommitData();

        if (commitData.isEmpty()) return;

        final DataService dataservice = getDataService();
        final Set<DataService> services = commitData.keySet();

        if (services.size() == 1 &&
                ObjectUtils.equals(services.iterator().next(), dataservice))
        {
            final DataServiceRemote.CommitContext<Entity> context = createCommitContext(dataservice, commitData);
            final Map<Entity, Entity> map = dataservice.commit(context);

            for (Datasource<Entity> datasource : commitData.get(dataservice)) {
                ((DatasourceImplementation) datasource).commited(map);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    protected DataServiceRemote.CommitContext<Entity> createCommitContext(DataService dataservice, Map<DataService, Collection<Datasource<Entity>>> commitData) {
        Set<Entity> commitInstances = new HashSet<Entity>();
        Set<Entity> deleteInstances = new HashSet<Entity>();

        for (Datasource<Entity> datasource : commitData.get(dataservice)) {
            final DatasourceImplementation<Entity> implementation = (DatasourceImplementation) datasource;

            commitInstances.addAll(implementation.getItemsToCreate());
            commitInstances.addAll(implementation.getItemsToUpdate());

            deleteInstances.addAll(implementation.getItemsToDelete());
        }

        final DataServiceRemote.CommitContext<Entity> context =
                new DataServiceRemote.CommitContext<Entity>(commitInstances, deleteInstances);
        return context;
    }

    protected Map<DataService, Collection<Datasource<Entity>>> collectCommitData() {
        final Collection<Datasource> datasources = datasourceMap.values();
        final Map<DataService,Collection<Datasource<Entity>>> commitDatasources =
                new HashMap<DataService,Collection<Datasource<Entity>>>();

        for (Datasource datasource : datasources) {
            if (!Datasource.CommitMode.NOT_SUPPORTED.equals(datasource.getCommitMode()) &&
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

    public void regirterDependency(final Datasource datasource, final Datasource dependFrom, final String propertyName) {
        if (dependencies.containsKey(datasource)) throw new UnsupportedOperationException();

        final DatasourceListener listener = new CollectionDatasourceListener<Entity>() {
            public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
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

            public void collectionChanged(CollectionDatasource ds, CollectionOperation operation) {
                if (CollectionOperation.Type.REFRESH.equals(operation.getType())) {
                    datasource.refresh();
                }
            }
        };

        dependFrom.addListener(listener);
        dependencies.put(datasource, dependFrom);
    }

    public DataService getDataService() {
        return dataservice;
    }

    public <T extends Datasource> T get(String id) {
        return (T) datasourceMap.get(id);
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
}
