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
        } else if ("grouptable".equals(tag)) {
            return IScrollGroupTable.class;
        } else if ("scrollablepanel".equals(tag)) {
            return IScrollablePanel.class;
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
        } else if (IBox.class.equals(classType)) {
            return new IBox();
        } else if (ITogglePanel.class.equals(classType)) {
            return new ITogglePanel();
        } else if (IPageTable.class.equals(classType)) {
            return new IPageTable();
        } else if (IScrollTable.class.equals(classType)) {
            return new IScrollTable();
        } else if (IScrollGroupTable.class.equals(classType)) {
            return new IScrollGroupTable();
        }
        return super.createWidget(uidl);
    }
}
