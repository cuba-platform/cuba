/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 15:13:56
 *
 * $Id$
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.gui.xml.layout.loaders.util.ComponentLoaderHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.core.global.MessageProvider;

/**
 * GenericUI class holding information about main menu structure.
 * <br>Reference can be obtained via {@link com.haulmont.cuba.gui.AppConfig#getMenuConfig()}
 */
public class MenuConfig implements Serializable
{
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

    /**
     * Main menu root items
     */
    public List<MenuItem> getRootItems() {
        return Collections.unmodifiableList(rootItems);
    }

    public void loadConfig(Element rootElem) {
        rootItems.clear();
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
            } else {
                log.warn(String.format("Unknown tag '%s' in menu-config", element.getName()));
            }

            if (parentItem != null) {
                parentItem.getChildren().add(menuItem);
            }
            else {
                rootItems.add(menuItem);
            }
        }
    }

    private void loadShortcut(MenuItem menuItem, Element element) {
        String shortcut = element.attributeValue("shortcut");
        if (shortcut == null || shortcut.isEmpty()) {
            return;
        }

        try {
            ShortcutAction.KeyCombination keyCombination = ComponentLoaderHelper.keyCombination(shortcut);
            menuItem.setShortcut(keyCombination);
        }
        catch (IllegalArgumentException e) {
            log.warn("Invalid menu shortcut value: '" + shortcut + "'");
        }
    }
}
