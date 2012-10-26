/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

/**
 * @author artamonov
 * @version $Id$
 */
public class PasswordHashDescriptor extends HashDescriptor {

    private final String accessHash;

    public PasswordHashDescriptor(String hash, String accessHash, String salt) {
        super(hash, salt);
        this.accessHash = accessHash;
    }

    public PasswordHashDescriptor(HashDescriptor descriptor, String accessHash) {
        super(descriptor.getHash(), descriptor.getSalt());
        this.accessHash = accessHash;
    }

    public String getAccessHash() {
        return accessHash;
    }
}
