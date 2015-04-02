/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sys;

import com.google.gwt.dom.client.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class ToolsImpl {

    protected native void setTextSelectionEnable(Element el) /*-{
        if (typeof $doc.textSelectionFalseFunction != "function") {
            $doc.textSelectionFalseFunction = function() {
                return false;
            };
        }

        el.addEventListener("selectstart", $doc.textSelectionFalseFunction, true);
    }-*/;

    protected native void setTextSelectionDisable(Element el) /*-{
        if (typeof $doc.textSelectionFalseFunction != "function") {
            $doc.textSelectionFalseFunction = function() {
                return false;
            };
        }

        el.removeEventListener("selectstart", $doc.textSelectionFalseFunction, true);
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