/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CreditsLoader {

    private Log log = LogFactory.getLog(getClass());

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
                    Boolean.valueOf(element.attributeValue("fork")));
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