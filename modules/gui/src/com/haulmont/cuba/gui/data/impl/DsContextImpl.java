/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParameterInfo;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class DsContextImpl implements DsContextImplementation {

    private FrameContext windowContext;
    private DataSupplier dataservice;

    private DsContext parent;
    private List<DsContext> children = new ArrayList<>();

    private Map<String, Datasource> datasourceMap = new HashMap<>();

    protected Map<Datasource, Datasource> dependencies = new HashMap<>();

    protected Metadata metadata;

    // TODO implement ContextListeners
//    private Map<String, Collection<Datasource>> contextListeners =
//            new HashMap<String, Collection<Datasource>>();

    protected List<LazyTask> lazyTasks = new ArrayList<>();

    private Set<CommitListener> commitListeners = new LinkedHashSet<>();

    public DsContextImpl(DataSupplier dataservice) {
        this.dataservice = dataservice;
        this.metadata = AppBeans.get(Metadata.NAME);
    }

    @Override
    public void addLazyTask(LazyTask lazyTask) {
        if (!lazyTasks.contains(lazyTask)) lazyTasks.add(lazyTask);
    }

    @Override
    public void executeLazyTasks() {
        for (LazyTask lazyTask : lazyTasks) {
            lazyTask.execute(this);
        }
    }

    @Override
    public void resumeSuspended() {
        LinkedList<CollectionDatasource.Suspendable> list = new LinkedList<>();

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

    @Override
    public FrameContext getFrameContext() {
        return windowContext;
    }

    @Override
    public void setFrameContext(FrameContext windowContext) {
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

    @Override
    public boolean commit() {
        Map<DataSupplier, Collection<Datasource<Entity>>> commitData = collectCommitData();

        boolean committed = false;

        if (!commitData.isEmpty()) {
            DataSupplier dataservice = getDataSupplier();
            Set<DataSupplier> suppliers = commitData.keySet();

            if (suppliers.size() == 1 &&
                    ObjectUtils.equals(suppliers.iterator().next(), dataservice))
            {
                CommitContext context = createCommitContext(dataservice, commitData);

                fireBeforeCommit(context);

                Set<Entity> committedEntities = dataservice.commit(context);

                fireAfterCommit(context, committedEntities);

                notifyAllDsCommited(dataservice, committedEntities);

                committed = true;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        for (DsContext childDsContext : children) {
            boolean c = commitToParent(childDsContext.getAll());
            committed = committed || c;
        }
        boolean c = commitToParent(datasourceMap.values());
        committed = committed || c;

        return committed;
    }

    private boolean commitToParent(Collection<Datasource> datasources) {
        List<Datasource> list = new ArrayList<>();
        for (Datasource datasource : datasources) {
            if (Datasource.CommitMode.PARENT.equals(datasource.getCommitMode())
                    && (datasource.isModified()  || !((DatasourceImplementation) datasource).getItemsToCreate().isEmpty())) {
                list.add(datasource);
            }
        }

        List<Datasource> sortedList = new DsTree(list).toDsList();
        for (Datasource datasource : sortedList) {
            datasource.commit();
        }
        return !list.isEmpty();
    }

    private void notifyAllDsCommited(DataSupplier dataservice, Set<Entity> committedEntities) {
        // Notify all datasources in context
        List<Datasource> datasources = new LinkedList<>();
        for (DsContext childDsContext : children) {
            for (Datasource ds : childDsContext.getAll()) {
                if (ObjectUtils.equals(ds.getDataSupplier(), dataservice)
                        && ds.getCommitMode() == Datasource.CommitMode.DATASTORE)
                    datasources.add(ds);
            }
        }
        for (Datasource ds : datasourceMap.values())
            if (ObjectUtils.equals(ds.getDataSupplier(), dataservice)
                    && ds.getCommitMode() == Datasource.CommitMode.DATASTORE)
                datasources.add(ds);

        List<Datasource> sortedList = new DsTree(datasources).toDsList();
        for (Datasource datasource : sortedList) {
            ((DatasourceImplementation) datasource).committed(committedEntities);
        }
    }

    public void fireBeforeCommit(CommitContext context) {
        for (CommitListener commitListener : commitListeners) {
            commitListener.beforeCommit(context);
        }
        for (DsContext childDsContext : children) {
            ((DsContextImplementation) childDsContext).fireBeforeCommit(context);
        }
    }

    public void fireAfterCommit(CommitContext context, Set<Entity> committedEntities) {
        for (DsContext childDsContext : children) {
            ((DsContextImplementation) childDsContext).fireAfterCommit(context, committedEntities);
        }
        for (CommitListener commitListener : commitListeners) {
            commitListener.afterCommit(context, committedEntities);
        }
    }

    protected CommitContext createCommitContext(DataSupplier dataservice,
                                                Map<DataSupplier, Collection<Datasource<Entity>>> commitData) {
        CommitContext context = new CommitContext();

        for (Datasource<Entity> datasource : commitData.get(dataservice)) {
            if (datasource instanceof DatasourceImplementation) {
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
        }
        repairReferences(context);

        return context;
    }


    protected void repairReferences(CommitContext context) {
        for (Entity entity : context.getCommitInstances()) {
            for (Entity otherEntity : context.getCommitInstances()) {
                if (!entity.equals(otherEntity)) {
                    metadata.getTools().traverseAttributes(otherEntity, new ReferenceRepairingAttributeVisitor(entity));
                }
            }
        }
    }

    private void addToContext(Entity entity, Datasource<Entity> datasource,
                              Collection<Entity> entities, Map<Object, View> views) {
        if (datasource instanceof NestedDatasource)
            replaceMasterCopies(entity, ((NestedDatasource) datasource));

        entities.add(entity);
        views.put(entity, datasource.getView());

        if (datasource instanceof RuntimePropsDatasource) {
            views.put(entity, ((RuntimePropsDatasource) datasource).getAttributeValueView());
        }
    }

    // Replace the reference to master entity with actual entity containing in the master datasource,
    // because in case of nested property datasources there may be references to cloned master entities.
    private void replaceMasterCopies(Entity entity, NestedDatasource datasource) {
        Datasource masterDs = datasource.getMaster();
        MetaProperty metaProperty = datasource.getProperty();
        if (masterDs != null && metaProperty != null) {
            MetaProperty inverseProp = metaProperty.getInverse();
            if (inverseProp != null) {
                MetaClass metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(inverseProp.getDomain());
                if (metaClass.equals(datasource.getMetaClass())
                        && (PersistenceHelper.isLoaded(entity, inverseProp.getName())
                            && entity.getValue(inverseProp.getName()) != null)) // replace master only if it's already set
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
    }

    protected Map<DataSupplier, Collection<Datasource<Entity>>> collectCommitData() {
        Collection<Datasource> datasources = new ArrayList<>();

        for (DsContext childDsContext : children) {
            datasources.addAll(childDsContext.getAll());
        }
        datasources.addAll(datasourceMap.values());

        final Map<DataSupplier,Collection<Datasource<Entity>>> commitDatasources = new HashMap<>();

        for (Datasource datasource : datasources) {
            if (Datasource.CommitMode.DATASTORE == datasource.getCommitMode() &&
                datasource.isAllowCommit() &&
                (datasource.isModified() || !((DatasourceImplementation) datasource).getItemsToCreate().isEmpty())) {

                final DataSupplier dataservice = datasource.getDataSupplier();
                Collection<Datasource<Entity>> collection = commitDatasources.get(dataservice);
                if (collection == null) {
                    collection = new ArrayList<>();
                    commitDatasources.put(dataservice, collection);
                }
                collection.add(datasource);
            }
        }
        return commitDatasources;
    }

    @Override
    public void registerDependency(final Datasource datasource, final Datasource dependFrom, final String propertyName) {
        Datasource ds = dependencies.get(datasource);
        if (ds != null) {
            if (ds.equals(dependFrom)) {
                return;
            } else {
                throw new UnsupportedOperationException("Datasource couldn't depend from two different sources");
            }
        }

        //noinspection unchecked
        dependFrom.addItemChangeListener(e -> {
            if (datasource.getState() == Datasource.State.VALID) {
                datasource.refresh();
            }
        });

        if (dependFrom instanceof CollectionDatasource) {
            //noinspection unchecked
            ((CollectionDatasource) dependFrom).addCollectionChangeListener(e -> {
                if (e.getOperation() == CollectionDatasource.Operation.REFRESH) {
                    datasource.refresh();
                }
            });
        }

        //noinspection unchecked
        dependFrom.addItemPropertyChangeListener(e -> {
            if (propertyName != null) {
                // parameter can use a property path with more than one element, e.g. :ds$driversDs.status.id,
                // but we should listen the first level property
                String listeningProperty = propertyName.split("\\.")[0];
                if (listeningProperty.equals(e.getProperty())) {
                    final Entity item = Datasource.State.VALID.equals(dependFrom.getState()) ? dependFrom.getItem() : null;
                    if (ObjectUtils.equals(item, e.getItem())) {
                        datasource.refresh();
                    }
                }
            }
        });

        dependencies.put(datasource, dependFrom);
    }

    @Override
    public void addListener(CommitListener listener) {
        commitListeners.add(listener);
    }

    @Override
    public void removeListener(CommitListener listener) {
        commitListeners.remove(listener);
    }

    @Override
    public DataSupplier getDataSupplier() {
        return dataservice;
    }

    @Override
    public Datasource get(String id) {
        Preconditions.checkNotNullArgument(id, "Null datasource ID");
        Datasource ds = null;
        if (!id.contains(".")) {
            ds = datasourceMap.get(id);
            if (ds == null && parent != null) {
                ds = parent.get(id);
            }
        } else {
            if (windowContext != null) {
                String nestedFramePath = id.substring(0, id.indexOf("."));
                Component nestedFrame = getFrameContext().getFrame().getComponent(nestedFramePath);
                if ((nestedFrame) != null && (nestedFrame instanceof Frame)) {
                    String nestedDsId = id.substring(id.indexOf(".") + 1);
                    ds = ((Frame) nestedFrame).getDsContext().get(nestedDsId);
                }
            }
        }
        return ds;
    }

    @Override
    public Datasource getNN(String name) {
        Datasource datasource = get(name);
        if (datasource == null)
            throw new IllegalArgumentException("Datasource '" + name + "' is not found");
        return datasource;
    }

    @Override
    public Collection<Datasource> getAll() {
        return datasourceMap.values();
    }

    @Override
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

    @Override
    public void refresh() {
        final Collection<Datasource> datasources = datasourceMap.values();
        for (Datasource datasource : datasources) {
            if (dependencies.containsKey(datasource)) continue;
            datasource.refresh();
        }
    }

    @Override
    public void register(Datasource datasource) {
        datasourceMap.put(datasource.getId(), datasource);
    }

    @Override
    public void unregister(Datasource datasource) {
        datasourceMap.remove(datasource.getId());
    }

    @Override
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

    @Override
    public DsContext getParent() {
        return parent;
    }

    @Override
    public void setParent(DsContext parent) {
        this.parent = parent;
        if (!parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
    }

    @Override
    public List<DsContext> getChildren() {
        return children;
    }

    protected static class ReferenceRepairingAttributeVisitor implements EntityAttributeVisitor {
        protected Entity contextEntity;

        public ReferenceRepairingAttributeVisitor(Entity contextEntity) {
            this.contextEntity = contextEntity;
        }

        @Override
        public void visit(Entity entity, MetaProperty property) {
            if (!property.getRange().isClass() || !property.getRange().asClass().equals(contextEntity.getMetaClass()))
                return;

            if (PersistenceHelper.isLoaded(entity, property.getName())) {
                Object value = entity.getValue(property.getName());
                if (value != null) {
                    if (property.getRange().getCardinality().isMany()) {
                        Collection<Object> collection = (Collection<Object>) value;
                        for (Object item : new ArrayList<>(collection)) {
                            if (contextEntity.equals(item) && contextEntity != item) {
                                if (collection instanceof List) {
                                    List<Object> list = (List<Object>) collection;
                                    list.set(list.indexOf(item), contextEntity);
                                } else {
                                    collection.remove(item);
                                    collection.add(contextEntity);
                                }
                            }
                        }
                    } else {
                        if (contextEntity.equals(value) && contextEntity != value) {
                            entity.setValue(property.getName(), contextEntity);
                        }
                    }
                }
            }
        }
    }
}