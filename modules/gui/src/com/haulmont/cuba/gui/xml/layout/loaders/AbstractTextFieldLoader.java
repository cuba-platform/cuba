/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TextInputField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractTextFieldLoader<T extends TextInputField> extends AbstractFieldLoader<T> {

    protected void loadTrimming(TextInputField.TrimSupported component, Element element) {
        String trim = element.attributeValue("trim");
        if (!StringUtils.isEmpty(trim)) {
            component.setTrimming(Boolean.valueOf(trim));
        }
    }

    protected void loadMaxLength(TextInputField.MaxLengthLimited component, Element element) {
        final String maxLength = element.attributeValue("maxLength");
        if (!StringUtils.isEmpty(maxLength)) {
            component.setMaxLength(Integer.parseInt(maxLength));
        }
    }
}