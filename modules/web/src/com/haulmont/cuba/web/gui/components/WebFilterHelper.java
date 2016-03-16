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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.GroupCondition;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.folders.AppFolderEditWindow;
import com.haulmont.cuba.web.app.folders.CubaFoldersPane;
import com.haulmont.cuba.web.app.folders.FolderEditWindow;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 */
@org.springframework.stereotype.Component(FilterHelper.NAME)
public class WebFilterHelper implements FilterHelper {

    @Inject
    protected Metadata metadata;

    @Inject
    protected Messages messages;

    @Inject
    protected Configuration configuration;

    @Override
    public void setLookupNullSelectionAllowed(LookupField lookupField, boolean value) {
        ComboBox vComboBox = (ComboBox) WebComponentsHelper.unwrap(lookupField);
        vComboBox.setNullSelectionAllowed(value);
    }

    @Override
    public void setLookupTextInputAllowed(LookupField lookupField, boolean value) {
        ComboBox vComboBox = (ComboBox) WebComponentsHelper.unwrap(lookupField);
        vComboBox.setTextInputAllowed(value);
    }

    @Override
    @Nullable
    public AbstractSearchFolder saveFolder(AbstractSearchFolder folder) {
        AppWindow appWindow = AppUI.getCurrent().getAppWindow();
        FoldersPane foldersPane = appWindow.getMainWindow().getFoldersPane();
        if (foldersPane == null)
            return null;

        CubaFoldersPane foldersPaneImpl = (CubaFoldersPane) WebComponentsHelper.unwrap(foldersPane);
        AbstractSearchFolder savedFolder = (AbstractSearchFolder) foldersPaneImpl.saveFolder(folder);
        foldersPaneImpl.refreshFolders();
        return savedFolder;
    }

    @Override
    public void openFolderEditWindow(boolean isAppFolder, AbstractSearchFolder folder, Presentations presentations, Runnable commitHandler) {
        FolderEditWindow window = AppFolderEditWindow.create(isAppFolder, false, folder, presentations, commitHandler);
        AppUI.getCurrent().addWindow(window);
    }

    @Override
    public boolean isFolderActionsEnabled() {
        return configuration.getConfig(WebConfig.class).getFoldersPaneEnabled();
    }

    @Override
    public void initConditionsDragAndDrop(final Tree tree, final ConditionsTree conditions) {
        final com.vaadin.ui.Tree vTree = (com.vaadin.ui.Tree) WebComponentsHelper.unwrap(tree);
        vTree.setDragMode(com.vaadin.ui.Tree.TreeDragMode.NODE);
        vTree.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                Transferable t = event.getTransferable();

                if (t.getSourceComponent() != vTree)
                    return;

                com.vaadin.ui.Tree.TreeTargetDetails target = (com.vaadin.ui.Tree.TreeTargetDetails) event.getTargetDetails();

                VerticalDropLocation location = target.getDropLocation();
                Object sourceItemId = t.getData("itemId");
                Object targetItemId = target.getItemIdOver();

                if (targetItemId == null) return;

                CollectionDatasource datasource = tree.getDatasource();

                AbstractCondition sourceCondition = (AbstractCondition) datasource.getItem(sourceItemId);
                AbstractCondition targetCondition = (AbstractCondition) datasource.getItem(targetItemId);

                Node<AbstractCondition> sourceNode = conditions.getNode(sourceCondition);
                Node<AbstractCondition> targetNode = conditions.getNode(targetCondition);

                if (isAncestorOf(targetNode, sourceNode)) return;

                boolean moveToTheSameParent = Objects.equals(sourceNode.getParent(), targetNode.getParent());

                if (location == VerticalDropLocation.MIDDLE) {
                    if (sourceNode.getParent() == null) {
                        conditions.getRootNodes().remove(sourceNode);
                    } else {
                        sourceNode.getParent().getChildren().remove(sourceNode);
                    }
                    targetNode.addChild(sourceNode);
                    refreshConditionsDs();
                    tree.expand(targetCondition.getId());
                } else {
                    List<Node<AbstractCondition>> siblings;
                    if (targetNode.getParent() == null)
                        siblings = conditions.getRootNodes();
                    else
                        siblings = targetNode.getParent().getChildren();

                    int targetIndex = siblings.indexOf(targetNode);
                    if (location == VerticalDropLocation.BOTTOM)
                        targetIndex++;

                    int sourceNodeIndex;
                    if (sourceNode.getParent() == null) {
                        sourceNodeIndex = conditions.getRootNodes().indexOf(sourceNode);
                        conditions.getRootNodes().remove(sourceNode);
                    } else {
                        sourceNodeIndex = sourceNode.getParent().getChildren().indexOf(sourceNode);
                        sourceNode.getParent().getChildren().remove(sourceNode);
                    }

                    //decrease drop position index if dragging from top to bottom inside the same parent node
                    if (moveToTheSameParent && (sourceNodeIndex < targetIndex))
                        targetIndex--;

                    if (targetNode.getParent() == null) {
                        sourceNode.parent = null;
                        conditions.getRootNodes().add(targetIndex, sourceNode);
                    } else {
                        targetNode.getParent().insertChildAt(targetIndex, sourceNode);
                    }

                    refreshConditionsDs();
                }
            }

