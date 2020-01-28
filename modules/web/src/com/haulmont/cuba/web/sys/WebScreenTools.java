/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenTools;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.sys.navigation.EditorTypeExtractor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(ScreenTools.NAME)
public class WebScreenTools implements ScreenTools {

    private static final Logger log = LoggerFactory.getLogger(WebScreenTools.class);

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected Metadata metadata;
    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected UserSettingService userSettingService;

    @Override
    public void openDefaultScreen(Screens screens) {
        String defaultScreenId = webConfig.getDefaultScreenId();

        if (webConfig.getUserCanChooseDefaultScreen()) {
            String userDefaultScreen = userSettingService.loadSetting(ClientType.WEB, "userDefaultScreen");

            defaultScreenId = StringUtils.isEmpty(userDefaultScreen)
                    ? defaultScreenId
                    : userDefaultScreen;
        }

        if (StringUtils.isEmpty(defaultScreenId)) {
            return;
        }

        if (!windowConfig.hasWindow(defaultScreenId)) {
            log.info("Can't find default screen: {}", defaultScreenId);
            return;
        }

        Screen screen = screens.create(defaultScreenId, OpenMode.NEW_TAB);

        if (screen instanceof EditorScreen) {
            ((EditorScreen) screen).setEntityToEdit(getEntityToEdit(defaultScreenId));
        }

        screen.show();

        Window window = screen.getWindow();

        WebWindow webWindow;
        if (window instanceof Window.Wrapper) {
            webWindow = (WebWindow) ((Window.Wrapper) window).getWrappedWindow();
        } else {
            webWindow = (WebWindow) window;
        }
        webWindow.setDefaultScreenWindow(true);

        if (!webConfig.getDefaultScreenCanBeClosed()) {
            window.setCloseable(false);
        }
    }

    protected Entity getEntityToEdit(String screenId) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(screenId);
        Class<? extends Entity> entityClass = EditorTypeExtractor.extractEntityClass(windowInfo);

        if (entityClass == null) {
            throw new UnsupportedOperationException(
                    String.format("Unable to open default screen '%s'. Failed to determine editor entity type",
                            screenId));
        }

        return metadata.create(entityClass);
    }
}
