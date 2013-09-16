/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
