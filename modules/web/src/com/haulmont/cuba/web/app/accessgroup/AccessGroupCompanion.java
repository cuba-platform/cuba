/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.app.accessgroup;

import com.haulmont.cuba.gui.app.security.group.browse.GroupBrowser;
import com.haulmont.cuba.gui.app.security.group.browse.GroupChangeEvent;
import com.haulmont.cuba.gui.app.security.group.browse.UserGroupChangedEvent;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.widgets.CubaTable;
import com.haulmont.cuba.web.widgets.CubaTableDragSourceExtension;
import com.haulmont.cuba.web.widgets.CubaTree;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.grid.DropLocation;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AccessGroupCompanion implements GroupBrowser.Companion {

    // cross browser data type
    protected final static String TRANSFER_DATA_TYPE = "text";
    protected final static String TEXT_PLAIN_DATA_TYPE = "text/plain";

    @Override
    public void initDragAndDrop(Table<User> usersTable, Tree<Group> groupsTree,
                                Consumer<UserGroupChangedEvent> userGroupChangedHandler,
                                Consumer<GroupChangeEvent> groupChangeEventHandler) {
        CubaTable vTable = usersTable.unwrap(CubaTable.class);
        new CubaTableDragSourceExtension<>(vTable);

        //noinspection unchecked
        CubaTree<Group> vTree = groupsTree.unwrap(CubaTree.class);

        // tree as drag source
        TreeGridDragSource<Group> treeGridDragSource = new TreeGridDragSource<>(vTree.getCompositionRoot());
        treeGridDragSource.setDragDataGenerator(TRANSFER_DATA_TYPE, group -> group.getId().toString());

        // tree as drop target
        TreeGridDropTarget<Group> treeGridDropTarget = new TreeGridDropTarget<>(vTree.getCompositionRoot(), DropMode.ON_TOP);
        treeGridDropTarget.addTreeGridDropListener(event -> {
            // if we drop users from table
            if (event.getDragSourceExtension().isPresent() &&
                    event.getDragSourceExtension().get() instanceof CubaTableDragSourceExtension) {
                // return if we drop user between rows
                if (event.getDropLocation() == DropLocation.BELOW) {
                    return;
                }

                //noinspection unchecked
                CubaTableDragSourceExtension<CubaTable> sourceExtension =
                        (CubaTableDragSourceExtension<CubaTable>) event.getDragSourceExtension().get();

                List<Object> itemIds = sourceExtension.getLastDraggedItemIds();
                TableItems<User> tableItems = usersTable.getItems();

                List<User> users = new ArrayList<>();
                for (Object id : itemIds) {
                    users.add(tableItems.getItem(id));
                }

                if (event.getDropTargetRow().isPresent()) {
                    Group group = event.getDropTargetRow().get();
                    userGroupChangedHandler.accept(new UserGroupChangedEvent(groupsTree, users, group));
                }
                // if we reorder groups inside tree
            } else {
                String draggedItemId = event.getDataTransferData().get(TEXT_PLAIN_DATA_TYPE);

                if (isEdgeOrIE() && draggedItemId == null) {
                    draggedItemId = event.getDataTransferText();
                }

                if (draggedItemId == null) {
                    return;
                }

                String[] draggedItemIds = draggedItemId.split("\\r?\\n");

                for (String itemId : draggedItemIds) {

                    Group draggedGroup = groupsTree.getItems().getItem(UUID.fromString(itemId));

                    if (event.getDropTargetRow().isPresent()) {
                        Group targetGroup = event.getDropTargetRow().get();

                        // if we drop to itself
                        if (targetGroup.getId().equals(draggedGroup.getId())) {
                            continue;
                        }

                        // if we drop parent to its child
                        if (isParentDroppedToChild(draggedGroup, targetGroup, vTree)) {
                            continue;
                        }

                        // if we drop child to the same parent
                        if (draggedGroup.getParent() != null
                                && (draggedGroup.getParent().getId().equals(targetGroup.getId()))) {
                            continue;
                        }

                        groupChangeEventHandler.accept(new GroupChangeEvent(groupsTree, draggedGroup.getId(), targetGroup.getId()));

                        // if we drop group to empty space make it root
                    } else if (event.getDropLocation() == DropLocation.EMPTY) {
                        groupChangeEventHandler.accept(new GroupChangeEvent(groupsTree, draggedGroup.getId(), null));
                    }
                }
            }
        });
    }

    protected boolean isParentDroppedToChild(Group parent, Group child, CubaTree<Group> vTree) {
        if (!vTree.hasChildren(parent)) {
            return false;
        }

        return checkAllChildrenRecursively(vTree.getChildren(parent), child, vTree);
    }

    protected boolean checkAllChildrenRecursively(Collection<Group> children, Group dropOver, CubaTree<Group> vTree) {
        for (Group group : children) {
            if (group.equals(dropOver)) {
                return true;
            } else if (vTree.hasChildren(group)) {
                if (checkAllChildrenRecursively(vTree.getChildren(group), dropOver, vTree)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean isEdgeOrIE() {
        return Page.getCurrent().getWebBrowser().isIE()
                || Page.getCurrent().getWebBrowser().isEdge();
    }
}
