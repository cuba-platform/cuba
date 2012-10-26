/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.User;

/**
 * Encryption support for hashing passwords and security
 *
 * @author artamonov
 * @version $Id$
 */
public interface Encryption {

    String NAME = "cuba_Encryption";

    /**
     * Hash string
     *
     * @param content Content for hashing
     * @return Hash with additional params (such as salt)
     */
    HashDescriptor getHash(String content);

    /**
     * Hash password
     *
     * @param password Content for hashing
     * @return Hash with additional params (such as salt)
     */
    HashDescriptor getPasswordHash(String password);

    /**
     * Hash string
     *
     * @param content Content for hashing
     * @param salt    Salt
     * @return Hex string of hash
     */
    String getHash(String content, String salt);

    /**
     * Hash string without salt
     *
     * @param content Content for hashing
     * @return Hex string of hash
     */
    String getPlainHash(String content);

    /**
     * Check credentials for user
     *
     * @param user          User
     * @param givenPassword Given password
     * @return True if access permitted and credentials are valid
     */
    boolean checkUserAccess(User user, String givenPassword);
}