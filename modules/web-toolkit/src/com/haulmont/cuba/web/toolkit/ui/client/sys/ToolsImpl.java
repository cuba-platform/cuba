/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sys;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class ToolsImpl {

    protected JavaScriptObject falseFunction;

    public ToolsImpl() {
        this.falseFunction = initFalseFunction();
    }

    public native int parseSize(String s) /*-{
        try {
            var result = /^(\d+)(%|px|em|ex|in|cm|mm|pt|pc)$/.exec(s);
            return parseInt(result[1]);
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
        elem.innerHTML = text; //todo gorodnov: support line breaks
    }-*/;

    public native void setInnerText(Element elem, String text) /*-{
        while (elem.firstChild) {
            elem.removeChild(elem.firstChild);
        }
        if (text != null) {
            var arr = text.replace(/\r/g, "").split("\n");
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

    public native boolean isRadio(Element e) /*-{
        return (e && e.tagName.toUpperCase() == "INPUT" && e.type == "radio");
    }-*/;

    public native boolean isCheckbox(Element e) /*-{
        return (e && e.tagName.toUpperCase() == "INPUT" && e.type == "checkbox");
    }-*/;

    protected native JavaScriptObject initFalseFunction() /*-{
        return function () {
            return false;
        };
    }-*/;

    protected native void setTextSelectionEnable(Element el) /*-{
        el.addEventListener("selectstart", this.@com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl::falseFunction, true);
    }-*/;

    protected native void setTextSelectionDisable(Element el) /*-{
        el.removeEventListener("selectstart", this.@com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl::falseFunction, true);
    }-*/;

    public native void textSelectionEnable(Element el, boolean enable) /*-{
        var ToolsImpl = this;

        // CAUTION Do not use jQuery disable text selection pack, it caches html nodes and we have memory leaks

        var walkEach = function (element, action) {
            if (typeof element != "undefined") {
                action(element);
                var children = element.childNodes;
                if (typeof children != "undefined") {
                    for (var i = 0; i < children.length; i++)
                        if (children[i].nodeType == 1)
                            walkEach(children[i], action);
                }
            }
        };

        if (!enable) {
            walkEach(el, ToolsImpl.@com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl::setTextSelectionDisable(Lcom/google/gwt/dom/client/Element;));
        } else {
            walkEach(el, ToolsImpl.@com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl::setTextSelectionEnable(Lcom/google/gwt/dom/client/Element;));
        }

        walkEach = null;
    }-*/;

    public native void removeElementWithEvents(Element el) /*-{
        $wnd.jQuery(el).remove();
    }-*/;

    public native void updatePrimaryAndDependentStyleNames(Element elem,
                                                           String newPrimaryStyle) /*-{
        var classes = elem.className.split(/\s+/);
        if (!classes) {
            return;
        }

        var oldPrimaryStyle = classes[0];
        var oldPrimaryStyleLen = oldPrimaryStyle.length;

        classes[0] = newPrimaryStyle;
        for (var i = 1, n = classes.length; i < n; i++) {
            var name = classes[i];
            if (name.length > oldPrimaryStyleLen
                && name.charAt(oldPrimaryStyleLen) == '-'
                && name.indexOf(oldPrimaryStyle) == 0) {
                classes[i] = newPrimaryStyle + name.substring(oldPrimaryStyleLen);
            }
        }
        elem.className = classes.join(" ");
    }-*/;

    public native boolean hasStyleName(Element el, String style) /*-{
        var classes = elem.className.split(/\s+/);
        if (!classes) {
            return false;
        }
        for (var i = 0; i < classes.length; i++) {
            if (classes[i] == style) return true;
        }
        return false;
    }-*/;

    public native void fixFlashTitleIEJS() /*-{
        var originalTitle = $doc.title.split("#")[0];
        var fixFlashTitleIEWorking = false;
        $doc.attachEvent('onpropertychange', function (evt) {
            if(evt.propertyName == 'title' && $doc.title != originalTitle && !fixFlashTitleIEWorking) {
                fixFlashTitleIEWorking = true;
                $doc.title = originalTitle;
                fixFlashTitleIEWorking = false;
            }
        });
    }-*/;
}