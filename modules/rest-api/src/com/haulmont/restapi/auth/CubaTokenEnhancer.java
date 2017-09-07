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

package com.haulmont.restapi.auth;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides additional token details from authentication details with ext_ prefix.
 */
public class CubaTokenEnhancer implements TokenEnhancer {

    public static final String EXTENDED_DETAILS_ATTRIBUTE_PREFIX = "ext_";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, String> requestParameters = authentication.getOAuth2Request().getRequestParameters();
        Map<String, Object> additionalInfos = null;

        for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
            if (entry.getKey().startsWith(EXTENDED_DETAILS_ATTRIBUTE_PREFIX)) {
                String detailsKey = entry.getKey().substring(EXTENDED_DETAILS_ATTRIBUTE_PREFIX.length());
                if (additionalInfos == null) {
                    additionalInfos = new HashMap<>();
                }
                additionalInfos.put(detailsKey, entry.getValue());
            }
        }

        if (additionalInfos != null) {
            additionalInfos.putAll(accessToken.getAdditionalInformation());

            DefaultOAuth2AccessToken mutableAccessToken = (DefaultOAuth2AccessToken) accessToken;
            mutableAccessToken.setAdditionalInformation(additionalInfos);
        }

        return accessToken;
    }
}