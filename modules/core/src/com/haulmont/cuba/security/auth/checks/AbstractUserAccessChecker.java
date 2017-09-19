/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.security.auth.checks;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.UserAccessChecker;

import java.util.Locale;

public abstract class AbstractUserAccessChecker implements UserAccessChecker {

    protected static final String MSG_PACK = "com.haulmont.cuba.security";

    protected Messages messages;

    public AbstractUserAccessChecker(Messages messages) {
        this.messages = messages;
    }

    protected String getInvalidCredentialsMessage(String login, Locale locale) {
        return messages.formatMessage(MSG_PACK, "LoginException.InvalidLoginOrPassword", locale, login);
    }
}