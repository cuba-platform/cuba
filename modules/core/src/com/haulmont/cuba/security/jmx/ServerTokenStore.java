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

package com.haulmont.cuba.security.jmx;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

@Component(ServerTokenStoreMBean.NAME)
public class ServerTokenStore implements ServerTokenStoreMBean {

    @Inject
    protected com.haulmont.cuba.restapi.ServerTokenStore serverTokenStore;

    @Override
    public String removeTokensByUserLogin(String userLogin) {
        if (StringUtils.isEmpty(userLogin)) {
            return "Please specify the user's login";
        }

        try {
            Set<String> tokens = serverTokenStore.getAccessTokenValuesByUserLogin(userLogin);
            if (tokens.isEmpty()) {
                return String.format("No tokens found for user '%s'", userLogin);
            }

            tokens.forEach(serverTokenStore::removeAccessToken);

            return String.format("%s tokens were removed for user '%s' successfully.", tokens.size(), userLogin);
        } catch (Throwable t) {
            return ExceptionUtils.getStackTrace(t);
        }
    }
}
