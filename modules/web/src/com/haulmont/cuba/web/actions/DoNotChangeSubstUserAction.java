/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.actions;

import com.haulmont.cuba.gui.components.AbstractAction;

/**
 * @author shishov
 * @version $Id$
 */
public class DoNotChangeSubstUserAction extends AbstractAction {

    protected DoNotChangeSubstUserAction() {
        super("doNotChangeSubstUserAction");
    }

    @Override
    public String getIcon() {
        return "icons/cancel.png";
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
    }
}