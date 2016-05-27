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

package com.haulmont.cuba.desktop.sys.vcl;

import com.haulmont.cuba.desktop.plaf.nimbus.SearchComboBoxPainter;

import javax.swing.*;
import javax.swing.plaf.synth.SynthComboBoxUI;
import java.awt.*;

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
        setHideButton(true);
    }

    public void showSearchPopup() {
        super.setPopupVisible(true);
    }

    public void hideSearchPopup() {
        super.setPopupVisible(false);
    }
}