/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2009 16:37:13
 *
 * $Id$
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
