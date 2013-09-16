/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import org.apache.commons.collections.map.LinkedMap;

@SuppressWarnings("unchecked")
public class GroupInfo<P> {

    private LinkedMap group;
    private P groupProperty;
    
    public GroupInfo(LinkedMap group) {
        this.group = new LinkedMap(group);
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
