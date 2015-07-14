/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.annotation.Nullable;
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

    @Inject
    protected Label expectedResultLabel;

    @Inject
    protected ThemeConstants theme;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams()
                .setWidth(theme.getInt("cuba.gui.ResetPasswordsDialog.width"))
                .setResizable(false);

        generatePasswordsCheckBox.addListener(new ValueListener<CheckBox>() {
            @Override
            public void valueChanged(CheckBox source, String property, Object prevValue, Object value) {
                if (Boolean.TRUE.equals(generatePasswordsCheckBox.getValue())) {
                    sendEmailsCheckBox.setEnabled(true);

                    updateExpectedResultLabel();
                } else {
                    sendEmailsCheckBox.setValue(false);
                    sendEmailsCheckBox.setEnabled(false);
                }

                expectedResultLabel.setVisible(getGeneratePasswords());
            }
        });

        sendEmailsCheckBox.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                updateExpectedResultLabel();
            }
        });
    }

    protected void updateExpectedResultLabel() {
        if (Boolean.TRUE.equals(sendEmailsCheckBox.getValue())) {
            expectedResultLabel.setValue(getMessage("sendPasswords"));
        } else {
            expectedResultLabel.setValue(getMessage("printPasswords"));
        }
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