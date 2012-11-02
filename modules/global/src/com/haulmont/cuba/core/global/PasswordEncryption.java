/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

/**
 * Encryption support for hashing passwords and security</br>
 * Used for hashing passwords and check passwords at user logon
 *
 * @author artamonov
 * @version $Id$
 */
public interface PasswordEncryption {

    String NAME = "cuba_PasswordEncryption";

    /**
     * @return Random password with Base64 symbols
     */
    String generateRandomPassword();

    /**
     * @return Using hash method
     */
    HashMethod getHashMethod();

    /**
     * Hash string.
     *
     * @param content content for hashing
     * @return Hash with additional params (such as salt)
     */
    HashDescriptor getHash(String content);

    /**
     * Hash password.
     *
     * @param userId user id
     * @param password content for hashing
     * @return Hash with additional params (such as salt)
     */
    String getPasswordHash(UUID userId, String password);

    /**
     * Hash string.
     *
     * @param content content for hashing
     * @param salt    salt
     * @return Hex string of hash
     */
    String getHash(String content, String salt);

    /**
     * Hash string without salt.
     *
     * @param content content for hashing
     * @return Hex string of hash
     */
    String getPlainHash(String content);

    /**
     * Check credentials for user.
     *
     * @param user          user
     * @param givenPassword given password
     * @return True if access permitted and credentials are valid
     */
    boolean checkPassword(User user, String givenPassword);
}