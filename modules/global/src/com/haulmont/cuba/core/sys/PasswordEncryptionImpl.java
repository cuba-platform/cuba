/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.ManagedBean;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(PasswordEncryption.NAME)
public class PasswordEncryptionImpl implements PasswordEncryption {

    private EncryptionModule encryptionModule;

    public void setEncryptionModule(EncryptionModule encryptionModule) {
        this.encryptionModule = encryptionModule;
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
        return new String(Base64.encodeBase64(passwordBytes), StandardCharsets.UTF_8).replace("=", "");
    }

    @Override
    public String getHashMethod() {
        return encryptionModule.getHashMethod();
    }

    @Override
    public HashDescriptor getHash(String content) {
        return encryptionModule.getHash(content);
    }

    @Override
    public String getPasswordHash(UUID userId, String password) {
        return encryptionModule.getPasswordHash(userId, password);
    }

    @Override
    public String getHash(String content, String salt) {
        return encryptionModule.getHash(content, salt);
    }

    @Override
    public String getPlainHash(String content) {
        return encryptionModule.getPlainHash(content);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return encryptionModule.checkPassword(user, password);
    }
}