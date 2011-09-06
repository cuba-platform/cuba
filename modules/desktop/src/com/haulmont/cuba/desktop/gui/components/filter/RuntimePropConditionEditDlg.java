/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.AbstractRuntimePropConditionEditDlg;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropConditionEditDlg extends AbstractRuntimePropConditionEditDlg<JDialog> {

    private Editor impl;
    private static final int FIELD_WIDTH = 250;

    public RuntimePropConditionEditDlg(final RuntimePropCondition condition) {
        super(condition);
    }

    @Override
    protected void closeDlg() {
        impl.dispose();
        App.getInstance().enable();
    }

    @Override
    public JDialog getImpl() {
        if (impl == null)
            impl = new Editor();
        return impl;
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    protected void showNotification(String msg, IFrame.NotificationType type) {
        App.getInstance().showNotificationPopup(msg, type);
    }

    protected class Editor extends JDialog {
        public Editor() {
            super(App.getInstance().getMainFrame());
            setLocationRelativeTo(App.getInstance().getMainFrame());
            setTitle(condition.getLocCaption());
            setSize(350, 230);
            setResizable(false);
            MigLayout layout = new MigLayout();
            setLayout(layout);
            JPanel mainPanel = new JPanel(new MigLayout("wrap 2"));
            add(mainPanel, "wrap");

            mainPanel.add(DesktopComponentsHelper.unwrap(categoryLabel));
            JComboBox categories = (JComboBox) DesktopComponentsHelper.unwrap(categorySelect);
            DesktopComponentsHelper.adjustSize(categories);
            Dimension size = categories.getSize();
            size.width = FIELD_WIDTH;
            categories.setPreferredSize(size);

            mainPanel.add(categories);
            mainPanel.add(DesktopComponentsHelper.unwrap(attributeLabel));
            JComboBox attributes = (JComboBox) DesktopComponentsHelper.unwrap(attributeSelect);
            DesktopComponentsHelper.adjustSize(attributes);
            size = attributes.getSize();
            size.width = FIELD_WIDTH;
            attributes.setPreferredSize(size);

            mainPanel.add(attributes);
            mainPanel.add(DesktopComponentsHelper.unwrap(operationLabel));
            JComboBox operations = (JComboBox) DesktopComponentsHelper.unwrap(operationSelect);
            DesktopComponentsHelper.adjustSize(operations);
            size = operations.getSize();
            size.width = FIELD_WIDTH;
            operations.setPreferredSize(size);

            mainPanel.add(operations);

            JPanel buttonsPanel = new JPanel(new MigLayout());
            add(buttonsPanel);
            JButton okButton = (JButton) DesktopComponentsHelper.unwrap(btnOk);
            DesktopComponentsHelper.adjustSize(okButton);
            buttonsPanel.add(okButton);
            JButton cancelButton = (JButton) DesktopComponentsHelper.unwrap(btnCancel);
            DesktopComponentsHelper.adjustSize(cancelButton);
            buttonsPanel.add(cancelButton);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    App.getInstance().enable();
                }

                @Override
                public void windowIconified(WindowEvent e) {
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                }

                @Override
                public void windowActivated(WindowEvent e) {
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                }
            });
        }
    }
}
