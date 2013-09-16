/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.data;

/**
 * @author artamonov
 * @version $Id$
 */
import com.vaadin.data.Container;

public class StaticItemSetChangeEvent implements Container.ItemSetChangeEvent {

    private final Container container;

    public StaticItemSetChangeEvent(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() {
        return container;
    }
}