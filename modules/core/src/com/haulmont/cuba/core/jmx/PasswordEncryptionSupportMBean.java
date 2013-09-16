/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.*;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedResource(description = "Generate hashes for passwords")
public interface PasswordEncryptionSupportMBean {

    @ManagedAttribute(description = "Default Hash method")
    String getPasswordHashMethod();

    @ManagedAttribute(description = "Supported hash methods")
    String getSupportedHashMethods();

    @ManagedOperation(description = "Get random password")
    String getRandomPassword();

    @ManagedOperation(description = "Get hash and salt for content")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "content", description = "String for hash")
    })
    String getHash(String content);

    @ManagedOperation(description = "Get hash for content with specified salt")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "content", description = "String for hash"),
            @ManagedOperationParameter(name = "salt", description = "Salt")
    })
    String getHash(String content, String salt);

    @ManagedOperation(description = "Get plain hash without salt")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "content", description = "String for hash")
    })
    String getPlainHash(String content);

    @ManagedOperation(description = "Get hash and salt for password")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "userId", description = "User id"),
            @ManagedOperationParameter(name = "password", description = "Password for hash")
    })
    String getPasswordHash(String userId, String password);

    @ManagedOperation(description = "Get hash and salt with specified method")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "content", description = "String for hash"),
            @ManagedOperationParameter(name = "method", description = "Hash method")
    })
    String getSpecificHash(String content, String method);

    @ManagedOperation(description = "Get hash and salt for password")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "userId", description = "User id"),
            @ManagedOperationParameter(name = "password", description = "Password for hash"),
            @ManagedOperationParameter(name = "method", description = "Hash method")
    })
    String getSpecificPasswordHash(String userId, String password, String method);

    @ManagedOperation(description = "Get hash with specified salt and method")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "content", description = "String for hash"),
            @ManagedOperationParameter(name = "salt", description = "Salt"),
            @ManagedOperationParameter(name = "method", description = "Hash method")
    })
    String getSpecificHash(String content, String salt, String method);

    @ManagedOperation(description = "Get plain hash without salt with specified method")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "content", description = "String for hash"),
            @ManagedOperationParameter(name = "method", description = "Hash method")
    })
    String getSpecificPlainHash(String content, String method);
}