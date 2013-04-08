/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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