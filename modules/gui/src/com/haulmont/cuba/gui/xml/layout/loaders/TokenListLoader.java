/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class TokenListLoader extends AbstractFieldLoader {

    public TokenListLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Field field, Element element, Component parent) {
        TokenList component = (TokenList) field;

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadDatasource(component, element);

        loadVisible(component, element);
        loadEditable(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);
        loadDescription(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String position = element.attributeValue("position");
        if (!StringUtils.isEmpty(position)) {
            component.setPosition(TokenList.Position.valueOf(position));
        }

        String inline = element.attributeValue("inline");
        if (!StringUtils.isEmpty(inline)) {
            component.setInline(BooleanUtils.toBoolean(inline));
        }

        Element lookupElement = element.element("lookup");
        if (lookupElement == null) {
            throw new GuiDevelopmentException("'tokenList' must contain 'lookup' element", context.getFullFrameId(),
                    "TokenList ID", element.attributeValue("id"));
        }

        String optionsDatasource = lookupElement.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(optionsDatasource)) {
            final CollectionDatasource ds = context.getDsContext().get(optionsDatasource);
            component.setOptionsDatasource(ds);
        }

        String optionsCaptionProperty = lookupElement.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(optionsCaptionProperty)) {
            component.setOptionsCaptionMode(CaptionMode.PROPERTY);
            component.setOptionsCaptionProperty(optionsCaptionProperty);
        }

        String lookup = lookupElement.attributeValue("lookup");
        if (!StringUtils.isEmpty(lookup)) {
            component.setLookup(BooleanUtils.toBoolean(lookup));
            if (component.isLookup()) {
                String lookupScreen = lookupElement.attributeValue("lookupScreen");
                if (!StringUtils.isEmpty(lookupScreen)) {
                    component.setLookupScreen(lookupScreen);
                }
                String openType = lookupElement.attributeValue("openType");
                if (!StringUtils.isEmpty(openType)) {
                    component.setLookupOpenMode(WindowManager.OpenType.valueOf(openType));
                }
            }
        }

        String multiSelect = lookupElement.attributeValue("multiselect");
        if (!StringUtils.isEmpty(multiSelect)) {
            component.setMultiSelect(BooleanUtils.toBoolean(multiSelect));
        }

        loadFilterMode(component, lookupElement);

        Element buttonElement = element.element("button");
        if (buttonElement != null) {
            String caption = buttonElement.attributeValue("caption");
            if (caption != null) {
                if (!StringUtils.isEmpty(caption))
                    caption = loadResourceString(caption);
                component.setAddButtonCaption(caption);
            }

            String icon = buttonElement.attributeValue("icon");
            if (!StringUtils.isEmpty(icon)) {
                component.setAddButtonIcon(loadResourceString(icon));
            }
        }

        String simple = element.attributeValue("simple");
        if (!StringUtils.isEmpty(simple)) {
            component.setSimple(BooleanUtils.toBoolean(simple));
        } else {
            component.setSimple(false);
        }

        assignFrame(component);
    }

    protected void loadDatasource(TokenList component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            final CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null)
                throw new GuiDevelopmentException(String.format("Datasource '%s' is not defined", datasource), context.getFullFrameId());
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