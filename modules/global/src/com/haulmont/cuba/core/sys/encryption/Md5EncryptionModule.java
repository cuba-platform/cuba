/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.encryption;

import com.haulmont.cuba.core.global.HashDescriptor;
import com.haulmont.cuba.core.global.HashMethod;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_Md5EncryptionModule")
public class Md5EncryptionModule implements EncryptionModule {

    @Override
    public HashMethod getHashMethod() {
        return HashMethod.MD5;
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
    public boolean checkPassword(User user, String givenPassword) {
        return StringUtils.equals(user.getPassword(), givenPassword);
    }
}