/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.Group;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public interface UserManagementService {

    String NAME = "cuba_UserManagementService";

    /**
     * Copy access group with all properties and subgroups
     *
     * @param accessGroupId Source access group Id
     * @return Cloned group
     */
    Group copyAccessGroup(UUID accessGroupId);

    /**
     * Move specified users to access group
     *
     * @param userIds             Ids for moved users
     * @param targetAccessGroupId Id of target access group, may be null
     * @return Count of users moved to target group
     */
    Integer moveUsersToGroup(List<UUID> userIds, @Nullable UUID targetAccessGroupId);

    /**
     * Change passwords at logon for specified users and send emails with generated passwords
     *
     * @param userIds User ids
     * @return Count of users
     */
    Integer changePasswordsAtLogonAndSendEmails(List<UUID> userIds);

    /**
     * Change passwords at logon for specified users
     *
     * @param userIds User ids
     * @return Map with userId and new password
     */
    Map<UUID, String> changePasswordsAtLogon(List<UUID> userIds);
}