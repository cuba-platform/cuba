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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.core.sys.encryption.UnsupportedHashMethodException;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * PasswordEncryptionSupport MBean implementation
 *
 */
@Component("cuba_PasswordEncryptionSupportMBean")
public class PasswordEncryptionSupport implements PasswordEncryptionSupportMBean {

    private static final String UNSUPPORTED_HASH_METHOD = "Unsupported Hash method";

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Override
    public String getPasswordHashMethod() {
        return passwordEncryption.getHashMethod();
    }

    @Override
    public String getSupportedHashMethods() {
        Map<String, EncryptionModule> encryptionModules = AppBeans.getAll(EncryptionModule.class);
        Set<String> methods = new HashSet<>();
        for (EncryptionModule module : encryptionModules.values()) {
            methods.add(module.getHashMethod());
        }

        return StringUtils.join(methods, ", ");
    }

    @Override
    public String getRandomPassword() {
        return passwordEncryption.generateRandomPassword();
    }

    @Override
    public String getHash(String content) {
        return passwordEncryption.getHash(content).toString();
    }

    @Override
    public String getHash(String content, String salt) {
        return passwordEncryption.getHash(content, salt);
    }

    @Override
    public String getPlainHash(String content) {
        return passwordEncryption.getPlainHash(content);
    }

    @Override
    public String getPasswordHash(String userId, String password) {
        UUID userUUID;
        try {
            userUUID = UUID.fromString(userId);
        } catch (Exception e) {
            return "Invalid user Id";
        }

        return passwordEncryption.getPasswordHash(userUUID, password);
    }

    @Override
    public String getSpecificHash(String content, String method) {
        EncryptionModule module;
        try {
            module = getEncryptionModule(method);
        } catch (UnsupportedHashMethodException ex) {
            return UNSUPPORTED_HASH_METHOD;
        }
        return module.getHash(content).toString();
    }

    @Override
    public String getSpecificPasswordHash(String userId, String password, String method) {
        UUID userUUID;
        try {
            userUUID = UUID.fromString(userId);
        } catch (Exception e) {
            return "Invalid user Id";
        }

        EncryptionModule module;
        try {
            module = getEncryptionModule(method);
        } catch (UnsupportedHashMethodException ex) {
            return UNSUPPORTED_HASH_METHOD;
        }
        return module.getPasswordHash(userUUID, password);
    }

    @Override
    public String getSpecificHash(String content, String salt, String method) {
        EncryptionModule module;
        try {
            module = getEncryptionModule(method);
        } catch (UnsupportedHashMethodException ex) {
            return UNSUPPORTED_HASH_METHOD;
        }
        return module.getHash(content, salt);
    }

    @Override
    public String getSpecificPlainHash(String content, String method) {
        EncryptionModule module;
        try {
            module = getEncryptionModule(method);
        } catch (UnsupportedHashMethodException ex) {
            return UNSUPPORTED_HASH_METHOD;
        }
        return module.getPlainHash(content);
    }

    protected EncryptionModule getEncryptionModule(String hashMethod) {
        Map<String, EncryptionModule> encryptionModules = AppBeans.getAll(EncryptionModule.class);
        for (EncryptionModule module : encryptionModules.values()) {
            if (StringUtils.equals(hashMethod, module.getHashMethod())) {
                return module;
            }
        }
        throw new UnsupportedHashMethodException();
    }
}