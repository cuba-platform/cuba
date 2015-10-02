/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CheckBox;

/**
 * @author artamonov
 * @version $Id$
 */
public class CheckBoxLoader extends AbstractFieldLoader<CheckBox> {
    @Override
    public void createComponent() {
        resultComponent = (CheckBox) factory.createComponent(CheckBox.NAME);
        loadId(resultComponent, element);
    }
}
