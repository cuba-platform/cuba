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

import com.haulmont.cuba.gui.components.FileUploadField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.HashSet;

/**
 */
public class FileUploadFieldLoader extends AbstractComponentLoader<FileUploadField> {
    @Override
    public void createComponent() {
        resultComponent = (FileUploadField) factory.createComponent(FileUploadField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadIcon(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadAccept(resultComponent, element);

        loadPermittedExtentions(resultComponent, element);

        String fileSizeLimit = element.attributeValue("fileSizeLimit");
        if (StringUtils.isNotEmpty(fileSizeLimit)) {
            resultComponent.setFileSizeLimit(Long.valueOf(fileSizeLimit));
        }
    }

    protected void loadAccept(FileUploadField uploadField, Element element) {
        String accept = element.attributeValue("accept");
        if (StringUtils.isNotEmpty(accept)) {
            uploadField.setAccept(accept);
        }
    }

    protected void loadPermittedExtentions(FileUploadField uploadField, Element element) {
        String permittedExtensions = element.attributeValue("permittedExtensions");
        if (StringUtils.isNotEmpty(permittedExtensions)) {
            uploadField.setPermittedExtensions(new HashSet<>(Arrays.asList(permittedExtensions.split("\\s*,\\s*"))));
        }
    }
}