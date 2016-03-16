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
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;

/**
 * Screen's registration information.
 *
 * @see WindowConfig
 *
 */
public class WindowInfo {

    private String id;
    private Element descriptor;
    private Class screenClass;

    public WindowInfo(String id, Element descriptor) {
        this.id = id;
        this.descriptor = descriptor;
    }

    /**
     * Screen ID as set in <code>screens.xml</code>
     */
    public String getId() {
        return id;
    }

    /**
     * Screen template path as set in <code>screens.xml</code>
     */
    public String getTemplate() {
        return descriptor.attributeValue("template");
    }

    /**
     * Screen class as set in <code>screens.xml</code>
     */
    @Nullable
    public Class getScreenClass() {
        if (screenClass == null) {
            String className = descriptor.attributeValue("class");
            if (className != null) {
                Scripting scripting = AppBeans.get(Scripting.NAME);
                screenClass = scripting.loadClass(className);
            }
        }

        return screenClass;
    }

    public boolean getMultipleOpen() {
        return Boolean.parseBoolean(descriptor.attributeValue("multipleOpen"));
    }

    /**
     * The whole XML element of the screen as set in <code>screens.xml</code>
     */
    public Element getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Element descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String toString() {
        String template = getTemplate();
        return "id='" + id + '\'' +
                (template != null ? ", template=" + template : "") +
                (screenClass != null ? ", screenClass=" + screenClass : "");
    }
}