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
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.UploadField;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.HashSet;

public abstract class AbstractUploadFieldLoader<T extends UploadField> extends AbstractComponentLoader<T> {

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

        loadHtmlSanitizerEnabled(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadTabIndex(resultComponent, element);

        loadAccept(resultComponent, element);

        loadPermittedExtensions(resultComponent, element);

        loadDropZone(resultComponent, element);
        loadPasteZone(resultComponent, element);

        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);

        String fileSizeLimit = element.attributeValue("fileSizeLimit");
        if (StringUtils.isNotEmpty(fileSizeLimit)) {
            resultComponent.setFileSizeLimit(Long.parseLong(fileSizeLimit));
        }
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
            Component dropZone = findComponent(dropZoneId);
            if (dropZone instanceof BoxLayout) {
                uploadField.setDropZone(new UploadField.DropZone((BoxLayout) dropZone));
            } else if (dropZone != null) {
                throw new GuiDevelopmentException("Unsupported dropZone class " + dropZone.getClass().getName(),
                        context);
            } else {
                throw new GuiDevelopmentException("Unable to find dropZone component with id: " + dropZoneId, context);
            }
        }

        String dropZonePrompt = element.attributeValue("dropZonePrompt");
        if (StringUtils.isNotEmpty(dropZonePrompt)) {
            uploadField.setDropZonePrompt(loadResourceString(dropZonePrompt));
        }
    }

    protected void loadPasteZone(UploadField uploadField, Element element) {
        String pasteZoneId = element.attributeValue("pasteZone");
        if (StringUtils.isNotEmpty(pasteZoneId)) {
            Component pasteZone = findComponent(pasteZoneId);
            if (pasteZone instanceof ComponentContainer) {
                uploadField.setPasteZone((ComponentContainer) pasteZone);
            } else if (pasteZone != null) {
                throw new GuiDevelopmentException("Unsupported pasteZone class " + pasteZone.getClass().getName(),
                        context);
            } else {
                throw new GuiDevelopmentException("Unable to find pasteZone component with id: " + pasteZoneId, context);
            }
        }
    }
}