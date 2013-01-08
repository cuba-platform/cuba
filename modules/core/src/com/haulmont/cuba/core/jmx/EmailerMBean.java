/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 10:55:55
 *
 * $Id$
 */
package com.haulmont.cuba.core.jmx;

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
}
