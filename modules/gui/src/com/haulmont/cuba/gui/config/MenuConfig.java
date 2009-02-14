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

public class MenuConfig
{
    private Log log = LogFactory.getLog(MenuConfig.class);
    
    private List<MenuItem> rootItems = new ArrayList<MenuItem>();

    private ResourceBundle resourceBundle;

    private ClientType clientType;
    private UserSession userSession;

    public MenuConfig(ClientType clientType, UserSession userSession) {
        this.clientType = clientType;
        this.userSession = userSession;
    }

    public List<MenuItem> getRootItems() {
        return Collections.unmodifiableList(rootItems);
    }

    public void loadConfig(ResourceBundle resourceBundle, String xml) {
        rootItems.clear();

        this.resourceBundle = resourceBundle;

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

                menuItem = new MenuItem(parentItem, id, resourceBundle.getString("menu-config." + id));
                menuItem.setDescriptor(element);

                loadMenuItems(element, menuItem);

                if (menuItem.getChildren().isEmpty()) {
                    // do not add empty branches
                    menuItem = null;
                }
            } else if ("item".equals(element.getName())) {
                String id = element.attributeValue("id");
                if (!StringUtils.isBlank(id) && isScreenPermitted(id)) {
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

    private boolean isScreenPermitted(String screenId) {
        return userSession.isPermitted(PermissionType.SCREEN, clientType.getId() + ":" + screenId);
    }

    private String getCaption(String key, String def) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return def;
        }
    }
}
