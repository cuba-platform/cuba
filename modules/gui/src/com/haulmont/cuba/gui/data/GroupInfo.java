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
package com.haulmont.cuba.gui.data;

import org.apache.commons.collections.map.LinkedMap;

public class GroupInfo<P> {

    private LinkedMap group;
    private P groupProperty;
    
    public GroupInfo(LinkedMap group) {
        this.group = new LinkedMap(group);
        //noinspection unchecked
        groupProperty = (P) group.get(group.size() - 1);
    }

    public Object getPropertyValue(P propertyPath) {
        if (!group.containsKey(propertyPath)) {
            throw new IllegalArgumentException();
        }
        return group.get(propertyPath);
    }

    public P getProperty() {
        return groupProperty;
    }

    public Object getValue() {
        if (groupProperty == null) {
            throw new IllegalStateException();
        }
        return getPropertyValue(groupProperty);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        for (int groupIndex = 0; groupIndex < group.size(); groupIndex++) {
            final Object value = group.getValue(groupIndex);
            sb.append("[")
                    .append(group.get(groupIndex))
                    .append(":")
                    .append(value != null
                            ? value.toString() : "")
                    .append("]")
                    .append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupInfo groupInfo = (GroupInfo) o;

        return toString().equals(groupInfo.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}