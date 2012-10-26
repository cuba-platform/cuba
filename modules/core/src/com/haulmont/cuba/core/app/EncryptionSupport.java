/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.HashMethod;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AbstractEncryption;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_EncryptionSupport")
public class EncryptionSupport extends ManagementBean implements EncryptionSupportMBean {

    private static final String UNSUPPORTED_HASH_METHOD = "Unsupported Hash method";

    @Inject
    private Configuration configuration;

    @Override
    public String getPasswordHashMethod() {
        return configuration.getConfig(ServerConfig.class).getPasswordEncryption().getId();
    }

    @Override
    public String getSupportedHashMethods() {
        List<String> methods = new LinkedList<>();
        for (HashMethod hashMethod : HashMethod.values())
            methods.add(hashMethod.getId());
        return StringUtils.join(methods, ", ");
    }

    @Override
    public String getRandomPassword() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] passwordBytes = new byte[8];
        random.nextBytes(passwordBytes);
        return new String(Base64.encodeBase64(passwordBytes)).replace("=", "");
    }

    @Override
    public String getHash(String content) {
        return encryption.getHash(content).toString();
    }

    @Override
    public String getHash(String content, String salt) {
        return encryption.getHash(content, salt);
    }

    @Override
    public String getPlainHash(String content) {
        return encryption.getPlainHash(content);
    }

    @Override
    public String getPasswordHash(String password) {
        return encryption.getPasswordHash(password).toString();
    }

    @Override
    public String getSpecificHash(String content, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        AbstractEncryption abstractEncryption = (AbstractEncryption) encryption;
        EncryptionModule module = abstractEncryption.getModule(hashMethod);
        return module.getHash(content).toString();
    }

    @Override
    public String getSpecificPasswordHash(String password, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        AbstractEncryption abstractEncryption = (AbstractEncryption) encryption;
        EncryptionModule module = abstractEncryption.getModule(hashMethod);
        return module.getPasswordHash(password).toString();
    }

    @Override
    public String getSpecificHash(String content, String salt, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        AbstractEncryption abstractEncryption = (AbstractEncryption) encryption;
        EncryptionModule module = abstractEncryption.getModule(hashMethod);
        return module.getHash(content, salt);
    }

    @Override
    public String getSpecificPlainHash(String content, String method) {
        HashMethod hashMethod = HashMethod.fromId(method);
        if (hashMethod == null)
            return UNSUPPORTED_HASH_METHOD;
        AbstractEncryption abstractEncryption = (AbstractEncryption) encryption;
        EncryptionModule module = abstractEncryption.getModule(hashMethod);
        return module.getPlainHash(content);
    }
}