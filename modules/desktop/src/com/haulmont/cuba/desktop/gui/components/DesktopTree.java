/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTree
    extends DesktopAbstractActionOwnerComponent<JTree>
    implements Tree
{
    private String hierarchyProperty;
    private HierarchicalDatasource<Entity<Object>, Object> datasource;
    private JScrollPane treeView;
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;
    private TreeModelAdapter model;

    public DesktopTree() {
        impl = new JTree();
        treeView = new JScrollPane(impl);

        impl.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        impl.setExpandsSelectedPaths(true);
    }

    @Override
    public JComponent getComposition() {
        return treeView;
    }

    @Override
    public void expandTree() {
        if (model == null)
            return;

        impl.expandPath(new TreePath(model.getRoot()));
    }

    @Override
    public void expand(Object itemId) {
        if (datasource == null)
            return;
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null)
            return;

        impl.expandPath(getTreePath(item));
    }

    @Override
    public void collapseTree() {
        if (model == null)
            return;

        impl.collapsePath(new TreePath(model.getRoot()));
    }

    @Override
    public void collapse(Object itemId) {
        if (datasource == null)
            return;
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null)
            return;

        impl.collapsePath(getTreePath(item));
    }

    @Override
    public boolean isExpanded(Object itemId) {
        if (datasource == null)
            return false;
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null)
            return false;

        return impl.isExpanded(getTreePath(item));
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        if (model != null)
            model.setCaptionMode(captionMode);
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (model != null)
            model.setCaptionProperty(captionProperty);
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        this.datasource = datasource;
        hierarchyProperty = datasource.getHierarchyPropertyName();

        if (!datasource.getState().equals(Datasource.State.VALID)) {
            datasource.refresh();
        }
        model = new TreeModelAdapter(datasource, captionMode, captionProperty);
        impl.setModel(model);

        impl.addTreeSelectionListener(new SelectionListener());
    }

    @Override
    public boolean isMultiSelect() {
        return impl.getSelectionModel().getSelectionMode() != TreeSelectionModel.SINGLE_TREE_SELECTION;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        impl.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }

    @Override
    public <T extends Entity> T getSingleSelected() {
        TreePath[] selectionPaths = impl.getSelectionPaths();
        if (selectionPaths != null && selectionPaths.length > 0) {
            Object sel = selectionPaths[0].getLastPathComponent();
            if (sel instanceof TreeModelAdapter.Node)
                return (T) ((TreeModelAdapter.Node) sel).getEntity();
        }
        return null;
    }

    @Override
    public Set getSelected() {
        Set<Object> selected = new HashSet<Object>();
        TreePath[] selectionPaths = impl.getSelectionPaths();
        if (selectionPaths != null) {
            for (TreePath selectionPath : selectionPaths) {
                Object sel = selectionPath.getLastPathComponent();
                if (sel instanceof TreeModelAdapter.Node)
                    selected.add(((TreeModelAdapter.Node) sel).getEntity());
            }
        }
        return selected;
    }

    private TreePath getTreePath(Entity item) {
        List<Object> list = new ArrayList<Object>();
        list.add(model.createNode(item));
        Instance instance = (Instance) item;
        while (instance.getValue(hierarchyProperty) != null) {
            instance = instance.getValue(hierarchyProperty);
            list.add(0, model.createNode((Entity) instance));
        }
        return new TreePath(list.toArray(new Object[list.size()]));
    }

    @Override
    public void setSelected(Entity item) {
        TreePath path = getTreePath(item);
        impl.setSelectionPath(path);
    }

    @Override
    public void setSelected(Collection<Entity> items) {
        TreePath[] paths = new TreePath[items.size()];
        int i = 0;
        for (Entity item : items) {
            paths[i] = getTreePath(item);
        }
        impl.setSelectionPaths(paths);
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void refresh() {
        datasource.refresh();
    }

    private class SelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            Set selected = getSelected();
            if (selected.isEmpty()) {
                datasource.setItem(null);
            } else {
                Object item = selected.iterator().next();
                if (item instanceof Entity) {
                    datasource.setItem((Entity) item);
                } else {
                    datasource.setItem(null);
                }
            }
        }
    }
}
