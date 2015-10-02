/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.LinkButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class LinkButtonLoader extends ButtonLoader {

    @Override
    public void createComponent() {
        resultComponent = (Button) factory.createComponent(LinkButton.NAME);
        loadId(resultComponent, element);
    }
}