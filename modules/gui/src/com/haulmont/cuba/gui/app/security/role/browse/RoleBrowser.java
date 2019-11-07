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
package com.haulmont.cuba.gui.app.security.role.browse;

import com.google.common.io.Files;
import com.haulmont.cuba.core.app.importexport.CollectionImportPolicy;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.RolesService;
import org.apache.commons.io.IOUtils;
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

public class RoleBrowser extends AbstractLookup {

    protected static final String DEFAULT_ROLE_PROPERTY = "defaultRole";

    private final Logger log = LoggerFactory.getLogger(RoleBrowser.class);

    @Inject
    protected Table<Role> rolesTable;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected Filter filter;

    @Inject
    protected Security security;
    @Inject
    protected Metadata metadata;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected ViewRepository viewRepository;
    @Inject
    protected FileUploadingAPI fileUploadingAPI;
    @Inject
    protected ExportDisplay exportDisplay;

    @Inject
    protected EntityImportExportService entityImportExportService;

    @Inject
    protected CollectionDatasource<Role, UUID> rolesDs;

    @Inject
    protected FileUploadField importRolesUpload;

    @Inject
    protected PopupButton exportBtn;

    @Inject
    protected Button createBtn;

    @Inject
    protected Button removeBtn;

    @Inject
    protected Button copyBtn;

    @Inject
    protected RolesService rolesService;

    @Named("rolesTable.remove")
    protected RemoveAction removeRolesAction;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        removeRolesAction.setBeforeActionPerformedHandler(() -> {
            Set<Role> selectedRoles = rolesTable.getSelected();
            for (Role role : selectedRoles) {
                if (role.isPredefined()) {
                    showNotification(getMessage("predefinedRoleDeletion"));
                    return false;
                }
            }
            return true;
        });

        Action copyRoles = new ItemTrackingAction("copy")
                .withCaption(getMessage("actions.Copy"))
                .withHandler(event -> {
                    if (rolesTable.getSingleSelected() != null && rolesTable.getSingleSelected().isPredefined()) {
                        userManagementService.copyRole(rolesTable.getSingleSelected().getName());
                    } else {
                        userManagementService.copyRole(rolesTable.getSingleSelected().getId());
                    }
                    rolesDs.refresh();
                });

        boolean hasPermissionsToCreateRole = security.isEntityOpPermitted(Role.class, EntityOp.CREATE);
        copyRoles.setEnabled(hasPermissionsToCreateRole && rolesService.isDatabaseModeAvailable());

        rolesTable.addAction(copyRoles);

        Action assignToUsersAction = new ItemTrackingAction(rolesTable, "assignToUsers")
                .withCaption(getMessage("assignToUsers"))
                .withHandler(event -> {
                    Set<Role> selected = rolesTable.getSelected();
                    if (selected.isEmpty()) {
                        showNotification(getMessage("selectRole.msg"), NotificationType.HUMANIZED);
                        return;
                    }

                    Role role = selected.iterator().next();
                    Map<String, Object> userLookupParams = new HashMap<>();
                    WindowParams.MULTI_SELECT.set(userLookupParams, true);
                    openLookup(User.class, items -> {
                        assignRoleUsers(role, items);
                    }, OpenType.THIS_TAB, userLookupParams);
                });
        rolesTable.addAction(assignToUsersAction);

        boolean hasPermissionsToCreateUserRole = security.isEntityOpPermitted(UserRole.class, EntityOp.CREATE);

        Action copy = rolesTable.getAction("assignToUsers");
        if (copy != null) {
            copy.setEnabled(hasPermissionsToCreateUserRole);
        }

        String windowOpener = (String) params.get("param$windowOpener");
        if ("sec$User.edit".equals(windowOpener)) {
            rolesTable.setMultiSelect(true);
        }

        rolesDs.addItemPropertyChangeListener(e -> {
            if (DEFAULT_ROLE_PROPERTY.equals(e.getProperty())) {
                Role reloadedRole = dataManager.reload(e.getItem(), View.LOCAL);
                reloadedRole.setDefaultRole(e.getItem().getDefaultRole());
                rolesDs.updateItem(reloadedRole);
                rolesDs.modifyItem(reloadedRole);
                rolesDs.commit();
            }
        });

        importRolesUpload.addFileUploadSucceedListener(event ->
                importRoles()
        );
        importRolesUpload.setCaption(null);
        importRolesUpload.setUploadButtonCaption(null);

        if (!rolesService.isDatabaseModeAvailable()) {
            createBtn.setVisible(false);
            removeBtn.setVisible(false);
            copyBtn.setVisible(false);
            exportBtn.setVisible(false);
            importRolesUpload.setVisible(false);
        }

