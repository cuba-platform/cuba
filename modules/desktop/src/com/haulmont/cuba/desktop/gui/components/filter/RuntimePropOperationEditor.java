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
import com.haulmont.cuba.gui.components.filter.HasAction;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropOperationEditor extends OperationEditor implements HasAction {

    private JButton btn;

    public RuntimePropOperationEditor(final AbstractCondition condition) {
        super(condition);
    }

    private JButton createButton() {
        return new JButton();
    }

    @Override
    protected void createEditor() {
        btn = createButton();
        DesktopComponentsHelper.adjustSize(btn);
        setCaption(btn);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAction();
            }
        });
        impl = btn;
    }

    private void setCaption(JButton btn) {
        String caption = condition.getOperationCaption();
        if (StringUtils.isEmpty(caption)) {
            caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Edit");
        }
        btn.setText(caption);
    }

    @Override
    public void doAction() {
        final RuntimePropConditionEditDlg dlg = new RuntimePropConditionEditDlg((RuntimePropCondition) condition);
        dlg.getImpl().addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                setCaption(btn);
            }
        });
        JDialog dlgImpl = dlg.getImpl();
        App.getInstance().disable(null);
        dlgImpl.setVisible(true);
    }
}
