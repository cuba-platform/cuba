/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:31:30
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import org.dom4j.Element;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

public interface ComponentLoader {

    Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException;
}
