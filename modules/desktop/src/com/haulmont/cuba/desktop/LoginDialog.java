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
    protected Locale resolvedLocale;
    protected Map<String,Locale> locales;
    protected Messages messages = AppBeans.get(Messages.NAME);
    protected PasswordEncryption passwordEncryption = AppBeans.get(PasswordEncryption.NAME);

    protected JTextField nameField;
    protected JTextField passwordField;
    protected JComboBox<String> localeCombo;
    protected JButton loginBtn;
    protected LoginProperties loginProperties;

    public LoginDialog(JFrame owner, Connection connection) {
        super(owner);
        this.connection = connection;
        this.loginProperties = new LoginProperties();
        Configuration configuration = AppBeans.get(Configuration.NAME);
        this.locales = configuration.getConfig(GlobalConfig.class).getAvailableLocales();
        resolvedLocale = resolveLocale();
        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        DesktopComponentsHelper.getTopLevelFrame(LoginDialog.this).activate();
                    }
                }
        );
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(messages.getMainMessage("loginWindow.caption", resolvedLocale));
        setContentPane(createContentPane());
        setResizable(false);
        pack();
    }

    protected Container createContentPane() {
        MigLayout layout = new MigLayout("fillx, insets dialog", "[right][]");
        JPanel panel = new JPanel(layout);

        panel.add(new JLabel(messages.getMainMessage("loginWindow.loginField", resolvedLocale)));

        nameField = new JTextField();
        passwordField = new JPasswordField();

        String defaultName = AppContext.getProperty("cuba.desktop.loginDialogDefaultUser");
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

        panel.add(new JLabel(messages.getMainMessage("loginWindow.passwordField", resolvedLocale)));
        String defaultPassword = AppContext.getProperty("cuba.desktop.loginDialogDefaultPassword");
        if (!StringUtils.isBlank(defaultPassword))
            passwordField.setText(defaultPassword);
        panel.add(passwordField, "width 150!, wrap");

        Configuration configuration = AppBeans.get(Configuration.NAME);

        localeCombo = new JComboBox<>();
        initLocales(localeCombo);
        if (configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible()) {
            panel.add(new JLabel(messages.getMainMessage("loginWindow.localesSelect")));
            panel.add(localeCombo, "width 150!, wrap");
        }

        loginBtn = new JButton(messages.getMainMessage("loginWindow.okButton", resolvedLocale));
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

        if (App.getInstance().isTestMode()) {
            panel.setName("contentPane");
            nameField.setName("nameField");
            passwordField.setName("passwordField");
            loginBtn.setName("loginBtn");
            localeCombo.setName("localeCombo");
        }

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
            loginProperties.save(name, messages.getTools().localeToString(locale));
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
        String currLocale = loginProperties.loadLastLocale();
        if (StringUtils.isBlank(currLocale)) {
            currLocale = messages.getTools().localeToString(resolvedLocale);
        }
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

    protected Locale resolveLocale() {
        Locale appLocale;
        String lastLocale = this.loginProperties.loadLastLocale();
        if (StringUtils.isNotEmpty(lastLocale)) {
            String[] locParts = lastLocale.split("_");
            if (locParts.length == 1)
                appLocale = new Locale(locParts[0]);
            else
                appLocale = new Locale(locParts[0], locParts[1]);
        } else {
            appLocale = Locale.getDefault();
        }

        for (Locale locale : locales.values()) {
            if (locale.equals(appLocale)) {
                return locale;
            }
        }

        // if not found and application locale contains country, try to match by language only
        if (StringUtils.isNotEmpty(appLocale.getCountry())) {
            Locale languageTagLocale = Locale.forLanguageTag(appLocale.getLanguage());
            for (Locale locale : locales.values()) {
                if (Locale.forLanguageTag(locale.getLanguage()).equals(languageTagLocale)) {
                    return locale;
                }
            }
        }

        // return first locale set in the cuba.availableLocales app property
        return locales.values().iterator().next();
    }
}
