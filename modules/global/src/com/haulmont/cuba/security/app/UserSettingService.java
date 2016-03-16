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
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.security.entity.User;

/**
 * Service providing current user settings functionality:
 * an application can save/load some "setting" (plain or XML string) for current user.
 * <p/>It is ususally used by UI forms and components.
 *
 */
public interface UserSettingService {

    String NAME = "cuba_UserSettingService";

    /** Load settings for the current user and null client type. Returns null if no such setting found. */
    String loadSetting(String name);

    /** Load settings for the current user. Returns null if no such setting found. */
    String loadSetting(ClientType clientType, String name);

    /** Save settings for the current user and null client type */
    void saveSetting(String name, String value);

    /** Save settings for the current user */
    void saveSetting(ClientType clientType, String name, String value);

    /** Delete settings for the current user */
    void deleteSettings(ClientType clientType, String name);

    /** Copy user settings to another user */
    void copySettings(User fromUser, User toUser);
}