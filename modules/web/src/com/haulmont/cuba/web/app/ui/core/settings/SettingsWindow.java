/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.ui.core.settings;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.app.UserTimeZone;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.ComboBox;

import javax.inject.Inject;
import java.util.*;

import static com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection.EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE;

/**
 * @author krivopustov
 * @version $Id$
 */
public class SettingsWindow extends AbstractWindow {

    protected boolean changeThemeEnabled = true;
    protected String msgTabbed;
    protected String msgSingle;

    @Inject
    protected UserSettingsTools userSettingsTools;

    @Inject
    protected UserSession userSession;

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected ClientConfig clientConfig;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected TimeZones timeZones;

    @Inject
    protected Button okBtn;

    @Inject
    protected Button cancelBtn;

    @Inject
    protected Button changePasswordBtn;

    @Inject
    protected OptionsGroup modeOptions;

    @Inject
    protected LookupField appThemeField;

    @Inject
    protected LookupField timeZoneLookup;

    @Inject
    protected LookupField appLangField;

    @Inject
    protected CheckBox timeZoneAutoField;

    @Inject
    protected UserSessionSource userSessionSource;

    @Override
    public void init(Map<String, Object> params) {
        Boolean changeThemeEnabledParam = (Boolean) params.get("changeThemeEnabled");
        if (changeThemeEnabledParam != null) {
            changeThemeEnabled = changeThemeEnabledParam;
        }

        AppWorkArea.Mode mode = userSettingsTools.loadAppWindowMode();
        msgTabbed = getMessage("modeTabbed");
        msgSingle = getMessage("modeSingle");

        modeOptions.setOptionsList(Arrays.asList(msgTabbed, msgSingle));
        if (mode == AppWorkArea.Mode.TABBED)
            modeOptions.setValue(msgTabbed);
        else
            modeOptions.setValue(msgSingle);

        ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
        Set<String> supportedThemes = themeRepository.getAvailableThemes();
        appThemeField.setOptionsList(new ArrayList<>(supportedThemes));

        String userAppTheme = userSettingsTools.loadAppWindowTheme();
        appThemeField.setValue(userAppTheme);

        ComboBox vAppThemeField = (ComboBox) WebComponentsHelper.unwrap(appThemeField);
        vAppThemeField.setTextInputAllowed(false);
        appThemeField.setEditable(changeThemeEnabled);

        initTimeZoneFields();

        final User user = userSession.getUser();
        changePasswordBtn.setAction(
                new AbstractAction("changePassw") {
                    @Override
                    public void actionPerform(Component component) {
                        Window passwordDialog = openWindow("sec$User.changePassword", WindowManager.OpenType.DIALOG,
                                Collections.<String, Object>singletonMap("currentPasswordRequired", true));
                        passwordDialog.addCloseListener(actionId -> {
                            // move focus back to window
                            changePasswordBtn.requestFocus();
                        });
                    }
                }
        );
        if (!user.equals(userSession.getCurrentOrSubstitutedUser())
                || Boolean.TRUE.equals(userSession.getAttribute(EXTERNAL_AUTH_USER_SESSION_ATTRIBUTE))) {
            changePasswordBtn.setEnabled(false);
        }

        AbstractAction commitAction = new AbstractAction("ok", clientConfig.getCommitShortcut()) {
            @Override
            public void actionPerform(Component component) {
                if (changeThemeEnabled) {
                    String selectedTheme = appThemeField.getValue();
                    userSettingsTools.saveAppWindowTheme(selectedTheme);
                    App.getInstance().setUserAppTheme(selectedTheme);
                }
                AppWorkArea.Mode m = modeOptions.getValue() == msgTabbed ? AppWorkArea.Mode.TABBED : AppWorkArea.Mode.SINGLE;
                userSettingsTools.saveAppWindowMode(m);
                saveTimeZoneSettings();
                if (appLangField.getValue() != null){
                    saveLocaleSettings();
                }
                showNotification(getMessage("modeChangeNotification"), NotificationType.HUMANIZED);

                close(COMMIT_ACTION_ID);
            }
        };

        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        TreeMap<String, Object> options = new TreeMap<>();
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            options.put(entry.getKey(), messages.getTools().localeToString(entry.getValue()));
        }
        appLangField.setOptionsMap(options);
        appLangField.setValue(messages.getTools().localeToString(userSessionSource.getUserSession().getLocale()));
        addAction(commitAction);
        okBtn.setAction(commitAction);

        cancelBtn.setAction(
                new AbstractAction("cancel") {
                    @Override
                    public void actionPerform(Component component) {
                        close(CLOSE_ACTION_ID);
                    }
                }
        );
    }

    protected void initTimeZoneFields() {
        Map<String, Object> options = new TreeMap<>();
        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone timeZone = TimeZone.getTimeZone(id);
            options.put(timeZones.getDisplayNameLong(timeZone), id);
        }
        timeZoneLookup.setOptionsMap(options);

        timeZoneAutoField.setCaption(messages.getMainMessage("timeZone.auto"));
        timeZoneAutoField.setDescription(messages.getMainMessage("timeZone.auto.descr"));
        timeZoneAutoField.addValueChangeListener(e -> timeZoneLookup.setEnabled(!Boolean.TRUE.equals(e.getValue())));

        UserTimeZone userTimeZone = userManagementService.loadOwnTimeZone();
        timeZoneLookup.setValue(userTimeZone.name);
        timeZoneAutoField.setValue(userTimeZone.auto);
    }

    protected void saveTimeZoneSettings() {
        UserTimeZone userTimeZone = new UserTimeZone(timeZoneLookup.getValue(), timeZoneAutoField.getValue());
        userManagementService.saveOwnTimeZone(userTimeZone);
    }

    protected void saveLocaleSettings() {
        Locale userLocale = new Locale(appLangField.getValue());
        userManagementService.saveOwnLocale(userLocale);
    }
}