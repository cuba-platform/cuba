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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 */
@Component("cuba_Md5EncryptionModule")
public class Md5EncryptionModule implements EncryptionModule {

    @Override
    public String getHashMethod() {
        return "md5";
    }

    @Override
    public HashDescriptor getHash(String content) {
        return new HashDescriptor(DigestUtils.md5Hex(content), null);
    }

    @Override
    public String getHash(String content, String salt) {
        return getPlainHash(content);
    }

    @Override
    public String getPlainHash(String content) {
        return DigestUtils.md5Hex(content);
    }

    @Override
    public String getPasswordHash(UUID userId, String password) {
        return getPlainHash(password);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return StringUtils.equals(user.getPassword(), password);
    }
}