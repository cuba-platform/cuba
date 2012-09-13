/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.filter.HasAction;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CustomOperationEditor extends OperationEditor implements HasAction {
    public CustomOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
        final JButton btn = new JButton();
        DesktopComponentsHelper.adjustSize(btn);
        btn.setText(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Edit"));
        btn.setEnabled(UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAction();
            }
        });
        impl = btn;
    }

    @Override
    public void doAction() {
        CustomConditionEditDlg dlg = new CustomConditionEditDlg((CustomCondition) condition);
        DesktopComponentsHelper.getTopLevelFrame(getImpl()).deactivate(null);
        dlg.getImpl().setVisible(true);
    }
}
