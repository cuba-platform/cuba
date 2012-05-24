/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.ConfigurationResourceLoader;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.ShortcutAction;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;

/**
 * GenericUI class holding information about main menu structure.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean("cuba_MenuConfig")
public class MenuConfig implements Serializable
{
    public static final String MENU_CONFIG_XML_PROP = "cuba.menuConfig";

    private static Log log = LogFactory.getLog(MenuConfig.class);
    
    private List<MenuItem> rootItems = new ArrayList<MenuItem>();

    private static final long serialVersionUID = 6791874036524436320L;

    /**
     * Localized menu item caption
     * @param id screen ID as defined in <code>screen-config.xml</code>
     */
    public static String getMenuItemCaption(String id) {
        String messagePack = AppContext.getProperty(AppConfig.MESSAGES_PACK_PROP);
        try {
            return MessageProvider.getMessage(messagePack, "menu-config." + id);
        } catch (MissingResourceException e) {
            return id;
        }
    }

    public MenuConfig() {
        final String configName = AppContext.getProperty(MENU_CONFIG_XML_PROP);

        ConfigurationResourceLoader resourceLoader = new ConfigurationResourceLoader();
        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resourceLoader.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    loadConfig(stream);
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

    public void loadConfig(Element rootElem) {
        loadMenuItems(rootElem, null);
    }

    public void loadConfig(InputStream stream) {
        Document doc;
        try {
            SAXReader reader = new SAXReader();
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        loadConfig(doc.getRootElement());
    }

    public void loadConfig(String xml) {
        Document doc;
        try {
            SAXReader reader = new SAXReader();
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        loadConfig(doc.getRootElement());
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
            menuItem.setShortcut(ShortcutAction.KeyCombination.create(shortcut));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid menu shortcut value: '" + shortcut + "'");
        }
    }
}
