/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.BrowserFrame;
import com.haulmont.cuba.web.widgets.CubaBrowserFrame;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class WebBrowserFrame extends WebAbstractResourceView<CubaBrowserFrame> implements BrowserFrame {

    public WebBrowserFrame() {
        component = createComponent();
    }

    protected CubaBrowserFrame createComponent() {
        return new CubaBrowserFrame();
    }

    public String getSandbox() {
        return component.getSandbox();
    }

    @Override
    public void setSandbox(String value) {
        component.setSandbox(value);
    }

    @Override
    public void setSandbox(Sandbox sandbox) {
        component.setSandbox(sandbox.getValue());
    }

    @Override
    public void setSandbox(EnumSet<Sandbox> sandboxSet) {
        if (sandboxSet != null) {
            component.setSandbox(sandboxSet.stream()
                    .map(Sandbox::getValue)
                    .collect(Collectors.joining(" ")));
        } else {
            component.setSandbox(null);
        }
    }

    @Override
    public void setSrcdoc(String value) {
        component.setSrcdoc(value);
    }

    @Override
    public String getSrcdoc() {
        return component.getSrcdoc();
    }

    @Override
    public void setAllow(String value) {
        component.setAllow(value);
    }

    @Override
    public void setAllow(Allow allow) {
        component.setAllow(allow.getValue());
    }

    @Override
    public void setAllow(EnumSet<Allow> allowSet) {
        if (allowSet != null) {
            component.setAllow(allowSet.stream()
                    .map(Allow::getValue)
                    .collect(Collectors.joining(" ")));
        } else {
            component.setAllow(null);
        }
    }

    @Override
    public String getAllow() {
        return component.getAllow();
    }

    @Override
    public String getReferrerPolicy() {
        return component.getReferrerPolicy();
    }

    @Override
    public void setReferrerPolicy(String value) {
        component.setReferrerPolicy(value);
    }

    @Override
    public void setReferrerPolicy(ReferrerPolicy referrerPolicy) {
        component.setReferrerPolicy(referrerPolicy.getValue());
    }
}
