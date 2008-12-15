/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 15:13:56
 *
 * $Id$
 */
package com.haulmont.cuba.web.config;

import com.haulmont.cuba.web.resource.Messages;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MenuConfig
{
    private List<MenuItem> rootItems = new ArrayList<MenuItem>();
    private ActionConfig actionConfig;

    public MenuConfig(ActionConfig actionConfig) {
        this.actionConfig = actionConfig;
        init();
    }

    public List<MenuItem> getRootItems() {
        return rootItems;
    }

    private void init() {
        InputStream stream = MenuConfig.class.getResourceAsStream("/com/haulmont/cuba/web/config/menu-config.xml");
        if (stream == null)
            throw new IllegalStateException("menu-config.xml not found");

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElem = doc.getRootElement();
        createMenuItems(rootElem, null);
    }

    private void createMenuItems(Element parentElement, MenuItem parentItem) {
        for (Element element : ((List<Element>) parentElement.elements())) {
            MenuItem menuItem = null;
            if ("menu".equals(element.getName())) {
                String s = element.attributeValue("name");
                if (StringUtils.isBlank(s))
                    throw new IllegalStateException("Invalid menu-config: 'name' attribute not defined");
                String menuCaption = Messages.getString("menu-config." + s);
                menuItem = new MenuItem(parentItem, menuCaption);
                createMenuItems(element, menuItem);
            }
            else if ("item".equals(element.getName())) {
                String actionName = element.attributeValue("action");
                if (!StringUtils.isBlank(actionName)) {
                    ScreenAction action = actionConfig.getAction(actionName);
                    String menuCaption = getCaption("menu-config." + actionName, action.getCaption());
                    menuItem = new MenuItem(parentItem, menuCaption);
                    menuItem.setAction(action);
                }
            }
            else {
                throw new IllegalStateException("Invalid menu-config: element " + element.getName());
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
        ResourceBundle bundle = Messages.getResourceBundle();
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return def;
        }
    }
}
