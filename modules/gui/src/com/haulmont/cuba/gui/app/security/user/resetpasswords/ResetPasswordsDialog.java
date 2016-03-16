/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
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

        generatePasswordsCheckBox.addValueChangeListener(e -> {
            if (Boolean.TRUE.equals(e.getValue())) {
                sendEmailsCheckBox.setEnabled(true);

                updateExpectedResultLabel();
            } else {
                sendEmailsCheckBox.setValue(false);
                sendEmailsCheckBox.setEnabled(false);
            }

            expectedResultLabel.setVisible(getGeneratePasswords());
        });

        sendEmailsCheckBox.addValueChangeListener(e -> updateExpectedResultLabel());
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