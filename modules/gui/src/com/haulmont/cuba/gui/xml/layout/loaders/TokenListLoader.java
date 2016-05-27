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
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TokenList;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class TokenListLoader extends AbstractFieldLoader<TokenList> {
    @Override
    public void createComponent() {
        resultComponent = (TokenList) factory.createComponent(TokenList.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadDatasource(resultComponent, element);

        loadVisible(resultComponent, element);
        loadEditable(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadIcon(resultComponent, element);
        loadDescription(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadCaptionProperty(resultComponent, element);
        loadPosition(resultComponent, element);

        loadInline(resultComponent, element);

        loadLookup(resultComponent, element);

        loadButton(resultComponent, element);

        loadSimple(resultComponent, element);

        loadClearEnabled(resultComponent, element);
        loadClearButton(resultComponent, element);
    }

    protected void loadClearEnabled(TokenList component, Element element) {
        String clearEnabled = element.attributeValue("clearEnabled");
        if (StringUtils.isNotEmpty(clearEnabled)) {
            component.setClearEnabled(Boolean.parseBoolean(clearEnabled));
        }
    }

    protected void loadClearButton(TokenList component, Element element) {
        Element buttonElement = element.element("clearButton");
        if (buttonElement != null) {
            String caption = buttonElement.attributeValue("caption");
            if (caption != null) {
                if (!StringUtils.isEmpty(caption)) {
                    caption = loadResourceString(caption);
                }
                component.setClearButtonCaption(caption);
            }

            String icon = buttonElement.attributeValue("icon");
            if (!StringUtils.isEmpty(icon)) {
                component.setClearButtonIcon(loadResourceString(icon));
            }
        }
    }

    protected void loadSimple(TokenList component, Element element) {
        String simple = element.attributeValue("simple");
        if (StringUtils.isNotEmpty(simple)) {
            component.setSimple(Boolean.parseBoolean(simple));
        } else {
            component.setSimple(false);
        }
    }

    protected void loadButton(TokenList component, Element element) {
        Element buttonElement = element.element("button");
        if (buttonElement != null) {
            String caption = buttonElement.attributeValue("caption");
            if (caption != null) {
                if (!StringUtils.isEmpty(caption)) {
                    caption = loadResourceString(caption);
                }
                component.setAddButtonCaption(caption);
            }

            String icon = buttonElement.attributeValue("icon");
            if (!StringUtils.isEmpty(icon)) {
                component.setAddButtonIcon(loadResourceString(icon));
            }
        }
    }

    protected void loadLookup(TokenList component, Element element) {
        Element lookupElement = element.element("lookup");
        if (lookupElement == null) {
            throw new GuiDevelopmentException("'tokenList' must contain 'lookup' element", context.getFullFrameId(),
                    "TokenList ID", element.attributeValue("id"));
        }

        String optionsDatasource = lookupElement.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(optionsDatasource)) {
            CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(optionsDatasource);
            component.setOptionsDatasource(ds);
        }

        String optionsCaptionProperty = lookupElement.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(optionsCaptionProperty)) {
            component.setOptionsCaptionMode(CaptionMode.PROPERTY);
            component.setOptionsCaptionProperty(optionsCaptionProperty);
        }

        String lookup = lookupElement.attributeValue("lookup");
        if (StringUtils.isNotEmpty(lookup)) {
            component.setLookup(Boolean.parseBoolean(lookup));
            if (component.isLookup()) {
                String lookupScreen = lookupElement.attributeValue("lookupScreen");
                if (StringUtils.isNotEmpty(lookupScreen)) {
                    component.setLookupScreen(lookupScreen);
                }
                String openType = lookupElement.attributeValue("openType");
                if (StringUtils.isNotEmpty(openType)) {
                    component.setLookupOpenMode(OpenType.valueOf(openType));
                }
            }
        }

        String multiSelect = lookupElement.attributeValue("multiselect");
        if (StringUtils.isNotEmpty(multiSelect)) {
            component.setMultiSelect(Boolean.parseBoolean(multiSelect));
        }

        loadFilterMode(component, lookupElement);
    }

    protected void loadInline(TokenList component, Element element) {
        String inline = element.attributeValue("inline");
        if (StringUtils.isNotEmpty(inline)) {
            component.setInline(Boolean.parseBoolean(inline));
        }
    }

    protected void loadPosition(TokenList component, Element element) {
        String position = element.attributeValue("position");
        if (StringUtils.isNotEmpty(position)) {
            component.setPosition(TokenList.Position.valueOf(position));
        }
    }

    protected void loadCaptionProperty(TokenList component, Element element) {
        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }
    }

    protected void loadDatasource(TokenList component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException(String.format("Datasource '%s' is not defined", datasource), context.getFullFrameId());
            }
            component.setDatasource(ds);
        }
    }

    protected void loadFilterMode(TokenList component, Element element) {
        final String filterMode = element.attributeValue("filterMode");
        if (!StringUtils.isEmpty(filterMode)) {
            component.setFilterMode(LookupField.FilterMode.valueOf(filterMode));
        }
    }
}