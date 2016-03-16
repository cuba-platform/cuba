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

package com.haulmont.cuba.core.sys.encryption;

import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.UUID;

/**
 */
@Component("cuba_Sha1EncryptionModule")
public class Sha1EncryptionModule implements EncryptionModule {

    protected static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    protected static final String RANDOMIZE_ALGORITHM = "SHA1PRNG";
    protected static final int DERIVED_KEY_LENGTH_BITS = 160;

    protected static final int SALT_LENGTH_BYTES = 8;

    protected static final int ITERATIONS = 20000;

    protected static final String STATIC_SALT = "bae5b072f23b2417";

    @Override
    public String getHashMethod() {
        return "sha1";
    }

    @Override
    public HashDescriptor getHash(String content) {
        String salt;
        String result;
        try {
            salt = generateSalt();
            result = apply(content, salt.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new HashDescriptor(result, salt);
    }

    @Override
    public String getPasswordHash(UUID userId, String password) {
        String plainHash = getPlainHash(password);
        return getHash(plainHash, userId.toString());
    }

    @Override
    public String getHash(String content, String salt) {
        if (StringUtils.isEmpty(salt))
            salt = STATIC_SALT;
        String result;
        try {
            result = apply(content, salt.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public String getPlainHash(String content) {
        return getHash(content, STATIC_SALT);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        String hashedPassword = getHash(password, user.getId().toString());
        return StringUtils.equals(hashedPassword, user.getPassword());
    }

    protected String generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(RANDOMIZE_ALGORITHM);
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        random.nextBytes(salt);
        return new String(Hex.encodeHex(salt));
    }

    protected KeySpec getKeySpec(String content, byte[] salt) {
        return new PBEKeySpec(content.toCharArray(), salt, ITERATIONS, DERIVED_KEY_LENGTH_BITS);
    }

    protected String apply(String content, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = getKeySpec(content, salt);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);

        byte[] encoded = keyFactory.generateSecret(keySpec).getEncoded();
        return new String(Hex.encodeHex(encoded));
    }
}