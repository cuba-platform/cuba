/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTree extends DesktopAbstractActionsHolderComponent<JTree> implements Tree {

    protected String hierarchyProperty;
    protected HierarchicalDatasource<Entity<Object>, Object> datasource;
    protected JScrollPane treeView;
    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;
    protected TreeModelAdapter model;

    public DesktopTree() {
        impl = new JTree();
        treeView = new JScrollPane(impl);

        impl.setRootVisible(false);
        impl.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        impl.setExpandsSelectedPaths(true);

        impl.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        showPopup(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        showPopup(e);
                    }

                    private void showPopup(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            // select row
                            Point p = e.getPoint();
                            TreePath treePath = impl.getPathForLocation(p.x, p.y);
                            if (treePath != null) {
                                TreeSelectionModel model = impl.getSelectionModel();
                                model.setSelectionPath(treePath);
                            }
                            // show popup menu
                            createPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
        );
    }

    @Override
    public JComponent getComposition() {
        return treeView;
    }

    @Override
    public void expandTree() {
        if (model == null) {
            return;
        }

        if (!model.isLeaf(model.getRoot())) {
            recursiveExpand(model.getRoot());
        }
    }

    private void recursiveExpand(Object node) {
        impl.expandPath(model.getTreePath(node));
        for (int i = 0; i < model.getChildCount(node); i++) {
            Object child = model.getChild(node, i);
            if (!model.isLeaf(child)) {
                impl.expandPath(model.getTreePath(child));
                recursiveExpand(child);
            }
        }
    }

    @Override
    public void expand(Object itemId) {
        if (datasource == null) {
            return;
        }
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null) {
            return;
        }

        impl.expandPath(model.getTreePath(item));
    }

    @Override
    public void collapseTree() {
        if (model == null) {
            return;
        }

        impl.collapsePath(new TreePath(model.getRoot()));
    }

    @Override
    public void collapse(Object itemId) {
        if (datasource == null) {
            return;
        }
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null) {
            return;
        }

        impl.collapsePath(model.getTreePath(item));
    }

    @Override
    public boolean isExpanded(Object itemId) {
        if (datasource == null) {
            return false;
        }
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null) {
            return false;
        }

        return impl.isExpanded(model.getTreePath(item));
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        if (model != null) {
            model.setCaptionMode(captionMode);
        }
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (model != null) {
            model.setCaptionProperty(captionProperty);
        }
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        this.datasource = datasource;
        hierarchyProperty = datasource.getHierarchyPropertyName();

        model = new TreeModelAdapter(datasource, captionMode, captionProperty, true);
        impl.setModel(model);

        impl.addTreeSelectionListener(new SelectionListener());

        UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);
        if (uss.getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        datasource.addListener(
                new CollectionDsActionsNotifier(this) {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, CollectionDatasourceListener.Operation operation, List<Entity> items) {
                        // #PL-2035, reload selection from ds
                        Set<Entity> selectedItems = getSelected();
                        if (selectedItems == null) {
                            selectedItems = Collections.emptySet();
                        }

                        Set<Entity> newSelection = new HashSet<>();
                        for (Entity entity : selectedItems) {
                            if (ds.containsItem(entity.getId())) {
                                newSelection.add(entity);
                            }
                        }

                        if (ds.getState() == Datasource.State.VALID && ds.getItem() != null) {
                            newSelection.add(ds.getItem());
                        }

                        if (newSelection.isEmpty()) {
                            setSelected((Entity) null);
                        } else {
                            setSelected(newSelection);
                        }
                    }
                }
        );

        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    @Override
    public boolean isMultiSelect() {
        return impl.getSelectionModel().getSelectionMode() != TreeSelectionModel.SINGLE_TREE_SELECTION;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        int mode = multiselect ?
                TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION : TreeSelectionModel.SINGLE_TREE_SELECTION;
        impl.getSelectionModel().setSelectionMode(mode);
    }

    @Override
    public <T extends Entity> T getSingleSelected() {
        Set selected = getSelected();
        return selected.isEmpty() ? null : (T) selected.iterator().next();
    }

    @Override
    public <T extends Entity> Set<T> getSelected() {
        Set<T> selected = new HashSet<>();
        TreePath[] selectionPaths = impl.getSelectionPaths();
        if (selectionPaths != null) {
            for (TreePath selectionPath : selectionPaths) {
                Entity entity = model.getEntity(selectionPath.getLastPathComponent());
                if (entity != null) {
                    selected.add((T) entity);
                }
            }
        }
        return selected;
    }

    @Override
    public void setSelected(@Nullable Entity item) {
        if (item != null) {
            TreePath path = model.getTreePath(item);
            impl.setSelectionPath(path);
        } else {
            impl.setSelectionPath(null);
        }
    }

    @Override
    public void setSelected(Collection<Entity> items) {
        TreePath[] paths = new TreePath[items.size()];
        int i = 0;
        for (Entity item : items) {
            paths[i] = model.getTreePath(item);
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

    @Override
    public boolean isEditable() {
        return impl.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        impl.setEditable(editable);
    }

    protected class SelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            Set selected = getSelected();
            if (selected.isEmpty()) {
                datasource.setItem(null);
            } else {
                Object item = selected.iterator().next();
                if (item instanceof Entity) {
                    // reset selection and select new item
                    if (isMultiSelect()) {
                        datasource.setItem(null);
                    }
                    datasource.setItem((Entity) item);
                } else {
                    datasource.setItem(null);
                }
            }
        }
    }

    protected JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        for (final com.haulmont.cuba.gui.components.Action action : actionList) {
            if (StringUtils.isNotBlank(action.getCaption())) {
                menuItem = new JMenuItem(action.getCaption());
                if (action.getIcon() != null) {
                    menuItem.setIcon(App.getInstance().getResources().getIcon(action.getIcon()));
                }
                if (action.getShortcut() != null) {
                    menuItem.setAccelerator(DesktopComponentsHelper.convertKeyCombination(action.getShortcut()));
                }
                menuItem.setEnabled(action.isEnabled());
                menuItem.setVisible(action.isVisible());
                menuItem.addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                action.actionPerform(DesktopTree.this);
                            }
                        }
                );
                popup.add(menuItem);
            }
        }
        return popup;
    }
}