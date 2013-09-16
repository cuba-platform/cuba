/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
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
public class CustomOperationEditor extends OperationEditor implements HasAction<JComponent> {
    public CustomOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
        final JButton btn = new JButton();
        DesktopComponentsHelper.adjustSize(btn);
        Messages messages = AppBeans.get(Messages.NAME);
        btn.setText(messages.getMainMessage("actions.Edit"));
        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        btn.setEnabled(sessionSource.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAction(btn);
            }
        });
        impl = btn;
    }

    @Override
    public void doAction(JComponent component) {
        CustomConditionEditDlg dlg = new CustomConditionEditDlg((CustomCondition) condition, component);
        DesktopComponentsHelper.getTopLevelFrame(component).deactivate(null);
        dlg.getImpl().setVisible(true);
    }
}
