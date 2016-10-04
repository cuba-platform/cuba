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

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service providing maintenance operations on security entities.
 *
 */
public interface UserManagementService {

    String NAME = "cuba_UserManagementService";

    /**
     * Copy access group with all its properties and subgroups.
     *
     * @param accessGroupId Source access group Id
     * @return Cloned group
     */
    Group copyAccessGroup(UUID accessGroupId);

    /**
     * Copy user role with all its permissions.
     *
     * @param roleId Source access role Id
     * @return Cloned role
     */
    Role copyRole(UUID roleId);

    /**
     * Move specified users to a new access group.
     *
     * @param userIds             Ids for moved users
     * @param targetAccessGroupId Id of target access group, may be null
     * @return Number of users moved to target group
     */
    Integer moveUsersToGroup(List<UUID> userIds, @Nullable UUID targetAccessGroupId);

    /**
     * Update passwords for specified users, send them emails with new generated passwords and make them change
     * passwords at next logon.
     *
     * @param userIds User ids
     * @return Count of users
     */
    Integer changePasswordsAtLogonAndSendEmails(List<UUID> userIds);

    /**
     * Make specified users to change passwords at next logon.
     *
     * @param userIds          User ids
     * @param generatePassword Generate new passwords
     * @return Map with userId and new password
     */
    Map<UUID, String> changePasswordsAtLogon(List<UUID> userIds, boolean generatePassword);

    /**
     * @param userId       User id
     * @param passwordHash Plain hash of new password
     * @return True if the new and old passwords are equal
     */
    boolean checkPassword(UUID userId, String passwordHash);

    /**
     * Remove remember me tokens for users
     *
     * @param userIds User ids
     */
    void resetRememberMeTokens(List<UUID> userIds);

    /**
     * Remove remember me tokens for all users
     */
    void resetRememberMeTokens();

    /**
     * Generate and store to DB {@link com.haulmont.cuba.security.entity.RememberMeToken}
     *
     * @return token string
     */
    String generateRememberMeToken(UUID userId);

    /**
     * Load current user's time zone settings.
     */
    UserTimeZone loadOwnTimeZone();

    /**
     * Save current user's time zone settings.
     */
    void saveOwnTimeZone(UserTimeZone timeZone);

    /**
     * Load current user's language.
     */
    String loadOwnLocale();

    /**
     * Save current user's language settings.
     */
    void saveOwnLocale(String locale);

    /**
     * Change password for user
     *
     * @param userId user id
     * @param newPasswordHash password hash
     */
    void changeUserPassword(UUID userId, String newPasswordHash);
}