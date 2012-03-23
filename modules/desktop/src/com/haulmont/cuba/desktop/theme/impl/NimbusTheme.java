/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.plaf.nimbus.MandatoryComboBoxComboBoxTextFieldPainter;

import javax.swing.*;

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
        lafDefaults.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", new MandatoryComboBoxComboBoxTextFieldPainter());
    }
}
