/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.encryption;

import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.security.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("cuba_BCryptEncryptionModule")
public class BCryptEncryptionModule implements EncryptionModule {
    @Override
    public String getHashMethod() {
        return "bcrypt";
    }

    @Override
    public HashDescriptor getHash(String content) {
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(content, salt);
        return new HashDescriptor(hash, salt);
    }

    @Override
    public String getPasswordHash(UUID userId, String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    @Override
    public String getHash(String content, String salt) {
        salt = BCrypt.gensalt();
        return BCrypt.hashpw(content, salt);
    }

    @Override
    @Deprecated
    public String getPlainHash(String content) {
        throw new UnsupportedOperationException("Deprecated method");
    }

    @Override
    public boolean checkPassword(User user, String rawPassword) {
        return BCrypt.checkpw(rawPassword, user.getPassword());
    }
}
