/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2009 15:37:57
 *
 * $Id$
 */
package com.haulmont.cuba.gui.settings;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.UserSettingService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public class SettingsImpl implements Settings, Serializable {

    private final String name;
    private transient UserSettingService service;
    private Element root;
    private boolean modified;

    private static final long serialVersionUID = 3938766157133492378L;

    public SettingsImpl(String name, UserSettingService service) {
        this.name = name;
        this.service = service;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        service = ServiceLocator.lookup(UserSettingService.NAME);
    }

    private void checkLoaded() {
        if (root == null) {
            String xml = service.loadSetting(AppConfig.getInstance().getClientType(), name);
            if (StringUtils.isBlank(xml)) {
                root = DocumentHelper.createDocument().addElement("settings");
            } else {
                root = Dom4j.readDocument(xml).getRootElement();
            }
        }
    }

    public Element get() {
        checkLoaded();
        Element e = root.element("window");
        if (e == null)
            e = root.addElement("window");
        return e;
    }

    public Element get(final String componentId) {
        checkLoaded();
        Element componentsRoot = root.element("components");
        if (componentsRoot == null) {
            componentsRoot = root.addElement("components");
        }
        for (Element e : ((List<Element>) componentsRoot.elements())) {
            if (componentId.equals(e.attributeValue("name")))
                return e;
        }
        Element e = componentsRoot.addElement("component");
        e.addAttribute("name", componentId);
        return e;
    }

    public void setModified(boolean modified) {
        this.modified = this.modified || modified;
    }

    public void commit() {
        if (modified && root != null) {
            String xml = Dom4j.writeDocument(root.getDocument(), true);
            service.saveSetting(AppConfig.getInstance().getClientType(), name, xml);
            modified = false;
        }
    }
}
