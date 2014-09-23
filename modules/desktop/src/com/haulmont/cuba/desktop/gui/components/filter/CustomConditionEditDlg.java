/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.DialogWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionEditDlg;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.components.filter.ParamType;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CustomConditionEditDlg extends AbstractCustomConditionEditDlg<JDialog> {

    protected EditDlg impl;

    protected JComponent component;
    protected TopLevelFrame topLevelFrame;

    public CustomConditionEditDlg(final CustomCondition condition, JComponent component) {
        super(condition);
        this.component = component;
        this.topLevelFrame = DesktopComponentsHelper.getTopLevelFrame(component);

        DialogWindow lastDialogWindow = topLevelFrame.getWindowManager().getLastDialogWindow();
        if (lastDialogWindow == null) {
            topLevelFrame.deactivate(null);
        } else {
            lastDialogWindow.disableWindow(null);
        }
    }

    @Override
    public JDialog getImpl() {
        if (impl == null) {
            impl = new EditDlg();
            initShortcuts();
        }
        return impl;
    }

    protected void initShortcuts() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        KeyCombination close = KeyCombination.create(clientConfig.getCloseShortcut());
        KeyCombination commit = KeyCombination.create(clientConfig.getCommitShortcut());

        Action escAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDlg();
            }
        };
        DesktopComponentsHelper.addShortcutAction("close", impl.getRootPane(),
                DesktopComponentsHelper.convertKeyCombination(close), escAction);

        Action commitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commit()) {
                    closeDlg();
                }
            }
        };
        DesktopComponentsHelper.addShortcutAction("commit", impl.getRootPane(),
                DesktopComponentsHelper.convertKeyCombination(commit), commitAction);
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    protected void showNotification(String msg, IFrame.NotificationType type) {
        topLevelFrame.showNotification(msg, type);
    }

    @Override
    protected void closeDlg() {
        impl.setVisible(false);
        DesktopWindowManager wm = topLevelFrame.getWindowManager();

        DialogWindow lastDialogWindow = wm.getLastDialogWindow();
        if (lastDialogWindow == null) {
            topLevelFrame.activate();
        } else {
            lastDialogWindow.enableWindow();
        }
    }

    private class EditDlg extends JDialog {

        public EditDlg() {
            super(topLevelFrame);
            setLocationRelativeTo(App.getInstance().getMainFrame());
            setSize(430, 380);
            setResizable(false);
            setTitle(condition.getLocCaption());

            Messages messages = AppBeans.get(Messages.NAME);

            MigLayout layout = new MigLayout("wrap 1");
            setLayout(layout);
            entityAlias = condition.getEntityAlias();
            JLabel eaLab = new JLabel("<html>" +
                    messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.hintLabel") + "</html>");
            add(eaLab, "wrap");
            MigLayout mainLayout = new MigLayout("wrap 2");
            JPanel mainPanel = new JPanel(mainLayout);
            add(mainPanel);
            if (StringUtils.isBlank(condition.getCaption())) {
                mainPanel.add(DesktopComponentsHelper.unwrap(nameLab), new CC().alignX("right"));
                JTextField nameTextField = (JTextField) DesktopComponentsHelper.unwrap(nameText);
                Dimension nameSize = nameTextField.getSize();
                nameSize.width = COMPONENT_WIDTH;
                nameTextField.setSize(nameSize);
                nameTextField.setPreferredSize(nameSize);
                mainPanel.add(nameTextField);
            } else {
                joinText.requestFocus();
            }

            mainPanel.add(DesktopComponentsHelper.unwrap(joinLab), new CC().alignX("right"));
            mainPanel.add(DesktopComponentsHelper.unwrap(joinText),
                    new CC().width(COMPONENT_WIDTH + ":" + COMPONENT_WIDTH + ":" + COMPONENT_WIDTH));

            mainPanel.add(DesktopComponentsHelper.unwrap(whereLab), new CC().alignX("right"));
            mainPanel.add(DesktopComponentsHelper.unwrap(whereText),
                    new CC().width(COMPONENT_WIDTH + ":" + COMPONENT_WIDTH + ":" + COMPONENT_WIDTH));

            mainPanel.add(DesktopComponentsHelper.unwrap(typeLab), new CC().alignX("right"));

            JPanel typePanel = new JPanel(new MigLayout(new LC().insetsAll("0")));
            Dimension size = typePanel.getSize();
            size.width = COMPONENT_WIDTH;
            typePanel.setSize(size);
            JComboBox types = (JComboBox) DesktopComponentsHelper.unwrap(typeSelect);
            DesktopComponentsHelper.adjustSize(types);
            typePanel.add(types);

            typePanel.add(DesktopComponentsHelper.unwrap(typeCheckBox));

            mainPanel.add(typePanel);

            mainPanel.add(DesktopComponentsHelper.unwrap(entityLab), new CC().alignX("right"));

            JComboBox entities = (JComboBox) DesktopComponentsHelper.unwrap(entitySelect);
            DesktopComponentsHelper.adjustSize(entities);
            size = entities.getSize();
            size.width = COMPONENT_WIDTH;
            entities.setPreferredSize(size);

            mainPanel.add(entities, new CC().width(COMPONENT_WIDTH + ":" + COMPONENT_WIDTH + ":" + COMPONENT_WIDTH));

            mainPanel.add(DesktopComponentsHelper.unwrap(entityParamWhereLab), new CC().alignX("right"));

            JTextArea entityParamWhereArea = (JTextArea) DesktopComponentsHelper.unwrap(entityParamWhereText);
            size = entityParamWhereArea.getSize();
            size.width = COMPONENT_WIDTH;
            entityParamWhereArea.setSize(size);
            entityParamWhereArea.setMinimumSize(size);
            entityParamWhereArea.setPreferredSize(size);
            mainPanel.add(entityParamWhereArea);

            mainPanel.add(DesktopComponentsHelper.unwrap(entityParamViewLab), new CC().alignX("right"));

            JTextField entityParamView = (JTextField) DesktopComponentsHelper.unwrap(entityParamViewText);

            size = entityParamView.getSize();
            size.width = COMPONENT_WIDTH;
            entityParamView.setSize(size);
            entityParamView.setPreferredSize(size);
            entityParamView.setText(condition.getEntityParamView());
            entityParamView.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));

            mainPanel.add(entityParamView);

            JPanel buttonsPanel = new JPanel(new MigLayout());
            JButton okButton = (JButton) DesktopComponentsHelper.unwrap(btnOk);
            DesktopComponentsHelper.adjustSize(okButton);
            buttonsPanel.add(okButton);

            JButton cancelButton = (JButton) DesktopComponentsHelper.unwrap(btnCancel);
            DesktopComponentsHelper.adjustSize(cancelButton);
            buttonsPanel.add(cancelButton);
            add(buttonsPanel);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    closeDlg();
                }
            });
        }
    }
}