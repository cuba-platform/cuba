/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.LoginProperties;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.global.LoginException;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LoginDialog extends JDialog {

    protected Log log = LogFactory.getLog(getClass());

    protected Connection connection;
    protected Map<String,Locale> locales;
    protected Messages messages = AppBeans.get(Messages.class);
    protected PasswordEncryption passwordEncryption = AppBeans.get(PasswordEncryption.class);

    protected JTextField nameField;
    protected JTextField passwordField;
    protected JComboBox<String> localeCombo;
    protected JButton loginBtn;
    protected LoginProperties loginProperties;

    public LoginDialog(JFrame owner, Connection connection) {
        super(owner);
        this.connection = connection;
        Configuration configuration = AppBeans.get(Configuration.class);
        this.locales = configuration.getConfig(GlobalConfig.class).getAvailableLocales();

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        DesktopComponentsHelper.getTopLevelFrame(LoginDialog.this).activate();
                    }
                }
        );
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(messages.getMainMessage("loginWindow.caption", Locale.getDefault()));
        setContentPane(createContentPane());
        setResizable(false);
        pack();
    }

    protected Container createContentPane() {
        MigLayout layout = new MigLayout("fillx, insets dialog", "[right][]");
        JPanel panel = new JPanel(layout);

        panel.add(new JLabel(messages.getMainMessage("loginWindow.loginField", Locale.getDefault())));

        nameField = new JTextField();
        passwordField = new JPasswordField();

        String defaultName = AppContext.getProperty("cuba.desktop.loginDialogDefaultUser");
        loginProperties = new LoginProperties();
        String lastLogin = loginProperties.loadLastLogin();
        if (!StringUtils.isBlank(lastLogin)) {
            nameField.setText(lastLogin);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    passwordField.requestFocus();
                }
            });
        } else if (!StringUtils.isBlank(defaultName))
            nameField.setText(defaultName);
        panel.add(nameField, "width 150!, wrap");

        panel.add(new JLabel(messages.getMainMessage("loginWindow.passwordField", Locale.getDefault())));
        String defaultPassword = AppContext.getProperty("cuba.desktop.loginDialogDefaultPassword");
        if (!StringUtils.isBlank(defaultPassword))
            passwordField.setText(defaultPassword);
        panel.add(passwordField, "width 150!, wrap");

        Configuration configuration = AppBeans.get(Configuration.class);

        localeCombo = new JComboBox<>();
        initLocales(localeCombo);
        if (configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible()) {
            panel.add(new JLabel(messages.getMainMessage("loginWindow.localesSelect")));
            panel.add(localeCombo, "width 150!, wrap");
        }

        loginBtn = new JButton(messages.getMainMessage("loginWindow.okButton", Locale.getDefault()));
        loginBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
        loginBtn.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        doLogin();
                    }
                }
        );
        DesktopComponentsHelper.adjustSize(loginBtn);
        panel.add(loginBtn, "span, align center");

        getRootPane().setDefaultButton(loginBtn);

        return panel;
    }

    protected void doLogin() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String selectedItem = (String) localeCombo.getSelectedItem();
        Locale locale = locales.get(selectedItem);
        try {
            connection.login(name, passwordEncryption.getPlainHash(password), locale);
            setVisible(false);
            loginProperties.saveLogin(name);
            DesktopComponentsHelper.getTopLevelFrame(this).activate();
        } catch (LoginException ex) {
            log.info("Login failed: " + ex.toString());
            String caption = messages.getMainMessage("loginWindow.loginFailed", locale);
            App.getInstance().getMainFrame().showNotification(
                    caption,
                    ex.getMessage(),
                    IFrame.NotificationType.ERROR
            );
        }
    }

    protected void initLocales(JComboBox<String> localeCombo) {
        String currLocale = messages.getTools().localeToString(Locale.getDefault());
        String selected = null;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            localeCombo.addItem(entry.getKey());
            if (messages.getTools().localeToString(entry.getValue()).equals(currLocale))
                selected = entry.getKey();
        }
        if (selected == null)
            selected = locales.keySet().iterator().next();

        localeCombo.setSelectedItem(selected);
    }

    public void open() {
        DesktopComponentsHelper.getTopLevelFrame(this).deactivate(null);
        setVisible(true);
    }
}
