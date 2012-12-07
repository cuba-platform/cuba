/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.components.IFrame;
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
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CustomConditionEditDlg extends AbstractCustomConditionEditDlg<JDialog> {

    protected EditDlg impl;

    protected JComponent component;

    public CustomConditionEditDlg(final CustomCondition condition, JComponent component) {
        super(condition);
        this.component = component;
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
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDlg();
            }
        };
        DesktopComponentsHelper.addShortcutAction("escape", impl.getRootPane(), esc, escAction);
        KeyStroke commitKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK, false);
        Action commitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commit()) {
                    closeDlg();
                }
            }
        };
        DesktopComponentsHelper.addShortcutAction("commit", impl.getRootPane(), commitKey, commitAction);
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    protected void showNotification(String msg, IFrame.NotificationType type) {
        DesktopComponentsHelper.getTopLevelFrame(getImpl()).showNotification(msg, type);
    }

    @Override
    protected void closeDlg() {
        impl.dispose();
        DesktopComponentsHelper.getTopLevelFrame(getImpl()).activate();
    }

    private class EditDlg extends JDialog {
        public EditDlg() {
            super(DesktopComponentsHelper.getTopLevelFrame(component));
            setLocationRelativeTo(App.getInstance().getMainFrame());
            setSize(430, 380);
            setResizable(false);
            setTitle(condition.getLocCaption());

            MigLayout layout = new MigLayout("wrap 1");
            setLayout(layout);
            entityAlias = condition.getEntityAlias();
            JLabel eaLab = new JLabel("<html>" + MessageProvider.formatMessage(MESSAGES_PACK, "CustomConditionEditDlg.hintLabel", entityAlias) + "</html>");
            add(eaLab, "wrap");
            MigLayout mainLayout = new MigLayout("wrap 2");
            JPanel mainPanel = new JPanel(mainLayout);
            add(mainPanel);
            if (StringUtils.isBlank(condition.getCaption())) {
                mainPanel.add(DesktopComponentsHelper.unwrap(nameLab));
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

            JLabel typeLab = new JLabel(MessageProvider.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.paramTypeLabel"));
            mainPanel.add(typeLab);

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
                    DesktopComponentsHelper.getTopLevelFrame(EditDlg.this).activate();
                }
            });
        }
    }
}
