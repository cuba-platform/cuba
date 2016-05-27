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

package com.haulmont.cuba.desktop.app.security.user;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;

import javax.inject.Inject;

public class ChangePasswordLauncher implements Runnable {

    @Inject
    protected WindowConfig windowConfig;

    @Override
    public void run() {
        WindowManager wm = App.getInstance().getMainFrame().getWindowManager();

        WindowInfo windowInfo = windowConfig.getWindowInfo("sec$User.changePassword");

        wm.openWindow(windowInfo, OpenType.DIALOG, ParamsMap.of("currentPasswordRequired", true));
    }
}