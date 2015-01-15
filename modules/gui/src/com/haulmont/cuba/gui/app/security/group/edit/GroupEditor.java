/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.group.edit;

import com.haulmont.cuba.gui.components.AbstractEditor;

import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class GroupEditor extends AbstractEditor {

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidthAuto();
    }
}