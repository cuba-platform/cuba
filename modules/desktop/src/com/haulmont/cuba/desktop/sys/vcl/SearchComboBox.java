/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import com.haulmont.cuba.desktop.plaf.nimbus.SearchComboBoxPainter;

import javax.swing.*;
import javax.swing.plaf.synth.SynthComboBoxUI;
import java.awt.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class SearchComboBox extends ExtendedComboBox {

    public SearchComboBox() {
        UIDefaults laf = new UIDefaults();

        laf.put("ComboBox[Disabled+Editable].backgroundPainter", SearchComboBoxPainter.backgroundDisabledEditablePainter());
        laf.put("ComboBox[Editable+Enabled].backgroundPainter", SearchComboBoxPainter.backgroundEnabledEditablePainter());
        laf.put("ComboBox[Editable+Focused].backgroundPainter", SearchComboBoxPainter.backgroundEditableFocusedPainter());
        laf.put("ComboBox[Editable+MouseOver].backgroundPainter", SearchComboBoxPainter.backgroundEditableMouseOverPainter());
        laf.put("ComboBox[Editable+Pressed].backgroundPainter", SearchComboBoxPainter.backgroundEditablePressedPainter());

        putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
        putClientProperty("Nimbus.Overrides", laf);

        SwingUtilities.updateComponentTreeUI(this);

        setUI(new SynthComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        setButtonVisible(false);
    }

    public void showSearchPopup() {
        super.setPopupVisible(true);
    }
}