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
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.haulmont.cuba.gui.icons.Icons.ICON_NAME_REGEX;

/**
 * GenericUI class holding information about the main menu structure.
 */
@Component(MenuConfig.NAME)
public class MenuConfig {

    private final Logger log = LoggerFactory.getLogger(MenuConfig.class);

    public static final String NAME = "cuba_MenuConfig";

    public static final String MENU_CONFIG_XML_PROP = "cuba.menuConfig";

    protected List<MenuItem> rootItems = new ArrayList<>();

    @Inject
    protected Resources resources;

    @Inject
    protected Messages messages;

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Inject
    protected Dom4jTools dom4JTools;

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Localized menu item caption.
     *
     * @param id screen ID as defined in <code>screens.xml</code>
     * @deprecated Use {@link MenuConfig#getItemCaption(String)} or {@link MenuConfig#getItemCaption(MenuItem)}
     */
    @Deprecated
    public static String getMenuItemCaption(String id) {
        MenuConfig menuConfig = AppBeans.get(MenuConfig.NAME);
        return menuConfig.getItemCaption(id);
    }

    public String getItemCaption(String id) {
        return messages.getMainMessage("menu-config." + id);
    }

    public String getItemCaption(MenuItem menuItem) {
        String caption = menuItem.getCaption();
        if (StringUtils.isNotEmpty(caption)) {
            String localizedCaption = loadResourceString(caption);
            if (StringUtils.isNotEmpty(localizedCaption)) {
                return localizedCaption;
            }
        }
        return getItemCaption(menuItem.getId());
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

    protected void init() {
        rootItems.clear();

        String configName = AppContext.getProperty(MENU_CONFIG_XML_PROP);

        StringTokenizer tokenizer = new StringTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                try (InputStream stream = resource.getInputStream()) {
                    Element rootElement = dom4JTools.readDocument(stream).getRootElement();
                    loadMenuItems(rootElement, null);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read menu config", e);
                }
            } else {
                log.warn("Resource {} not found, ignore it", location);
            }
        }
    }

    /**
     * Make the config to reload screens on next request.
     */
    public void reset() {
        initialized = false;
    }

    /**
     * Main menu root items
     */
    public List<MenuItem> getRootItems() {
        lock.readLock().lock();
        try {
            checkInitialized();
            return Collections.unmodifiableList(rootItems);
        } finally {
            lock.readLock().unlock();
        }
    }

    protected void loadMenuItems(Element parentElement, @Nullable MenuItem parentItem) {
        for (Element element : parentElement.elements()) {
            MenuItem menuItem = null;
            MenuItem currentParentItem = parentItem;
            MenuItem nextToItem = null;
            boolean before = true;
            String nextTo = element.attributeValue("insertBefore");
            if (StringUtils.isBlank(nextTo)) {
                before = false;
                nextTo = element.attributeValue("insertAfter");
            }
            if (!StringUtils.isBlank(nextTo)) {
                for (MenuItem rootItem : rootItems) {
                    nextToItem = findItem(nextTo, rootItem);
                    if (nextToItem != null) {
                        if (nextToItem.getParent() != null)
                            currentParentItem = nextToItem.getParent();
                        break;
                    }
                }
            }

            if ("menu".equals(element.getName())) {
                String id = element.attributeValue("id");

                if (StringUtils.isBlank(id)) {
                    log.warn("Invalid menu-config: 'id' attribute not defined");
                }

                menuItem = new MenuItem(currentParentItem, id);
                menuItem.setMenu(true);
                menuItem.setDescriptor(element);
                loadIcon(element, menuItem);
                loadShortcut(menuItem, element);
                loadStylename(element, menuItem);
                loadExpanded(element, menuItem);
                loadCaption(element, menuItem);
                loadDescription(element, menuItem);
                loadMenuItems(element, menuItem);
            } else if ("item".equals(element.getName())) {
                menuItem = createMenuItem(element, currentParentItem);
            } else if ("separator".equals(element.getName())) {
                String id = element.attributeValue("id");
                if (StringUtils.isBlank(id))
                    id = "-";
                menuItem = new MenuItem(currentParentItem, id);
                menuItem.setSeparator(true);
                if (!StringUtils.isBlank(id)) {
                    menuItem.setDescriptor(element);
                }
            } else {
                log.warn(String.format("Unknown tag '%s' in menu-config", element.getName()));
                continue;
            }

            if (currentParentItem != null) {
                addItem(currentParentItem.getChildren(), menuItem, nextToItem, before);
            } else {
                addItem(rootItems, menuItem, nextToItem, before);
            }
        }
    }

    protected MenuItem createMenuItem(Element element, @Nullable MenuItem currentParentItem) {
        String id = element.attributeValue("id");

        String idFromActions;

        String screen = element.attributeValue("screen");
        idFromActions = StringUtils.isNotEmpty(screen) ? screen : null;

        String runnableClass = element.attributeValue("class");
        checkDuplicateAction(idFromActions, runnableClass);
        idFromActions = StringUtils.isNotEmpty(runnableClass) ? runnableClass : idFromActions;

        String bean = element.attributeValue("bean");
        String beanMethod = element.attributeValue("beanMethod");

        if (StringUtils.isNotEmpty(bean) && StringUtils.isEmpty(beanMethod) ||
                StringUtils.isEmpty(bean) && StringUtils.isNotEmpty(beanMethod)) {
            throw new IllegalStateException("Both bean and beanMethod should be defined.");
        }

        checkDuplicateAction(idFromActions, bean, beanMethod);

        String fqn = bean + "#" + beanMethod;
        idFromActions = StringUtils.isNotEmpty(bean) && StringUtils.isNotEmpty(beanMethod) ? fqn : idFromActions;

        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(idFromActions)) {
            throw new IllegalStateException("MenuItem should have at least one action");
        }

