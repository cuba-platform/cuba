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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.Nonnull;

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
                root = AppBeans.get(Dom4jTools.class).readDocument(xml).getRootElement();
            }
        }
    }

    @Override
    @Nonnull
    public Element get() {
        checkLoaded();
        Element e = root.element("window");
        if (e == null) {
            e = root.addElement("window");
        }
        // automatically track changes on top level
        return new SettingsElementWrapper(e, this);
    }

    @Nonnull
    @Override
    public Element get(String componentId) {
        checkLoaded();
        Element componentsRoot = root.element("components");
        if (componentsRoot == null) {
            componentsRoot = root.addElement("components");
        }
        for (Element e : componentsRoot.elements()) {
            if (componentId.equals(e.attributeValue("name"))) {
                return new SettingsElementWrapper(e, this);
            }
        }
        Element e = componentsRoot.addElement("component");
        e.addAttribute("name", componentId);
        return new SettingsElementWrapper(e, this);
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    public void commit() {
        if (modified && root != null) {
            String xml = AppBeans.get(Dom4jTools.class).writeDocument(root.getDocument(), true);
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