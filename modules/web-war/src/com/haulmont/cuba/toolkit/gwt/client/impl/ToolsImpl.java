/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 19.12.2008 14:16:24
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.impl;

import com.google.gwt.dom.client.Element;

public class ToolsImpl {

    public native int parseSize(String s) /*-{
         try {
            var result = /^(\d+)(%|px|em|ex|in|cm|mm|pt|pc)$/.testexec(s);
            return parseInt(result[0]);
         } catch (e) {
            return -1;
         }
    }-*/;

    public native String format(String message) /*-{
        return message.replace(/\[br\]/g, "<br/>")
                .replace(/\[b\]/g, "<b>")
                .replace(/\[\/b\]/g, "</b>")
                .replace(/\[i\]/g, "<i>")
                .replace(/\[\/i\]/g, "</i>");
    }-*/;

    public native void setInnerHTML(Element elem, String text) /*-{
        elem.innerHTML = text; //todo
    }-*/;

    public native void setInnerText(Element elem, String text) /*-{
        while (elem.firstChild) {
            elem.removeChild(elem.firstChild);
        }
        if (text != null) {
            var arr = new Array();
            arr = text.replace(/\r/g, "").split("\n");
            if (arr.length > 0) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i]) {
                        elem.appendChild($doc.createTextNode(arr[i]));
                        elem.appendChild($doc.createElement("br"));
                    }
                }
            } else {
                elem.appendChild($doc.createTextNode(text));
            }
        }
    }-*/;

}
