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

@Connect(HtmlAttributesExtension.class)
public class HtmlAttributesExtensionConnector extends AbstractExtensionConnector {

    protected HtmlAttributesClientRpc rpc = new HtmlAttributesClientRpc() {
        @Override
        public void removeCssProperties(Set<String> propertyNames) {
            withElement(element -> {
                for (String property : propertyNames) {
                    element.getStyle().clearProperty(property);
                }
            });
        }

        @Override
        public void removeDomAttributes(Set<String> attributeNames) {
            withElement(element -> {
                for (String attributeName : attributeNames) {
                    element.removeAttribute(attributeName);
                }
            });
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

        withElement(element -> {
            HtmlAttributesExtensionState state = getState();

            for (Map.Entry<String, String> entry : state.dom.entrySet()) {
                element.setAttribute(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : state.css.entrySet()) {
                element.getStyle().setProperty(entry.getKey(), entry.getValue());
            }
        });
    }

    protected void withElement(Consumer<Element> action) {
        ServerConnector parent = getParent();

        if (parent instanceof AbstractComponentConnector) {
            Widget widget = ((AbstractComponentConnector) parent).getWidget();
            if (widget != null) {
                action.accept(widget.getElement());
            }
        }
    }
}