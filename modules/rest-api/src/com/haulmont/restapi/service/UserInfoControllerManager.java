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
 */

package com.haulmont.restapi.service;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.restapi.data.UserInfo;

import javax.inject.Inject;

/**
 * Class that is used by the {@link com.haulmont.restapi.controllers.UserInfoController} for getting an information
 * about the current user
 */
public class UserInfoControllerManager {

    @Inject
    protected UserSessionSource userSessionSource;

    public UserInfo getUserInfo() {
        User user = userSessionSource.getUserSession().getCurrentOrSubstitutedUser();
        UserInfo userInfo = new UserInfo(user);
        userInfo.setLocale(userSessionSource.getUserSession().getLocale().toString());
        return userInfo;
    }
}
