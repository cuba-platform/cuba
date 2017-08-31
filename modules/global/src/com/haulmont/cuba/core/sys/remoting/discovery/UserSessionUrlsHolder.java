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

package com.haulmont.cuba.core.sys.remoting.discovery;

import com.haulmont.cuba.security.global.UserSession;

import java.util.List;

/**
 * Stores the list of server URLs in the UserSession.
 */
public class UserSessionUrlsHolder implements SessionUrlsHolder {

    private static final String SESSION_ATTR = UserSessionUrlsHolder.class.getName() + ".lastSessionUrls";

    private UserSession userSession;

    public UserSessionUrlsHolder(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public List<String> getUrls(String selectorId) {
        return userSession.getLocalAttribute(getAttributeName(selectorId));
    }

    @Override
    public void setUrls(String selectorId, List<String> urls) {
        userSession.setLocalAttribute(getAttributeName(selectorId), urls);
    }

    private String getAttributeName(String selectorId) {
        return SESSION_ATTR + (selectorId != null ? "." + selectorId : "");
    }
}
