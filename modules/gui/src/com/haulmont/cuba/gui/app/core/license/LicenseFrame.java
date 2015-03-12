/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.license;

import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.security.app.UserSessionService;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LicenseFrame extends AbstractFrame {

    @Inject
    private UserSessionService uss;
    @Inject
    private TextArea licenseTxtField;

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Object> info = uss.getLicenseInfo();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : info.entrySet()) {
            sb.append(getMessage(entry.getKey())).append(": ").append(entry.getValue()).append("\n");
        }

        licenseTxtField.setValue(sb.toString());
        licenseTxtField.setEditable(false);

    }
}
