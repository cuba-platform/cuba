/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.HashMethod;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.core.sys.encryption.UnsupportedHashMethodException;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * PasswordEncryptionSupport MBean implementation
 *
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_EncryptionSupport")
public class PasswordEncryptionSupport extends ManagementBean implements PasswordEncryptionSupportMBean {

    private static final String UNSUPPORTED_HASH_METHOD = "Unsupported Hash method";

    @Override
    public String getPasswordHashMethod() {
        return passwordEncryption.getHashMethod().getId();
    }

    @Override
    public String getSupportedHashMethods() {
        Map<String, EncryptionModule> encryptionModules = AppBeans.getAll(EncryptionModule.class);
        Set<String> methods = new HashSet<>();
        for (EncryptionModule module : encryptionModules.values())
            methods.add(module.getHashMethod().getId());

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
    public String getPasswordHash(String password) {
        return passwordEncryption.getPasswordHash(password).getDescription();
    }

    @Override
    public String getSpecificHash(String content, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        EncryptionModule module = getEncryptionModule(hashMethod);
        return module.getHash(content).getDescription();
    }

    @Override
    public String getSpecificPasswordHash(String password, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        EncryptionModule module = getEncryptionModule(hashMethod);
        return module.getPasswordHash(password).getDescription();
    }

    @Override
    public String getSpecificHash(String content, String salt, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        EncryptionModule module = getEncryptionModule(hashMethod);
        return module.getHash(content, salt);
    }

    @Override
    public String getSpecificPlainHash(String content, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        EncryptionModule module = getEncryptionModule(hashMethod);
        return module.getPlainHash(content);
    }

    private EncryptionModule getEncryptionModule(HashMethod hashMethod) {
        Map<String, EncryptionModule> encryptionModules = AppBeans.getAll(EncryptionModule.class);
        for (EncryptionModule module : encryptionModules.values()) {
            if (hashMethod == module.getHashMethod())
                return module;
        }
        throw new UnsupportedHashMethodException();
    }
}