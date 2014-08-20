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