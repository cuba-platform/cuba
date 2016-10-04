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

import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RoleBrowser extends AbstractLookup {

    protected static final String DEFAULT_ROLE_PROPERTY = "defaultRole";

    protected static final Logger log = LoggerFactory.getLogger(RoleBrowser.class);

    @Inject
    protected Table<Role> rolesTable;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected CollectionDatasource<Role, UUID> rolesDs;

    @Inject
    protected ExportDisplay exportDisplay;

    @Inject
    protected EntityImportExportService entityImportExportService;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected FileUploadField importRolesUpload;

    @Inject
    protected FileUploadingAPI fileUploadingAPI;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        Action copyRoles = new ItemTrackingAction("copy") {
            @Override
            public void actionPerform(Component component) {
                userManagementService.copyRole(rolesTable.getSingleSelected().getId());
                rolesDs.refresh();
            }
        };

        boolean hasPermissionsToCreateRole = security.isEntityOpPermitted(Role.class, EntityOp.CREATE);
        copyRoles.setEnabled(hasPermissionsToCreateRole);

        rolesTable.addAction(copyRoles);

        rolesTable.addAction(new ItemTrackingAction("assignToUsers") {
            @Override
            public void actionPerform(Component component) {
                if (target.getSelected().isEmpty()) {
                    showNotification(getMessage("selectRole.msg"), NotificationType.HUMANIZED);
                    return;
                }

                final Role role = (Role) target.getSingleSelected();
                Map<String, Object> params = new HashMap<>();
                WindowParams.MULTI_SELECT.set(params, true);
                openLookup("sec$User.lookup", new Handler() {
                    @Override
                    public void handleLookup(Collection items) {
                        if (items == null) return;
                        List<Entity> toCommit = new ArrayList<>();
                        for (Object item : items) {
                            User user = (User) item;
                            LoadContext<UserRole> ctx = new LoadContext<>(UserRole.class).setView("user.edit");
                            LoadContext.Query query = ctx.setQueryString("select ur from sec$UserRole ur where ur.user.id = :user");
                            query.setParameter("user", user);
                            List<UserRole> userRoles = dataManager.loadList(ctx);

                            boolean roleExist = false;
                            for (UserRole userRole : userRoles) {
                                if (role.equals(userRole.getRole())) {
                                    roleExist = true;
                                    break;
                                }
                            }
                            if (!roleExist) {
                                UserRole ur = metadata.create(UserRole.class);
                                ur.setUser(user);
                                ur.setRole(role);
                                toCommit.add(ur);
                            }
                        }

                        if (!toCommit.isEmpty()) {
                            dataManager.commit(new CommitContext(toCommit));
                        }

                        showNotification(getMessage("rolesAssigned.msg"), NotificationType.HUMANIZED);
                    }
                }, WindowManager.OpenType.THIS_TAB, params);
            }

            @Override
            public String getCaption() {
                return getMessage("assignToUsers");
            }
        });

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


        ExportAction action = new ExportAction();
        rolesTable.addAction(action);

        importRolesUpload.addFileUploadSucceedListener(event -> {
            File file = fileUploadingAPI.getFile(importRolesUpload.getFileId());
            if (file == null) {
                String errorMsg = String.format("Entities import upload error. File with id %s not found", importRolesUpload.getFileId());
                throw new RuntimeException(errorMsg);
            }
            byte[] zipBytes;
            try (InputStream is = new FileInputStream(file)) {
                zipBytes = IOUtils.toByteArray(is);
            } catch (IOException e) {
                throw new RuntimeException("Unable to import file", e);
            }

            try {
                fileUploadingAPI.deleteFile(importRolesUpload.getFileId());
            } catch (FileStorageException e) {
                log.error("Unable to delete temp file", e);
            }

            try {
                Collection<Entity> importedEntities = entityImportExportService.importEntities(zipBytes, createRolesImportView());
                long importedRolesCount = importedEntities.stream().filter(entity -> entity instanceof Role).count();
                showNotification(importedRolesCount + " roles imported", NotificationType.HUMANIZED);
                rolesDs.refresh();
            } catch (Exception e) {
                showNotification(formatMessage("importError", e.getMessage()), NotificationType.ERROR);
            }
        });
        importRolesUpload.setCaption(null);
        importRolesUpload.setUploadButtonCaption(null);
    }

    protected EntityImportView createRolesImportView() {
        return new EntityImportView(Role.class)
                .addLocalProperties()
                .addProperty("permissions", new EntityImportView(Permission.class).addLocalProperties());
    }

    protected class ExportAction extends ItemTrackingAction {
        public ExportAction() {
            super("export");
        }

        @Override
        public void actionPerform(Component component) {
            Set<Role> selected = rolesTable.getSelected();
            View view = viewRepository.getView(Role.class, "role.export");
            if (view == null) {
                throw new DevelopmentException("View 'role.export' for sec$Role was not found");
            }
            if (!selected.isEmpty()) {
                try {
                    exportDisplay.show(new ByteArrayDataProvider(entityImportExportService.exportEntities(selected, view)), "Roles", ExportFormat.ZIP);
                } catch (Exception e) {
                    showNotification(getMessage("exportFailed"), e.getMessage(), NotificationType.ERROR);
                    log.error("Roles export failed", e);
                }
            }
        }

        @Override
        public String getCaption() {
            return null;
        }
    }
}