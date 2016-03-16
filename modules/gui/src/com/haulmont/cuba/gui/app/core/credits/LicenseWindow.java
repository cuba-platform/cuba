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

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
 */
public class LicenseWindow extends AbstractWindow {

    @Inject
    protected TextArea licenseTextArea;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams()
                .setWidth(themeConstants.getInt("cuba.gui.LicenseWindow.width"))
                .setHeight(themeConstants.getInt("cuba.gui.LicenseWindow.height"))
                .setResizable(false);

        String licenseText = (String) params.get("licenseText");
        if (licenseText != null) {
            licenseTextArea.setValue(licenseText);
            licenseTextArea.setEditable(false);
            licenseTextArea.setCursorPosition(0);
        }
    }
}