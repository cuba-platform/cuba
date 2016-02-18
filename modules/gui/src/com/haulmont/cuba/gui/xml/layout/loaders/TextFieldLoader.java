/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang.StringUtils;

/**
 * @author abramov
 */
public class TextFieldLoader extends AbstractTextFieldLoader<TextField> {
    @Override
    public void loadComponent() {
        super.loadComponent();

        loadMaxLength(resultComponent, element);
        loadTrimming(resultComponent, element);

        String datatypeAttribute = element.attributeValue("datatype");
        if (StringUtils.isNotEmpty(datatypeAttribute)) {
            Datatype datatype = Datatypes.get(datatypeAttribute);
            resultComponent.setDatatype(datatype);
        }

        resultComponent.setFormatter(loadFormatter(element));

        loadInputPrompt(resultComponent, element);
    }

    @Override
    public void createComponent() {
        resultComponent = (TextField) factory.createComponent(TextField.NAME);
        loadId(resultComponent, element);
    }
}