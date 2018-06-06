/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.desktop.gui.icons;

import com.haulmont.cuba.desktop.App;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component(IconResolver.NAME)
public class IconResolverImpl implements IconResolver {
    protected static final String BACKWARD_PATH_PREFIX = "../";

    @Override
    public Icon getIconResource(String icon) {
        if (StringUtils.isEmpty(icon)) {
            return null;
        }

        String themeIcon = App.getInstance().getThemeConstants()
                .get("icons." + processPath(icon));

        if (StringUtils.isNotEmpty(themeIcon)) {
            return getResource(themeIcon);
        }

        return getResource(icon);
    }

    protected Icon getResource(String icon) {
        if (icon.startsWith(BACKWARD_PATH_PREFIX)) {
            icon = "classpath:VAADIN/themes/" + icon.substring(BACKWARD_PATH_PREFIX.length());
        }

        return App.getInstance().getResources()
                .getIcon(icon);
    }

    protected String processPath(String icon) {
        if (icon.contains("/")) {
            icon = icon.replace("/", ".");
        }

        if (icon.contains(":")) {
            icon = icon.split(":")[1];
        }

        return icon;
    }
}
