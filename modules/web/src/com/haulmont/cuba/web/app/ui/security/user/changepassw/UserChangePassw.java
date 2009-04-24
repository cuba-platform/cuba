/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.2009 15:47:50
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.changepassw;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
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

    public UserChangePassw(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        userDs = getDsContext().get("user");

        passwField = getComponent("passw");
        confirmPasswField = getComponent("confirmPassw");
    }

    public boolean commit() {
        String passw = passwField.getValue();
        String confPassw = confirmPasswField.getValue();
        if (ObjectUtils.equals(passw, confPassw)) {
            if (StringUtils.isEmpty(passw))
                userDs.getItem().setPassword(null);
            else
                userDs.getItem().setPassword(DigestUtils.md5Hex(passw));
            return super.commit();
        } else {
            showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
            return false;
        }
    }
}
