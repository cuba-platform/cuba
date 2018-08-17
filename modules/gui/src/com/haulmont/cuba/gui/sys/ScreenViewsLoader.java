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

package com.haulmont.cuba.gui.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import org.apache.commons.io.IOUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * Loads views defined in screen descriptors.
 */
@Component(ScreenViewsLoader.NAME)
public class ScreenViewsLoader {

    public static final String NAME = "cuba_ScreenViewsLoader";

    @Inject
    protected AbstractViewRepository viewRepository;

    @Inject
    protected Resources resources;

    /**
     * Deploy views defined in <code>metadataContext</code> of a frame.
     *
     * @param rootElement root element of a frame XML
     */
    public void deployViews(Element rootElement) {
        Element metadataContextEl = rootElement.element("metadataContext");
        if (metadataContextEl != null) {
            for (Element fileEl : Dom4j.elements(metadataContextEl, "deployViews")) {
                String resource = fileEl.attributeValue("name");
                InputStream resourceInputStream = getInputStream(resource);
                try {
                    viewRepository.deployViews(resourceInputStream);
                } finally {
                    IOUtils.closeQuietly(resourceInputStream);
                }
            }

            for (Element viewEl : Dom4j.elements(metadataContextEl, "view")) {
                viewRepository.deployView(metadataContextEl, viewEl);
            }
        }
    }

    protected InputStream getInputStream(String resource) {
        InputStream resourceInputStream = resources.getResourceAsStream(resource);
        if (resourceInputStream == null) {
            throw new RuntimeException("View resource not found: " + resource);
        }
        return resourceInputStream;
    }

}
