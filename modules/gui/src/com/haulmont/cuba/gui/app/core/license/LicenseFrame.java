/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.license;

import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LicenseFrame extends AbstractFrame {

    @Inject
    private Resources resources;
    @Inject
    private UserSession userSession;

    @Inject
    private UserSessionService uss;
    @Inject
    private TextArea licenseTxtField;
    @Inject
    private LinkButton licenseLink;
    @Inject
    private TextField licensedToField;
    @Inject
    private Label licensedSessions;
    @Inject
    private Label activeSessions;

    private String linkAddressMsg;

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Object> info = uss.getLicenseInfo();

        String licenseType = (String) info.get("licenseType");
        licenseTxtField.setValue(getMessage(licenseType));
        licenseTxtField.setEditable(false);

        String linkKey = licenseType + "Link";
        String linkMsg = getMessage(linkKey);
        if (!linkMsg.equals(linkKey)) {
            licenseLink.setVisible(true);
            licenseLink.setCaption(linkMsg);

            String linkAddressKey = licenseType + "LinkAddress";
            linkAddressMsg = getMessage(linkAddressKey);
        }

        if (!licenseType.equals("starter")) {
            licensedToField.setVisible(true);
            licensedToField.setValue(info.get("licensedTo"));
            licensedToField.setEditable(false);
        }

        Integer licensed = (Integer) info.get("licensedSessions");
        String licensedStr = licensed == 0 ? getMessage("unlimited") : String.valueOf(licensed);
        licensedSessions.setValue(messages.formatMessage(getMessagesPack(), "licensedSessions", licensedStr));

        activeSessions.setValue(messages.formatMessage(getMessagesPack(), "activeSessions", info.get("activeSessions")));
    }

    public void showLicense() {
        if (linkAddressMsg != null) {
            showWebPage(linkAddressMsg, null);
        }
    }
}
