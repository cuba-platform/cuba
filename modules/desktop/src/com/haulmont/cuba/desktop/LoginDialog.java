/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.LoginException;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LoginDialog extends JDialog {

    private Connection connection;

    public LoginDialog(Connection connection) {
        this.connection = connection;
        setTitle(MessageProvider.getMessage(AppConfig.getMessagesPack(), "loginWindow.caption", Locale.getDefault()));
        setContentPane(createContentPane());
        setResizable(false);
        pack();
    }

    private Container createContentPane() {
        MigLayout layout = new MigLayout("fillx, insets dialog", "[right][]");
        JPanel panel = new JPanel(layout);

        panel.add(new JLabel(MessageProvider.getMessage(AppConfig.getMessagesPack(), "loginWindow.loginField", Locale.getDefault())));

        final JTextField nameField = new JTextField();
        String defaultName = AppContext.getProperty("cuba.desktop.loginDialogDefaultUser");
        if (!StringUtils.isBlank(defaultName))
            nameField.setText(defaultName);
        panel.add(nameField, "width 150!, wrap");

        panel.add(new JLabel(MessageProvider.getMessage(AppConfig.getMessagesPack(), "loginWindow.passwordField", Locale.getDefault())));

        final JTextField passwordField = new JPasswordField();
        String defaultPassword = AppContext.getProperty("cuba.desktop.loginDialogDefaultPassword");
        if (!StringUtils.isBlank(defaultPassword))
            passwordField.setText(defaultPassword);
        panel.add(passwordField, "width 150!, wrap");

        JButton loginBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "loginWindow.okButton", Locale.getDefault()));
        loginBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String name = nameField.getText();
                            String password = passwordField.getText();
                            connection.login(name, DigestUtils.md5Hex(password), Locale.getDefault());
                            setVisible(false);
                        } catch (LoginException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        panel.add(loginBtn, "span, align center");

        return panel;
    }

}
