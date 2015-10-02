/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.VBoxLayout;

/**
 * @author abramov
 * @version $Id$
 */
public class VBoxLayoutLoader extends AbstractBoxLoader<VBoxLayout> {
    @Override
    public void createComponent() {
        resultComponent = (VBoxLayout) factory.createComponent(VBoxLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }
}