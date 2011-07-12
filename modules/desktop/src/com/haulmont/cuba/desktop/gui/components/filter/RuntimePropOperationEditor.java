/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropOperationEditor extends OperationEditor {
    public RuntimePropOperationEditor(final AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
        JButton btn = new JButton();
        DesktopComponentsHelper.adjustSize(btn);
        btn.setText(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Edit"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final RuntimePropConditionEditDlg dlg = new RuntimePropConditionEditDlg((RuntimePropCondition) condition);
                JDialog dlgImpl = dlg.getImpl();
                App.getInstance().disable(null);
                dlgImpl.setVisible(true);
            }
        });
        impl = btn;
    }
}
