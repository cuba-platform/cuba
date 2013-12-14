/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.encryption;

import com.haulmont.cuba.core.global.HashDescriptor;
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