/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.TextArea;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LicenseWindow extends AbstractWindow {

    @Inject
    protected TextArea licenseTextArea;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(700).setHeight(500).setResizable(false);
        String licenseText = (String) params.get("licenseText");
        if (licenseText != null) {
            licenseTextArea.setValue(licenseText);
            licenseTextArea.setEditable(false);
        }
    }
}