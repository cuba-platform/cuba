/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.entity.HashMethod;
import com.haulmont.cuba.core.global.Encryption;
import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.core.sys.encryption.Md5EncryptionModule;
import com.haulmont.cuba.core.sys.encryption.Sha1EncryptionModule;
import com.haulmont.cuba.core.sys.encryption.UnsupportedHashMethodException;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class AbstractEncryption implements Encryption {

    protected abstract HashMethod getEncryptionMethod();

    protected volatile EncryptionModule encryptionModule;

    protected EncryptionModule getEncryptionModule() {
        if (encryptionModule == null) {
            synchronized (this) {
                if (encryptionModule == null) {
                    encryptionModule = getModule(getEncryptionMethod());
                }
            }
        }
        return encryptionModule;
    }

    public EncryptionModule getModule(HashMethod hashMethod) {
        switch (hashMethod) {
            case MD5:
                return new Md5EncryptionModule();
            case SHA1:
                return new Sha1EncryptionModule();
            default:
                throw new UnsupportedHashMethodException();
        }
    }

    @Override
    public String generateRandomPassword() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] passwordBytes = new byte[6];
        random.nextBytes(passwordBytes);
        return new String(Base64.encodeBase64(passwordBytes)).replace("=", "");
    }

    @Override
    public HashDescriptor getHash(String content) {
        return getEncryptionModule().getHash(content);
    }

    @Override
    public HashDescriptor getPasswordHash(String password) {
        return getEncryptionModule().getPasswordHash(password);
    }

    @Override
    public String getHash(String content, String salt) {
        return getEncryptionModule().getHash(content, salt);
    }

    @Override
    public String getPlainHash(String content) {
        return getEncryptionModule().getPlainHash(content);
    }

    @Override
    public boolean checkUserAccess(User user, String givenPassword) {
        return getEncryptionModule().checkUserAccess(user, givenPassword);
    }
}