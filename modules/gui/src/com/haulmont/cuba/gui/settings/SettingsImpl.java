/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.settings;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class SettingsImpl implements Settings {

    protected final String name;
    protected transient SettingsClient settingsClient;
    protected Element root;
    protected boolean modified;

    public SettingsImpl(String name) {
        this.name = name;
    }

    protected SettingsClient getSettingsClient() {
        if (settingsClient == null) {
            settingsClient = AppBeans.get(SettingsClient.NAME);
        }
        return settingsClient;
    }

    protected void checkLoaded() {
        if (root == null) {
            // use cache
            String xml = getSettingsClient().getSetting(name);
            if (StringUtils.isBlank(xml)) {
                root = DocumentHelper.createDocument().addElement("settings");
            } else {
                root = Dom4j.readDocument(xml).getRootElement();
            }
        }
    }

    @Override
    public Element get() {
        checkLoaded();
        Element e = root.element("window");
        if (e == null) {
            e = root.addElement("window");
        }
        return e;
    }

    @Override
    public Element get(final String componentId) {
        checkLoaded();
        Element componentsRoot = root.element("components");
        if (componentsRoot == null) {
            componentsRoot = root.addElement("components");
        }
        for (Element e : ((List<Element>) componentsRoot.elements())) {
            if (componentId.equals(e.attributeValue("name"))) {
                return e;
            }
        }
        Element e = componentsRoot.addElement("component");
        e.addAttribute("name", componentId);
        return e;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = this.modified || modified;
    }

    @Override
    public void commit() {
        if (modified && root != null) {
            String xml = Dom4j.writeDocument(root.getDocument(), true);
            getSettingsClient().setSetting(name, xml);
            modified = false;
        }
    }
}