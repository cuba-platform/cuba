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

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.communication.ClientRpc;

import java.util.Map;

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