/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.RenderInformation;

/**
 * @author gorodnov
 * @version $Id$
 */
public class Tools {
    private static ToolsImpl impl;

    static {
        impl = GWT.create(ToolsImpl.class);
    }

    public static int parseSize(String s) {
        return impl.parseSize(s);
    }

    public static String format(String s) {
        return impl.format(s);
    }

    public static void removeChildren(Element e) {
        int childCount = DOM.getChildCount(e);
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                DOM.removeChild(e, DOM.getChild(e, 0));
            }
        }
    }

    public static void setInnerHTML(Element elem, String text) {
        impl.setInnerHTML(elem, text);
    }

    public static void setInnerText(Element elem, String text) {
        impl.setInnerText(elem, text);
    }

    public static boolean isRadio(Element elem) {
        return impl.isRadio(elem);
    }

    public static boolean isCheckbox(Element elem) {
        return impl.isCheckbox(elem);
    }

    public static void textSelectionEnable(Element el, boolean b) {
        impl.textSelectionEnable(el, b);
    }

    public static void removeElementWithEvents(Element el) {
        impl.removeElementWithEvents(el);
    }

    public static String setStyleName(Element el, String style) {
        if (style == null) throw new RuntimeException("Style cannot be null");
        style = style.trim();
        DOM.setElementProperty(el, "className", style);
        return style;
    }

    public static String getStyleName(Element el) {
        return DOM.getElementProperty(el, "className");
    }

    public static String addStyleName(Element el, String style) {
        if (style == null) throw new RuntimeException("Style cannot be null");
        style = style.trim();
        el.addClassName(style);
        return style;
    }

    public static void removeStyleName(Element el, String style) {
        if (style == null) throw new RuntimeException("Style cannot be null");
        style = style.trim();
        el.removeClassName(style);
    }

    public static String setStylePrimaryName(Element el, String style) {
        if (style == null) throw new RuntimeException("Style cannot be null");
        style = style.trim();
        impl.updatePrimaryAndDependentStyleNames(el, style);
        return style;
    }

    public static String getStylePrimaryName(Element el) {
        String className = DOM.getElementProperty(el, "className");
        int spaceIdx = className.indexOf(' ');
        if (spaceIdx >= 0) {
            return className.substring(0, spaceIdx);
        }
        return className;
    }

    public static String addStyleDependentName(Element el, String styleSuffix) {
        String s = getStylePrimaryName(el) + '-' + styleSuffix;
        addStyleName(el, s);
        return s;
    }

    public static void removeStyleDependentName(Element el, String styleSuffix) {
        removeStyleName(el, getStylePrimaryName(el) + '-' + styleSuffix);
    }

    public static boolean hasStyleDependentName(Element el, String styleSuffix) {
        return hasStyleName(el, getStylePrimaryName(el) + '-' + styleSuffix);
    }

    public static boolean hasStyleName(Element el, String style) {
        if (style == null) throw new RuntimeException("Style cannot be null");
        style = style.trim();
        return impl.hasStyleName(el, style);
    }

    public static String[] getStyleNames(Element el) {
        return getStyleName(el).split("[\\s+]");
    }

    public static RenderInformation.Size definePaddingBorders(Element el) {
        String w = DOM.getStyleAttribute(el, "width");
        String h = DOM.getStyleAttribute(el, "height");

        DOM.setStyleAttribute(el, "overflow", "hidden");
        DOM.setStyleAttribute(el, "width", "0px");
        DOM.setStyleAttribute(el, "height", "0px");

        RenderInformation.Size s = new RenderInformation.Size();
        s.setWidth(el.getOffsetWidth());
        s.setHeight(el.getOffsetHeight());

        DOM.setStyleAttribute(el, "width", w);
        DOM.setStyleAttribute(el, "height", h);
        DOM.setStyleAttribute(el, "overflow", "");

        return s;
    }

    public static void replaceClassNames(Element element, String from, String to){
        String className = element.getClassName();
        String newClassName = "";
        String[] classNames = className.split(" ");
        for (String classNamePart : classNames) {
            if (classNamePart.startsWith(from + "-"))
                classNamePart = classNamePart.replace(from + "-", to + "-");
            else if (classNamePart.equals(from))
                classNamePart = to;

            newClassName = newClassName + " " + classNamePart;
        }
        element.setClassName(newClassName.trim());
    }

    public static void fixFlashTitleIE8() {
        // if url has '#' then title changed in ie8 after flash loaded. This fix changed set normal title
        if (BrowserInfo.get().isIE8()) {
            impl.fixFlashTitleIE8JS();
        }
    }

}