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
package com.haulmont.cuba.gui.app.security.group.browse;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.ReferenceImportBehaviour;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GroupBrowser extends AbstractWindow {

    protected static final Logger log = LoggerFactory.getLogger(GroupBrowser.class);

    @Inject
    protected UserManagementService userManagementService;

    @Named("groupsTree.copy")
    protected Action groupCopyAction;

    @Inject
    protected PopupButton groupCreateButton;

    @Inject
    protected HierarchicalDatasource<Group, UUID> groupsDs;

    @Inject
    private CollectionDatasource<Constraint, UUID> constraintsDs;

    @Inject
    protected Tree<Group> groupsTree;

    @Inject
    protected Table<User> usersTable;

    @Inject
    protected TabSheet tabsheet;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Security security;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected FileUploadField importUpload;

    @Inject
    protected FileUploadingAPI fileUploadingAPI;

    @Inject
    protected EntityImportExportService entityImportExportService;

    @Inject
    protected ExportDisplay exportDisplay;

    @Inject
    protected ViewRepository viewRepository;

    protected boolean constraintsTabInitialized = false;
    protected boolean attributesTabInitialized = false;

    protected GroupPropertyCreateAction attributeCreateAction;
    protected GroupPropertyCreateAction constraintCreateAction;
    protected GroupPropertyCreateAction userCreateAction;

    @Override
    public void init(final Map<String, Object> params) {
        CreateAction createAction = new CreateAction(groupsTree) {
            @Override
            protected void afterCommit(Entity entity) {
                groupsTree.expandTree();
            }
        };
        groupsTree.addAction(createAction);
        createAction.setCaption(getMessage("action.create"));

        createAction.setOpenType(OpenType.DIALOG);

        EditAction groupEditAction = new EditAction(groupsTree) {
            @Override
            protected void afterCommit(Entity entity) {
                groupsTree.expandTree();
            }
        };
        groupEditAction.setOpenType(OpenType.DIALOG);
        groupsTree.addAction(groupEditAction);

        groupCreateButton.addAction(createAction);
        groupCreateButton.addAction(groupCopyAction);

        userCreateAction = new GroupPropertyCreateAction(usersTable) {
            @Override
            protected void afterCommit(Entity entity) {
                usersTable.getDatasource().refresh();
            }
        };

        groupsTree.addAction(new RemoveAction(groupsTree) {
            @Override
            protected boolean isApplicable() {
                if (target != null && target.getDatasource() != null && target.getSingleSelected() != null) {
                    @SuppressWarnings("unchecked")
                    HierarchicalDatasource<Group, UUID> ds = (HierarchicalDatasource<Group, UUID>) target.getDatasource();
                    UUID selectedItemId = (UUID) target.getSingleSelected().getId();
                    return ds.getChildren(selectedItemId).isEmpty();
                }

                return false;
            }
        });
        usersTable.addAction(userCreateAction);
        usersTable.addAction(new ItemTrackingAction("moveToGroup") {
            @Override
            public String getIcon() {
                return "icons/move.png";
            }

            @Override
            public void actionPerform(Component component) {
                final Set<User> selected = usersTable.getSelected();
                if (!selected.isEmpty()) {
                    Window lookupWindow = openLookup(Group.class, items -> {
                        if (items.size() == 1) {
                            Group group = (Group) items.iterator().next();
                            List<UUID> usersForModify = new ArrayList<>();
                            for (User user : selected) {
                                usersForModify.add(user.getId());
                            }
                            userManagementService.moveUsersToGroup(usersForModify, group.getId());

                            usersTable.getDatasource().refresh();
                        }
                    }, OpenType.DIALOG);

                    lookupWindow.addCloseListener(actionId -> {
                        usersTable.requestFocus();
                    });
                }
            }

            @Override
            protected boolean isPermitted() {
                MetaClass userMetaClass = metadata.getSession().getClass(User.class);
                return security.isEntityOpPermitted(userMetaClass, EntityOp.UPDATE);
            }
        });

        tabsheet.addListener(newTab -> {
            if ("constraintsTab".equals(newTab.getName())) {
                initConstraintsTab();
            } else if ("attributesTab".equals(newTab.getName())) {
                initAttributesTab();
            }
        });

        final boolean hasPermissionsToCreateGroup =
                security.isEntityOpPermitted(metadata.getSession().getClass(Group.class),
                        EntityOp.CREATE);

        // enable actions if group is selected
        groupsDs.addItemChangeListener(e -> {
            if (userCreateAction != null) {
                userCreateAction.setEnabled(e.getItem() != null);
            }
            if (attributeCreateAction != null) {
                attributeCreateAction.setEnabled(e.getItem() != null);
            }
            if (constraintCreateAction != null) {
                constraintCreateAction.setEnabled(e.getItem() != null);
            }
            groupCopyAction.setEnabled(hasPermissionsToCreateGroup && e.getItem() != null);
            CollectionDatasource ds = usersTable.getDatasource();
            if (ds instanceof CollectionDatasource.SupportsPaging) {
                ((CollectionDatasource.SupportsPaging) ds).setFirstResult(0);
            }
        });

        groupsDs.refresh();
        groupsTree.expandTree();

        final Collection<UUID> itemIds = groupsDs.getRootItemIds();
        if (!itemIds.isEmpty()) {
            groupsTree.setSelected(groupsDs.getItem(itemIds.iterator().next()));
        }

        groupCreateButton.setEnabled(hasPermissionsToCreateGroup);
        groupCopyAction.setEnabled(hasPermissionsToCreateGroup);

        groupsTree.addAction(new ExportAction());

        importUpload.addFileUploadSucceedListener(event -> {
            File file = fileUploadingAPI.getFile(importUpload.getFileId());
            if (file == null) {
                String errorMsg = String.format("Entities import upload error. File with id %s not found", importUpload.getFileId());
                throw new RuntimeException(errorMsg);
            }
            byte[] zipBytes;
            try (InputStream is = new FileInputStream(file)) {
                zipBytes = IOUtils.toByteArray(is);
            } catch (IOException e) {
                throw new RuntimeException("Unable to import file", e);
            }

            try {
                fileUploadingAPI.deleteFile(importUpload.getFileId());
            } catch (FileStorageException e) {
                log.error("Unable to delete temp file", e);
            }

            try {
                Collection<Entity> importedEntities = entityImportExportService.importEntities(zipBytes, createGroupsImportView());
                long importedGroupsCount = importedEntities.stream().filter(entity -> entity instanceof Group).count();
                showNotification(importedGroupsCount + " groups imported", NotificationType.HUMANIZED);
                groupsDs.refresh();
            } catch (Exception e) {
                showNotification(formatMessage("importError", e.getMessage()), NotificationType.ERROR);
            }
        });
    }

    protected EntityImportView createGroupsImportView() {
        return new EntityImportView(Group.class)
                .addLocalProperties()
                .addProperty("parent", ReferenceImportBehaviour.ERROR_ON_MISSING)
                .addProperty("hierarchyList", new EntityImportView(GroupHierarchy.class)
                        .addLocalProperties()
                        .addProperty("parent", ReferenceImportBehaviour.ERROR_ON_MISSING))
                .addProperty("sessionAttributes", new EntityImportView(SessionAttribute.class).addLocalProperties())
                .addProperty("constraints", new EntityImportView(Constraint.class).addLocalProperties());
    }

    public void copyGroup() {
        Group group = groupsDs.getItem();
        if (group != null) {
            userManagementService.copyAccessGroup(group.getId());
            groupsDs.refresh();
        }
    }

    protected void initConstraintsTab() {
        if (constraintsTabInitialized) {
            return;
        }

        Table<Constraint> constraintsTable = (Table) getComponentNN("constraintsTable");
        constraintCreateAction = new GroupPropertyCreateAction(constraintsTable);
        constraintsTable.addAction(constraintCreateAction);

        ItemTrackingAction activateAction = new ItemTrackingAction("activate") {
            @Override
            public void actionPerform(Component component) {
                Constraint constraint = (Constraint) constraintsTable.getSingleSelected();
                if (constraint != null) {
                    constraint.setIsActive(!Boolean.TRUE.equals(constraint.getIsActive()));
                    constraintsDs.commit();
                    constraintsDs.refresh();
                }
            }
        };
        constraintsTable.addAction(activateAction);

        constraintsDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                activateAction.setCaption(Boolean.TRUE.equals(e.getItem().getIsActive()) ?
                                getMessage("deactivate") : getMessage("activate"));
            }
        });

        constraintsTable.addGeneratedColumn(
                "entityName",
                constraint -> {
                    if (StringUtils.isEmpty(constraint.getEntityName())) {
                        return componentsFactory.createComponent(Label.class);
                    }

                    MetaClass metaClass = metadata.getClassNN(constraint.getEntityName());
                    MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(metaClass);
                    Label label = componentsFactory.createComponent(Label.class);
                    label.setValue(effectiveMetaClass.getName());
                    return label;
                }
        );

        constraintsTabInitialized = true;
        constraintsTable.refresh();
    }

    protected void initAttributesTab() {
        if (attributesTabInitialized) {
            return;
        }

        Table attributesTable = (Table) getComponentNN("attributesTable");
        attributeCreateAction = new GroupPropertyCreateAction(attributesTable);
        attributesTable.addAction(attributeCreateAction);

        attributesTabInitialized = true;
        attributesTable.refresh();
    }

    /**
     * Create action for the objects associated with the group
     */
    protected class GroupPropertyCreateAction extends CreateAction {

        public GroupPropertyCreateAction(ListComponent owner) {
            super(owner);
            Set<Group> selected = groupsTree.getSelected();
            setEnabled(selected != null && selected.size() == 1);
        }

        @Override
        public Map<String, Object> getInitialValues() {
            return ParamsMap.of("group", groupsTree.getSingleSelected());
        }

        @Override
        public void actionPerform(Component component) {
            Set<Group> selected = groupsTree.getSelected();
            if (selected == null || selected.size() != 1) {
                return;
            }

            super.actionPerform(component);
        }
    }

    protected class ExportAction extends ItemTrackingAction {
        public ExportAction() {
            super("export");

            setCaption(messages.getMainMessage("actions.Export"));
        }

        @Override
        public void actionPerform(Component component) {
            Set<Group> selected = groupsTree.getSelected();
            View view = viewRepository.getView(Group.class, "group.export");
            if (view == null) {
                throw new DevelopmentException("View 'group.export' for sec$Group was not found");
            }

            if (!selected.isEmpty()) {
                try {
                    exportDisplay.show(
                            new ByteArrayDataProvider(entityImportExportService.exportEntities(selected, view)),
                            "Groups", ExportFormat.ZIP);

                } catch (Exception e) {
                    showNotification(getMessage("exportFailed"), e.getMessage(), NotificationType.ERROR);
                    log.error("Groups export failed", e);
                }
            }
        }
    }
}