/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.app.security.user.resetpasswords.ResetPasswordsDialog;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
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

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataService dataService;

    @Inject
    protected UserManagementService userManagementService;

    @Override
    public void init(Map<String, Object> params) {
        usersDs.addListener(new DsListenerAdapter<User>() {
            @Override
            public void itemChanged(Datasource<User> ds, User prevItem, User item) {
                if (removeAction != null)
                    removeAction.setEnabled(!(userSession.getUser().equals(item) ||
                            userSession.getCurrentOrSubstitutedUser().equals(item)));
            }
        });

        Boolean multiSelect = BooleanUtils.toBooleanObject((String) params.get("multiselect"));
        if (multiSelect != null)
            usersTable.setMultiSelect(multiSelect);

        boolean hasPermissionsToCreateUsers =
                userSession.isEntityOpPermitted(metadata.getSession().getClass(User.class),
                        EntityOp.CREATE);

        Action copy = usersTable.getAction("copy");
        if (copy != null) {
            copy.setEnabled(hasPermissionsToCreateUsers);
        }
    }

    @SuppressWarnings("unused")
    public void copy() {
        Set<User> selected = usersTable.getSelected();
        if (!selected.isEmpty()) {
            User selectedUser = selected.iterator().next();
            selectedUser = dataService.reload(selectedUser, "user.edit");
            User newUser = metadata.create(User.class);
            if (selectedUser.getUserRoles() != null) {
                List<UserRole> userRoles = new ArrayList<>();
                for (UserRole oldUserRole : selectedUser.getUserRoles()) {
                    Role oldRole = dataService.reload(oldUserRole.getRole(), "_local");
                    if (BooleanUtils.isTrue(oldRole.getDefaultRole()))
                        continue;
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
                    usersDs.refresh();
                }
            });
        }
    }

    @SuppressWarnings("unused")
    public void copySettings() {
        Set<User> selected = usersTable.getSelected();
        if (!selected.isEmpty()) {
            openWindow(
                    "sec$User.copySettings",
                    WindowManager.OpenType.DIALOG,
                    new SingletonMap("users", selected)
            );
        }
    }

    @SuppressWarnings("unused")
    public void changePassword() {
        if (!usersTable.getSelected().isEmpty()) {
            final Editor changePasswordDialog = openEditor(
                    "sec$User.changePassw",
                    usersTable.getSelected().iterator().next(),
                    WindowManager.OpenType.DIALOG
            );

            changePasswordDialog.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (COMMIT_ACTION_ID.equals(actionId)) {
                        User item = (User) changePasswordDialog.getItem();
                        usersDs.updateItem(item);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unused")
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
                        resetPasswordsForUsers(users, sendEmails, generatePasswords);
                    }
                }
            });
        }
    }

    private void resetPasswordsForUsers(Set<User> users, boolean sendEmails, boolean generatePasswords) {
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
                openWindow("sec$User.newPasswords", WindowManager.OpenType.DIALOG, params);
            } else {
                showNotification(String.format(getMessage("changePasswordAtLogonCompleted"), changedPasswords.size()),
                        NotificationType.HUMANIZED);
            }
            usersDs.refresh();
        }
    }
}
