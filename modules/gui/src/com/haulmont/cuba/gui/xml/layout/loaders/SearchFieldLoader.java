/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SearchField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class SearchFieldLoader extends LookupFieldLoader {

    public SearchFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        SearchField component = (SearchField) super.loadComponent(factory, element, parent);

        String minSearchStringLength = element.attributeValue("minSearchStringLength");
        if (StringUtils.isNotEmpty(minSearchStringLength)) {
            component.setMinSearchStringLength(Integer.parseInt(minSearchStringLength));
        }

        String modeString = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(modeString)) {
            SearchField.Mode mode;
            try {
                mode = SearchField.Mode.valueOf(StringUtils.upperCase(modeString));
            } catch (IllegalArgumentException e) {
                throw new GuiDevelopmentException("Unable to parse mode for search", context.getFullFrameId(), "mode", modeString);
            }
            component.setMode(mode);
        }

        return component;
    }
}