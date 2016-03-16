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

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.plaf.nimbus.MandatoryComboBoxTextFieldPainter;

import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;

/**
 * Performs initialization needed to highlight empty mandatory ("missing value") fields.
 *
 */
public class NimbusTheme extends DesktopThemeImpl {

    @Override
    public void init() {
        super.init();

        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
        lafDefaults.put("Nimbus.keepAlternateRowColor", true); // deny SwingX to remove alternate row color
        lafDefaults.put("ComboBox:\"ComboBox.textField\".contentMargins", new InsetsUIResource(0, 6, 0, 6));
        lafDefaults.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", MandatoryComboBoxTextFieldPainter.backgroundEnabledPainter());
        lafDefaults.put("ComboBox:\"ComboBox.textField\"[Disabled].backgroundPainter", MandatoryComboBoxTextFieldPainter.backgroundDisabledPainter());
        lafDefaults.put("ComboBox:\"ComboBox.textField\"[Selected].backgroundPainter", MandatoryComboBoxTextFieldPainter.backgroundSelectedPainter());
    }
}