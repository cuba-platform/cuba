/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import org.apache.commons.lang.StringUtils;

/**
 * Container for password hash and salt.
 *
 * @author artamonov
 * @version $Id$
 */
public class HashDescriptor {

    public static final char PASSWORD_SALT_SEPARATOR = ':';

    private final String hash;

    private final String salt;

    public HashDescriptor(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public static HashDescriptor parse(String credentialsString) {
        int separatorIndex = StringUtils.indexOf(credentialsString, HashDescriptor.PASSWORD_SALT_SEPARATOR);
        String passwordHash;
        String salt;

        if (separatorIndex >= 0) {
            passwordHash = StringUtils.substring(credentialsString, 0, separatorIndex);
            salt = StringUtils.substring(credentialsString, separatorIndex + 1);
        } else {
            passwordHash = credentialsString;
            salt = "";
        }
        return new HashDescriptor(passwordHash, salt);
    }

    public String toCredentialsString() {
        StringBuilder sb = new StringBuilder();
        if (hash != null)
            sb.append(hash);
        if (salt != null) {
            sb.append(PASSWORD_SALT_SEPARATOR);
            sb.append(salt);
        }
        return sb.toString();
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hash: ")
                .append(hash)
                .append("\n");
        sb.append("Salt: ")
                .append(salt);

        return sb.toString();
    }

    @Override
    public String toString() {
        return toCredentialsString();
    }
}