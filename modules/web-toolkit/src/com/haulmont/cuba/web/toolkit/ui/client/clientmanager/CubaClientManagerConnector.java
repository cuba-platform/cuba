/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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