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

package com.haulmont.cuba.web.security.idp;

import com.haulmont.cuba.security.global.IdpSession;

import javax.annotation.Nullable;
import java.security.Principal;
import java.util.Locale;

public class IdpSessionPrincipalImpl implements Principal, IdpSessionPrincipal {
    private final IdpSession idpSession;

    public IdpSessionPrincipalImpl(IdpSession idpSession) {
        this.idpSession = idpSession;
    }

    @Override
    public String getName() {
        return idpSession.getLogin();
    }

    @Override
    public IdpSession getIdpSession() {
        return idpSession;
    }

    @Nullable
    public Locale getLocale() {
        String locale = idpSession.getLocale();
        if (locale == null) {
            return null;
        }

        return Locale.forLanguageTag(locale);
    }
}