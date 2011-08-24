/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.06.2010 12:05:05
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.settings;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.app.UserSettingHelper;

import java.util.Arrays;
import java.util.Map;

public class SettingsWindow extends AbstractWindow {

    private boolean changeThemeEnabled = true;
    protected OptionsGroup modeOptions;
    protected String msgTabbed;
    protected String msgSingle;

    public SettingsWindow(IFrame frame) {
        super(frame);
    }

    protected java.util.List<String> getThemesList()
    {
        String themeBlacklabel = "blacklabel";
        String themePeyto = "peyto";
        return Arrays.asList(themeBlacklabel, themePeyto);
    }

    protected void init(Map<String, Object> params) {
        Boolean changeThemeEnabledParam = (Boolean) params.get("changeThemeEnabled");
        if (changeThemeEnabledParam != null) {
            changeThemeEnabled = changeThemeEnabledParam;
        }
        AppWindow.Mode mode = UserSettingHelper.loadAppWindowMode();
        msgTabbed = getMessage("modeTabbed");
        msgSingle = getMessage("modeSingle");

        modeOptions = getComponent("mainWindowMode");
        modeOptions.setOptionsList(Arrays.asList(msgTabbed, msgSingle));
        if (mode == AppWindow.Mode.TABBED)
            modeOptions.setValue(msgTabbed);
        else
            modeOptions.setValue(msgSingle);

        final LookupField theme = getComponent("mainWindowTheme");
        final String themeBlacklabel = "blacklabel";
        final String themePeyto = "peyto";
        theme.setOptionsList(getThemesList());
        if (themeBlacklabel.equals(UserSettingHelper.loadAppWindowTheme())) {
            theme.setValue(themeBlacklabel);
        } else {
            theme.setValue(themePeyto);
        }
        theme.setEditable(changeThemeEnabled);

        Button changePasswBtn = getComponent("changePassw");
        final User user = UserSessionClient.getUserSession().getUser();
        changePasswBtn.setAction(
                new AbstractAction("changePassw") {
                    public void actionPerform(Component component) {
                        openEditor("sec$User.changePassw", user, WindowManager.OpenType.DIALOG);
                    }
                }
        );
        if (!user.equals(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser())) {
            changePasswBtn.setEnabled(false);
        }


        Button okBtn = getComponent("ok");
        okBtn.setAction(
                new AbstractAction("ok") {
                    public void actionPerform(Component component) {
                        if (changeThemeEnabled) {
                            UserSettingHelper.saveAppWindowTheme(theme.<String>getValue());
                        }
                        AppWindow.Mode m = modeOptions.getValue() == msgTabbed ? AppWindow.Mode.TABBED : AppWindow.Mode.SINGLE;
                        UserSettingHelper.saveAppWindowMode(m);
                        showNotification(getMessage("modeChangeNotification"), IFrame.NotificationType.HUMANIZED);
                        close("ok");
                    }
                }
        );

        Button cancelBtn = getComponent("cancel");
        cancelBtn.setAction(
                new AbstractAction("cancel") {
                    public void actionPerform(Component component) {
                        close("cancel");
                    }
                }
        );
    }
}
