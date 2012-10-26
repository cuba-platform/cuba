/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.HashMethod;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Encryption;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(Encryption.NAME)
public class EncryptionImpl extends AbstractEncryption {

    @Inject
    private Configuration configuration;

    @Override
    protected HashMethod getEncryptionMethod() {
        return configuration.getConfig(ServerConfig.class).getPasswordEncryption();
    }
}