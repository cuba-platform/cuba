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

import com.google.common.base.Splitter;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.BulkEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BulkEditorLoader extends AbstractComponentLoader<BulkEditor> {

    protected void loadValidators(BulkEditor component, Element element) {
        List<Element> validatorElements = element.elements("validator");
        if (!validatorElements.isEmpty()) {
            List<Field.Validator> modelValidators = new ArrayList<>();
            Map<String, Field.Validator> fieldValidators = new LinkedHashMap<>();

            for (Element validatorElement : validatorElements) {
                Field.Validator validator = loadValidator(validatorElement);
                String field = validatorElement.attributeValue("field");

                if (StringUtils.isNotBlank(field)) {
                    fieldValidators.put(field, validator);
                } else {
                    modelValidators.add(validator);
                }
            }

            if (!fieldValidators.isEmpty()) {
                component.setFieldValidators(fieldValidators);
            }

            if (!modelValidators.isEmpty()) {
                component.setModelValidators(modelValidators);
            }
        }
    }

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(BulkEditor.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadIcon(resultComponent, element);

        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);
        loadResponsive(resultComponent, element);

        loadTabIndex(resultComponent, element);

        UserSession userSession = getUserSessionSource().getUserSession();
        if (!userSession.isSpecificPermitted(BulkEditor.PERMISSION)) {
            resultComponent.setVisible(false);
        }

        String openType = element.attributeValue("openType");
        if (StringUtils.isNotEmpty(openType)) {
            resultComponent.setOpenType(WindowManager.OpenType.valueOf(openType));
        }

        String exclude = element.attributeValue("exclude");
        String includeProperties = element.attributeValue("includeProperties");

        if (StringUtils.isNotBlank(exclude) && StringUtils.isNotBlank(includeProperties)) {
            throw new GuiDevelopmentException("BulkEditor cannot define simultaneously exclude and includeProperties attributes",
                    getContext().getCurrentFrameId());
        }

        if (StringUtils.isNotBlank(exclude)) {
            resultComponent.setExcludePropertiesRegex(exclude.replace(" ", ""));
        }

        if (StringUtils.isNotBlank(includeProperties)) {
            resultComponent.setIncludeProperties(
                    Splitter.on(',').omitEmptyStrings().trimResults().splitToList(includeProperties)
            );
        }

        String listComponent = element.attributeValue("for");
        if (StringUtils.isEmpty(listComponent)) {
            throw new GuiDevelopmentException("'for' attribute of bulk editor is not specified",
                    context.getFullFrameId(), "componentId", resultComponent.getId());
        }

        String loadDynamicAttributes = element.attributeValue("loadDynamicAttributes");
        if (StringUtils.isNotEmpty(loadDynamicAttributes)) {
            resultComponent.setLoadDynamicAttributes(Boolean.parseBoolean(loadDynamicAttributes));
        }

        String useConfirmDialog = element.attributeValue("useConfirmDialog");
        if (StringUtils.isNotEmpty(useConfirmDialog)) {
            resultComponent.setUseConfirmDialog(Boolean.parseBoolean(useConfirmDialog));
        }

        context.addPostInitTask((context1, window) -> {
            // todo artamonov here we can use post wrap instead of post init
            if (resultComponent.getListComponent() == null) {
                Component bindComponent = resultComponent.getFrame().getComponent(listComponent);
                if (!(bindComponent instanceof ListComponent)) {
                    throw new GuiDevelopmentException("Specify 'for' attribute: id of table or tree",
                            context1.getFullFrameId(), "componentId", resultComponent.getId());
                }

                resultComponent.setListComponent((ListComponent) bindComponent);
            }
        });

        loadValidators(resultComponent, element);

        loadFocusable(resultComponent, element);
    }

    protected UserSessionSource getUserSessionSource() {
        return beanLocator.get(UserSessionSource.NAME);
    }
}