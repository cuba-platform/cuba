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

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.*;

/**
 * GenericUI class holding information about main menu structure.
 * <br>Reference can be obtained via {@link com.haulmont.cuba.gui.AppConfig#getMenuConfig()}
 */
public class MenuConfig
{
    private Log log = LogFactory.getLog(MenuConfig.class);
    
    private List<MenuItem> rootItems = new ArrayList<MenuItem>();
    private String msgPack;

    /**
     * Main menu root items
     */
    public List<MenuItem> getRootItems() {
        return Collections.unmodifiableList(rootItems);
    }

    public void loadConfig(String msgPack, String xml) {
        rootItems.clear();

        this.msgPack = msgPack;

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        Element rootElem = doc.getRootElement();

        loadMenuItems(rootElem, null);
    }

    private void loadMenuItems(Element parentElement, MenuItem parentItem) {
        for (Element element : ((List<Element>) parentElement.elements())) {
            MenuItem menuItem = null;

            if ("menu".equals(element.getName())) {
                String id = element.attributeValue("id");

                if (StringUtils.isBlank(id)) {
                    log.warn(String.format("Invalid menu-config: 'id' attribute not defined"));
                }

                menuItem = new MenuItem(parentItem, id, getCaption("menu-config." + id, id));
                menuItem.setDescriptor(element);

                loadMenuItems(element, menuItem);

                if (menuItem.getChildren().isEmpty()) {
                    // do not add empty branches
                    menuItem = null;
                }
            } else if ("item".equals(element.getName())) {
                String id = element.attributeValue("id");
                if (!StringUtils.isBlank(id)) {
                    String menuCaption = getCaption("menu-config." + id, id);
                    menuItem = new MenuItem(parentItem, id, menuCaption);
                    menuItem.setDescriptor(element);
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

    private String getCaption(String key, String def) {
        try {
            return MessageProvider.getMessage(msgPack, key);
        } catch (MissingResourceException e) {
            return def;
        }
    }
}
