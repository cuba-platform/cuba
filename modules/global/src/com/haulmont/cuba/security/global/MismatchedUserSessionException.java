/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.security.global;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SupportedByClient;

import java.util.UUID;

/**
 * Raised if {@link UserSession} in UI is mismatched with session in App.
 */
@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class MismatchedUserSessionException extends RuntimeException {

    private static final long serialVersionUID = 6993619407662317614L;

    public MismatchedUserSessionException(UUID sessionId) {
        super(String.format("UI user session doesn't match app user session: %s",
                sessionId.toString()));
    }
}
