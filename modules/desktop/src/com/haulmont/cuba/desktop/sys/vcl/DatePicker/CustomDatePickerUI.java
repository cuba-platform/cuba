/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl.DatePicker;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

import javax.swing.*;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CustomDatePickerUI extends BasicDatePickerUI {
    protected JButton createPopupButton() {
        JButton b = new JButton();
        b.setName("popupButton");
        b.setRolloverEnabled(false);
        b.setMargin(new Insets(0, 3, 0, 3));
        b.setIcon(App.getInstance().getResources().getIcon("datefield/open-button.png"));
        b.setFocusable(false);
        b.setPreferredSize(new Dimension(22, DesktopComponentsHelper.BUTTON_HEIGHT));
        return b;
    }
}
