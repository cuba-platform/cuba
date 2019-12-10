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

import com.google.common.io.Files;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.ConstraintLocalizationService;
import com.haulmont.cuba.core.app.importexport.CollectionImportPolicy;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.ReferenceImportBehaviour;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.app.security.user.browse.UserRemoveAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.cuba.gui.components.DialogAction.Type;

public class GroupBrowser extends AbstractWindow {

    private final Logger log = LoggerFactory.getLogger(GroupBrowser.class);

    @Inject
    protected UserManagementService userManagementService;

    @Named("groupsTree.copy")
    protected Action groupCopyAction;

    @Inject
    protected PopupButton groupCreateButton;

    @Inject
    protected PopupButton exportBtn;

    @Inject
    protected HierarchicalDatasource<Group, UUID> groupsDs;

    @Inject
    protected CollectionDatasource<Constraint, UUID> constraintsDs;

    @Inject
    protected CollectionDatasource<SessionAttribute, UUID> attributesDs;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

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
    protected UiComponents uiComponents;

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

    @Inject
    protected ConstraintLocalizationService constraintLocalizationService;

    protected boolean constraintsTabInitialized = false;
    protected boolean attributesTabInitialized = false;

    protected GroupPropertyCreateAction attributeCreateAction;
    protected GroupPropertyCreateAction constraintCreateAction;
    protected GroupPropertyCreateAction userCreateAction;
    @Inject
    private DataManager dataManager;

    public interface Companion {
        void initDragAndDrop(Table<User> usersTable,
                             Tree<Group> groupsTree,
                             Consumer<UserGroupChangedEvent> userGroupChangedHandler,
                             Consumer<GroupChangeEvent> groupChangeEventHandler);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        initListeners();

        initUsersTableActions();

        initGroupTreeActions();

        initLazyTabs();

        initGroupsImport();

        initDragAndDrop();
    }

    protected void initListeners() {
        groupsDs.addItemChangeListener(e -> {
            Group group = e.getItem();

            constraintsDs.refresh(ParamsMap.of("group", group));
            attributesDs.refresh(ParamsMap.of("group", group));

            if (userCreateAction != null) {
                userCreateAction.setEnabled(group != null);
            }
            if (attributeCreateAction != null) {
                attributeCreateAction.setEnabled(group != null);
            }
            if (constraintCreateAction != null) {
                constraintCreateAction.setEnabled(group != null);
            }

            boolean hasPermissionsToCreateGroup = security.isEntityOpPermitted(Group.class, EntityOp.CREATE);
            groupCopyAction.setEnabled(hasPermissionsToCreateGroup && isNotPredefinedGroup());
            exportBtn.setEnabled(group != null && isNotPredefinedGroup());

            if (usersDs instanceof CollectionDatasource.SupportsPaging) {
                ((CollectionDatasource.SupportsPaging) usersDs).setFirstResult(0);
            }

            if (e.getItem() != null && !isNotPredefinedGroup()) {
                showNotification(getMessage("predefinedGroupIsUnchangeable"));
            }
        });
    }

    protected void initGroupsImport() {
        importUpload.addFileUploadSucceedListener(event -> {
            File file = fileUploadingAPI.getFile(importUpload.getFileId());
            if (file == null) {
                String errorMsg = String.format("Entities import upload error. File with id %s not found", importUpload.getFileId());
                throw new RuntimeException(errorMsg);
            }
            importGroups(file);
        });
    }

    protected void initLazyTabs() {
        tabsheet.addSelectedTabChangeListener(event -> {
            if ("constraintsTab".equals(event.getSelectedTab().getName())) {
                initConstraintsTab();
            } else if ("attributesTab".equals(event.getSelectedTab().getName())) {
                initAttributesTab();
            }
        });
    }

    protected void initDragAndDrop() {
        Companion companion = getCompanion();
        if (companion != null) {
            companion.initDragAndDrop(usersTable, groupsTree, (event) -> {
                if (event.getUsers().size() == 1) {
                    if (moveSelectedUsersToGroup(event)) {
                        showNotification(formatMessage("userMovedToGroup",
                                event.getUsers().get(0).getLogin(), event.getGroup().getName()));
                    }
                } else {
                    showOptionDialog(
                            messages.getMainMessage("dialogs.Confirmation"),
                            formatMessage("dialogs.moveToGroup.message", event.getGroup().getName(), event.getUsers().size()),
                            MessageType.CONFIRMATION,
                            new Action[]{
                                    new DialogAction(Type.OK).withHandler(dialogEvent -> {
                                        if (moveSelectedUsersToGroup(event)) {
                                            showNotification(formatMessage("usersMovedToGroup", event.getGroup().getName()));
                                        }
                                    }),
                                    new DialogAction(Type.CANCEL, Action.Status.PRIMARY)
                            }
                    );
                }
            }, (groupEvent) -> {
                userManagementService.changeGroupParent(groupEvent.getGroupId(), groupEvent.getNewParentId());
                groupsDs.refresh();
            });
        }
    }