        if (StringUtils.isEmpty(id) && StringUtils.isNotEmpty(idFromActions)) {
            id = idFromActions;
        }

        if (StringUtils.isNotEmpty(id) && StringUtils.isEmpty(idFromActions)) {
            screen = id;
        }

        MenuItem menuItem = new MenuItem(currentParentItem, id);

        menuItem.setScreen(screen);
        menuItem.setRunnableClass(runnableClass);
        menuItem.setBean(bean);
        menuItem.setBeanMethod(beanMethod);

        menuItem.setDescriptor(element);
        loadIcon(element, menuItem);
        loadShortcut(menuItem, element);
        loadStylename(element, menuItem);
        loadCaption(element, menuItem);
        loadDescription(element, menuItem);

        return menuItem;
    }

    protected void checkDuplicateAction(@Nullable String menuItemId, String... actionDefinition) {
        boolean actionDefined = true;
        for (String s : actionDefinition) {
            actionDefined &= StringUtils.isNotEmpty(s);
        }
        if (StringUtils.isNotEmpty(menuItemId) && actionDefined) {
            throw new IllegalStateException("MenuItem can't have more than one action.");
        }
    }

    protected void loadExpanded(Element element, MenuItem menuItem) {
        String expanded = element.attributeValue("expanded");
        if (StringUtils.isNotEmpty(expanded)) {
            menuItem.setExpanded(Boolean.parseBoolean(expanded));
        }
    }

    protected void loadCaption(Element element, MenuItem menuItem) {
        String caption = element.attributeValue("caption");
        if (StringUtils.isNotEmpty(caption)) {
            menuItem.setCaption(caption);
        }
    }

    protected void loadDescription(Element element, MenuItem menuItem) {
        String description = element.attributeValue("description");
        if (StringUtils.isNotBlank(description)) {
            menuItem.setDescription(description);
        }
    }

    protected void loadStylename(Element element, MenuItem menuItem) {
        String stylename = element.attributeValue("stylename");
        if (StringUtils.isNotBlank(stylename)) {
            menuItem.setStylename(stylename);
        }
    }

    protected void loadIcon(Element element, MenuItem menuItem) {
        String icon = element.attributeValue("icon");
        if (StringUtils.isNotEmpty(icon)) {
            menuItem.setIcon(getIconPath(icon));
        }
    }

    @Nullable
    protected String getIconPath(@Nullable String icon) {
        if (icon == null || icon.isEmpty()) {
            return null;
        }

        String iconPath = null;

        if (ICON_NAME_REGEX.matcher(icon).matches()) {
            iconPath = AppBeans.get(Icons.class)
                    .get(icon);
        }

        if (StringUtils.isEmpty(iconPath)) {
            String themeValue = loadThemeString(icon);
            iconPath = loadResourceString(themeValue);
        }

        return iconPath;
    }

    protected String loadResourceString(String caption) {
        return messages.getTools().loadString(messages.getMainMessagePack(), caption);
    }

    protected String loadThemeString(String value) {
        if (value != null && value.startsWith(ThemeConstants.PREFIX)) {
            value = themeConstantsManager.getConstants()
                    .get(value.substring(ThemeConstants.PREFIX.length()));
        }
        return value;
    }

    protected void addItem(List<MenuItem> items, MenuItem menuItem, @Nullable MenuItem beforeItem, boolean before) {
        if (beforeItem == null) {
            items.add(menuItem);
        } else {
            int i = items.indexOf(beforeItem);
            if (before)
                items.add(i, menuItem);
            else
                items.add(i + 1, menuItem);
        }
    }

    @Nullable
    protected MenuItem findItem(String id, MenuItem item) {
        if (id.equals(item.getId()))
            return item;
        else if (!item.getChildren().isEmpty()) {
            for (MenuItem child : item.getChildren()) {
                MenuItem menuItem = findItem(id, child);
                if (menuItem != null)
                    return menuItem;
            }
        }
        return null;
    }

    protected void loadShortcut(MenuItem menuItem, Element element) {
        String shortcut = element.attributeValue("shortcut");
        if (shortcut == null || shortcut.isEmpty()) {
            return;
        }
        // If the shortcut string looks like a property, try to get it from the application properties
        if (shortcut.startsWith("${") && shortcut.endsWith("}")) {
            String property = AppContext.getProperty(shortcut.substring(2, shortcut.length() - 1));
            if (!StringUtils.isEmpty(property))
                shortcut = property;
            else
                return;
        }
        try {
            menuItem.setShortcut(KeyCombination.create(shortcut));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid menu shortcut value: '" + shortcut + "'");
        }
    }
}