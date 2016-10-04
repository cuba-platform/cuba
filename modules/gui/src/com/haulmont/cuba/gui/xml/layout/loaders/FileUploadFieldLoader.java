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

import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.UploadField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.HashSet;

public class FileUploadFieldLoader extends AbstractFieldLoader<FileUploadField> {
    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadEnable(resultComponent, element);
        loadEditable(resultComponent, element);
        loadRequired(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadIcon(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadAccept(resultComponent, element);

        loadPermittedExtensions(resultComponent, element);

        loadDropZone(resultComponent, element);

        loadFileSizeLimit();

        loadDatasource(resultComponent, element);
        loadMode(resultComponent, element);
        loadShowFileName(resultComponent, element);

        loadClearButton(resultComponent, element);
        loadUploadButton(resultComponent, element);
    }

    protected void loadFileSizeLimit() {
        String fileSizeLimit = element.attributeValue("fileSizeLimit");
        if (StringUtils.isNotEmpty(fileSizeLimit)) {
            resultComponent.setFileSizeLimit(Long.valueOf(fileSizeLimit));
        }
    }

    protected void loadUploadButton(FileUploadField resultComponent, Element element) {
        String uploadButtonCaption = element.attributeValue("uploadButtonCaption");
        if (uploadButtonCaption != null) {
            resultComponent.setUploadButtonCaption(loadResourceString(uploadButtonCaption));
        }

        String uploadButtonIcon = element.attributeValue("uploadButtonIcon");
        if (StringUtils.isNotEmpty(uploadButtonIcon)) {
            resultComponent.setUploadButtonIcon(uploadButtonIcon);
        }

        String uploadButtonDescription = element.attributeValue("uploadButtonDescription");
        if (uploadButtonDescription != null) {
            resultComponent.setUploadButtonDescription(loadResourceString(uploadButtonDescription));
        }
    }

    protected void loadClearButton(FileUploadField resultComponent, Element element) {
        String showClearButton = element.attributeValue("showClearButton");
        if (StringUtils.isNotEmpty(showClearButton)) {
            resultComponent.setShowClearButton(Boolean.valueOf(showClearButton));
        }

        String clearButtonCaption = element.attributeValue("clearButtonCaption");
        if (clearButtonCaption != null) {
            resultComponent.setClearButtonCaption(loadResourceString(clearButtonCaption));
        }

        String clearButtonIcon = element.attributeValue("clearButtonIcon");
        if (StringUtils.isNotEmpty(clearButtonIcon)) {
            resultComponent.setClearButtonIcon(clearButtonIcon);
        }

        String clearButtonDescription = element.attributeValue("clearButtonDescription");
        if (clearButtonDescription != null) {
            resultComponent.setClearButtonDescription(loadResourceString(clearButtonDescription));
        }
    }

    protected void loadShowFileName(FileUploadField resultComponent, Element element) {
        String showFileName = element.attributeValue("showFileName");
        if (StringUtils.isNotEmpty(showFileName)) {
            resultComponent.setShowFileName(Boolean.valueOf(showFileName));
        }
    }

    protected void loadMode(FileUploadField resultComponent, Element element) {
        String fileStoragePutMode = element.attributeValue("fileStoragePutMode");
        if (StringUtils.isNotEmpty(fileStoragePutMode)) {
            resultComponent.setMode(FileUploadField.FileStoragePutMode.valueOf(fileStoragePutMode));
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (FileUploadField) factory.createComponent(FileUploadField.NAME);
        loadId(resultComponent, element);
    }

    protected void loadAccept(UploadField uploadField, Element element) {
        String accept = element.attributeValue("accept");
        if (StringUtils.isNotEmpty(accept)) {
            uploadField.setAccept(accept);
        }
    }

    protected void loadPermittedExtensions(UploadField uploadField, Element element) {
        String permittedExtensions = element.attributeValue("permittedExtensions");
        if (StringUtils.isNotEmpty(permittedExtensions)) {
            uploadField.setPermittedExtensions(new HashSet<>(Arrays.asList(permittedExtensions.split("\\s*,\\s*"))));
        }
    }

    protected void loadDropZone(UploadField uploadField, Element element) {
        String dropZoneId = element.attributeValue("dropZone");
        if (StringUtils.isNotEmpty(dropZoneId)) {
            Component dropZone = context.getFrame().getComponent(dropZoneId);
            if (dropZone instanceof BoxLayout) {
                uploadField.setDropZone(new UploadField.DropZone((BoxLayout) dropZone));
            }
        }

        String dropZonePrompt = element.attributeValue("dropZonePrompt");
        if (StringUtils.isNotEmpty(dropZonePrompt)) {
            uploadField.setDropZonePrompt(loadResourceString(dropZonePrompt));
        }
    }
}