/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Window;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class ResetPasswordsDialog extends AbstractWindow {

    @Inject
    protected CheckBox sendEmailsCheckBox;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setResizable(false);
    }

    @SuppressWarnings("unused")
    public void ok() {
        close(Window.COMMIT_ACTION_ID);
    }

    @SuppressWarnings("unused")
    public void cancel() {
        close(Window.CLOSE_ACTION_ID);
    }

    public boolean getSendEmails() {
        return Boolean.TRUE.equals(sendEmailsCheckBox.getValue());
    }
}