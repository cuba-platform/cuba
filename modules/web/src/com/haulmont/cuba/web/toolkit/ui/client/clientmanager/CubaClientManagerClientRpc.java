/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.clientmanager;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.communication.ClientRpc;

import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaClientManagerClientRpc extends ClientRpc {

    String COMMUNICATION_ERROR_CAPTION_KEY = "communicationErrorCaption";

    String COMMUNICATION_ERROR_MESSAGE_KEY = "communicationErrorMessage";

    String AUTHORIZATION_ERROR_CAPTION_KEY = "authorizationErrorCaption";

    String AUTHORIZATION_ERROR_MESSAGE_KEY = "authorizationErrorMessage";

    String SESSION_EXPIRED_ERROR_CAPTION_KEY = "sessionExpiredErrorCaption";

    String SESSION_EXPIRED_ERROR_MESSAGE_KEY = "sessionExpiredErrorMessage";

    @NoLayout
    void updateSystemMessagesLocale(Map<String, String> localeMap);
}