/*
 * Copyright (c) 2008-2013 Haulmont. All rights reservelafDefaults.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.plaf.nimbus.MandatoryComboBoxTextFieldPainter;

import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;

/**
 * Performs initialization needed to highlight empty mandatory ("missing value") fields.
 *
 * @author Alexander Budarov
 * @version $Id$
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