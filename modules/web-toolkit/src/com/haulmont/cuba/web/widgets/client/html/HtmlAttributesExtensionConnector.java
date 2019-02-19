/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.html;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.HtmlAttributesExtension;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.haulmont.cuba.web.widgets.client.html.HtmlAttributesExtensionState.DEFAULT_SELECTOR;

@Connect(HtmlAttributesExtension.class)
public class HtmlAttributesExtensionConnector extends AbstractExtensionConnector {

    protected HtmlAttributesClientRpc rpc = new HtmlAttributesClientRpc() {
        @Override
        public void removeCssProperties(Map<String, Set<String>> propertiesToRemove) {
            for (Map.Entry<String, Set<String>> entry : propertiesToRemove.entrySet()) {
                withElement(entry.getKey(), element -> {
                    for (String property : entry.getValue()) {
                        element.getStyle().clearProperty(property);
                    }
                });
            }
        }

        @Override
        public void removeDomAttributes(Map<String, Set<String>> attributesToRemove) {
            for (Map.Entry<String, Set<String>> entry : attributesToRemove.entrySet()) {
                withElement(entry.getKey(), element -> {
                    for (String attribute : entry.getValue()) {
                        element.removeAttribute(attribute);
                    }
                });
            }
        }
    };

    public HtmlAttributesExtensionConnector() {
        registerRpc(HtmlAttributesClientRpc.class, rpc);
    }

    @Override
    protected void extend(ServerConnector target) {
        ((AbstractComponentConnector) target).getWidget();
    }

    @Override
    public HtmlAttributesExtensionState getState() {
        return (HtmlAttributesExtensionState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        HtmlAttributesExtensionState state = getState();

        for (Map.Entry<String, Set<AttributeInfo>> entry : state.attributes.entrySet()) {
            withElement(entry.getKey(), element -> {
                for (AttributeInfo attributeInfo : entry.getValue()) {
                    String name = attributeInfo.getName();
                    String value = attributeInfo.getValue();
                    switch (attributeInfo.getType()) {
                        case DOM:
                            element.setAttribute(name, value);
                            break;
                        case CSS:
                            element.getStyle().setProperty(name, value);
                            break;
                    }
                }
            });
        }
    }

    protected void withElement(String querySelector, Consumer<Element> action) {
        ServerConnector parent = getParent();

        if (parent instanceof AbstractComponentConnector) {
            Widget widget = ((AbstractComponentConnector) parent).getWidget();
            if (widget != null) {
                Element element = widget.getElement();
                if (DEFAULT_SELECTOR.equals(querySelector)) {
                    action.accept(element);
                } else {
                    NodeList<Element> subElements = findSubElements(element, querySelector);
                    for (int i = 0; i < subElements.getLength(); i++) {
                        action.accept(subElements.getItem(i));
                    }
                }
            }
        }
    }

    private static native NodeList<Element> findSubElements(Element parent, String querySelector) /*-{
        return parent.querySelectorAll(querySelector);
    }-*/;
}