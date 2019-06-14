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

package com.haulmont.cuba.gui.spring;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.sys.RouteDefinition;
import com.haulmont.cuba.gui.sys.UiControllerDefinition;
import com.haulmont.cuba.gui.sys.UiControllerMeta;
import com.haulmont.cuba.gui.sys.UiControllersConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.*;

public class ScreensConfigurationParser extends AbstractBeanDefinitionParser implements BeanDefinitionParser {

    public static final String BASE_PACKAGES = "base-packages";

    private static final Logger log = LoggerFactory.getLogger(ScreensConfigurationParser.class);

    @Override
    protected AbstractBeanDefinition parseInternal(@Nonnull Element element, @Nonnull ParserContext parserContext) {
        String attribute = element.getAttribute(BASE_PACKAGES);
        String[] packages = StringUtils.delimitedListToStringArray(attribute, ",", " ");

        log.trace("Registering @UiController configuration for packages {}", Arrays.toString(packages));

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UiControllersConfiguration.class);

        if (packages.length > 0) {
            builder.addPropertyValue("basePackages", Arrays.asList(packages));
        }

        NodeList screenElements = element.getChildNodes();
        List<UiControllerDefinition> definitions = new ArrayList<>();
        for (int i = 0; i < screenElements.getLength(); i++) {
            if (screenElements.item(i) instanceof Element) {
                Element item = (Element) screenElements.item(i);

                if ("screen".equals(item.getLocalName())) {
                    String id = item.getAttribute("id");
                    if (Strings.isNullOrEmpty(id)) {
                        parserContext.getReaderContext().error(
                                "Id is required for element '" + parserContext.getDelegate().getLocalName(element)
                                        + "' when registering screen", element);
                        continue;
                    }

                    String clazz = item.getAttribute("class");
                    if (Strings.isNullOrEmpty(clazz)) {
                        parserContext.getReaderContext().error(
                                "class is required for element '" + parserContext.getDelegate().getLocalName(element)
                                        + "' when registering screen", element);
                        continue;
                    }

                    definitions.add(new UiControllerDefinition(new UiControllerMeta() {
                        @Override
                        public String getId() {
                            return id;
                        }

                        @Override
                        public String getControllerClass() {
                            return clazz;
                        }

                        @Override
                        public RouteDefinition getRouteDefinition() {
                            return null;
                        }

                        @Override
                        public Map<String, Object> getAnnotationAttributes(String annotationName) {
                            return Collections.emptyMap();
                        }
                    }));
                }
            }
        }

        if (!definitions.isEmpty()) {
            builder.addPropertyValue("explicitDefinitions", definitions);
        }

        return builder.getBeanDefinition();
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}