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
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.security.entity.User;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

public class UserChangePassw extends AbstractEditor
{
    public UserChangePassw(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Datasource<User> userDs = getDsContext().get("user");

        TextField passwField = getComponent("passw");
        TextField confirmPasswField = getComponent("confirmPassw");

        passwField.addListener(
                new ValueListener()
                {
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        if (StringUtils.isBlank((String) value))
                            userDs.getItem().setPassword(null);
                        else
                            userDs.getItem().setPassword(DigestUtils.md5Hex((String) value));
                    }
                }
        );
        confirmPasswField.addListener(
                new ValueListener()
                {
                    public void valueChanged(Object source, String property, Object prevValue, Object value) {
                        // TODO KK: implement comparison after Bug#3305 is fixed
                    }
                }
        );
    }
}
