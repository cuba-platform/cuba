/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:31:30
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;

import java.util.Locale;
import java.util.ResourceBundle;

public interface ComponentLoader {
    Locale getLocale();
    void setLocale(Locale locale);

    ResourceBundle getResourceBundle();
    void setResourceBundle(ResourceBundle resourceBundle);

    Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException;
}
