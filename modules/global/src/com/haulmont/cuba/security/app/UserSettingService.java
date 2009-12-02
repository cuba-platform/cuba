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

import javax.ejb.Local;

/**
 * Local interface to {@link com.haulmont.cuba.security.app.UserSettingServiceBean}
 */
@Local
public interface UserSettingService
{
    String JNDI_NAME = "cuba/security/UserSettingService";

    /** Load settings for the current user and null client type */
    String loadSetting(String name);

    /** Load settings for the current user */
    String loadSetting(ClientType clientType, String name);

    /** Save settings for the current user and null client type */
    void saveSetting(String name, String value);

    /** Save settings for the current user */
    void saveSetting(ClientType clientType, String name, String value);
}
