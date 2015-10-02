/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.HBoxLayout;

/**
 * @author abramov
 * @version $Id$
 */
public class HBoxLayoutLoader extends AbstractBoxLoader<HBoxLayout> {
    @Override
    public void createComponent() {
        resultComponent = (HBoxLayout) factory.createComponent(HBoxLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }
}