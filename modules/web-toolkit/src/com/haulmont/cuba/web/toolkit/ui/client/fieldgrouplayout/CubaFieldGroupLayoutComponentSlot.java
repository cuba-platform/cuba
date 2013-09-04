/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.haulmont.cuba.web.toolkit.ui.client.gridlayout.CubaGridLayoutComponentSlot;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.ManagedLayout;

/**
 * Component slot with horizontal layout for caption and component
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayoutComponentSlot extends CubaGridLayoutComponentSlot {

    public CubaFieldGroupLayoutComponentSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }

    @Override
    protected boolean isCaptionInline() {
        // todo artamonov implement vertical/horizontal option for captions
        return true;
    }
}