    protected void initUsersTableActions() {
        userCreateAction = new GroupPropertyCreateAction(usersTable);
        userCreateAction.setAfterCommitHandler(entity ->
                usersDs.refresh()
        );

        usersTable.addAction(userCreateAction);

        usersTable.addAction(new UserRemoveAction(usersTable, userManagementService));

        Action moveToGroupAction = new ItemTrackingAction("moveToGroup")
                .withIcon("icons/move.png")
                .withCaption(getMessage("moveToGroup"))
                .withHandler(event -> {
                    Set<User> selected = usersTable.getSelected();
                    if (!selected.isEmpty()) {
                        moveUsersToGroup(selected);
                    }
                });

        moveToGroupAction.setEnabled(security.isEntityOpPermitted(User.class, EntityOp.UPDATE));

        usersTable.addAction(moveToGroupAction);
    }

    protected void initGroupTreeActions() {
        CreateAction createAction = new CreateAction(groupsTree);
        createAction.setAfterCommitHandler(entity ->
                groupsTree.expandTree()
        );
        groupsTree.addAction(createAction);
        createAction.setCaption(getMessage("action.create"));

        createAction.setOpenType(OpenType.DIALOG);

        EditAction groupEditAction = new EditAction(groupsTree);
        groupEditAction.setAfterCommitHandler(entity ->
                groupsTree.expandTree()
        );
        groupEditAction.setOpenType(OpenType.DIALOG);
        groupsTree.addAction(groupEditAction);

        groupCreateButton.addAction(createAction);
        groupCreateButton.addAction(groupCopyAction);

        groupsTree.addAction(new RemoveAction(groupsTree) {
            @Override
            protected boolean isApplicable() {
                Group group = groupsDs.getItem();
                if (group != null) {
                    return groupsDs.getChildren(group.getId()).isEmpty() && isNotPredefinedGroup();
                }

                return false;
            }
        });

        final boolean hasPermissionsToCreateGroup = security.isEntityOpPermitted(Group.class, EntityOp.CREATE);

        groupsDs.refresh();
        groupsTree.expandTree();

        final Collection<UUID> itemIds = groupsDs.getRootItemIds();
        if (!itemIds.isEmpty()) {
            groupsTree.setSelected(groupsDs.getItem(itemIds.iterator().next()));
        }

        groupCreateButton.setEnabled(hasPermissionsToCreateGroup);
        groupCopyAction.setEnabled(hasPermissionsToCreateGroup);
    }

    protected void importGroups(File file) {
        byte[] fileBytes;
        try (InputStream is = new FileInputStream(file)) {
            fileBytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to import file", e);
        }

        try {
            fileUploadingAPI.deleteFile(importUpload.getFileId());
        } catch (FileStorageException e) {
            log.warn("Unable to delete temp file", e);
        }

        try {
            Collection<Entity> importedEntities;
            if ("json".equals(Files.getFileExtension(importUpload.getFileName()))) {
                String json = new String(fileBytes, StandardCharsets.UTF_8);
                importedEntities = entityImportExportService.importEntitiesFromJSON(json, createGroupsImportView());
            } else {
                importedEntities = entityImportExportService.importEntitiesFromZIP(fileBytes, createGroupsImportView());
            }
            long importedGroupsCount = importedEntities.stream()
                    .filter(entity -> entity instanceof Group)
                    .count();
            showNotification(importedGroupsCount + " groups imported", NotificationType.HUMANIZED);
            groupsDs.refresh();
        } catch (Exception e) {
            showNotification(formatMessage("importError", e.getMessage()), NotificationType.ERROR);
            log.error("Groups import failed", e);
        }
    }

