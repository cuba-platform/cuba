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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Embedded;
import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class EmbeddedLoader extends AbstractComponentLoader<Embedded> {

    protected static final String URL_PREFIX = "url://";

    protected static final String FILE_PREFIX = "file://";

    protected static final String THEME_PREFIX = "theme://";

    @Override
    public void createComponent() {
        resultComponent = (Embedded) factory.createComponent(Embedded.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        String typeAttribute = element.attributeValue("type");
        if (StringUtils.isNotEmpty(typeAttribute)) {
            Embedded.Type type = Embedded.Type.valueOf(typeAttribute);
            resultComponent.setType(type);
        }

        String srcAttr = element.attributeValue("src");
        if (srcAttr != null) {
            if (srcAttr.startsWith(URL_PREFIX)) {
                String src = srcAttr.substring(URL_PREFIX.length());

                URL targetUrl;
                try {
                    targetUrl = new URL(src);
                } catch (MalformedURLException e) {
                    throw new GuiDevelopmentException("Incorrect URL in Embedded src attribute", context.getFullFrameId(),
                            "src", srcAttr);
                }

                resultComponent.setType(Embedded.Type.BROWSER);
                resultComponent.setSource(targetUrl);

            } else if (srcAttr.startsWith(THEME_PREFIX)) {
                resultComponent.setSource(srcAttr);
            } else if (srcAttr.startsWith(FILE_PREFIX)) {
                String src = srcAttr.substring(FILE_PREFIX.length());
                resultComponent.setType(Embedded.Type.OBJECT);
                resultComponent.setSource(src);
            } else {
                throw new GuiDevelopmentException("Illegal src attribute value. 'url://' or 'file://' or theme:// prefix expected",
                        context.getFullFrameId(), "src", srcAttr);
            }
        }

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);
    }
}