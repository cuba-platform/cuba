/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.optiongroup.CubaOptionGroupState;
import com.haulmont.cuba.web.toolkit.ui.client.optiongroup.OptionGroupOrientation;
import com.vaadin.ui.OptionGroup;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaOptionGroup extends OptionGroup {

    @Override
    protected CubaOptionGroupState getState() {
        return (CubaOptionGroupState) super.getState();
    }

    @Override
    protected CubaOptionGroupState getState(boolean markAsDirty) {
        return (CubaOptionGroupState) super.getState(markAsDirty);
    }

    public OptionGroupOrientation getOrientation() {
        return getState(false).orientation;
    }

    public void setOrientation(OptionGroupOrientation orientation) {
        if (orientation != getOrientation()) {
            getState().orientation = orientation;
        }
    }
}