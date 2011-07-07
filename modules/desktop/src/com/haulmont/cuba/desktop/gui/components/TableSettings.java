/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
