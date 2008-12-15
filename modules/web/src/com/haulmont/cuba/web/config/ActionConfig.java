/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.12.2008 11:03:19
 *
 * $Id$
 */
package com.haulmont.cuba.web.config;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.InputStream;

import com.haulmont.cuba.web.resource.Messages;

public class ActionConfig
{
    private Map<String, ScreenAction> actions = new HashMap<String, ScreenAction>();

    public ActionConfig() {
        init();
    }

    private void init() {
        InputStream stream = MenuConfig.class.getResourceAsStream("/com/haulmont/cuba/web/config/action-config.xml");
        if (stream == null)
            throw new IllegalStateException("action-config.xml not found");

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElem = doc.getRootElement();
        for (Element element : ((List<Element>) rootElem.elements("action"))) {
            String name = element.attributeValue("name");
            if (StringUtils.isBlank(name))
                throw new IllegalStateException("Invalid action-config: 'name' attribute not defined");
            if (actions.containsKey(name))
                throw new IllegalStateException("Action duplicated: " + name);

            String caption = Messages.getString("action-config." + name);
            ScreenAction action = new ScreenAction(name, caption);
            action.setClassName(element.attributeValue("class"));

            actions.put(name, action);
        }
    }

    public ScreenAction getAction(String name) {
        ScreenAction action = actions.get(name);
        if (action == null)
            throw new IllegalArgumentException("Action not found: " + name);
        return action;
    }
}
