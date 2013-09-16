/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.KeyCombination;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;

/**
 * GenericUI class holding information about the main menu structure.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(MenuConfig.NAME)
public class MenuConfig {

    public static final String NAME = "cuba_MenuConfig";

    public static final String MENU_CONFIG_XML_PROP = "cuba.menuConfig";

    private static Log log = LogFactory.getLog(MenuConfig.class);
    
    private List<MenuItem> rootItems = new ArrayList<MenuItem>();

    /**
     * Localized menu item caption.
     * @param id screen ID as defined in <code>screens.xml</code>
     */
    public static String getMenuItemCaption(String id) {
        try {
            return AppBeans.get(Messages.class).getMainMessage("menu-config." + id);
        } catch (MissingResourceException e) {
            return id;
        }
    }

    @Inject
    public MenuConfig(Resources resources) {
        final String configName = AppContext.getProperty(MENU_CONFIG_XML_PROP);

        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    Element rootElement = Dom4j.readDocument(stream).getRootElement();
                    loadMenuItems(rootElement, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }
    }

    /**
     * Main menu root items
     */
    public List<MenuItem> getRootItems() {
        return Collections.unmodifiableList(rootItems);
    }

    private void loadMenuItems(Element parentElement, MenuItem parentItem) {
        for (Element element : ((List<Element>) parentElement.elements())) {
            MenuItem menuItem = null;

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
                            parentItem = nextToItem.getParent();
                        break;
                    }
                }
            }

            if ("menu".equals(element.getName())) {
                String id = element.attributeValue("id");

                if (StringUtils.isBlank(id)) {
                    log.warn(String.format("Invalid menu-config: 'id' attribute not defined"));
                }

                menuItem = new MenuItem(parentItem, id);
                menuItem.setDescriptor(element);

                loadShortcut(menuItem, element);
                loadMenuItems(element, menuItem);

                if (menuItem.getChildren().isEmpty()) {
                    // do not add empty branches
                    //menuItem = null;
                }
            } else if ("item".equals(element.getName())) {
                String id = element.attributeValue("id");
                if (!StringUtils.isBlank(id)) {
                    menuItem = new MenuItem(parentItem, id);
                    menuItem.setDescriptor(element);
                    loadShortcut(menuItem, element);
                }
            } else if ("separator".equals(element.getName())) {
                String id = element.attributeValue("id");
                if (StringUtils.isBlank(id))
                    id = "-";
                menuItem = new MenuItem(parentItem, id);
                menuItem.setSeparator(true);
                if (!StringUtils.isBlank(id)) {
                    menuItem.setDescriptor(element);
                }
            } else {
                log.warn(String.format("Unknown tag '%s' in menu-config", element.getName()));
            }

            if (parentItem != null) {
                addItem(parentItem.getChildren(), menuItem, nextToItem, before);
            }
            else {
                addItem(rootItems, menuItem, nextToItem, before);
            }
        }
    }

    private void addItem(List<MenuItem> items, MenuItem menuItem, MenuItem beforeItem, boolean before) {
        if (beforeItem == null) {
            items.add(menuItem);
        } else {
            int i = items.indexOf(beforeItem);
            if (before)
                items.add(i, menuItem);
            else
                items.add(i+1, menuItem);
        }
    }

    private MenuItem findItem(String id, MenuItem item) {
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

    private void loadShortcut(MenuItem menuItem, Element element) {
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
