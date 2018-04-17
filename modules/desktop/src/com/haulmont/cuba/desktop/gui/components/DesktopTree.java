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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.desktop.gui.icons.IconResolver;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class DesktopTree<E extends Entity> extends DesktopAbstractActionsHolderComponent<JTree>
        implements Tree<E>, LookupComponent.LookupSelectionChangeNotifier {

    protected String hierarchyProperty;
    protected HierarchicalDatasource<Entity<Object>, Object> datasource;
    protected JScrollPane treeView;
    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;
    protected TreeModelAdapter model;

    protected ButtonsPanel buttonsPanel;
    protected MigLayout layout;
    protected JPanel panel;
    protected JPanel topPanel;

    protected Action doubleClickAction;
    protected MouseAdapter itemClickListener;
    protected Action enterPressAction;
    protected boolean editable = true;

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;

    protected CollectionDsActionsNotifier collectionDsActionsNotifier;

    protected List<LookupSelectionChangeListener> lookupSelectionChangeListeners = new ArrayList<>();

    public DesktopTree() {
        layout = new MigLayout("flowy, fill, insets 0", "", "[min!][fill]");
        panel = new JPanel(layout);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setVisible(false);
        panel.add(topPanel, "growx");

        impl = new JTree();
        treeView = new JScrollPane(impl);
        panel.add(treeView, "grow");

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

        impl.addKeyListener(new KeyAdapter() {
            protected static final int ENTER_CODE = 10;

            @Override
            public void keyPressed(KeyEvent e) {
                if (ENTER_CODE == e.getKeyCode() &&
                        e.getComponent() == DesktopTree.this.getComponent()) {
                    if (enterPressAction != null) {
                        enterPressAction.actionPerform(DesktopTree.this);
                    } else {
                        handleClickAction();
                    }
                }
            }
        });
    }

    protected void handleClickAction() {
        Action action = getItemClickAction();
        if (action == null) {
            action = getEnterPressAction();
            if (action == null) {
                action = getAction("edit");
                if (action == null) {
                    action = getAction("view");
                }
            }
        }

        if (action != null && action.isEnabled()) {
            Window window = ComponentsHelper.getWindowImplementation(DesktopTree.this);
            if (window instanceof Window.Wrapper) {
                window = ((Window.Wrapper) window).getWrappedWindow();
            }

            if (!(window instanceof Window.Lookup)) {
                action.actionPerform(DesktopTree.this);
            } else {
                Window.Lookup lookup = (Window.Lookup) window;

                com.haulmont.cuba.gui.components.Component lookupComponent = lookup.getLookupComponent();
                if (lookupComponent != this)
                    action.actionPerform(DesktopTree.this);
                else if (action.getId().equals(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID)) {
                    action.actionPerform(DesktopTree.this);
                }
            }
        }
    }

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        impl.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int rowForLocation = impl.getRowForLocation(e.getX(), e.getY());
                    TreePath pathForLocation = impl.getPathForRow(rowForLocation);
                    if (pathForLocation != null) {
                        CollectionDatasource treeCds = getDatasource();
                        if (treeCds != null) {
                            TreeModelAdapter.Node treeItem =
                                    (TreeModelAdapter.Node) pathForLocation.getLastPathComponent();
                            if (treeItem != null) {
                                treeCds.setItem(treeItem.getEntity());
                                selectHandler.run();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public Collection getLookupSelectedItems() {
        return getSelected();
    }

    @Override
    public JComponent getComposition() {
        return panel;
    }

    @Override
    protected void updateEnabled() {
        super.updateEnabled();

        impl.setEnabled(isEnabledWithParent());
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

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Override
    public void setButtonsPanel(ButtonsPanel panel) {
        if (buttonsPanel != null && topPanel != null) {
            topPanel.remove(DesktopComponentsHelper.unwrap(buttonsPanel));
            buttonsPanel.setParent(null);
        }
        buttonsPanel = panel;
        if (panel != null) {
            if (panel.getParent() != null && panel.getParent() != this) {
                throw new IllegalStateException("Component already has parent");
            }

            topPanel.add(DesktopComponentsHelper.unwrap(panel), BorderLayout.LINE_START);
            topPanel.setVisible(true);
            panel.setParent(this);
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
    public void expandUpTo(int level) {
        if (getDatasource() == null) {
            return;
        }

        HierarchicalDatasource ds = getDatasource();
        java.util.List<Object> currentLevelItemIds = new ArrayList<>(ds.getRootItemIds());
        int i = 0;
        while (i < level && !currentLevelItemIds.isEmpty()) {
            for (Object itemId : new ArrayList<>(currentLevelItemIds)) {
                Entity<Object> item = datasource.getItem(itemId);
                impl.expandPath(model.getTreePath(item));

                currentLevelItemIds.remove(itemId);
                currentLevelItemIds.addAll(ds.getChildren(itemId));
            }
            i++;
        }
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
        if (this.captionMode != captionMode) {
            this.captionMode = captionMode;
            if (model != null) {
                model.setCaptionMode(captionMode);
            }
        }
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        if (!Objects.equals(this.captionProperty, captionProperty)) {
            this.captionProperty = captionProperty;
            if (model != null) {
                model.setCaptionProperty(captionProperty);

                if (captionProperty != null) {
                    setCaptionMode(CaptionMode.PROPERTY);
                } else {
                    setCaptionMode(CaptionMode.ITEM);
                }
            }
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

        collectionChangeListener = e -> {
            // #PL-2035, reload selection from ds
            Set<E> selectedItems = getSelected();
            if (selectedItems == null) {
                selectedItems = Collections.emptySet();
            }

            Set<E> newSelection = new HashSet<>();
            for (E entity : selectedItems) {
                if (e.getDs().containsItem(entity.getId())) {
                    newSelection.add(entity);
                }
            }

            if (e.getDs().getState() == Datasource.State.VALID && e.getDs().getItem() != null) {
                //noinspection unchecked
                newSelection.add((E) e.getDs().getItem());
            }

            if (newSelection.isEmpty()) {
                setSelected((Entity) null);
            } else {
                setSelected(newSelection);
            }
        };
        //noinspection unchecked
        datasource.addCollectionChangeListener(new WeakCollectionChangeListener(datasource, collectionChangeListener));

        collectionDsActionsNotifier = new CollectionDsActionsNotifier(this);
        collectionDsActionsNotifier.bind(datasource);

        for (Action action : getActions()) {
            action.refreshState();
        }

        assignAutoDebugId();
    }

    @Override
    public void setItemClickAction(Action action) {
        if (this.doubleClickAction != action) {
            if (action != null) {
                if (itemClickListener == null) {
                    itemClickListener = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (isEditable()) {
                                if (e.getButton() == MouseEvent.BUTTON1
                                        && e.getClickCount() == 2
                                        && doubleClickAction != null) {

                                    int rowForLocation = impl.getRowForLocation(e.getX(), e.getY());
                                    TreePath pathForLocation = impl.getPathForRow(rowForLocation);

                                    if (pathForLocation != null) {
                                        impl.setSelectionPath(pathForLocation);

                                        doubleClickAction.actionPerform(DesktopTree.this);
                                    }
                                }
                            }
                        }
                    };
                    impl.addMouseListener(itemClickListener);
                    impl.setToggleClickCount(0);
                }
            } else {
                impl.removeMouseListener(itemClickListener);
                impl.setToggleClickCount(2);
                itemClickListener = null;
            }
            this.doubleClickAction = action;
        }
    }

    @Override
    public Action getItemClickAction() {
        return doubleClickAction;
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return getClass().getSimpleName()  + "_" + datasource.getId();
        }

        return getClass().getSimpleName();
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
    public  E getSingleSelected() {
        Set<E> selected = getSelected();
        return selected.isEmpty() ? null : selected.iterator().next();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<E> getSelected() {
        Set<E> selected = new HashSet<>();
        TreePath[] selectionPaths = impl.getSelectionPaths();
        if (selectionPaths != null) {
            for (TreePath selectionPath : selectionPaths) {
                Entity entity = model.getEntity(selectionPath.getLastPathComponent());
                if (entity != null) {
                    selected.add((E) entity);
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
    public void setSelected(Collection<E> items) {
        TreePath[] paths = new TreePath[items.size()];
        int i = 0;
        for (Entity item : items) {
            paths[i] = model.getTreePath(item);
        }
        impl.setSelectionPaths(paths);
    }

    @Override
    public HierarchicalDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void refresh() {
        datasource.refresh();
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
    }


    @Override
    protected void attachAction(Action action) {
        if (action instanceof Action.HasTarget) {
            ((Action.HasTarget) action).setTarget(this);
        }

        super.attachAction(action);
    }

    @Override
    public void addLookupValueChangeListener(LookupSelectionChangeListener listener) {
        if (!lookupSelectionChangeListeners.contains(listener)) {
            lookupSelectionChangeListeners.add(listener);
        }
    }

    @Override
    public void removeLookupValueChangeListener(LookupSelectionChangeListener listener) {
        lookupSelectionChangeListeners.remove(listener);
    }

    protected class SelectionListener implements TreeSelectionListener {

        @SuppressWarnings("unchecked")
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            Set<E> selected = getSelected();
            if (selected.isEmpty()) {
                Entity dsItem = datasource.getItemIfValid();
                datasource.setItem(null);

                if (dsItem == null) {
                    // in this case item change event will not be generated
                    refreshActionsState();
                }
            } else {
                Object item = selected.iterator().next();
                if (item != null) {
                    // reset selection and select new item
                    if (isMultiSelect()) {
                        datasource.setItem(null);
                    }
                    Entity newItem = selected.iterator().next();
                    Entity dsItem = datasource.getItemIfValid();
                    datasource.setItem(newItem);

                    if (Objects.equals(dsItem, newItem)) {
                        // in this case item change event will not be generated
                        refreshActionsState();
                    }
                } else {
                    // todo remove this if branch, should not happen
                    Entity dsItem = datasource.getItemIfValid();
                    datasource.setItem(null);

                    if (dsItem == null) {
                        // in this case item change event will not be generated
                        refreshActionsState();
                    }
                }
            }

            LookupSelectionChangeEvent selectionChangeEvent = new LookupSelectionChangeEvent(DesktopTree.this);
            for (LookupSelectionChangeListener listener : lookupSelectionChangeListeners) {
                listener.lookupValueChanged(selectionChangeEvent);
            }
        }
    }

    protected void refreshActionsState() {
        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    protected JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        for (final com.haulmont.cuba.gui.components.Action action : actionList) {
            if (StringUtils.isNotBlank(action.getCaption())
                    && action.isVisible()) {
                menuItem = new JMenuItem(action.getCaption());
                if (action.getIcon() != null) {
                    menuItem.setIcon(AppBeans.get(IconResolver.class).getIconResource(action.getIcon()));
                }
                if (action.getShortcutCombination() != null) {
                    menuItem.setAccelerator(DesktopComponentsHelper.convertKeyCombination(action.getShortcutCombination()));
                }
                menuItem.setEnabled(action.isEnabled());
                menuItem.addActionListener(e -> action.actionPerform(DesktopTree.this));
                popup.add(menuItem);
            }
        }
        return popup;
    }

    @Override
    public void setStyleProvider(@Nullable StyleProvider<? super E> styleProvider) {
        // do nothing
    }

    @Override
    public void addStyleProvider(StyleProvider<? super E> styleProvider) {
        // do nothing
    }

    @Override
    public void removeStyleProvider(StyleProvider<? super E> styleProvider) {
        // do nothing
    }

    @Override
    public void setIconProvider(IconProvider<? super E> iconProvider) {
        // do nothing
    }

    @Override
    public void repaint() {
        // do nothing
    }

    @Override
    public void setEnterPressAction(Action action) {
        enterPressAction = action;
    }

    @Override
    public Action getEnterPressAction() {
        return enterPressAction;
    }
}