/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.entity.HashMethod;
import com.haulmont.cuba.core.global.Encryption;
import com.haulmont.cuba.core.sys.AbstractEncryption;
import com.haulmont.cuba.security.app.LoginService;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(Encryption.NAME)
public class EncryptionClientImpl extends AbstractEncryption {

    @Inject
    private LoginService loginService;

    @Override
    protected HashMethod getEncryptionMethod() {
        return loginService.getPasswordEncryptionMethod();
    }
}