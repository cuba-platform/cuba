/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.user.changepassw;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class UserChangePassw extends AbstractEditor
{
    private TextField passwField;
    private TextField confirmPasswField;
    private Datasource<User> userDs;

    public void init(Map<String, Object> params) {
        userDs = getDsContext().get("user");

        passwField = getComponent("passw");
        confirmPasswField = getComponent("confirmPassw");
    }

    public void commitAndClose() {
        String passw = passwField.getValue();
        String confPassw = confirmPasswField.getValue();
        if (StringUtils.isBlank(passw) || StringUtils.isBlank(confPassw)) {
            showNotification(getMessage("emptyPassword"), NotificationType.WARNING);
        } else if (ObjectUtils.equals(passw, confPassw)) {
            ClientConfig passwordPolicyConfig = ConfigProvider.getConfig(ClientConfig.class);
            if (passwordPolicyConfig.getPasswordPolicyEnabled()) {
                String regExp = passwordPolicyConfig.getPasswordPolicyRegExp();
                if (passw.matches(regExp)) {
                    userDs.getItem().setPassword(DigestUtils.md5Hex(passw));
                    super.commitAndClose();
                } else {
                    showNotification(getMessage("simplePassword"), NotificationType.WARNING);
                }
            } else {
                userDs.getItem().setPassword(DigestUtils.md5Hex(passw));
                super.commitAndClose();
            }
        } else {
            showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
        }
    }
}
