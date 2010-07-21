package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.container.XEnumerationAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.table.XCellRange;
import com.sun.star.text.*;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XCloseable;

/*
* Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: FONTANENKO VASILIY
* Created: 04.07.2010 13:38:18
*
* $Id$
*/
public class ODTUnoConverter {

    public static XDesktop asXDesktop(Object o) {
        return (XDesktop) UnoRuntime.queryInterface(XDesktop.class, o);
    }

    public static XComponentLoader asXComponentLoader(Object o) {
        return (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, o);
    }

    public static XStorable asXStorable(Object o) {
        return (XStorable) UnoRuntime.queryInterface(XStorable.class, o);
    }

    public static XCloseable asXCloseable(Object o) {
        return (XCloseable) UnoRuntime.queryInterface(XCloseable.class, o);
    }

    public static XTextDocument asXTextDocument(Object o) {
        return (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, o);
    }

    public static XTextContent asXTextContent(Object o) {
        return (XTextContent) UnoRuntime.queryInterface(XTextContent.class, o);
    }

    public static XText asXText(Object o) {
        return (XText) UnoRuntime.queryInterface(XText.class, o);
    }

    public static XTextRange asXTextRange(Object o) {
        return (XTextRange) UnoRuntime.queryInterface(XTextRange.class, o);
    }

    public static XEnumerationAccess asXEnumerationAccess(Object o) {
        return (XEnumerationAccess) UnoRuntime.queryInterface(XEnumerationAccess.class, o);
    }

    public static XServiceInfo asXServiceInfo(Object o) {
        return (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, o);
    }

    public static XTextTablesSupplier asXTextTablesSupplier(Object o) {
        return (XTextTablesSupplier) UnoRuntime.queryInterface(XTextTablesSupplier.class, o);
    }

    public static XCellRange asXCellRange(Object o) {
        return (XCellRange) UnoRuntime.queryInterface(XCellRange.class, o);
    }
}
