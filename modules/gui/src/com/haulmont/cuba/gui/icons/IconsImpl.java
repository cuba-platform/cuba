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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

@Component(Icons.NAME)
public class IconsImpl implements Icons {
    protected final Pattern ICON_NAME_REGEX = Pattern.compile("[A-Z_]*");

    protected static final LoadingCache<String, String> iconsCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(@Nonnull String key) throws Exception {
                    return resolveIcon(key);
                }
            });

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    protected static final List<Class<? extends Icon>> iconSets = new ArrayList<>();

    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    protected volatile boolean initialized;

    public void init() {
        String iconSetsProp = AppContext.getProperty("cuba.iconsConfig");
        if (StringUtils.isEmpty(iconSetsProp))
            return;

        String[] iconSetFqns = iconSetsProp.split(" ");
        for (String iconSetFqn : iconSetFqns) {
            try {
                Class<?> iconSetClass = getClass().getClassLoader()
                        .loadClass(iconSetFqn);

                if (!Icon.class.isAssignableFrom(iconSetClass))
                    continue;

                //noinspection unchecked
                iconSets.add((Class<? extends Icon>) iconSetClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(String.format("Unable to load icon set class: %s", iconSetFqn), e);
            }
        }
    }

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    @Override
    public String get(@Nullable Icon icon) {
        if (icon == null)
            return null;

        return get(icon.name());
    }

    @Override
    public String get(@Nullable String icon) {
        if (StringUtils.isEmpty(icon))
            return null;

        if (!ICON_NAME_REGEX.matcher(icon).matches())
            throw new IllegalArgumentException("Icon name can contain only uppercase letters and underscores.");

        lock.readLock().lock();
        try {
            checkInitialized();

            String themeIcon = getThemeIcon(icon);

            if (StringUtils.isNotEmpty(themeIcon))
                return themeIcon;

            return iconsCache.getUnchecked(icon);
        } finally {
            lock.readLock().unlock();
        }
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

    protected static String resolveIcon(String iconName) {
        String iconSource = null;

        for (Class<? extends Icon> iconSet : iconSets) {
            try {
                Object obj = iconSet.getDeclaredField(iconName).get(null);
                iconSource = ((Icon) obj).source();
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
            }
        }

        return iconSource;
    }
}