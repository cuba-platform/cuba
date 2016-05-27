/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.settings;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

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

    @Override
    public void delete() {
        getSettingsClient().deleteSettings(name);
        modified = false;
    }
}