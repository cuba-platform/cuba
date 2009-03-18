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

import com.haulmont.cuba.toolkit.gwt.client.ui.IPagingTable;
import com.haulmont.cuba.toolkit.gwt.client.ui.ITreeTable;
import com.itmill.toolkit.terminal.gwt.client.DefaultWidgetSet;
import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;

public class WidgetSet extends DefaultWidgetSet {
    protected Class resolveWidgetType(UIDL uidl) {
        final String tag = uidl.getTag();
        if ("pagingtable".equals(tag)) {
            return IPagingTable.class;
//        } else if ("menubar".equals(tag)) {
//            return "com.haulmont.cuba.toolkit.gwt.client.ui.IMenuBar";
        } else if ("treetable".equals(tag)) {
            return ITreeTable.class;
        }
        return super.resolveWidgetType(uidl);
    }

    public Paintable createWidget(UIDL uidl) {
        final Class classType = resolveWidgetType(uidl);
        if (IPagingTable.class.equals(classType)) {
            return new IPagingTable();
//        } else if ("com.haulmont.cuba.toolkit.gwt.client.ui.IMenuBar".equals(className)) {
//            return new IMenuBar();
        } else if (ITreeTable.class.equals(classType)) {
            return new ITreeTable();
        }
        return super.createWidget(uidl);
    }
}
