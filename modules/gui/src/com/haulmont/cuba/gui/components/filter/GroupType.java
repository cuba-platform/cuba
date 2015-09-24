/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

/**
 * Generic filter grouping condition type.
 *
 * @author krivopustov
 * @version $Id$
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