    protected void moveUsersToGroup(Set<User> selected) {
        Map<String, Object> lookupParams = ParamsMap.of(
                "exclude", groupsTree.getSelected().iterator().next(),
                "excludeChildren", false);

        AbstractLookup lookupWindow = openLookup(Group.class, items -> {
            if (items.size() == 1) {
                Group group = (Group) items.iterator().next();
                List<UUID> usersForModify = new ArrayList<>();
                for (User user : selected) {
                    usersForModify.add(user.getId());
                }

                if (group.isPredefined()) {
                    userManagementService.moveUsersToGroup(usersForModify, group.getName());
                } else {
                    userManagementService.moveUsersToGroup(usersForModify, group.getId());
                }

                if (selected.size() == 1) {
                    User user = selected.iterator().next();
                    showNotification(formatMessage("userMovedToGroup", user.getLogin(), group.getName()));
                } else {
                    showNotification(formatMessage("usersMovedToGroup", group.getName()));
                }

                usersTable.getDatasource().refresh();
            }
        }, OpenType.DIALOG, lookupParams);

        lookupWindow.addCloseListener(actionId ->
                usersTable.requestFocus()
        );
    }

    protected boolean moveSelectedUsersToGroup(UserGroupChangedEvent event) {
        List<UUID> userIds = event.getUsers().stream()
                .map(BaseUuidEntity::getId)
                .collect(Collectors.toList());

        int count;

        if (event.getGroup().isPredefined()) {
            count = userManagementService.moveUsersToGroup(userIds, event.getGroup().getName());
        } else {
            count = userManagementService.moveUsersToGroup(userIds, event.getGroup().getId());
        }

        if (count != 0) {
            usersDs.refresh();
            return true;
        }
        return false;
    }

    protected EntityImportView createGroupsImportView() {
        return new EntityImportView(Group.class)
                .addLocalProperties()
                .addManyToOneProperty("parent", ReferenceImportBehaviour.ERROR_ON_MISSING)
                .addOneToManyProperty("sessionAttributes",
                        new EntityImportView(SessionAttribute.class).addLocalProperties(),
                        CollectionImportPolicy.REMOVE_ABSENT_ITEMS)
                .addOneToManyProperty("constraints",
                        new EntityImportView(Constraint.class).addLocalProperties(),
                        CollectionImportPolicy.REMOVE_ABSENT_ITEMS);
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

        @SuppressWarnings("unchecked")
        Table<Constraint> constraintsTable = (Table) getComponentNN("constraintsTable");
        constraintCreateAction = new GroupPropertyCreateAction(constraintsTable) {
            @Override
            protected boolean isApplicable() {
                return super.isApplicable() && isNotPredefinedGroup();
            }
        };
        constraintsTable.addAction(constraintCreateAction);

        constraintsTable.addAction(new RemoveAction(constraintsTable) {
            @Override
            protected boolean isApplicable() {
                return super.isApplicable() && isNotPredefinedGroup();
            }
        });

        ItemTrackingAction activateAction = new ItemTrackingAction("activate") {
            @Override
            protected boolean isApplicable() {
                return super.isApplicable() && isNotPredefinedGroup();
            }
        };
        activateAction.withHandler(event -> {
            Constraint constraint = constraintsTable.getSingleSelected();
            if (constraint != null) {
                constraint.setIsActive(!Boolean.TRUE.equals(constraint.getIsActive()));
                constraintsDs.commit();
                constraintsDs.refresh();
            }
        });
        constraintsTable.addAction(activateAction);

        constraintsTable.addAction(new ConstraintLocalizationEditAction(constraintsTable));

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
                        return uiComponents.create(Label.class);
                    }

