/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.components.filter.HasAction;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
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
public class RuntimePropOperationEditor extends OperationEditor implements HasAction<JComponent> {

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
                doAction(btn);
            }
        });
        impl = btn;
    }

    private void setCaption(JButton btn) {
        String caption = condition.getOperationCaption();
        if (StringUtils.isEmpty(caption)) {
            Messages messages = AppBeans.get(Messages.NAME);
            caption = messages.getMainMessage("actions.Edit");
        }
        btn.setText(caption);
    }

    @Override
    public void doAction(JComponent component) {
        final RuntimePropConditionEditDlg dlg = new RuntimePropConditionEditDlg((RuntimePropCondition) condition, component);
        dlg.getImpl().addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                setCaption(btn);
            }
        });
        JDialog dlgImpl = dlg.getImpl();
        DesktopComponentsHelper.getTopLevelFrame(component).deactivate(null);
        dlgImpl.setVisible(true);
    }
}
