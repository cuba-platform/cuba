/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.settings;

import org.dom4j.Element;

/**
 * Interface to user settings of a window
 */
public interface Settings {

    Element get();

    Element get(String componentId);

    void setModified(boolean modified);

    void commit();
}
