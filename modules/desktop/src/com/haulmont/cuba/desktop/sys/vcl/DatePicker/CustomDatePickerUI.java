/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl.DatePicker;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CustomDatePickerUI extends BasicDatePickerUI {
    @Override
    protected JButton createPopupButton() {
        JButton b = new JButton();
        b.setName("popupButton");
        b.setRolloverEnabled(false);
        b.setMargin(new Insets(0, 3, 0, 3));
        b.setIcon(App.getInstance().getResources().getIcon("/components/datefield/open-button.png"));
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