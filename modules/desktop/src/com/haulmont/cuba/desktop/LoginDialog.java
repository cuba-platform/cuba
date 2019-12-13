/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.LoginProperties;
import com.haulmont.cuba.desktop.sys.vcl.JTextFieldCapsLockSubscriber;
import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.gui.components.Frame.NotificationType;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.global.LoginException;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Map;

public class LoginDialog extends JDialog {

    private static final Logger log = LoggerFactory.getLogger(LoginDialog.class);
    private final DesktopConfig desktopConfig;

    protected Connection connection;
    protected Locale resolvedLocale;
    protected Map<String, Locale> locales;
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected JTextField nameField;
    protected JTextField passwordField;
    protected JComboBox<String> localeCombo;
    protected JButton loginBtn;
    protected LoginProperties loginProperties;
    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

    public LoginDialog(JFrame owner, Connection connection) {
        super(owner);
        this.connection = connection;
        this.loginProperties = new LoginProperties();
        Configuration configuration = AppBeans.get(Configuration.NAME);
        desktopConfig = configuration.getConfig(DesktopConfig.class);
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

        JTextFieldCapsLockSubscriber capsLockSubscriber = new JTextFieldCapsLockSubscriber();
        CapsLockIndicator capsLock = componentsFactory.createComponent(CapsLockIndicator.class);
        capsLock.setCapsLockOnMessage(messages.getMainMessage("capsLockIndicator.capsLockOnMessage", resolvedLocale));

        JComponent capsLockIndicator = DesktopComponentsHelper.getComposition(capsLock);
        panel.add(capsLockIndicator, "span, align center, height 18!");

        panel.add(new JLabel(messages.getMainMessage("loginWindow.loginField", resolvedLocale)));

        nameField = new JTextField();
        passwordField = new JPasswordField();

        String defaultName = desktopConfig.getLoginDialogDefaultUser();
        String lastLogin = loginProperties.loadLastLogin();
        if (!StringUtils.isBlank(lastLogin)) {
            nameField.setText(lastLogin);
            SwingUtilities.invokeLater(() ->
                    passwordField.requestFocus()
            );
        } else if (!StringUtils.isBlank(defaultName)) {
            nameField.setText(defaultName);
        }

        panel.add(nameField, "width 150!, wrap");

        panel.add(new JLabel(messages.getMainMessage("loginWindow.passwordField", resolvedLocale)));
        String defaultPassword = desktopConfig.getLoginDialogDefaultPassword();
        if (!StringUtils.isBlank(defaultPassword))
            passwordField.setText(defaultPassword);
        panel.add(passwordField, "width 150!, wrap");
        capsLockSubscriber.subscribe(passwordField, capsLock);

        Configuration configuration = AppBeans.get(Configuration.NAME);

        localeCombo = new JComboBox<>();
        initLocales(localeCombo);
        if (configuration.getConfig(GlobalConfig.class).getLocaleSelectVisible()) {
            panel.add(new JLabel(messages.getMainMessage("loginWindow.localesSelect", resolvedLocale)));
            panel.add(localeCombo, "width 150!, wrap");
        }

        loginBtn = new JButton(messages.getMainMessage("loginWindow.okButton", resolvedLocale));
        loginBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
        loginBtn.addActionListener(e ->
                doLogin()
        );
        DesktopComponentsHelper.adjustSize(loginBtn);
        panel.add(loginBtn, "span, align center, gapy 6 0");

        getRootPane().setDefaultButton(loginBtn);

        assignTestIdsIfNeeded(panel);

        return panel;
    }

    protected void assignTestIdsIfNeeded(JPanel panel) {
        if (App.getInstance().isTestMode()) {
            panel.setName("contentPane");
            nameField.setName("nameField");
            passwordField.setName("passwordField");
            loginBtn.setName("loginBtn");
            localeCombo.setName("localeCombo");
        }
    }

    protected void doLogin() {
        String name = nameField.getText();
        String password = passwordField.getText();

        String selectedItem = (String) localeCombo.getSelectedItem();
        Locale locale = locales.get(selectedItem);
        try {
            connection.login(name, password, locale);
            setVisible(false);
            loginProperties.save(name, messages.getTools().localeToString(locale));
            DesktopComponentsHelper.getTopLevelFrame(this).activate();
        } catch (LoginException ex) {
            log.info("Login failed: " + ex.toString());
            String caption = messages.getMainMessage("loginWindow.loginFailed", locale);

            TopLevelFrame mainFrame = App.getInstance().getMainFrame();
            mainFrame.showNotification(caption, ex.getMessage(), NotificationType.ERROR);
        }
    }

    @Deprecated
    protected boolean bruteForceProtectionCheck(String login, String ipAddress) {
        return true;
    }

    @Deprecated
    @Nullable
    protected String registerUnsuccessfulLoginAttempt(String login, String ipAddress) {
        return null;
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
            appLocale = LocaleResolver.resolve(lastLocale);
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