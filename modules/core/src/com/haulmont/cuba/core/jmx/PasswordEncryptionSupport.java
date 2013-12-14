/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.core.sys.encryption.UnsupportedHashMethodException;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * PasswordEncryptionSupport MBean implementation
 *
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_PasswordEncryptionSupportMBean")
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