/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 04.07.2010 13:38:18
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.oo;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.*;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.table.XCellRange;
import com.sun.star.text.*;
import com.sun.star.uno.Any;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XReplaceable;
import com.sun.star.view.XSelectionSupplier;

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

    public static XPropertySet asXPropertySet(Object o) {
        return (XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, o);
    }

    public static XNameContainer asXNameContainer(Object o) {
        return (XNameContainer) UnoRuntime.queryInterface(com.sun.star.container.XNameContainer.class, o);
    }

    public static XShape asXShape(Object o) {
        return (XShape) UnoRuntime.queryInterface(com.sun.star.drawing.XShape.class, o);
    }

    public static XMultiServiceFactory asXMultiServiceFactory(Object o) {
        return (XMultiServiceFactory) UnoRuntime.queryInterface(com.sun.star.lang.XMultiServiceFactory.class, o);
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

    public static XDispatchProvider asXDispatchProvider(Object o) {
        return (XDispatchProvider) UnoRuntime.queryInterface(XDispatchProvider.class, o);
    }

    public static XTextViewCursorSupplier asXTextCursorSupplier(Object o) {
        return (XTextViewCursorSupplier) UnoRuntime.queryInterface(XTextViewCursorSupplier.class, o);
    }

    public static XDispatchHelper asXDispatchHelper(Object o) {
        return (XDispatchHelper) UnoRuntime.queryInterface(XDispatchHelper.class, o);
    }

    public static XSelectionSupplier asXSelectionSupplier(Object o) {
        return (XSelectionSupplier) UnoRuntime.queryInterface(XSelectionSupplier.class, o);
    }

    public static XReplaceable asXReplaceable(Object o) {
        return (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, o);
    }

    public static Any createAny(Object o) {
        return new Any(new Type(o.getClass()), o);
    }
}