/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.haulmont.cuba.web.toolkit.ui.client.gridlayout.CubaGridLayoutSlot;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.ManagedLayout;

/**
 * Component slot with horizontal layout for caption and component
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayoutComponentSlot extends CubaGridLayoutSlot {

    public CubaFieldGroupLayoutComponentSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }

    @Override
    protected boolean isCaptionInline() {
        // todo artamonov implement vertical/horizontal option for captions
        return true;
    }
}