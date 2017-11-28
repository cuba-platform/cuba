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

package com.haulmont.cuba.web.gui.icons;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@Component
public class FontAwesomeIconProvider implements IconProvider {
    private static final Logger log = LoggerFactory.getLogger(FontAwesomeIconProvider.class);

    protected static final String[] FONT_AWESOME_PREFIXES = {"font-icon:", "font-awesome-icon:"};

    protected static final LoadingCache<String, Resource> iconsCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, Resource>() {
                @Override
                public Resource load(@Nonnull String iconPath) throws Exception {
                    return getIcon(iconPath);
                }
            });

    @Inject
    protected WebConfig webConfig;

    protected static Resource getIcon(String iconName) {
        Resource resource = null;

        try {
            resource = ((Resource) FontAwesome.class
                    .getDeclaredField(iconName)
                    .get(null));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.warn("There is no icon with name {} in the FontAwesome icon set", iconName);
        }

        return resource;
    }

    @Override
    public Resource getIconResource(String iconPath) {
        if (StringUtils.isEmpty(iconPath)) {
            return null;
        }

        String iconName = iconPath.contains(":") ? iconPath.split(":")[1] : iconPath;

        return iconsCache.getUnchecked(iconName);
    }

    @Override
    public boolean canProvide(String iconPath) {
        if (StringUtils.isEmpty(iconPath) || !webConfig.getUseFontIcons()) {
            return false;
        }

        for (String prefix : FONT_AWESOME_PREFIXES) {
            if (iconPath.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}
