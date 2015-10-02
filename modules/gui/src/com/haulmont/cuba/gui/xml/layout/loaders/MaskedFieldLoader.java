/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.MaskedField;
import org.apache.commons.lang.StringUtils;

/**
 * @author devyatkin
 * @version $Id$
 */
public class MaskedFieldLoader extends AbstractTextFieldLoader<MaskedField> {
    @Override
    public void createComponent() {
        resultComponent = (MaskedField) factory.createComponent(MaskedField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        String mask = element.attributeValue("mask");
        if (!StringUtils.isEmpty(mask)) {
            resultComponent.setMask(loadResourceString(mask));
        }
        String valueModeStr = element.attributeValue("valueMode", MaskedField.ValueMode.CLEAR.getId());
        resultComponent.setValueMode(MaskedField.ValueMode.fromId(valueModeStr));
    }
}