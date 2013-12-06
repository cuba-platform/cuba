package com.haulmont.cuba.desktop.app.security.user;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;

/**
 * @author kim
 * @version $Id$
 */
public class ChangePasswordLauncher implements Runnable {

    @Override
    public void run() {
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
        User user = userSessionSource.getUserSession().getCurrentOrSubstitutedUser();

        WindowManager windowManager = App.getInstance().getMainFrame().getWindowManager();

        WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
        WindowInfo windowInfo = windowConfig.getWindowInfo("sec$User.changePassw");

        windowManager.openEditor(windowInfo, user, WindowManager.OpenType.DIALOG);
    }
}
