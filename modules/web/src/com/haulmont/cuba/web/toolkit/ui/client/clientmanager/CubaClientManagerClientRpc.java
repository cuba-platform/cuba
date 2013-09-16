/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.clientmanager;

import com.vaadin.shared.communication.ClientRpc;

import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaClientManagerClientRpc extends ClientRpc {

    public String COMMUNICATION_ERROR_CAPTION_KEY = "communicationErrorCaption";

    public String COMMUNICATION_ERROR_MESSAGE_KEY = "communicationErrorMessage";

    public String AUTHORIZATION_ERROR_CAPTION_KEY = "authorizationErrorCaption";

    public String AUTHORIZATION_ERROR_MESSAGE_KEY = "authorizationErrorMessage";

    public String SESSION_EXPIRED_ERROR_CAPTION_KEY = "sessionExpiredErrorCaption";

    public String SESSION_EXPIRED_ERROR_MESSAGE_KEY = "sessionExpiredErrorMessage";

    public void updateSystemMessagesLocale(Map<String, String> localeMap);
}