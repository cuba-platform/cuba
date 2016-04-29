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

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * Management interface of the {@link com.haulmont.cuba.core.app.Emailer} MBean.
 *
 */
public interface EmailerMBean {

    String getFromAddress();

    void setFromAddress(String address);

    String getSmtpHost();

    int getSmtpPort();

    String getSmtpUser();

    boolean getSmtpAuthRequired();

    boolean getStarttlsEnable();

    boolean getSmtpSslEnabled();

    int getSmtpTimeoutSec();

    int getSmtpConnectionTimeoutSec();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "addresses", description = "")})
    String sendTestEmail(String addresses);

    @ManagedOperation(description = "Migrate existing email history to use file storage")
    String migrateEmailsToFileStorage(String password);
}
