/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.gui.icons;

import com.haulmont.bali.util.Preconditions;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.haulmont.cuba.web.gui.icons.IconProvider.LOWEST_PLATFORM_PRECEDENCE;

@Component
@Order(LOWEST_PLATFORM_PRECEDENCE - 30)
public class FileIconProvider implements IconProvider {

    protected static final String FILE_PREFIX = "file:";

    @Override
    public Resource getIconResource(String iconPath) {
        Preconditions.checkNotEmptyString(iconPath, "Icon path should not be empty");

        String icon = iconPath.substring(FILE_PREFIX.length());
        File iconFile = new File(icon);
        if (!iconFile.exists()) {
            throw new IllegalArgumentException("Icon file does not exist: " + icon);
        }

        return new FileResource(iconFile);
    }

    @Override
    public boolean canProvide(String iconPath) {
        return iconPath != null && !iconPath.isEmpty() && iconPath.startsWith(FILE_PREFIX);
    }
}
