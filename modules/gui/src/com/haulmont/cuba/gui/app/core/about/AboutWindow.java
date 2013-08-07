/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.about;

import com.haulmont.cuba.gui.components.AbstractWindow;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AboutWindow extends AbstractWindow {

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(450).setHeight(300).setResizable(false);
    }
}