                    MetaClass metaClass = metadata.getClassNN(constraint.getEntityName());
                    MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(metaClass);
                    Label<String> label = uiComponents.create(Label.NAME);
                    label.setValue(effectiveMetaClass.getName());
                    return label;
                }
        );

        constraintsDs.refresh();

        BaseAction moveToGroupAction = new ItemTrackingAction("moveToGroup") {
            @Override
            protected boolean isApplicable() {
                return super.isApplicable() && isNotPredefinedGroup();
            }
        };
        moveToGroupAction.withCaption(getMessage("actions.constraint.moveToGroup"))
                .withIcon("icons/move.png")
                .withHandler(e ->
                        moveConstraintToGroup(constraintsTable.getSingleSelected())
                );
        if (!security.isEntityAttrUpdatePermitted(metadata.getClass(Constraint.class), "group")) {
            moveToGroupAction.setEnabled(false);
        }
        constraintsTable.addAction(moveToGroupAction);

        constraintsTabInitialized = true;
    }

    protected void moveConstraintToGroup(Constraint constraint) {
        Map<String, Object> lookupParams = ParamsMap.of(
                "exclude", groupsTree.getSelected().iterator().next(),
                "excludeChildren", false);

        Window lookupWindow = openLookup(Group.class, items -> {
            if (items.isEmpty()) {
                return;
            }
            Group group = (Group) items.iterator().next();

            constraint.setGroup(group);

            dataManager.commit(constraint);

            constraintsDs.refresh();

            showNotification(formatMessage("notification.constraintMovedToGroup", group.getName()));
        }, OpenType.DIALOG, lookupParams);

        lookupWindow.addCloseListener(actionId ->
                getComponentNN("constraintsTable").requestFocus()
        );
    }

    protected void initAttributesTab() {
        if (attributesTabInitialized) {
            return;
        }

        Table attributesTable = (Table) getComponentNN("attributesTable");
        attributeCreateAction = new GroupPropertyCreateAction(attributesTable) {
            @Override
            protected boolean isApplicable() {
                return super.isApplicable() && isNotPredefinedGroup();
            }
        };
        attributesTable.addAction(attributeCreateAction);

        attributesTable.addAction(new RemoveAction(attributesTable) {
            @Override
            protected boolean isApplicable() {
                return super.isApplicable() && isNotPredefinedGroup();
            }
        });

        attributesTabInitialized = true;
        attributesDs.refresh();
    }

    protected class ConstraintLocalizationEditAction extends ItemTrackingAction {
        public static final String ACTION_ID = "localizationEdit";

        protected OpenType openType;

        public ConstraintLocalizationEditAction(ListComponent<? extends Constraint> target) {
            this(target, OpenType.DIALOG);
        }

        public ConstraintLocalizationEditAction(ListComponent<? extends Constraint> target, OpenType openType) {
            super(target, ACTION_ID);
            this.openType = openType;
            setCaption(getMessage("action.localize.caption"));
            setIcon("icons/globe.png");
        }

        @Override
        public void actionPerform(Component component) {
            final Set selected = target.getSelected();
            if (selected.size() == 1) {
                final CollectionDatasource datasource = target.getDatasource();
                internalOpenEditor((Constraint) datasource.getItem());
            }
        }

        protected void internalOpenEditor(Constraint constraint) {
            LocalizedConstraintMessage localization = constraintLocalizationService.findLocalizedConstraintMessage(
                    constraint.getEntityName(), constraint.getOperationType());
            if (localization == null) {
                localization = metadata.create(LocalizedConstraintMessage.class);
                localization.setEntityName(constraint.getEntityName());
                localization.setOperationType(constraint.getOperationType());
            }
            openEditor(localization, openType);
        }

        @Override
        protected boolean isApplicable() {
            return super.isApplicable() && target.getSelected().size() == 1 && isNotPredefinedGroup();
        }
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
            Group group = groupsTree.getSingleSelected();
            if (group != null) {
                if (group.isPredefined()) {
                    return ParamsMap.of("groupNames", group.getName());
                } else {
                    return ParamsMap.of("group", group);
                }
            }
            return Collections.emptyMap();
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

    public void exportZIP() {
        export(ExportFormat.ZIP);
    }

    public void exportJSON() {
        export(ExportFormat.JSON);
    }

    protected void export(ExportFormat exportFormat) {
        Set<Group> selected = groupsTree.getSelected();
        if (selected.isEmpty()
                && groupsTree.getItems() != null) {
            selected = groupsTree.getItems()
                    .getItems()
                    .collect(Collectors.toSet());
        }

        View view = viewRepository.getView(Group.class, "group.export");

        try {
            if (exportFormat == ExportFormat.ZIP) {
                byte[] data = entityImportExportService.exportEntitiesToZIP(selected, view);
                exportDisplay.show(new ByteArrayDataProvider(data), "Groups", ExportFormat.ZIP);
            } else if (exportFormat == ExportFormat.JSON) {
                byte[] data = entityImportExportService.exportEntitiesToJSON(selected, view)
                        .getBytes(StandardCharsets.UTF_8);
                exportDisplay.show(new ByteArrayDataProvider(data), "Groups", ExportFormat.JSON);
            }
        } catch (Exception e) {
            showNotification(getMessage("exportFailed"), e.getMessage(), NotificationType.ERROR);
            log.error("Groups export failed", e);
        }
    }

    protected boolean isNotPredefinedGroup() {
        return groupsDs.getItem() == null || !groupsDs.getItem().isPredefined();
    }
}