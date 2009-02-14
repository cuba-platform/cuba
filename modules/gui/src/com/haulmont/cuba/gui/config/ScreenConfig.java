/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.02.2009 16:19:06
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

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenConfig
{
    private Map<String, ScreenInfo> screens = new HashMap<String, ScreenInfo>();

    private Log log = LogFactory.getLog(ScreenConfig.class);

    public void loadConfig(String xml) {
        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        Element rootElem = doc.getRootElement();
        for (Element element : (List<Element>) rootElem.elements("screen")) {
            String id = element.attributeValue("id");
            if (StringUtils.isBlank(id)) {
                log.warn("Invalid screen-config: 'id' attribute not defined");
                continue;
            }
            ScreenInfo screenInfo = new ScreenInfo(id, element);
            screens.put(id, screenInfo);
        }
    }

    public ScreenInfo getScreenInfo(String id) {
        ScreenInfo screenInfo = screens.get(id);
        if (screenInfo == null)
            throw new IllegalStateException("Screen '" + id + "' is not defined");
        return screenInfo;
    }
}
