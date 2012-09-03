/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:18:21
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParameterInfo;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public class DsContextImpl implements DsContextImplementation {

    private WindowContext windowContext;
    private DataService dataservice;

    private DsContext parent;
    private List<DsContext> children = new ArrayList<DsContext>();

    private Map<String, Datasource> datasourceMap = new HashMap<String, Datasource>();

    protected Map<Datasource, Datasource> dependencies = new HashMap<Datasource, Datasource>();

    // TODO implement ContextListeners
//    private Map<String, Collection<Datasource>> contextListeners =
//            new HashMap<String, Collection<Datasource>>();

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

    public void resumeSuspended() {
        LinkedList<CollectionDatasource.Suspendable> list = new LinkedList<CollectionDatasource.Suspendable>();

        addDsContextToResume(this, list);

        for (CollectionDatasource.Suspendable suspendable : list) {
            suspendable.setSuspended(false);
        }
    }

    private void addDsContextToResume(DsContext dsContext, LinkedList<CollectionDatasource.Suspendable> list) {
        for (Datasource datasource : dsContext.getAll()) {
            if (datasource instanceof CollectionDatasource.Suspendable) {
                addDatasourceToResume(list, datasource);
            }
        }
        for (DsContext childContext : dsContext.getChildren()) {
            addDsContextToResume(childContext, list);
        }
    }

    private void addDatasourceToResume(LinkedList<CollectionDatasource.Suspendable> list, Datasource datasource) {
        if (list.contains(datasource))
            return;

        if (dependencies.containsKey(datasource)) {
            Datasource master = dependencies.get(datasource);
            addDatasourceToResume(list, master);
        }
        if (datasource instanceof CollectionDatasource.Suspendable
                && ((CollectionDatasource.Suspendable) datasource).isSuspended())
            list.add((CollectionDatasource.Suspendable) datasource);
    }

    public WindowContext getWindowContext() {
        return windowContext;
    }

    public void setWindowContext(WindowContext windowContext) {
        this.windowContext = windowContext;
        // TODO implement ContextListeners
//        for (Map.Entry<String, Collection<Datasource>> entry : contextListeners.entrySet()) {
//            final String property = entry.getKey();
//            final Object value = windowContext.getValue(property);
//
//            if (value != null) {
//                final Collection<Datasource> datasources = entry.getValue();
//                for (Datasource datasource : datasources) {
//                    datasource.setItem((Entity) value);
//                }
//            }
//        }
//
//        windowContext.addValueListener(new ValueListener() {
//            public void valueChanged(Object source, String property, Object prevValue, Object value) {
//                for (Map.Entry<String, Collection<Datasource>> entry : contextListeners.entrySet()) {
//                    if (entry.getKey().equals(property)) {
//                        final Collection<Datasource> datasources = entry.getValue();
//                        for (Datasource datasource : datasources) {
//                            datasource.setItem((Entity) value);
//                        }
//                    }
//                }
//            }
//        });
    }

    public boolean commit() {
        for (DsContext childDsContext : children) {
            commitToParent(childDsContext.getAll());
        }
        commitToParent(datasourceMap.values());

        final Map<DataService, Collection<Datasource<Entity>>> commitData = collectCommitData();

        if (commitData.isEmpty())
            return false;

        final DataService dataservice = getDataService();
        final Set<DataService> services = commitData.keySet();

        if (services.size() == 1 &&
                ObjectUtils.equals(services.iterator().next(), dataservice))
        {
            final CommitContext context = createCommitContext(dataservice, commitData);

            fireBeforeCommit(context);

            final Set<Entity> committedEntities = dataservice.commit(context);

            fireAfterCommit(context, committedEntities);

            notifyAllDsCommited(dataservice, committedEntities);
        } else {
            throw new UnsupportedOperationException();
        }
        return true;
    }

    private void commitToParent(Collection<Datasource> datasources) {
        List<Datasource> list = new ArrayList<Datasource>();
        for (Datasource datasource : datasources) {
            if (Datasource.CommitMode.PARENT.equals(datasource.getCommitMode())) {
                list.add(datasource);
            }
        }
        Collections.sort(list, new Comparator<Datasource>() {
            @Override
            public int compare(Datasource ds1, Datasource ds2) {
                if (ds1 instanceof NestedDatasource && ((NestedDatasource) ds1).getMaster() == ds2)
                    return 1;
                if (ds2 instanceof NestedDatasource && ((NestedDatasource) ds2).getMaster() == ds1)
                    return -1;
                return 0;
            }
        });
        for (Datasource datasource : list) {
            datasource.commit();
        }
    }

    private void notifyAllDsCommited(DataService dataservice, Set<Entity> committedEntities) {
        // Notify all datasources in context
        Collection<Datasource> datasources = new LinkedList<Datasource>();
        for (DsContext childDsContext : children) {
            for (Datasource ds : childDsContext.getAll()) {
                if (ObjectUtils.equals(ds.getDataService(), dataservice))
                    datasources.add(ds);
            }
        }
        for (Datasource ds : datasourceMap.values())
            if (ObjectUtils.equals(ds.getDataService(), dataservice))
                datasources.add(ds);

        for (Datasource datasource : datasources) {
            ((DatasourceImplementation) datasource).committed(committedEntities);
        }
    }

    private void fireBeforeCommit(CommitContext context) {
        for (CommitListener commitListener : commitListeners) {
            commitListener.beforeCommit(context);
        }
    }

    private void fireAfterCommit(CommitContext context, Set<Entity> committedEntities) {
        for (CommitListener commitListener : commitListeners) {
            commitListener.afterCommit(context, committedEntities);
        }
    }

    protected CommitContext createCommitContext(DataService dataservice,
                                                        Map<DataService, Collection<Datasource<Entity>>> commitData)
    {
        CommitContext context = new CommitContext();

        for (Datasource<Entity> datasource : commitData.get(dataservice)) {
            final DatasourceImplementation<Entity> implementation = (DatasourceImplementation) datasource;

            boolean listenersEnabled = implementation.enableListeners(false);
            try {
                for (Entity entity : implementation.getItemsToCreate()) {
                    addToContext(entity, datasource, context.getCommitInstances(), context.getViews());
                }
                for (Entity entity : implementation.getItemsToUpdate()) {
                    addToContext(entity, datasource, context.getCommitInstances(), context.getViews());
                }
                for (Entity entity : implementation.getItemsToDelete()) {
                    addToContext(entity, datasource, context.getRemoveInstances(), context.getViews());
                }
            } finally {
                implementation.enableListeners(listenersEnabled);
            }
        }
        return context;
    }

    private void addToContext(Entity entity, Datasource<Entity> datasource,
                              Collection<Entity> entities, Map<Object, View> views) {
        if (datasource instanceof NestedDatasource)
            replaceMasterCopies(entity, ((NestedDatasource) datasource));

        entities.add(entity);
        views.put(entity, datasource.getView());
    }

    // Replace the reference to master entity with actual entity containing in the master datasource,
    // because in case of nested property datasources there may be references to cloned master entities.
    private void replaceMasterCopies(Entity entity, NestedDatasource datasource) {
        Datasource masterDs = datasource.getMaster();
        MetaProperty metaProperty = datasource.getProperty();
        if (masterDs != null && metaProperty != null) {
            MetaProperty inverseProp = metaProperty.getInverse();
            if (inverseProp != null && inverseProp.getDomain().equals(datasource.getMetaClass())
                    && entity.getValue(inverseProp.getName()) != null) // replace master only if it's already set
            {
                Object masterItem;
                if (masterDs instanceof CollectionDatasource) {
                    Object id = ((Entity) entity.getValue(inverseProp.getName())).getId();
                    masterItem = ((CollectionDatasource) masterDs).getItem(id);
                } else {
                    masterItem = masterDs.getItem();
                }
                ((AbstractInstance) entity).setValue(inverseProp.getName(), masterItem, false);
            }
        }
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

    public void registerDependency(final Datasource datasource, final Datasource dependFrom, final String propertyName) {
        Datasource ds = dependencies.get(datasource);
        if (ds != null)
            if (ds.equals(dependFrom)) return;
            else throw new UnsupportedOperationException("Datasource couldn't depend from two different sources");

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
        for (DsContext childDsContext : children) {
            for (Datasource datasource : childDsContext.getAll()) {
                if (datasource.isModified()) {
                    return true;
                }
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

    public void registerListener(ParameterInfo item, Datasource datasource) {
        // TODO implement ContextListeners
//        if (ParametersHelper.ParameterInfo.Type.PARAM.equals(item.getType())) {
//            Collection<Datasource> collection = contextListeners.get(item.getPath());
//            if (collection == null) {
//                collection = new ArrayList<Datasource>();
//                contextListeners.put(item.getPath(), collection);
//            }
//            collection.add(datasource);
//        } else {
//            throw new UnsupportedOperationException();
//        }
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
