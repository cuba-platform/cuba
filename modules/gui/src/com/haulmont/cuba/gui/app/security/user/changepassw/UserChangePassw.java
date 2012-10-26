/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.user.changepassw;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Encryption;
import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $id$
 */
public class UserChangePassw extends AbstractEditor {
    @Inject
    protected TextField passwField;

    @Inject
    protected TextField confirmPasswField;

    @Inject
    protected Datasource<User> userDs;

    @Inject
    protected Configuration configuration;

    @Inject
    protected Encryption encryption;

    @Override
    public void commitAndClose() {
        String passw = passwField.getValue();
        String confPassw = confirmPasswField.getValue();
        if (StringUtils.isBlank(passw) || StringUtils.isBlank(confPassw)) {
            showNotification(getMessage("emptyPassword"), NotificationType.WARNING);
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
        HashDescriptor hDesc = encryption.getPasswordHash(passw);

        userDs.getItem().setPassword(hDesc.getHash());
        userDs.getItem().setSalt(hDesc.getSalt());
    }
}
