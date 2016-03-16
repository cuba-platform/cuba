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

package com.haulmont.cuba.web.toolkit.ui.client.button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.haulmont.cuba.web.toolkit.ui.CubaCopyButtonExtension;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VButton;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaCopyButtonExtension.class)
public class CubaCopyButtonExtensionConnector extends AbstractExtensionConnector {

    @Override
    public CubaCopyButtonExtensionState getState() {
        return (CubaCopyButtonExtensionState) super.getState();
    }

    @Override
    protected void extend(ServerConnector target) {
        final VButton button = (VButton) ((ComponentConnector) target).getWidget();

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (getState().copyTargetSelector != null) {
                    boolean success = copyToClipboard(getState().copyTargetSelector.startsWith(".")
                            ? getState().copyTargetSelector
                            : "." + getState().copyTargetSelector);
                    getRpcProxy(CubaCopyButtonExtensionServerRpc.class).copied(success);
                }
            }
        });

    }

    private native boolean copyToClipboard(String selector) /*-{
        var copyTextArea = $doc.querySelector(selector);
        copyTextArea.select();
        try {
            return $doc.execCommand('copy');
        } catch (e) {
            console.log(e.message);
            return false;
        }
    }-*/;
}
