/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class TabComponentLoader extends VBoxLayoutLoader {

    @Override
    protected boolean loadEnable(Component component, Element element) {
        // tab component always enabled
        return true;
    }

    @Override
    protected boolean loadVisible(Component component, Element element) {
        // tab component always visible
        return true;
    }
}