        if (rolesService.isPredefinedRolesModeAvailable()
                && rolesService.applicationHasPredefinedRoles()) {
            filter.setVisible(false);
        }
    }

    protected void importRoles() {
        File file = fileUploadingAPI.getFile(importRolesUpload.getFileId());
        if (file == null) {
            String errorMsg = String.format("Entities import upload error. File with id %s not found", importRolesUpload.getFileId());
            throw new RuntimeException(errorMsg);
        }

        byte[] fileBytes;
        try (InputStream is = new FileInputStream(file)) {
            fileBytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to import file", e);
        }

        try {
            Collection<Entity> importedEntities;
            deleteSoftDeletedEntities();
            if ("json".equals(Files.getFileExtension(importRolesUpload.getFileName()))) {
                String jsonContent = new String(fileBytes, StandardCharsets.UTF_8);
                importedEntities = entityImportExportService.importEntitiesFromJSON(jsonContent, createRolesImportView());
            } else {
                importedEntities = entityImportExportService.importEntitiesFromZIP(fileBytes, createRolesImportView());
            }
            long importedRolesCount = importedEntities.stream()
                    .filter(entity -> entity instanceof Role)
                    .count();
            showNotification(importedRolesCount + " roles imported", NotificationType.HUMANIZED);
            rolesDs.refresh();
        } catch (Exception e) {
            showNotification(formatMessage("importError", e.getMessage()), NotificationType.ERROR);
        }

        try {
            fileUploadingAPI.deleteFile(importRolesUpload.getFileId());
        } catch (FileStorageException e) {
            log.error("Unable to delete temp file", e);
        }
    }

    /**
     * Physically remove soft-deleted Roles and Permissions from the database before the import to avoid unique constraint violations.
     * Fix of the https://github.com/cuba-platform/cuba/issues/2288
     */
    protected void deleteSoftDeletedEntities() {
        List<Role> deletedRoles = dataManager.load(Role.class)
                .softDeletion(false)
                .query("select p from sec$Role p where p.deleteTs is not null")
                .list();
        List<Permission> deletedPermissions = dataManager.load(Permission.class)
                .softDeletion(false)
                .query("select p from sec$Permission p where p.deleteTs is not null")
                .list();
        List<Entity> entitiesToRemove = new ArrayList<>();
        entitiesToRemove.addAll(deletedRoles);
        entitiesToRemove.addAll(deletedPermissions);
        CommitContext ctx = new CommitContext();
        ctx.setSoftDeletion(false);
        ctx.setRemoveInstances(entitiesToRemove);
        dataManager.commit(ctx);
        if (!deletedPermissions.isEmpty() || !deletedRoles.isEmpty()) {
            log.debug("Soft deleted entities removed: {} roles and {} permissions", deletedRoles.size(), deletedPermissions.size());
        }
    }

    protected void assignRoleUsers(Role role, Collection<User> items) {
        if (items == null)
            return;

        List<Entity> toCommit = new ArrayList<>();
        for (User user : items) {
            LoadContext<UserRole> ctx = LoadContext.create(UserRole.class)
                    .setView("user.edit")
                    .setQuery(new LoadContext.Query("select ur from sec$UserRole ur where ur.user.id = :userId")
                            .setParameter("userId", user.getId())
                    );

            List<UserRole> userRoles = dataManager.loadList(ctx);

            boolean roleExist = false;
            for (UserRole userRole : userRoles) {
                if ((!role.isPredefined() && role.equals(userRole.getRole())
                        || (role.isPredefined() && role.getName().equals(userRole.getRoleName())))) {
                    roleExist = true;
                    break;
                }
            }
            if (!roleExist) {
                UserRole ur = metadata.create(UserRole.class);
                ur.setUser(user);
                if (role.isPredefined()) {
                    ur.setRoleName(role.getName());
                } else {
                    ur.setRole(role);
                }
                toCommit.add(ur);
            }
        }

        if (!toCommit.isEmpty()) {
            dataManager.commit(new CommitContext(toCommit));
        }

        showNotification(getMessage("rolesAssigned.msg"));
    }

    protected EntityImportView createRolesImportView() {
        return new EntityImportView(Role.class)
                .addLocalProperties()
                .addOneToManyProperty("permissions",
                        new EntityImportView(Permission.class).addLocalProperties(),
                        CollectionImportPolicy.REMOVE_ABSENT_ITEMS);
    }

    public void exportZIP() {
        export(ExportFormat.ZIP);
    }

    public void exportJSON() {
        export(ExportFormat.JSON);
    }

    protected void export(ExportFormat exportFormat) {
        Collection<Role> selected = rolesTable.getSelected();
        if (selected.isEmpty()
                && rolesTable.getItems() != null) {
            selected = rolesTable.getItems().getItems();
        }

        View view = viewRepository.getView(Role.class, "role.export");
        if (view == null) {
            throw new DevelopmentException("View 'role.export' for sec$Role was not found");
        }

        try {
            if (exportFormat == ExportFormat.ZIP) {
                byte[] data = entityImportExportService.exportEntitiesToZIP(selected, view);
                exportDisplay.show(new ByteArrayDataProvider(data), "Roles", ExportFormat.ZIP);
            } else if (exportFormat == ExportFormat.JSON) {
                byte[] data = entityImportExportService.exportEntitiesToJSON(selected, view)
                        .getBytes(StandardCharsets.UTF_8);
                exportDisplay.show(new ByteArrayDataProvider(data), "Roles", ExportFormat.JSON);
            }
        } catch (Exception e) {
            showNotification(getMessage("exportFailed"), e.getMessage(), NotificationType.ERROR);
            log.error("Roles export failed", e);
        }
    }
}