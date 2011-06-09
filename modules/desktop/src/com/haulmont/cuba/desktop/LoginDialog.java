/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
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
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LoginDialog extends JDialog {

    private Connection connection;
    private Map<String,Locale> locales;

    public LoginDialog(Connection connection) {
        this.connection = connection;
        locales = ConfigProvider.getConfig(GlobalConfig.class).getAvailableLocales();

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

        panel.add(new JLabel(MessageProvider.getMessage(AppConfig.getMessagesPack(), "loginWindow.localesSelect", Locale.getDefault())));

        final JComboBox localeCombo = new JComboBox();
        initLocales(localeCombo);
        panel.add(localeCombo, "width 150!, wrap");

        JButton loginBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "loginWindow.okButton", Locale.getDefault()));
        loginBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
        loginBtn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String name = nameField.getText();
                            String password = passwordField.getText();
                            Locale locale = locales.get((String) localeCombo.getSelectedItem());
                            connection.login(name, DigestUtils.md5Hex(password), locale);
                            setVisible(false);
                        } catch (LoginException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        DesktopComponentsHelper.adjustSize(loginBtn);
        panel.add(loginBtn, "span, align center");

        getRootPane().setDefaultButton(loginBtn);

        return panel;
    }

    protected void initLocales(JComboBox localeCombo) {
        Locale loc = new Locale(Locale.getDefault().getLanguage());
        String selected = null;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localeCombo.addItem(entry.getKey());
            if (entry.getValue().getLanguage().equals(loc.getLanguage()))
                selected = entry.getKey();
        }
        if (selected == null)
            selected = locales.keySet().iterator().next();

        localeCombo.setSelectedItem(selected);
    }

}
