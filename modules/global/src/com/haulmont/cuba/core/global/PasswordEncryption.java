/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

/**
 * Encryption support for hashing passwords.
 *
 */
public interface PasswordEncryption {

    String NAME = "cuba_PasswordEncryption";

    /**
     * @return a random password with Base64 symbols
     */
    String generateRandomPassword();

    /**
     * @return a hashing method in use
     */
    String getHashMethod();

    /**
     * Hashing string with salt.
     *
     * @param content string for hashing
     * @return hash with random salt. If the current HashMethod doesn't support salt, it is set to null.
     */
    HashDescriptor getHash(String content);

    /**
     * Hashing password to store it into DB.
     *
     * @param userId    user id
     * @param password  content for hashing
     * @return hash with salt, if it is supported by the current HashMethod
     */
    String getPasswordHash(UUID userId, String password);

    /**
     * Hashing string.
     *
     * @param content content for hashing
     * @param salt    salt
     * @return hash with salt, if it is supported by the current HashMethod
     */
    String getHash(String content, String salt);

    /**
     * Hashing string without salt.
     * This method must be used to encrypt password on a client tier before sending it to the middleware.
     *
     * @param content content for hashing
     * @return hash
     */
    String getPlainHash(String content);

    /**
     * Check password for a user.
     * This method is used on the middleware to compare password passed from a client with the one stored in the DB.
     *
     * @param user      user
     * @param password  password to check. It must be previously encrypted with {@link #getPlainHash(String)} method.
     * @return true if the password is valid
     */
    boolean checkPassword(User user, String password);
}