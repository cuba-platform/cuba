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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.codec.binary.Base64;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 */
@Component(PasswordEncryption.NAME)
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
            throw new RuntimeException("Unable to load SHA1PRNG", e);
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
        checkNotNullArgument(content);

        return encryptionModule.getHash(content);
    }

    @Override
    public String getPasswordHash(UUID userId, String password) {
        checkNotNullArgument(userId);
        checkNotNullArgument(password);

        return encryptionModule.getPasswordHash(userId, password);
    }

    @Override
    public String getHash(String content, String salt) {
        checkNotNullArgument(content);
        checkNotNullArgument(salt);

        return encryptionModule.getHash(content, salt);
    }

    @Override
    public String getPlainHash(String content) {
        checkNotNullArgument(content);

        return encryptionModule.getPlainHash(content);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        checkNotNullArgument(user);
        checkNotNullArgument(password);

        return encryptionModule.checkPassword(user, password);
    }
}