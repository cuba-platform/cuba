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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.*;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.PermissionType;

public class MenuConfig
{
    private Log LOG = LogFactory.getLog(MenuConfig.class);
    
    private Map<String, List<MenuItem>> rootItems = new HashMap<String, List<MenuItem>>();

    private ActionsConfig actionsConfig;
    private ResourceBundle resourceBundle;

    private ClientType clientType;
    private UserSession userSession;

    public MenuConfig(ClientType clientType, UserSession userSession) {
        this.clientType = clientType;
        this.userSession = userSession;
    }

    public List<MenuItem> getRootItems() {
        final List<MenuItem> res = new ArrayList<MenuItem>();
        for (Map.Entry<String, List<MenuItem>> entry : rootItems.entrySet()) {
            res.addAll(entry.getValue());
        }

        return res;
    }

    public void loadConfig(String moduleName, ActionsConfig actionsConfig, ResourceBundle resourceBundle, InputStream stream) {
        if (stream == null) {
            throw new NullPointerException("Null menu config");
        }

        rootItems.clear();

        this.actionsConfig = actionsConfig;
        this.resourceBundle = resourceBundle;

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        Element rootElem = doc.getRootElement();

        rootItems.put(moduleName, loadMenuItems(rootElem, null));
    }

    private List<MenuItem> loadMenuItems(Element parentElement, MenuItem parentItem) {
        List<MenuItem> res = new ArrayList<MenuItem>();

        for (Element element : ((List<Element>) parentElement.elements())) {
            MenuItem menuItem = null;

            if ("menu".equals(element.getName())) {
                String s = element.attributeValue("name");

                if (StringUtils.isBlank(s)) {
                    LOG.warn(String.format("Invalid menu-config: 'name' attribute not defined"));
                }

                menuItem = new MenuItem(parentItem, resourceBundle.getString("menu-config." + s));
                menuItem.setDescriptor(element);

                loadMenuItems(element, menuItem);

                if (menuItem.getChildren().isEmpty()) {
                    // do not add empty branches
                    menuItem = null;
                }
            } else if ("item".equals(element.getName())) {
                String actionName = element.attributeValue("action");
                if (!StringUtils.isBlank(actionName) && isActionPermitted(actionName)) {
                    Action action = actionsConfig.getAction(actionName);
                    String menuCaption = getCaption("menu-config." + actionName, action.getCaption());
                    menuItem = new MenuItem(parentItem, menuCaption);
                    menuItem.setAction(action);
                    menuItem.setDescriptor(element);
                }
            } else {
                LOG.warn(String.format("Unknown tag '%s' in menu-config", element.getName()));
            }

            if (menuItem != null) {
                res.add(menuItem);
            }

        }

        if (parentItem != null) {
            parentItem.getChildren().addAll(res);
        }

        return res;
    }

    private boolean isActionPermitted(String actionName) {
        return userSession.isPermitted(PermissionType.SCREEN, clientType.getId() + ":" + actionName);
    }


    private String getCaption(String key, String def) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return def;
        }
    }
}
