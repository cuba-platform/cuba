/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface TreeTableContainer extends Container.Hierarchical {
    boolean isCaption(Object itemId);

    String getCaption(Object itemId);
    boolean setCaption(Object itemId, String caption);

    int getLevel(Object itemId);
}
