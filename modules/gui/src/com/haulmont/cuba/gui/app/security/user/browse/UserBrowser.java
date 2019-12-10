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

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.security.user.resetpasswords.ResetPasswordsDialog;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.lang3.BooleanUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

public class UserBrowser extends AbstractLookup {
    @Inject
    protected Table<User> usersTable;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

    @Named("usersTable.copySettings")
    protected Action copySettingsAction;

    @Named("usersTable.copy")
    protected Action copyAction;

    @Named("usersTable.changePassw")
    protected Action changePasswAction;

    @Named("usersTable.changePasswAtLogon")
    protected Action changePasswAtLogonAction;

    @Named("usersTable.resetRememberMe")
    protected Action resetRememberMeAction;

    @Inject
    protected PopupButton additionalActionsBtn;

    @Inject
    protected Security security;
    @Inject
    protected Metadata metadata;
    @Inject
    protected DataSupplier dataSupplier;
    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected UserManagementService userManagementService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        final boolean hasPermissionsToCreateUsers =
                security.isEntityOpPermitted(User.class, EntityOp.CREATE);

        final boolean hasPermissionsToUpdateUsers =
                security.isEntityOpPermitted(User.class, EntityOp.CREATE);

        final boolean hasPermissionsToCreateSettings =
                security.isEntityOpPermitted(UserSetting.class, EntityOp.CREATE);

        // no selection
        copySettingsAction.setEnabled(false);
        changePasswAction.setEnabled(false);
        changePasswAtLogonAction.setEnabled(false);

        resetRememberMeAction.setEnabled(security.isEntityOpPermitted(RememberMeToken.class, EntityOp.DELETE));

        usersDs.addItemChangeListener(e -> {
            if (usersTable.getSelected().size() > 1) {
                copyAction.setEnabled(false);
                changePasswAction.setEnabled(false);
            } else {
                copyAction.setEnabled(hasPermissionsToCreateUsers && e.getItem() != null);
                changePasswAction.setEnabled(hasPermissionsToUpdateUsers && e.getItem() != null);
            }

            changePasswAtLogonAction.setEnabled(hasPermissionsToUpdateUsers && e.getItem() != null);
            copySettingsAction.setEnabled(hasPermissionsToCreateSettings && e.getItem() != null);
        });

        usersDs.addCollectionChangeListener(e -> {
            if (e.getDs().getState() == Datasource.State.VALID) {
                copyAction.setEnabled(hasPermissionsToCreateUsers && e.getDs().getItem() != null);
                changePasswAction.setEnabled(hasPermissionsToUpdateUsers && e.getDs().getItem() != null);
                changePasswAtLogonAction.setEnabled(hasPermissionsToUpdateUsers && e.getDs().getItem() != null);
                copySettingsAction.setEnabled(hasPermissionsToCreateSettings && e.getDs().getItem() != null);
            }
        });

        RemoveAction removeAction = new UserRemoveAction(usersTable, userManagementService);
        usersTable.addAction(removeAction);

        additionalActionsBtn.addAction(copySettingsAction);
        additionalActionsBtn.addAction(changePasswAction);
        additionalActionsBtn.addAction(changePasswAtLogonAction);
        additionalActionsBtn.addAction(resetRememberMeAction);

        if (WindowParams.MULTI_SELECT.getBool(getFrame().getContext())) {
            usersTable.setMultiSelect(true);
        }

