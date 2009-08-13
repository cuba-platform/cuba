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
import com.itmill.toolkit.terminal.gwt.client.DefaultWidgetSet;
import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;

public class WidgetSet extends DefaultWidgetSet {
    protected Class resolveWidgetType(UIDL uidl) {
        final String tag = uidl.getTag();
        /*if ("pagingtable".equals(tag)) {
            return IPagingTable.class;
        } else */
        if ("treetable".equals(tag)) {
            return IScrollTreeTable.class;
        } else if ("scrollablepanel".equals(tag)) {
            return IScrollablePanel.class;
        } else if ("select".equals(tag)) {
            if (uidl.hasAttribute("type")) {
                final String type = uidl.getStringAttribute("type");
                if (type.equals("twincol")) {
                    return com.itmill.toolkit.terminal.gwt.client.ui.ITwinColSelect.class;
                }
                if (type.equals("optiongroup")) {
                    return com.itmill.toolkit.terminal.gwt.client.ui.IOptionGroup.class;
                }
                if (type.equals("native")) {
                    return com.itmill.toolkit.terminal.gwt.client.ui.INativeSelect.class;
                }
                if (type.equals("list")) {
                    return com.itmill.toolkit.terminal.gwt.client.ui.IListSelect.class;
                }
            } else {
                if (uidl.hasAttribute("selectmode")
                        && uidl.getStringAttribute("selectmode")
                                .equals("multi")) {
                    return com.itmill.toolkit.terminal.gwt.client.ui.IListSelect.class;
                } else {
                    return IFilterSelect.class;
                }
            }
        } else if ("label".equals(tag)) {
            return ILabel.class;
        } else if ("horizontalBox".equals(tag) || "verticalBox".equals(tag)) {
            return IBox.class;
        } else if ("togglepanel".equals(tag)) {
            return ITogglePanel.class;
        }

        return super.resolveWidgetType(uidl);
    }

    public Paintable createWidget(UIDL uidl) {
        final Class classType = resolveWidgetType(uidl);
        if (IPagingTable.class.equals(classType)) {
            return new IPagingTable();
        } else if (IScrollTreeTable.class.equals(classType)) {
            return new IScrollTreeTable();
        } else if (IScrollablePanel.class.equals(classType)) {
            return new IScrollablePanel();
        } else if (IFilterSelect.class.equals(classType)) {
            return new IFilterSelect();
        } else if (ILabel.class.equals(classType)) {
            return new ILabel();
        } else if (IBox.class.equals(classType)) {
            return new IBox();
        } else if (ITogglePanel.class.equals(classType)) {
            return new ITogglePanel();
        }
        return super.createWidget(uidl);
    }
}
