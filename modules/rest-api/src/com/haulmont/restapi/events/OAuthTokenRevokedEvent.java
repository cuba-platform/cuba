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

package com.haulmont.restapi.events;

import com.haulmont.restapi.auth.TokenRevocationInitiator;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Event fired after OAuth token revocation.
 */
public class OAuthTokenRevokedEvent extends ApplicationEvent {
    protected TokenRevocationInitiator revocationInitiator;

    public OAuthTokenRevokedEvent(OAuth2AccessToken source, TokenRevocationInitiator revocationInitiator) {
        super(source);
        this.revocationInitiator = revocationInitiator;
    }

    @Override
    public OAuth2AccessToken getSource() {
        return (OAuth2AccessToken) super.getSource();
    }

    public OAuth2AccessToken getAccessToken() {
        return (OAuth2AccessToken) super.getSource();
    }

    public TokenRevocationInitiator getRevocationInitiator() {
        return revocationInitiator;
    }
}