            protected boolean isAncestorOf(Node childNode, Node possibleParentNode) {
                while (childNode.getParent() != null) {
                    if (childNode.getParent().equals(possibleParentNode)) return true;
                    childNode = childNode.getParent();
                }
                return false;
            }

            protected void refreshConditionsDs() {
                tree.getDatasource().refresh(Collections.<String, Object>singletonMap("conditions", conditions));
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new Or(new AbstractSelect.TargetItemIs(vTree, getGroupConditionIds().toArray()), new Not(AbstractSelect.VerticalLocationIs.MIDDLE));
            }

            protected List<UUID> getGroupConditionIds() {
                List<UUID> groupConditions = new ArrayList<>();
                List<AbstractCondition> list = conditions.toConditionsList();
                for (AbstractCondition condition : list) {
                    if (condition instanceof GroupCondition)
                        groupConditions.add(condition.getId());
                }
                return groupConditions;
            }
        });
    }

    @Override
    public Object getFoldersPane() {
        AppWindow appWindow = AppUI.getCurrent().getAppWindow();
        FoldersPane foldersPane = appWindow.getMainWindow().getFoldersPane();
        if (foldersPane == null) {
            return null;
        }

        return WebComponentsHelper.<CubaFoldersPane>unwrap(foldersPane);
    }

    @Override
    public void removeFolderFromFoldersPane(Folder folder) {
        AppWindow appWindow = AppUI.getCurrent().getAppWindow();
        FoldersPane foldersPane = appWindow.getMainWindow().getFoldersPane();
        if (foldersPane == null) {
            return;
        }

        CubaFoldersPane foldersPaneImpl = (CubaFoldersPane) WebComponentsHelper.unwrap(foldersPane);

        foldersPaneImpl.removeFolder(folder);
        foldersPaneImpl.refreshFolders();
    }

    @Override
    public boolean isTableActionsEnabled() {
        WebConfig config = configuration.getConfig(WebConfig.class);
        return config.getFoldersPaneEnabled();
    }

    @Override
    public void initTableFtsTooltips(Table table, final Map<UUID, String> tooltips) {
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        vTable.setItemDescriptionGenerator(new AbstractSelect.ItemDescriptionGenerator() {
            @SuppressWarnings("SuspiciousMethodCalls")
            @Override
            public String generateDescription(Component source, Object itemId, Object propertyId) {
                if (tooltips.keySet().contains(itemId)) {
                    return tooltips.get(itemId);
                }
                return null;
            }
        });
    }

    @Override
    public void removeTableFtsTooltips(Table table) {
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        vTable.setItemDescriptionGenerator(null);
    }

    @Override
    public void setFieldReadOnlyFocusable(TextField textField, boolean readOnlyFocusable) {
        CubaTextField vTextField = (CubaTextField) WebComponentsHelper.unwrap(textField);
        vTextField.setReadOnlyFocusable(readOnlyFocusable);
    }

    @Override
    public void setComponentFocusable(com.haulmont.cuba.gui.components.Component component, boolean focusable) {
        com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(component);
        if (vComponent instanceof Component.Focusable) {
            ((Component.Focusable) vComponent).setTabIndex(focusable ? 0 : -1);
        }
    }

    @Override
    public void setLookupCaptions(LookupField lookupField, Map<Object, String> captions) {
        ComboBox vLookupField = (ComboBox) WebComponentsHelper.unwrap(lookupField);
        vLookupField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        for (Map.Entry<Object, String> entry : captions.entrySet()) {
            vLookupField.setItemCaption(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addTextChangeListener(TextField textField, final TextChangeListener listener) {
        CubaTextField vTextField = (CubaTextField) WebComponentsHelper.unwrap(textField);
        vTextField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.TIMEOUT);
        vTextField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                listener.textChanged(event.getText());
            }
        });
    }

    @Override
    public void addShortcutListener(TextField textField, final ShortcutListener listener) {
        CubaTextField vTextField = (CubaTextField) WebComponentsHelper.unwrap(textField);
        int[] modifiers = null;
        KeyCombination.Modifier[] listenerModifiers = listener.getKeyCombination().getModifiers();
        if (listenerModifiers != null) {
            modifiers = new int[listenerModifiers.length];
            for (int i = 0; i < modifiers.length; i++) {
                modifiers[i] = listenerModifiers[i].getCode();

            }
        }
        vTextField.addShortcutListener(new com.vaadin.event.ShortcutListener(listener.getCaption(), listener.getKeyCombination().getKey().getCode(), modifiers) {
            @Override
            public void handleAction(Object sender, Object target) {
                listener.handleShortcutPressed();
            }
        });
    }

    @Override
    public void setLookupFieldPageLength(LookupField lookupField, int pageLength) {
        ComboBox vComboBox = (ComboBox) WebComponentsHelper.unwrap(lookupField);
        vComboBox.setPageLength(pageLength);
    }
}
