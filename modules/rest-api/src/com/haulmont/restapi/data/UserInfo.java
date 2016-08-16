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

package com.haulmont.restapi.data;

import com.haulmont.cuba.security.entity.User;

/**
 */
public class UserInfo {

    public String login;
    public String name;
    public String firstName;
    public String middleName;
    public String lastName;
    public String position;
    public String email;
    public String timeZone;
    public String language;
    public String _instanceName;
    public String locale;

    public UserInfo(User user) {
        this.login = user.getLogin();
        this.name = user.getName();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.position = user.getPosition();
        this.email = user.getEmail();
        this.timeZone = user.getTimeZone();
        this._instanceName = user.getInstanceName();
        this.language = user.getLanguage();
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
