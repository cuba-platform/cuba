/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.OptionsList;

/**
 * @author petunin
 */
public class OptionsListLoader extends AbstractOptionsBaseLoader<OptionsList> {
    @Override
    public void createComponent() {
        resultComponent = (OptionsList) factory.createComponent(OptionsList.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        loadDescription(resultComponent, element);
    }
}
