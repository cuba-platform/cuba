/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.sys;

import com.google.gwt.dom.client.Element;

public class ToolsImpl {

    protected native void setTextSelectionEnable(Element el) /*-{
        if (typeof $wnd.textSelectionFalseFunction != "function") {
            $wnd.textSelectionFalseFunction = function() {
                return false;
            };
        }

        el.addEventListener("selectstart", $doc.textSelectionFalseFunction, true);
    }-*/;

    protected native void setTextSelectionDisable(Element el) /*-{
        if (typeof $wnd.textSelectionFalseFunction != "function") {
            $wnd.textSelectionFalseFunction = function() {
                return false;
            };
        }

        el.removeEventListener("selectstart", $wnd.textSelectionFalseFunction, true);
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