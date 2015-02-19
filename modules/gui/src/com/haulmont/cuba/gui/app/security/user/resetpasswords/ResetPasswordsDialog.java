/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class ResetPasswordsDialog extends AbstractWindow {

    @Inject
    protected CheckBox sendEmailsCheckBox;

    @Inject
    protected CheckBox generatePasswordsCheckBox;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setResizable(false).setWidthAuto();

        generatePasswordsCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (Boolean.TRUE.equals(generatePasswordsCheckBox.getValue())) {
                    sendEmailsCheckBox.setEnabled(true);
                } else {
                    sendEmailsCheckBox.setValue(false);
                    sendEmailsCheckBox.setEnabled(false);
                }
            }
        });
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
        return Boolean.TRUE.equals(sendEmailsCheckBox.getValue()) && getGeneratePasswords();
    }

    public boolean getGeneratePasswords() {
        return Boolean.TRUE.equals(generatePasswordsCheckBox.getValue());
    }
}