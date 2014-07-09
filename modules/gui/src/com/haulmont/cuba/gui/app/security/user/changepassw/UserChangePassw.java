/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.user.changepassw;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UserChangePassw extends AbstractEditor {
    @Inject
    protected PasswordField passwField;

    @Inject
    protected PasswordField confirmPasswField;

    @Inject
    protected PasswordField currentPasswordField;

    @Inject
    protected Label currentPasswordLabel;

    @Named("windowActions.windowClose")
    protected Button closeBtn;

    @Inject
    protected Datasource<User> userDs;

    @Inject
    protected Configuration configuration;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected UserManagementService userManagementService;

    protected boolean cancelEnabled = true;
    protected boolean currentPasswordRequired = false;

    @Override
    protected void postInit() {
        if (!cancelEnabled) {
            closeBtn.setVisible(false);
        }
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        Boolean cancelEnabled = (Boolean) params.get("cancelEnabled");
        if (Boolean.FALSE.equals(cancelEnabled)) {
            this.cancelEnabled = false;
        }

        Boolean currentPasswordRequired = (Boolean) params.get("currentPasswordRequired");
        if (Boolean.TRUE.equals(currentPasswordRequired)) {
            currentPasswordField.setVisible(true);
            currentPasswordLabel.setVisible(true);

            this.currentPasswordRequired = true;
        }
    }

    @Override
    public void ready() {
        super.ready();

        if (currentPasswordField.isVisible() && currentPasswordField.isEnabled()) {
            currentPasswordField.requestFocus();
        }
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        super.postValidate(errors);

        String password = passwField.getValue();

        if (errors.isEmpty()) {
            String currentPassword = currentPasswordField.getValue();
            String passwordConfirmation = confirmPasswField.getValue();

            UUID userId = userDs.getItem().getId();

            if (currentPasswordRequired
                    && !userManagementService.checkPassword(userId, passwordEncryption.getPlainHash(currentPassword))) {
                errors.add(currentPasswordField, getMessage("wrongCurrentPassword"));

            } else if (userManagementService.checkPassword(userId, passwordEncryption.getPlainHash(password))) {
                errors.add(passwField, getMessage("currentPasswordWarning"));

            } else if (!ObjectUtils.equals(password, passwordConfirmation)) {
                errors.add(confirmPasswField, getMessage("passwordsDoNotMatch"));

            } else {
                ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
                if (clientConfig.getPasswordPolicyEnabled()) {
                    String regExp = clientConfig.getPasswordPolicyRegExp();
                    if (!password.matches(regExp)) {
                        errors.add(passwField, getMessage("simplePassword"));
                    }
                }
            }
        }

        if (errors.isEmpty()) {
            assignPasswordToUser(password);
        }
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed) {
            UUID userId = userDs.getItem().getId();
            userManagementService.resetRememberMeTokens(Collections.singletonList(userId));

            showNotification(getMessage("passwordChanged"), NotificationType.HUMANIZED);
        }

        return super.postCommit(committed, close);
    }

    protected void assignPasswordToUser(String passw) {
        User user = userDs.getItem();
        String passwordHash = passwordEncryption.getPasswordHash(user.getId(), passw);

        user.setPassword(passwordHash);
        user.setChangePasswordAtNextLogon(false);
    }
}