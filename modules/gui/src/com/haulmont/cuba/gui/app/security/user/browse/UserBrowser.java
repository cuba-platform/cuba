/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.app.security.user.resetpasswords.ResetPasswordsDialog;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class UserBrowser extends AbstractLookup {

    @Inject
    protected Table usersTable;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

    @Named("usersTable.remove")
    protected RemoveAction removeAction;

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
    protected UserSession userSession;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Override
    public void init(Map<String, Object> params) {
        MetaClass userMetaClass = metadata.getClassNN(User.class);

        final boolean hasPermissionsToCreateUsers =
                security.isEntityOpPermitted(userMetaClass, EntityOp.CREATE);

        final boolean hasPermissionsToUpdateUsers =
                security.isEntityOpPermitted(userMetaClass, EntityOp.CREATE);

        final boolean hasPermissionsToCreateSettings =
                security.isEntityOpPermitted(metadata.getClassNN(UserSetting.class), EntityOp.CREATE);

        changePasswAction.setEnabled(hasPermissionsToUpdateUsers);
        changePasswAtLogonAction.setEnabled(hasPermissionsToUpdateUsers);
        copySettingsAction.setEnabled(hasPermissionsToCreateSettings);

        resetRememberMeAction.setEnabled(security.isEntityOpPermitted(RememberMeToken.class, EntityOp.DELETE));

        usersDs.addListener(new CollectionDsListenerAdapter<User>() {
            @Override
            public void itemChanged(Datasource<User> ds, User prevItem, User item) {
                if (usersTable.getSelected().size() > 1) {
                    copyAction.setEnabled(false);
                    changePasswAction.setEnabled(false);
                } else {
                    copyAction.setEnabled(hasPermissionsToCreateUsers && item != null);
                    changePasswAction.setEnabled(hasPermissionsToUpdateUsers && item != null);
                }

                changePasswAtLogonAction.setEnabled(hasPermissionsToUpdateUsers && item != null);
                copySettingsAction.setEnabled(hasPermissionsToCreateSettings && item != null);
            }

            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<User> items) {
                if (ds.getState() == Datasource.State.VALID) {
                    copyAction.setEnabled(hasPermissionsToCreateUsers && ds.getItem() != null);
                    changePasswAction.setEnabled(hasPermissionsToUpdateUsers && ds.getItem() != null);
                    changePasswAtLogonAction.setEnabled(hasPermissionsToUpdateUsers && ds.getItem() != null);
                    copySettingsAction.setEnabled(hasPermissionsToCreateSettings && ds.getItem() != null);
                }
            }
        });

        usersTable.addAction(new RemoveAction(usersTable) {
            @Override
            public boolean isApplicable() {
                if (target != null) {
                    Set<Entity> selected = target.getSelected();
                    if (!selected.isEmpty()) {
                        return !(selected.contains(userSession.getUser())
                                || userSession.getCurrentOrSubstitutedUser().equals(target.getSingleSelected()));
                    }
                }

                return false;
            }
        });

        additionalActionsBtn.addAction(copySettingsAction);
        additionalActionsBtn.addAction(changePasswAction);
        additionalActionsBtn.addAction(changePasswAtLogonAction);
        additionalActionsBtn.addAction(resetRememberMeAction);

        if (WindowParams.MULTI_SELECT.getBool(getContext())) {
            usersTable.setMultiSelect(true);
        }

        initTimeZoneColumn();
    }

    protected void initTimeZoneColumn() {
        usersTable.addGeneratedColumn("timeZone", new Table.ColumnGenerator<User>() {
            @Override
            public Component generateCell(User entity) {
                Label label = componentsFactory.createComponent(Label.NAME);
                if (Boolean.TRUE.equals(entity.getTimeZoneAuto())) {
                    label.setValue(messages.getMainMessage("timeZone.auto"));
                } else if (entity.getTimeZone() != null) {
                    label.setValue(entity.getTimeZone());
                }
                return label;
            }
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
                    UserRole role = new UserRole();
                    role.setUser(newUser);
                    role.setRole(oldRole);
                    userRoles.add(role);
                }
                newUser.setUserRoles(userRoles);
            }
            newUser.setGroup(selectedUser.getGroup());
            UserEditor editor = openEditor("sec$User.edit", newUser, WindowManager.OpenType.THIS_TAB);
            editor.initCopy();
            editor.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        usersDs.refresh();
                    }
                    usersTable.requestFocus();
                }
            });
        }
    }

    public void copySettings() {
        Set<User> selected = usersTable.getSelected();
        if (!selected.isEmpty()) {
            @SuppressWarnings("unchecked")
            Window copySettingsWindow = openWindow(
                    "sec$User.copySettings",
                    WindowManager.OpenType.DIALOG,
                    new SingletonMap("users", selected)
            );
            copySettingsWindow.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    usersTable.requestFocus();
                }
            });
        }
    }

    public void changePassword() {
        final User selectedUser = usersTable.getSingleSelected();
        if (selectedUser != null) {
            Window changePasswordDialog = openWindow("sec$User.changePassword",
                    WindowManager.OpenType.DIALOG,
                    ParamsMap.of("user", selectedUser));

            changePasswordDialog.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (COMMIT_ACTION_ID.equals(actionId)) {
                        usersDs.updateItem(dataSupplier.reload(selectedUser, "user.browse"));
                    }
                    usersTable.requestFocus();
                }
            });
        }
    }

    public void changePasswordAtLogon() {
        if (!usersTable.getSelected().isEmpty()) {
            final ResetPasswordsDialog resetPasswordsDialog = openWindow("sec$User.resetPasswords", WindowManager.OpenType.DIALOG);
            resetPasswordsDialog.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        boolean sendEmails = resetPasswordsDialog.getSendEmails();
                        boolean generatePasswords = resetPasswordsDialog.getGeneratePasswords();
                        Set<User> users = usersTable.getSelected();
                        resetPasswords(users, sendEmails, generatePasswords);
                    }
                    usersTable.requestFocus();
                }
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
                Map<String, Object> params = Collections.singletonMap("passwords", (Object) userPasswords);
                Window newPasswordsWindow = openWindow("sec$User.newPasswords", WindowManager.OpenType.DIALOG, params);
                newPasswordsWindow.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        usersTable.requestFocus();
                    }
                });
            } else {
                showNotification(String.format(getMessage("changePasswordAtLogonCompleted"), changedPasswords.size()),
                        NotificationType.HUMANIZED);
            }
            usersDs.refresh();
        }
    }

    public void resetRememberMe() {
        if (usersTable.getSelected().isEmpty()) {
            showOptionDialog(getMessage("resetRememberMeTitle"), getMessage("resetRememberMeQuestion"), MessageType.CONFIRMATION,
                    new Action[]{
                            new AbstractAction("actions.ResetAll") {
                                @Override
                                public void actionPerform(Component component) {
                                    resetRememberMeAll();
                                }
                            },
                            new DialogAction(DialogAction.Type.CANCEL)
                    }
            );
        } else {
            showOptionDialog(getMessage("resetRememberMeTitle"), getMessage("resetRememberMeQuestion"), MessageType.CONFIRMATION,
                    new Action[]{
                            new AbstractAction("actions.ResetOptionSelected") {
                                @Override
                                public void actionPerform(Component component) {
                                    resetRememberMe(usersTable.<User>getSelected());
                                }
                            },
                            new AbstractAction("actions.ResetOptionAll") {
                                @Override
                                public void actionPerform(Component component) {
                                    resetRememberMeAll();
                                }
                            },
                            new DialogAction(DialogAction.Type.CANCEL)
                    }
            );
        }
    }

    public void resetRememberMe(Set<User> users) {
        List<UUID> usersForModify = new ArrayList<>();
        for (User user : users) {
            usersForModify.add(user.getId());
        }

        userManagementService.resetRememberMeTokens(usersForModify);

        showNotification(getMessage("resetRememberMeCompleted"), NotificationType.HUMANIZED);
    }

    public void resetRememberMeAll() {
        userManagementService.resetRememberMeTokens();

        showNotification(getMessage("resetRememberMeCompleted"), NotificationType.HUMANIZED);
    }
}