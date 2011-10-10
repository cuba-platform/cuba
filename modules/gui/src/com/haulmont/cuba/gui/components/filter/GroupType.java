/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter;

/**
 * Generic filter grouping condition type.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
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
}
