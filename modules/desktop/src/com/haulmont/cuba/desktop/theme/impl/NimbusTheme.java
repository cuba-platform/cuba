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

        lafDefaults.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", new MandatoryComboBoxComboBoxTextFieldPainter());

        /*// TextField
        lafDefaults.put("TextField.States", "Enabled, Disabled, Focused, Selected, MissingValue, Focused");
        lafDefaults.put("TextField.MissingValue", new MissingValueState());
        lafDefaults.put("TextField[Enabled+MissingValue].borderPainter", new MandatoryTextFieldPainter());
        inherit(lafDefaults, "TextField[Focused+MissingValue].borderPainter", "TextField[Focused].borderPainter");
        inherit(lafDefaults, "TextField[Disabled+MissingValue].borderPainter", "TextField[Disabled].borderPainter");

        // ComboBox
        lafDefaults.put("ComboBox.States", "Disabled, Editable, Enabled, MouseOver, Pressed, Selected, Focused, MissingValue");
        lafDefaults.put("ComboBox.MissingValue", new MissingValueState());
        lafDefaults.put("ComboBox[Editable+Enabled+MissingValue].backgroundPainter", new MandatoryComboBoxPainter());
        inherit(lafDefaults, "ComboBox[Editable+Focused+MissingValue].backgroundPainter", "ComboBox[Editable+Focused].backgroundPainter");

        // FormattedTextField
        lafDefaults.put("FormattedTextField.States", "Disabled, Enabled, Focused, Selected, MissingValue");
        lafDefaults.put("FormattedTextField.MissingValue", new MissingValueState());
        lafDefaults.put("FormattedTextField[Enabled+MissingValue].borderPainter", new MandatoryFormattedTextFieldPainter());
        inherit(lafDefaults, "FormattedTextField[Focused+MissingValue].borderPainter", "FormattedTextField[Focused].borderPainter");
        inherit(lafDefaults, "FormattedTextField[Disabled+MissingValue].borderPainter", "FormattedTextField[Disabled].borderPainter");

        // PasswordField
        lafDefaults.put("PasswordField.States", "Disabled, Enabled, Focused, Selected, MissingValue");
        lafDefaults.put("PasswordField.MissingValue", new MissingValueState());
        lafDefaults.put("PasswordField[Enabled+MissingValue].borderPainter", new MandatoryPasswordFieldPainter());
        inherit(lafDefaults, "PasswordField[Focused+MissingValue].borderPainter", "PasswordField[Focused].borderPainter");
        inherit(lafDefaults, "PasswordField[Disabled+MissingValue].borderPainter", "PasswordField[Disabled].borderPainter");

        // ScrollPane
        lafDefaults.put("ScrollPane.States", "Enabled, Focused, MissingValue");
        lafDefaults.put("ScrollPane.MissingValue", new MissingValueState());
        lafDefaults.put("ScrollPane[Enabled+MissingValue].borderPainter", new MandatoryScrollPanePainter());
        inherit(lafDefaults, "ScrollPane[Enabled+Focused+MissingValue].borderPainter", "ScrollPane[Enabled+Focused].borderPainter");*/
    }

    /*protected void inherit(UIDefaults lafDefaults, String style, String from) {
        lafDefaults.put(style, lafDefaults.get(from));
    }

    public static class MissingValueState extends com.sun.java.swing.plaf.nimbus.State<JComponent> {

        public MissingValueState() {
            super("MissingValue");
        }

        @Override
        protected boolean isInState(JComponent c) {
            return Boolean.TRUE.equals(c.getClientProperty(DesktopAbstractField.MISSING_VALUE_STATE));
        }
    }*/
}
