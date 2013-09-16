/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.user.changepassw;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.Map;

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
    protected Datasource<User> userDs;

    @Inject
    protected Configuration configuration;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected UserManagementService userManagementService;

    protected boolean cancelEnabled = true;

    @Override
    protected void postInit() {
        super.postInit();

        if (!cancelEnabled)
            getComponent("windowActions.windowClose").setVisible(false);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        Boolean cancelEnabled = (Boolean) params.get("cancelEnabled");
        if (Boolean.FALSE.equals(cancelEnabled)) {
            this.cancelEnabled = false;
        }
    }

    @Override
    public void commitAndClose() {
        String passw = passwField.getValue();
        String confPassw = confirmPasswField.getValue();
        if (StringUtils.isBlank(passw) || StringUtils.isBlank(confPassw)) {
            showNotification(getMessage("emptyPassword"), NotificationType.WARNING);
        } else if (userManagementService.checkEqualsOfNewAndOldPassword(
                userDs.getItem().getId(), passwordEncryption.getPlainHash(passw))) {
            // old password equals to new password
            showNotification(getMessage("oldPassword"), NotificationType.WARNING);
        } else if (ObjectUtils.equals(passw, confPassw)) {
            ClientConfig passwordPolicyConfig = configuration.getConfig(ClientConfig.class);
            if (passwordPolicyConfig.getPasswordPolicyEnabled()) {
                String regExp = passwordPolicyConfig.getPasswordPolicyRegExp();
                if (passw.matches(regExp)) {
                    assignPasswordToUser(passw);
                    super.commitAndClose();
                } else {
                    showNotification(getMessage("simplePassword"), NotificationType.WARNING);
                }
            } else {
                assignPasswordToUser(passw);
                super.commitAndClose();
            }
        } else {
            showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
        }
    }

    private void assignPasswordToUser(String passw) {
        User user = userDs.getItem();
        String passwordHash = passwordEncryption.getPasswordHash(user.getId(), passw);

        user.setPassword(passwordHash);
        user.setChangePasswordAtNextLogon(false);
    }
}