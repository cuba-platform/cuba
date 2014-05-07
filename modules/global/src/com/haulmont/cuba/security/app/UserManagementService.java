/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.Group;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service providing maintenance operations on security entities.
 *
 * @author artamonov
 * @version $Id$
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
     * @param userId          User id
     * @param newPasswordHash Plain hash of new password
     * @return True if the new and old passwords are equal
     * @deprecated Use ${@link com.haulmont.cuba.security.app.UserManagementService#checkPassword(java.util.UUID, String)}
     */
    @Deprecated
    boolean checkEqualsOfNewAndOldPassword(UUID userId, String newPasswordHash);
}