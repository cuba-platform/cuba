/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 06.03.2009 17:33:50
 * $Id$
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
