/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.03.2009 19:13:22
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.security.entity.User;

/**
 * Service interface to UserSettingServiceBean
 */
public interface UserSettingService
{
    String NAME = "cuba_UserSettingService";

    @Deprecated
    String JNDI_NAME = NAME;

    /** Load settings for the current user and null client type. Returns null if no such setting found. */
    String loadSetting(String name);

    /** Load settings for the current user. Returns null if no such setting found. */
    String loadSetting(ClientType clientType, String name);

    /** Save settings for the current user and null client type */
    void saveSetting(String name, String value);

    /** Save settings for the current user */
    void saveSetting(ClientType clientType, String name, String value);

    void copySettings(User fromUser,User toUser);
}
