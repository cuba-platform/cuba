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

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

/**
 * Generic filter grouping condition type.
 *
*/
public enum GroupType {

    AND("and"),
    OR("or");

    private String xml;

    GroupType(String xml) {
        this.xml = xml;
    }

    public String getXml() {
        return xml;
    }

    public static GroupType fromXml(String xml) {
        for (GroupType groupType : GroupType.values()) {
            if (groupType.getXml().equals(xml))
                return groupType;
        }
        throw new UnsupportedOperationException("Unknown xml element: " + xml);
    }

    public String getLocCaption() {
        return AppBeans.get(Messages.class).getMainMessage("GroupType." + this.name());
    }
}