        initTimeZoneColumn();
        initGroupColumn();
    }

    protected void initTimeZoneColumn() {
        usersTable.addGeneratedColumn("timeZone", entity -> {
            String timeZone = null;
            if (Boolean.TRUE.equals(entity.getTimeZoneAuto())) {
                timeZone = messages.getMainMessage("timeZone.auto");
            } else if (entity.getTimeZone() != null) {
                timeZone = entity.getTimeZone();
            }
            return new Table.PlainTextCell(timeZone);
        });
    }

    protected void initGroupColumn() {
        usersTable.addGeneratedColumn("group", entity -> {
            String groupName = entity.getGroup() != null ? entity.getGroup().getName() : entity.getGroupNames();
            return new Table.PlainTextCell(groupName);
        });
    }

    public void copy() {
        Set<User> selected = usersTable.getSelected();
        if (!selected.isEmpty()) {
            User selectedUser = selected.iterator().next();
            selectedUser = dataSupplier.reload(selectedUser, "user.edit");
            User newUser = metadata.create(User.class);
            if (selectedUser.getUserRoles() != null) {
                List<UserRole> userRoles = new ArrayList<>();
                for (UserRole oldUserRole : selectedUser.getUserRoles()) {
                    Role oldRole = dataSupplier.reload(oldUserRole.getRole(), "_local");
                    if (BooleanUtils.isTrue(oldRole.getDefaultRole())) {
                        continue;
                    }
                    UserRole role = metadata.create(UserRole.class);
                    role.setUser(newUser);
                    role.setRole(oldRole);
                    userRoles.add(role);
                }
                newUser.setUserRoles(userRoles);
            }
            newUser.setGroup(selectedUser.getGroup());
            AbstractEditor editor = openEditor("sec$User.edit", newUser, OpenType.THIS_TAB,
                    ParamsMap.of("initCopy", true));
            editor.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    usersDs.refresh();
                }
                usersTable.focus();
            });
        }
    }

    public void copySettings() {
        Set<User> selected = usersTable.getSelected();
        if (!selected.isEmpty()) {
            Window copySettingsWindow = openWindow(
                    "sec$User.copySettings",
                    OpenType.DIALOG,
                    ParamsMap.of("users", selected)
            );
            copySettingsWindow.addCloseListener(actionId ->
                    usersTable.focus()
            );
        }
    }

    public void changePassword() {
        User selectedUser = usersTable.getSingleSelected();
        if (selectedUser != null) {
            Window changePasswordDialog = openWindow("sec$User.changePassword",
                    OpenType.DIALOG,
                    ParamsMap.of("user", selectedUser));

            changePasswordDialog.addCloseListener(actionId -> {
                if (COMMIT_ACTION_ID.equals(actionId)) {
                    usersDs.updateItem(dataSupplier.reload(selectedUser, usersDs.getView()));
                }
                usersTable.focus();
            });
        }
    }

    public void changePasswordAtLogon() {
        if (!usersTable.getSelected().isEmpty()) {
            ResetPasswordsDialog resetPasswordsDialog = (ResetPasswordsDialog) openWindow("sec$User.resetPasswords", OpenType.DIALOG);
            resetPasswordsDialog.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    boolean sendEmails = resetPasswordsDialog.getSendEmails();
                    boolean generatePasswords = resetPasswordsDialog.getGeneratePasswords();
                    Set<User> users = usersTable.getSelected();
                    resetPasswords(users, sendEmails, generatePasswords);
                }
                usersTable.focus();
            });
        }
    }

    protected void resetPasswords(Set<User> users, boolean sendEmails, boolean generatePasswords) {
        List<UUID> usersForModify = new ArrayList<>();
        for (User user : users) {
            usersForModify.add(user.getId());
        }

        if (sendEmails) {
            Integer modifiedCount = userManagementService.changePasswordsAtLogonAndSendEmails(usersForModify);
            usersDs.refresh();

            showNotification(String.format(getMessage("resetPasswordCompleted"), modifiedCount),
                    NotificationType.HUMANIZED);
        } else {
            Map<UUID, String> changedPasswords = userManagementService.changePasswordsAtLogon(usersForModify, generatePasswords);

            if (generatePasswords) {
                Map<User, String> userPasswords = new LinkedHashMap<>();
                for (Map.Entry<UUID, String> entry : changedPasswords.entrySet()) {
                    userPasswords.put(usersDs.getItem(entry.getKey()), entry.getValue());
                }

                Window newPasswordsWindow = openWindow("sec$User.newPasswords", OpenType.DIALOG,
                        ParamsMap.of("passwords", userPasswords));
                newPasswordsWindow.addCloseListener(actionId ->
                        usersTable.focus()
                );
            } else {
                showNotification(
                        formatMessage("changePasswordAtLogonCompleted", changedPasswords.size()),
                        NotificationType.HUMANIZED);
            }
            usersDs.refresh();
        }
    }

    public void resetRememberMe() {
        if (usersTable.getSelected().isEmpty()) {
            showOptionDialog(
                    getMessage("resetRememberMeTitle"),
                    getMessage("resetRememberMeQuestion"),
                    MessageType.CONFIRMATION,
                    new Action[]{
                            new BaseAction("actions.ResetAll")
                                    .withCaption(getMessage("actions.ResetAll"))
                                    .withHandler(event -> resetRememberMeAll()),

                            new DialogAction(Type.CANCEL, Status.PRIMARY)
                    }
            );
        } else {
            showOptionDialog(
                    getMessage("resetRememberMeTitle"),
                    getMessage("resetRememberMeQuestion"),
                    MessageType.CONFIRMATION,
                    new Action[]{
                            new BaseAction("actions.ResetOptionSelected")
                                    .withCaption(getMessage("actions.ResetOptionSelected"))
                                    .withHandler(event -> resetRememberMe(usersTable.getSelected())),

                            new BaseAction("actions.ResetOptionAll")
                                    .withCaption(getMessage("actions.ResetOptionAll"))
                                    .withHandler(event -> resetRememberMeAll()),

                            new DialogAction(Type.CANCEL, Status.PRIMARY)
                    }
            );
        }
    }

    public void resetRememberMe(Set<User> users) {
        List<UUID> usersForModify = users.stream()
                .map(BaseUuidEntity::getId)
                .collect(Collectors.toList());

        userManagementService.resetRememberMeTokens(usersForModify);

        showNotification(getMessage("resetRememberMeCompleted"), NotificationType.HUMANIZED);
    }

    public void resetRememberMeAll() {
        userManagementService.resetRememberMeTokens();

        showNotification(getMessage("resetRememberMeCompleted"), NotificationType.HUMANIZED);
    }
}