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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.BrowserFrame;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class BrowserFrameLoader extends AbstractResourceViewLoader<BrowserFrame> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(BrowserFrame.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadSandbox(resultComponent, element);
        loadSrcdoc(resultComponent, element);
        loadSrcdocFile(resultComponent, element);
        loadAllow(resultComponent, element);
        loadReferrerpolicy(resultComponent, element);
    }

    protected void loadSandbox(BrowserFrame resultComponent, Element element) {
        String sandbox = element.attributeValue("sandbox");
        if (sandbox != null) {
            resultComponent.setSandbox(sandbox);
        }
    }

    protected void loadSrcdoc(BrowserFrame resultComponent, Element element) {
        String srcdoc = element.attributeValue("srcdoc");
        if (StringUtils.isNotEmpty(srcdoc)) {
            resultComponent.setSrcdoc(srcdoc);
        }
    }

    protected void loadSrcdocFile(BrowserFrame resultComponent, Element element) {
        String srcdocFile = element.attributeValue("srcdocFile");
        if (StringUtils.isNotEmpty(srcdocFile)) {
            Resources resources = beanLocator.get(Resources.class);
            String resource = resources.getResourceAsString(srcdocFile);

            if (resource == null) {
                String msg = String.format("Can't load srcdocFile in BrowserFrame. The file with given path not found: %s",
                        srcdocFile);
                throw new GuiDevelopmentException(msg, context.getFullFrameId());
            }

            resultComponent.setSrcdoc(resource);
        }
    }

    protected void loadAllow(BrowserFrame resultComponent, Element element) {
        String allow = element.attributeValue("allow");
        if (StringUtils.isNotEmpty(allow)) {
            resultComponent.setAllow(allow);
        }
    }

    protected void loadReferrerpolicy(BrowserFrame resultComponent, Element element) {
        String referrerpolicy = element.attributeValue("referrerpolicy");
        if (StringUtils.isNotEmpty(referrerpolicy)) {
            resultComponent.setReferrerPolicy(referrerpolicy);
        }
    }
}