/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.credits;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TextField;

import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LicenseWindow extends AbstractWindow {

    public LicenseWindow(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        getDialogParams().setWidth(700).setResizable(true);
        String licenseText = (String) params.get("licenseText");
        if (licenseText != null) {
            TextField textField = getComponent("text");
            textField.setValue(licenseText);
        }
    }
}
