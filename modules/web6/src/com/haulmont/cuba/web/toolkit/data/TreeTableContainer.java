/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

public interface TreeTableContainer
        extends Container.Hierarchical
{
    boolean isCaption(Object itemId);

    String getCaption(Object itemId);
    boolean setCaption(Object itemId, String caption);

    int getLevel(Object itemId);
}
