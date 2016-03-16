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

package com.haulmont.cuba.desktop.sys.vcl.DatePicker;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

import javax.swing.*;
import java.awt.*;

/**
 */
public class CustomDatePickerUI extends BasicDatePickerUI {
    @Override
    protected JButton createPopupButton() {
        JButton b = new JButton();
        b.setName("popupButton");
        b.setRolloverEnabled(false);
        b.setMargin(new Insets(0, 3, 0, 3));
        App app = App.getInstance();
        if (app != null) {
            b.setIcon(app.getResources().getIcon("/components/datefield/open-button.png"));
        }
        b.setFocusable(false);
        b.setPreferredSize(new Dimension(22, DesktopComponentsHelper.BUTTON_HEIGHT));
        return b;
    }

    @Override
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        ActionMap pickerMap = datePicker.getActionMap();
        pickerMap.remove(JXDatePicker.CANCEL_KEY);
        InputMap pickerInputMap = datePicker.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pickerInputMap.remove(KeyStroke.getKeyStroke("ESCAPE"));
    }
}