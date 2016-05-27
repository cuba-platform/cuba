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

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import java.util.*;

public class CreditsLoader {

    private Logger log = LoggerFactory.getLogger(getClass());

    private List<CreditsItem> items = new ArrayList<>();

    private Map<String, String> licenses = new HashMap<>();

    public List<CreditsItem> getItems() {
        return items;
    }

    public CreditsLoader load() {
        String configProperty = AppContext.getProperty("cuba.creditsConfig");
        if (StringUtils.isBlank(configProperty)) {
            log.info("Property cuba.creditsConfig is empty");
            return this;
        }

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        String[] locations = tokenizer.getTokenArray();

        for (String location : locations) {
            Resources resources = AppBeans.get(Resources.NAME);
            String xml = resources.getResourceAsString(location);
            if (xml == null) {
                log.debug("Resource " + location + " not found, ignore it");
                continue;
            }
            Element rootElement = Dom4j.readDocument(xml).getRootElement();
            loadLicenses(rootElement);
            loadConfig(rootElement);
        }

        Collections.sort(items);

        return this;
    }

    private void loadLicenses(Element rootElement) {
        Element licensesEl = rootElement.element("licenses");
        if (licensesEl == null)
            return;

        for (Element element : Dom4j.elements(licensesEl)) {
            licenses.put(element.attributeValue("id"), element.getText());
        }
    }

    private void loadConfig(Element rootElement) {
        Element itemsEl = rootElement.element("items");
        if (itemsEl == null)
            return;

        for (Element element : Dom4j.elements(itemsEl)) {
            CreditsItem item = new CreditsItem(element.attributeValue("name"), element.attributeValue("web"),
                    element.attributeValue("license"), loadLicense(element), loadAcknowledgement(element),
                    Boolean.parseBoolean(element.attributeValue("fork")));
            if (items.contains(item)) {
                items.set(items.indexOf(item), item);
            } else {
                items.add(item);
            }
        }
    }

    private String loadLicense(Element element) {
        String licenseRef = element.attributeValue("license");
        if (licenseRef != null) {
            String license = licenses.get(licenseRef);
            if (license == null)
                throw new IllegalStateException("License text for " + licenseRef + " not found");
            return license;
        } else {
            Element licenseEl = element.element("license");
            if (licenseEl == null)
                throw new IllegalStateException("Neither license attribute, nor license element is not set for " + element.attributeValue("name"));
            return licenseEl.getText();
        }
    }

    private String loadAcknowledgement(Element element) {
        Element acknowledgmentEl = element.element("acknowledgment");
        return acknowledgmentEl == null ? null : acknowledgmentEl.getText();
    }
}