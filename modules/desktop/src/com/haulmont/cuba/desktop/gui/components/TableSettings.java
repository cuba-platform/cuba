/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface TableSettings {

    boolean saveSettings(Element element);

    void apply(Element element, boolean sortable);
}
