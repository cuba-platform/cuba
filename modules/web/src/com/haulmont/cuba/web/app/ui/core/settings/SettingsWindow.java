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
package com.haulmont.cuba.web.app.ui.core.settings;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.settings.SettingsClient;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.app.UserTimeZone;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.security.ExternalUserCredentials;
import com.haulmont.cuba.web.settings.WebSettingsClient;
import com.vaadin.ui.ComboBox;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class SettingsWindow extends AbstractWindow {

    protected boolean changeThemeEnabled = true;
    protected String msgTabbed;
    protected String msgSingle;

    @Inject
    protected App app;

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
    protected Button resetScreenSettingsBtn;

    @Inject
    protected OptionsGroup<String, String> modeOptions;

    @Inject
    protected LookupField<String> appThemeField;
    @Inject
    protected LookupField<String> timeZoneLookup;
    @Inject
    protected LookupField<String> appLangField;
    @Inject
    protected CheckBox timeZoneAutoField;

    @Inject
    protected LookupField<String> defaultScreenField;

    @Inject
    protected MenuConfig menuConfig;
    @Inject
    protected WebConfig webConfig;
    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected UserSettingService userSettingService;
    @Inject
    protected SettingsClient settingsClient;

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

        User user = userSession.getUser();
        changePasswordBtn.setAction(new BaseAction("changePassw")
                .withCaption(getMessage("changePassw"))
                .withHandler(event -> {
                    Window passwordDialog = openWindow("sec$User.changePassword", OpenType.DIALOG,
                            ParamsMap.of("currentPasswordRequired", true));
                    passwordDialog.addCloseListener(actionId -> {
                        // move focus back to window
                        changePasswordBtn.focus();
                    });
                }));

        if (!user.equals(userSession.getCurrentOrSubstitutedUser())
                || ExternalUserCredentials.isLoggedInWithExternalAuth(userSession)) {
            changePasswordBtn.setEnabled(false);
        }

        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        TreeMap<String, String> options = new TreeMap<>();
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            options.put(entry.getKey(), messages.getTools().localeToString(entry.getValue()));
        }
        appLangField.setOptionsMap(options);
        appLangField.setValue(userManagementService.loadOwnLocale());

        Action commitAction = new BaseAction("commit")
                .withCaption(messages.getMainMessage("actions.Ok"))
                .withShortcut(clientConfig.getCommitShortcut())
                .withPrimary(true)
                .withHandler(event ->
                        commit()
                );
        addAction(commitAction);
        okBtn.setAction(commitAction);

        cancelBtn.setAction(new BaseAction("cancel")
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withHandler(event ->
                        cancel()
                ));

        resetScreenSettingsBtn.setAction(new BaseAction("resetScreenSettings")
                .withCaption(getMessage("resetScreenSettings"))
                .withHandler(buttonEvent ->
                        showOptionDialog(getMessage("resetScreenSettings"),
                                getMessage("resetScreenSettings.description"),
                                MessageType.CONFIRMATION,
                                new Action[]{
                                        new DialogAction(DialogAction.Type.YES)
                                                .withHandler(event -> resetScreenSettings()),
                                        new DialogAction(DialogAction.Type.NO)
                                })
                ));

        initDefaultScreenField();
    }

    protected void initDefaultScreenField() {
        boolean screenSelectionEnabled = webConfig.getUserCanChooseDefaultScreen();
        if (!screenSelectionEnabled) {
            defaultScreenField.setEditable(false);
            defaultScreenField.setDescription(getMessage("defaultScreenSelectionDisabled"));
        }

        Map<String, String> map = new LinkedHashMap<>();
        for (MenuItem item : collectPermittedScreens(menuConfig.getRootItems())) {
            map.put(menuConfig.getItemCaption(item.getId()), item.getScreen());
        }
        defaultScreenField.setOptionsMap(map);

        String defaultScreen = userSettingService.loadSetting(ClientType.WEB, "userDefaultScreen");
        if (StringUtils.isEmpty(defaultScreen) || !screenSelectionEnabled) {
            defaultScreen = webConfig.getDefaultScreenId();
        }
        defaultScreenField.setValue(defaultScreen);
    }

    protected List<MenuItem> collectPermittedScreens(List<MenuItem> menuItems) {
        List<MenuItem> collectedItems = new ArrayList<>();

        for (MenuItem item : menuItems) {
            if (!item.isPermitted(userSession))
                continue;

            if (StringUtils.isNotEmpty(item.getScreen())) {
                collectedItems.add(item);
            }

            if (CollectionUtils.isNotEmpty(item.getChildren())) {
                collectedItems.addAll(collectPermittedScreens(item.getChildren()));
            }
        }

        return collectedItems;
    }

    protected void commit() {
        if (changeThemeEnabled) {
            String selectedTheme = appThemeField.getValue();
            userSettingsTools.saveAppWindowTheme(selectedTheme);

            app.setUserAppTheme(selectedTheme);
        }

        AppWorkArea.Mode m = Objects.equals(modeOptions.getValue(), msgTabbed) ?
                AppWorkArea.Mode.TABBED : AppWorkArea.Mode.SINGLE;

        userSettingsTools.saveAppWindowMode(m);
        saveTimeZoneSettings();
        saveLocaleSettings();

        if (webConfig.getUserCanChooseDefaultScreen()) {
            userSettingService.saveSetting(ClientType.WEB, "userDefaultScreen", defaultScreenField.getValue());
        }

        showNotification(getMessage("modeChangeNotification"), NotificationType.HUMANIZED);

        close(COMMIT_ACTION_ID);
    }

    protected void cancel() {
        close(CLOSE_ACTION_ID);
    }

    protected void initTimeZoneFields() {
        Map<String, String> options = new TreeMap<>();
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
        String userLocale = appLangField.getValue();
        userManagementService.saveOwnLocale(userLocale);
    }

    protected void resetScreenSettings() {
        userSettingService.deleteScreenSettings(ClientType.WEB, getAllWindowIds());
        ((WebSettingsClient) settingsClient).clearCache();
        showNotification(getMessage("resetScreenSettings.notification"));
    }

    protected Set<String> getAllWindowIds() {
        Collection<WindowInfo> windows = windowConfig.getWindows();
        return windows.stream()
                .map(WindowInfo::getId)
                .collect(Collectors.toSet());
    }
}