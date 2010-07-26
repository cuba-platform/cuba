/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 21.07.2010 12:01:49
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class TokenListLoader extends AbstractFieldLoader {
    public TokenListLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final TokenList component = factory.createComponent("tokenList");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadDatasource(component, element);

        loadVisible(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadExpandable(component, element);

        String optionsCaptionProperty = element.attributeValue("optionsCaptionProperty");
        if (!StringUtils.isEmpty(optionsCaptionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setOptionsCaptionProperty(optionsCaptionProperty);
        }

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String buttonCaption = element.attributeValue("buttonCaption");
        if (!StringUtils.isEmpty(buttonCaption)) {
            component.setAddButtonCaption(loadResourceString(buttonCaption));
        }

        String buttonIcon = element.attributeValue("buttonIcon");
        if (!StringUtils.isEmpty(buttonIcon)) {
            component.setAddButtonIcon(loadResourceString(buttonIcon));
        }

        String position = element.attributeValue("position");
        if (!StringUtils.isEmpty(position)) {
            component.setPosition(TokenList.Position.valueOf(position));
        }

        String type = element.attributeValue("type");
        if (!StringUtils.isEmpty(type)) {
            component.setType(TokenList.Type.valueOf(type));
        }

        String inline = element.attributeValue("inline");
        if (!StringUtils.isEmpty(inline)) {
            component.setInline(BooleanUtils.toBoolean(inline));
        }

        String lookupScreen = element.attributeValue("lookupScreen");
        if (!StringUtils.isEmpty(lookupScreen)) {
            component.setLookupScreen(lookupScreen);
        }

        assignFrame(component);

        return component;
    }

    protected void loadDatasource(TokenList component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            final CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null)
                throw new IllegalStateException(String.format("Datasource '%s' not defined", datasource));

            component.setDatasource(ds);
        }

        final String optionsDatasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(optionsDatasource)) {
            final Datasource ds = context.getDsContext().get(optionsDatasource);
            component.setOptionsDatasource((CollectionDatasource) ds);
        }
    }
}
