/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.clientmanager.CubaClientManagerClientRpc;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaClientManager extends AbstractExtension {

    public void updateSystemMessagesLocale(SystemMessages msgs) {
        Map<String, String> localeMap = new HashMap<>();

        localeMap.put(CubaClientManagerClientRpc.COMMUNICATION_ERROR_CAPTION_KEY, msgs.communicationErrorCaption);
        localeMap.put(CubaClientManagerClientRpc.COMMUNICATION_ERROR_MESSAGE_KEY, msgs.communicationErrorMessage);

        localeMap.put(CubaClientManagerClientRpc.SESSION_EXPIRED_ERROR_CAPTION_KEY, msgs.sessionExpiredErrorCaption);
        localeMap.put(CubaClientManagerClientRpc.SESSION_EXPIRED_ERROR_MESSAGE_KEY, msgs.sessionExpiredErrorMessage);

        localeMap.put(CubaClientManagerClientRpc.AUTHORIZATION_ERROR_CAPTION_KEY, msgs.authorizationErrorCaption);
        localeMap.put(CubaClientManagerClientRpc.AUTHORIZATION_ERROR_MESSAGE_KEY, msgs.authorizationErrorMessage);

        getRpcProxy(CubaClientManagerClientRpc.class).updateSystemMessagesLocale(localeMap);
    }

    @Override
    public void extend(AbstractClientConnector target) {
        super.extend(target);
    }

    public static class SystemMessages {

        public String communicationErrorCaption;
        public String communicationErrorMessage;

        public String authorizationErrorCaption;
        public String authorizationErrorMessage;

        public String sessionExpiredErrorCaption;
        public String sessionExpiredErrorMessage;
    }
}