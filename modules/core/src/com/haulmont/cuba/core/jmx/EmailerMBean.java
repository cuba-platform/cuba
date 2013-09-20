/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * Management interface of the {@link com.haulmont.cuba.core.app.Emailer} MBean.<br>
 */
public interface EmailerMBean {

    String getFromAddress();

    void setFromAddress(String address);

    String getSmtpHost();

    int getSmtpPort();

    String getSmtpUser();

    boolean getSmtpAuthRequired();

    boolean getStarttlsEnable();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "addresses", description = "")})
    String sendTestEmail(String addresses);

    @ManagedOperation(description = "Migrate existing email history to use file storage")
    String migrateEmailsToFileStorage(String password);
}
