/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.encryption;

import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

/**
 * Encryption algorithm
 *
 * @author artamonov
 * @version $Id$
 */
public interface EncryptionModule {

    String getHashMethod();

    HashDescriptor getHash(String content);

    String getPasswordHash(UUID userId, String password);

    String getHash(String content, String salt);

    String getPlainHash(String content);

    boolean checkPassword(User user, String givenPassword);
}