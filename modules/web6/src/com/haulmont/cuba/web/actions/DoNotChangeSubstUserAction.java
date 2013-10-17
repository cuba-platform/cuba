/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.actions;

import com.haulmont.cuba.gui.components.AbstractAction;

/**
 * <p>$Id$</p>
 *
 * @author shishov
 */

public class DoNotChangeSubstUserAction extends AbstractAction {

    protected DoNotChangeSubstUserAction() {
        super("doNotChangeSubstUserAction");
    }

    @Override
    public String getIcon() {
        return "icons/cancel.png";
    }

    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
    }
}
