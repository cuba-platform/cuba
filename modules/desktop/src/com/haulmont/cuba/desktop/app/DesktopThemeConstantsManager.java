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

package com.haulmont.cuba.desktop.app;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;

import org.springframework.stereotype.Component;

/**
 */
@Component(ThemeConstantsManager.NAME)
public class DesktopThemeConstantsManager implements ThemeConstantsManager {
    @Override
    public ThemeConstants getConstants() {
        return App.getInstance().getThemeConstants();
    }

    @Override
    public String getThemeValue(String key) {
        return getConstants().get(key);
    }

    @Override
    public int getThemeValueInt(String key) {
        return getConstants().getInt(key);
    }
}