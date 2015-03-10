/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.clientmanager;

import com.haulmont.cuba.web.toolkit.ui.CubaClientManager;
import com.haulmont.cuba.web.toolkit.ui.client.tooltip.CubaTooltip;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaClientManager.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaClientManagerConnector extends AbstractExtensionConnector {

    public CubaClientManagerConnector() {
        registerRpc(CubaClientManagerClientRpc.class, new CubaClientManagerClientRpc() {
            @Override
            public void updateSystemMessagesLocale(Map<String, String> localeMap) {
                ApplicationConfiguration conf = getConnection().getConfiguration();
                ApplicationConfiguration.ErrorMessage communicationError = conf.getCommunicationError();
                communicationError.setCaption(localeMap.get(CubaClientManagerClientRpc.COMMUNICATION_ERROR_CAPTION_KEY));
                communicationError.setMessage(localeMap.get(CubaClientManagerClientRpc.COMMUNICATION_ERROR_MESSAGE_KEY));

                ApplicationConfiguration.ErrorMessage authError = conf.getAuthorizationError();
                authError.setCaption(localeMap.get(CubaClientManagerClientRpc.AUTHORIZATION_ERROR_CAPTION_KEY));
                authError.setMessage(localeMap.get(CubaClientManagerClientRpc.AUTHORIZATION_ERROR_MESSAGE_KEY));

                ApplicationConfiguration.ErrorMessage sessionExpiredError = conf.getSessionExpiredError();
                sessionExpiredError.setCaption(localeMap.get(CubaClientManagerClientRpc.SESSION_EXPIRED_ERROR_CAPTION_KEY));
                sessionExpiredError.setMessage(localeMap.get(CubaClientManagerClientRpc.SESSION_EXPIRED_ERROR_MESSAGE_KEY));
            }
        });
    }

    @Override
    protected void extend(ServerConnector target) {
        // check mode of required indicator icon/hidden
        // performed on page open or full refresh
        CubaTooltip.checkRequiredInicatorMode();
    }
}