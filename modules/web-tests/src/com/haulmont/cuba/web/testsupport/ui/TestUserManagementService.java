/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.testsupport.ui;

import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.app.UserTimeZone;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserSubstitution;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nullable;
import java.util.*;

public class TestUserManagementService implements UserManagementService {
    @Override
    public Group copyAccessGroup(UUID accessGroupId) {
        return null;
    }

    @Override
    public Role copyRole(UUID roleId) {
        return null;
    }

    @Override
    public Integer moveUsersToGroup(List<UUID> userIds, @Nullable UUID targetAccessGroupId) {
        return 0;
    }

    @Override
    public Integer changePasswordsAtLogonAndSendEmails(List<UUID> userIds) {
        return 0;
    }

    @Override
    public Map<UUID, String> changePasswordsAtLogon(List<UUID> userIds, boolean generatePassword) {
        return Collections.emptyMap();
    }

    @Override
    public boolean checkPassword(UUID userId, String passwordHash) {
        return true;
    }

    @Override
    public void resetRememberMeTokens(List<UUID> userIds) {
    }

    @Override
    public void resetRememberMeTokens() {
    }

    @Override
    public void removeRememberMeTokens(List<String> rememberMeTokens) {
    }

    @Override
    public boolean isRememberMeTokenValid(String login, String rememberMeToken) {
        return false;
    }

    @Override
    public String generateRememberMeToken(UUID userId) {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    @Override
    public List<String> getSessionAttributeNames(UUID groupId) {
        return Collections.emptyList();
    }

    @Override
    public UserTimeZone loadOwnTimeZone() {
        return null;
    }

    @Override
    public void saveOwnTimeZone(UserTimeZone timeZone) {
    }

    @Override
    public String loadOwnLocale() {
        return null;
    }

    @Override
    public void saveOwnLocale(String locale) {
    }

    @Override
    public void changeUserPassword(UUID userId, String newPasswordHash) {
    }

    @Override
    public boolean isUsersRemovingAllowed(Collection<String> userLogins) {
        return userLogins.stream().noneMatch(u -> "admin".equals(u) || "anonymous".equals(u));
    }

    @Override
    public void changeGroupParent(UUID groupId, UUID newParentId) {

    }

    @Override
    public boolean isAnonymousUser(String userLogin) {
        return false;
    }

    @Override
    public List<UserSubstitution> getSubstitutedUsers(UUID userId) {
        return Collections.emptyList();
    }
}