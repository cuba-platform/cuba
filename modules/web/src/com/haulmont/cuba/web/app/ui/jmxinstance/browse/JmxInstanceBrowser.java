/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.jmxinstance.browse;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;

import javax.inject.Named;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class JmxInstanceBrowser extends AbstractLookup {

    @Named("jmxInstancesTable.create")
    protected CreateAction createInstanceAction;

    @Named("jmxInstancesTable.edit")
    protected EditAction editInstanceAction;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        createInstanceAction.setOpenType(WindowManager.OpenType.DIALOG);
        editInstanceAction.setOpenType(WindowManager.OpenType.DIALOG);
    }
}