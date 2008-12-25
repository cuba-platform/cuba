/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.12.2008 11:03:19
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.io.StringReader;

public class ActionsConfig
{
    private Log LOG = LogFactory.getLog(ActionsConfig.class);
    private Map<String, Map<String, Action>> actions = new HashMap<String, Map<String, Action>>();

    public void loadConfig(String moduleName, ResourceBundle resourceBundle, String xml) {
        Map<String, Action> actions = new HashMap<String, Action>();

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        Element rootElem = doc.getRootElement();
        for (Element element : ((List<Element>) rootElem.elements("action"))) {
            String name = element.attributeValue("name");

            if (StringUtils.isBlank(name))
                LOG.warn("Invalid action-config: 'name' attribute not defined");
            if (actions.containsKey(name))
                LOG.warn(String.format("Action '%s' duplicated", name));

            Action action = new Action(name, resourceBundle.getString("action-config." + name));
            action.setDescriptor(element);

            actions.put(name, action);
        }

        this.actions.put(moduleName, actions);
    }

    public Action getAction(String name) {
        for (Map.Entry<String, Map<String, Action>> entry : actions.entrySet()) {
            final Action action = entry.getValue().get(name);
            if (action != null) return action;
        }

        return null;
    }
}
