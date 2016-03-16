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

package com.haulmont.cuba.desktop.gui.data;

import com.google.common.collect.Iterables;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class TreeModelAdapter implements TreeModel {

    protected HierarchicalDatasource<Entity<Object>, Object> datasource;

    protected Object rootNode = "Root";

    protected List<TreeModelListener> treeModelListeners = new ArrayList<>();

    protected CaptionMode captionMode;
    protected String captionProperty;

    protected boolean autoRefresh;

    protected MetadataTools metadataTools;

    public TreeModelAdapter(HierarchicalDatasource datasource, CaptionMode captionMode, String captionProperty,
                            boolean autoRefresh) {

        this.datasource = datasource;
        this.captionMode = captionMode;
        this.captionProperty = captionProperty;
        this.autoRefresh = autoRefresh;

        this.metadataTools = AppBeans.get(MetadataTools.NAME);

        //noinspection unchecked
        datasource.addCollectionChangeListener(new CollectionDatasource.CollectionChangeListener() {
            @Override
            public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
                switch (e.getOperation()) {
                    case CLEAR:
                    case REFRESH:
                    case ADD:
                    case REMOVE:
                        Object[] path = {getRoot()};
                        for (TreeModelListener listener : treeModelListeners) {
                            TreeModelEvent ev = new TreeModelEvent(this, path);
                            listener.treeStructureChanged(ev);
                        }
                        break;

                    case UPDATE:
                        for (Object item : e.getItems()) {
                            TreePath treePath = getTreePath(item);
                            for (TreeModelListener listener : treeModelListeners) {
                                TreeModelEvent ev = new TreeModelEvent(this, treePath.getPath());
                                listener.treeNodesChanged(ev);
                            }
                        }
                        break;
                }
            }
        });
    }

    @Override
    public Object getRoot() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return rootNode;
    }

    @Override
    public Object getChild(Object parent, int index) {
        Collection<Object> childrenIds;
        if (parent == rootNode) {
            childrenIds = datasource.getRootItemIds();
        } else {
            childrenIds = datasource.getChildren(((Node) parent).getEntity().getId());
        }
        Object id = Iterables.get(childrenIds, index);
        return new Node(parent, datasource.getItem(id));
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == rootNode) {
            return datasource.getRootItemIds().size();
        } else {
            Entity entity = ((Node) parent).getEntity();
            Collection<Object> childrenIds = datasource.getChildren(entity.getId());
            return childrenIds.size();
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node == rootNode) {
            return false;
        } else {
            Entity entity = ((Node) node).getEntity();
            Collection<Object> childrenIds = datasource.getChildren(entity.getId());
            return childrenIds.size() == 0;
        }
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null)
            return -1;

        Collection<Object> childrenIds;
        if (parent == rootNode) {
            childrenIds = datasource.getRootItemIds();
        } else {
            Entity entity = ((Node) parent).getEntity();
            childrenIds = datasource.getChildren(entity.getId());
        }
        final Entity childEntity = ((Node) child).getEntity();
        return Iterables.indexOf(childrenIds, id -> childEntity.getId().equals(id));
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if (!treeModelListeners.contains(l))
            treeModelListeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    public Node createNode(Entity entity) {
        return new Node(entity);
    }

    public Entity getEntity(Object object) {
        if (object instanceof Entity) {
            return (Entity) object;
        } else if (object instanceof Node) {
            return ((Node) object).getEntity();
        } else
            return null;
    }

    public TreePath getTreePath(Object object) {
        List<Object> list = new ArrayList<>();
        if (object instanceof Entity) {
            Node node = createNode((Entity) object);
            list.add(node);
            if (datasource.getHierarchyPropertyName() != null) {
                Entity entity = (Entity) object;
                while (entity.getValue(datasource.getHierarchyPropertyName()) != null) {
                    entity = entity.getValue(datasource.getHierarchyPropertyName());
                    // noinspection ConstantConditions
                    if (!datasource.containsItem(entity.getId())) {
                        break; // Child entities with removed parent are happen to be thrown to tree root.
                    }
                    Node parentNode = createNode(entity);
                    list.add(0, parentNode);
                    node.setParent(parentNode);
                    node = parentNode;
                }
            } else {
                List<Object> treePath = getTreePath(getRoot(), (Entity) object);
                if (treePath != null) {
                    treePath.add(0, rootNode);
                    return new TreePath(treePath.toArray());
                }
            }
            list.add(0, rootNode);
            node.setParent(rootNode);
        } else if (object instanceof Node) {
            list.add(object);
            Node n = (Node) object;
            while (n.getParent() != null) {
                Object parent = n.getParent();
                list.add(0, parent);
                if (!(parent instanceof Node))
                    break;
                else
                    n = (Node) parent;
            }
        } else {
            list.add(object);
        }
        return new TreePath(list.toArray(new Object[list.size()]));
    }

    public List<Object> getTreePath(Object node, Entity entity) {
        for (int i = 0; i < getChildCount(node); i++) {
            Node child = (Node) getChild(node, i);
            if (ObjectUtils.equals(entity, child.entity)) {
                List<Object> list = new LinkedList<>();
                list.add(createNode(entity));
                return list;
            } else {
                List<Object> path = getTreePath(child, entity);
                if (path != null) {
                    path.add(0, child);
                    return path;
                }
            }
        }
        return null;
    }

    public class Node {
        private Entity entity;
        private Object parent;

        public Node(Entity entity) {
            this(null, entity);
        }

        public Node(Object parent, Entity entity) {
            if (entity == null)
                throw new IllegalArgumentException("item must not be null");
            this.parent = parent;
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        public Object getParent() {
            return parent;
        }

        public void setParent(Object parent) {
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return entity.equals(node.entity);

        }

        @Override
        public int hashCode() {
            return entity.hashCode();
        }

        @Override
        public String toString() {
            String value;
            if (captionMode.equals(CaptionMode.ITEM)) {
                value = entity.getInstanceName();
            } else {
                Object propertyValue = entity.getValue(captionProperty);
                MetaProperty property = entity.getMetaClass().getProperty(captionProperty);
                return metadataTools.format(propertyValue, property);
            }
            return value;
        }
    }
}
