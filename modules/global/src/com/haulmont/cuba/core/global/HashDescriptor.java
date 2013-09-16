/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

/**
 * Container for password hash and salt.
 *
 * @author artamonov
 * @version $Id$
 */
public class HashDescriptor {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hash: ")
                .append(hash)
                .append("\n");
        sb.append("Salt: ")
                .append(salt);

        return sb.toString();
    }
}