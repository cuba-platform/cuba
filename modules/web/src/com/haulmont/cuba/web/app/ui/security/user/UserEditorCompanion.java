/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.user;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.AbstractCompanion;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.web.WebConfig;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class UserEditorCompanion extends AbstractCompanion implements UserEditor.Companion {

    public UserEditorCompanion(AbstractFrame frame) {
        super(frame);
    }

    public void initPasswordField(TextField passwordField) {
        passwordField.setRequired(!ConfigProvider.getConfig(WebConfig.class).getUseActiveDirectory());
    }
}
