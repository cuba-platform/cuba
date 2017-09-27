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

package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Parses screen XML taking into account 'assign' elements.
 */
@Component(ScreenXmlParser.NAME)
public class ScreenXmlParser {
    
    public static final String NAME = "cuba_ScreenXmlParser";

    public Document parseDescriptor(InputStream stream) {
        checkNotNullArgument(stream, "Input stream is null");

        String template;
        try {
            template = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return parseDescriptor(template);
    }

    public Document parseDescriptor(String template) {
        checkNotNullArgument(template, "template is null");

        Document document = Dom4j.readDocument(template);

        replaceAssignParameters(document);

        return document;
    }

    protected void replaceAssignParameters(Document document) {
        Map<String, String> assignedParams = new HashMap<>();

        List<Element> assignElements = Dom4j.elements(document.getRootElement(), "assign");
        ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
        for (Element assignElement : assignElements) {
            String name = assignElement.attributeValue("name");
            if (StringUtils.isEmpty(name)) {
                throw new RuntimeException("'name' attribute of assign tag is empty");
            }

            String value = assignElement.attributeValue("value");
            if (StringUtils.isEmpty(value)) {
                throw new RuntimeException("'value' attribute of assign tag is empty");
            }

            if (StringUtils.startsWith(value, ThemeConstants.PREFIX)) {
                ThemeConstants theme = themeManager.getConstants();
                value = theme.get(value.substring(ThemeConstants.PREFIX.length()));
            }

            assignedParams.put(name, value);
        }

        if (!assignedParams.isEmpty()) {
            Element layoutElement = document.getRootElement().element("layout");
            if (layoutElement != null) {
                Dom4j.walkAttributesRecursive(layoutElement, (element, attribute) -> {
                    String attributeValue = attribute.getValue();
                    if (StringUtils.isNotEmpty(attributeValue)
                            && attributeValue.startsWith("${")
                            && attributeValue.endsWith("}")) {
                        String paramKey = attributeValue.substring(2, attributeValue.length() - 1);

                        String assignedValue = assignedParams.get(paramKey);
                        if (assignedValue == null) {
                            throw new RuntimeException("Unable to find value of assign param: " + paramKey);
                        }

                        attribute.setValue(assignedValue);
                    }
                });
            }
        }
    }
}
