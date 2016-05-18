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

package com.haulmont.cuba.security.app;

import java.util.Set;

/**
 * Class is responsible for the brute-force protection on user login
 */
public interface BruteForceProtectionAPI {

    String NAME = "cuba_BruteForceProtectionAPI";

    /**
     * Returns a number of login attempts left for the specified pair of login and IP-address
     * @param login user login
     * @param ipAddress user IP-address
     * @return number of login attempts left
     */
    int loginAttemptsLeft(String login, String ipAddress);

    /**
     * Registers unsuccessful login attempt
     * @return a number of login attempts left for the specified pair of login and IP-address
     */
    int registerUnsuccessfulLogin(String login, String ipAddress);

    /**
     * @return true if brute-force protection is enabled in application settings
     */
    boolean isBruteForceProtectionEnabled();

    /**
     * Returns a time interval for which a user is blocked after the number
     * of allowed login attempts is exceeded
     */
    int getBruteForceBlockIntervalSec();

    /**
     * Returns an information about the locked users
     * @return a set of strings. Each string consists of two parts (login and IP-adress) separated by the '|' symbol
     */
    Set<String> getLockedUsersInfo();

    /**
     * Unlocks the blocked user
     * @param login user login
     * @param ipAddress user IP-address
     */
    void unlockUser(String login, String ipAddress);

}
