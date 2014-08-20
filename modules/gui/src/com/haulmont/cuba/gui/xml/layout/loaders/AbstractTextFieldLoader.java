/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractTextFieldLoader extends AbstractFieldLoader {

    public AbstractTextFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    protected void loadTrimming(Element element, TextInputField.TrimSupported component) {
        String trim = element.attributeValue("trim");
        if (!StringUtils.isEmpty(trim)) {
            component.setTrimming(Boolean.valueOf(trim));
        }
    }

    protected void loadMaxLength(Element element, TextInputField.MaxLengthLimited component) {
        final String maxLength = element.attributeValue("maxLength");
        if (!StringUtils.isEmpty(maxLength)) {
            component.setMaxLength(Integer.parseInt(maxLength));
        }
    }
}