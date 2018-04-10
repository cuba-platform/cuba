/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

import com.google.common.base.Strings;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.dom4j.Element;

@com.haulmont.chile.core.annotations.MetaClass(name = "sec$ScreenComponentDescriptor")
@SystemLevel
public class ScreenComponentDescriptor extends BaseUuidEntity {

    protected Element element;

    @MetaProperty
    protected ScreenComponentDescriptor parent;

    public ScreenComponentDescriptor(Element element, ScreenComponentDescriptor parent) {
        this.element = element;
        this.parent = parent;
    }

    @MetaProperty
    public String getCaption() {
        return toString();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public ScreenComponentDescriptor getParent() {
        return parent;
    }

    public void setParent(ScreenComponentDescriptor parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String id = element.attributeValue("id");
        id = Strings.isNullOrEmpty(id) ? element.attributeValue("property") : id;

        if (!Strings.isNullOrEmpty(id)) {
            sb.append(id).append(": ");
        }
        sb.append("<").append(element.getName()).append(">");

        return sb.toString();
    }
}
