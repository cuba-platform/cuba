/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.security.global.LoginException;

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
        setTitle("Login");
        setContentPane(createContentPane());
        setSize(300, 200);
//        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private Container createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(createButtonsPane(), BorderLayout.SOUTH);
        return contentPane;
    }

    private JComponent createButtonsPane() {
        JPanel buttonsPane = new JPanel(new FlowLayout());

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            connection.login("", "", Locale.getDefault());
                            setVisible(false);
                        } catch (LoginException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        buttonsPane.add(loginBtn);

        return buttonsPane;
    }
}
