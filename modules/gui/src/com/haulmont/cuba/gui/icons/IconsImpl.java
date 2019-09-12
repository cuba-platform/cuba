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

package com.haulmont.cuba.gui.icons;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component(Icons.NAME)
public class IconsImpl implements Icons {

    @Inject
    private Logger log;

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    protected LoadingCache<String, String> iconsCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(@Nonnull String key) {
                    return resolveIcon(key);
                }
            });

    protected List<Class<? extends Icon>> iconSets = new ArrayList<>();

    @EventListener(AppContextInitializedEvent.class)
    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 100)
    public void init() {
        String iconSetsProp = AppContext.getProperty("cuba.iconsConfig");
        if (StringUtils.isEmpty(iconSetsProp)) {
            return;
        }

        Iterable<String> iconSetsClasses = Splitter.on(' ')
                .omitEmptyStrings()
                .trimResults()
                .split(iconSetsProp);

        for (String iconSetFqn : iconSetsClasses) {
            Class<?> iconSetClass;
            try {
                iconSetClass = ReflectionHelper.loadClass(iconSetFqn);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to load icon set class: " + iconSetFqn, e);
            }

            if (!Icon.class.isAssignableFrom(iconSetClass)) {
                log.warn("Class {} does not implement Icon", iconSetClass);
                continue;
            }

            //noinspection unchecked
            iconSets.add((Class<? extends Icon>) iconSetClass);
        }
    }

    @Override
    public String get(@Nullable Icon icon) {
        if (icon == null)
            return null;

        return get(icon.iconName());
    }

    @Override
    public String get(@Nullable String icon) {
        if (StringUtils.isEmpty(icon))
            return null;

        if (!ICON_NAME_REGEX.matcher(icon).matches())
            throw new IllegalArgumentException("Icon name can contain only uppercase letters and underscores.");

        String themeIcon = getThemeIcon(icon);

        if (StringUtils.isNotEmpty(themeIcon))
            return themeIcon;

        return iconsCache.getUnchecked(icon);
    }

    protected String getThemeIcon(String iconName) {
        ThemeConstants theme = themeConstantsManager.getConstants();

        String icon = iconName.replace("/", ".");

        String themeIcon = theme.get("icons." + icon);

        if (StringUtils.isEmpty(themeIcon)) {
            themeIcon = theme.get("cuba.web." + icon);
        }

        return themeIcon;
    }

    protected String resolveIcon(String iconName) {
        String iconSource = null;

        for (Class<? extends Icon> iconSet : iconSets) {
            try {
                Object obj = iconSet.getDeclaredField(iconName).get(null);
                iconSource = ((Icon) obj).source();
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                // must be ignored, because some icon sets in the sequence may not contain the icon, e.g.:
                // assuming icon sets CubaIcon > MyCompIcon > MyAppIcon,
                // CubaIcon.OK - defined, MyCompIcon.OK - overrides, MyAppIcon.OK - not defined
                // then using MyCompIcon.OK
            }
        }

        return iconSource;
    }
}