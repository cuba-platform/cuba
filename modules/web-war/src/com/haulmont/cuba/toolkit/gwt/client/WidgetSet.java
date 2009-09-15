/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 10.12.2008 11:51:21
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client;

import com.haulmont.cuba.toolkit.gwt.client.ui.*;
import com.vaadin.terminal.gwt.client.DefaultWidgetSet;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.IScrollTable;

public class WidgetSet extends DefaultWidgetSet {
    protected Class resolveWidgetType(UIDL uidl) {
        final String tag = uidl.getTag();
        if ("treetable".equals(tag)) {
            if (uidl.hasAttribute("pagingMode") && "PAGE".equals(uidl.getStringAttribute("pagingMode"))) {
                return IPageTreeTable.class;
            } else {
                return IScrollTreeTable.class;
            }
        } else if ("table".equals(tag)) {
            if (uidl.hasAttribute("pagingMode") && "PAGE".equals(uidl.getStringAttribute("pagingMode"))) {
                return IPageTable.class;
            } else {
                return IScrollTable.class;
            }
        } else if ("scrollablepanel".equals(tag)) {
            return IScrollablePanel.class;
        } else if ("select".equals(tag)) {
            if (uidl.hasAttribute("type")) {
                final String type = uidl.getStringAttribute("type");
                if (type.equals("twincol")) {
                    return com.vaadin.terminal.gwt.client.ui.VTwinColSelect.class;
                }
                if (type.equals("optiongroup")) {
                    return com.vaadin.terminal.gwt.client.ui.VOptionGroup.class;
                }
                if (type.equals("native")) {
                    return com.vaadin.terminal.gwt.client.ui.VNativeSelect.class;
                }
                if (type.equals("list")) {
                    return com.vaadin.terminal.gwt.client.ui.VListSelect.class;
                }
            } else {
                if (uidl.hasAttribute("selectmode")
                        && uidl.getStringAttribute("selectmode")
                                .equals("multi")) {
                    return com.vaadin.terminal.gwt.client.ui.VListSelect.class;
                } else {
                    return IFilterSelect.class;
                }
            }
        } else if ("horizontalBox".equals(tag) || "verticalBox".equals(tag)) {
            return IBox.class;
        } else if ("togglepanel".equals(tag)) {
            return ITogglePanel.class;
        }

        return super.resolveWidgetType(uidl);
    }

    public Paintable createWidget(UIDL uidl) {
        final Class classType = resolveWidgetType(uidl);
        if (IScrollTreeTable.class.equals(classType)) {
            return new IScrollTreeTable();
        } if (IPageTreeTable.class.equals(classType)) {
            return new IPageTreeTable();
        } else if (IScrollablePanel.class.equals(classType)) {
            return new IScrollablePanel();
        } else if (IFilterSelect.class.equals(classType)) {
            return new IFilterSelect();
        } else if (IBox.class.equals(classType)) {
            return new IBox();
        } else if (ITogglePanel.class.equals(classType)) {
            return new ITogglePanel();
        } else if (IPageTable.class.equals(classType)) {
            return new IPageTable();
        } else if (IScrollTable.class.equals(classType)) {
            return new IScrollTable();
        }
        return super.createWidget(uidl);
    }
}
