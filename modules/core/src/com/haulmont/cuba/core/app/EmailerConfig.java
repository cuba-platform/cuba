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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.global.Secret;

/**
 * Configuration parameters interface used for sending emails.
 */
@Source(type = SourceType.DATABASE)
public interface EmailerConfig extends Config {

    /**
     * Default "from" address
     */
    @Property("cuba.email.fromAddress")
    @Default("DoNotReply@localhost")
    String getFromAddress();

    void setFromAddress(String fromAddress);

    /**
     * SMTP server address.
     */
    @Property("cuba.email.smtpHost")
    @Default("test.host")
    String getSmtpHost();

    void setSmtpHost(String smtpHost);
    /**
     * SMTP server port.
     */
    @Property("cuba.email.smtpPort")
    @Default("25")
    int getSmtpPort();

    void setSmtpPort(int smtpPort);

    /**
     * Whether to authenticate on SMTP server.
     */
    @Property("cuba.email.smtpAuthRequired")
    @DefaultBoolean(false)
    boolean getSmtpAuthRequired();

    void setSmtpAuthRequired(boolean smtpAuthRequired);

    /**
     * Whether to use STARTTLS command during the SMTP server authentication.
     */
    @Property("cuba.email.smtpStarttlsEnable")
    @DefaultBoolean(false)
    boolean getSmtpStarttlsEnable();

    void setSmtpStarttlsEnable(boolean smtpStarttlsEnable);

    /**
     * User name for the SMTP server authentication.
     */
    @Property("cuba.email.smtpUser")
    String getSmtpUser();

    void setSmtpUser(String smtpUser);

    /**
     * User password for the SMTP server authentication.
     */
    @Secret
    @Property("cuba.email.smtpPassword")
    String getSmtpPassword();

    /**
     * SMTP connection timeout value in seconds.
     */
    @Property("cuba.email.smtpConnectionTimeoutSec")
    @DefaultInt(20)
    int getSmtpConnectionTimeoutSec();

    void setSmtpConnectionTimeoutSec(int smtpConnectionTimeoutSec);

    /**
     * If set to true, use SSL to connect
     *
     */
    @Property("cuba.email.smtpSslEnabled")
    @DefaultBoolean(false)
    boolean getSmtpSslEnabled();

    void setSmtpSslEnabled(boolean smtpSslEnabled);

    /**
     * If set to "true", UTF-8 strings are allowed in message headers, e.g., in addresses
     *
     */
    @Property("cuba.email.allowutf8")
    @DefaultBoolean(false)
    boolean getUtf8Enabled();

    void setUtf8Enabled(boolean smtpSslEnabled);

     /**
     * SMTP I/O timeout value in seconds.
     */
    @Property("cuba.email.smtpTimeoutSec")
    @DefaultInt(60)
    int getSmtpTimeoutSec();

    void setSmtpTimeoutSec(int smtpTimeoutSec);

    /**
     * How many scheduler ticks to skip after server startup.
     * Actual sending will start with the next call.
     * <br> This reduces the server load on startup.
     */
    @Property("cuba.email.delayCallCount")
    @Default("2")
    int getDelayCallCount();

    void setDelayCallCount(int delayCallCount);

    /**
     * Scheduler will process no more than given number of queued messages per every scheduler tick.
     */
    @Property("cuba.email.messageQueueCapacity")
    @Default("100")
    int getMessageQueueCapacity();

    /**
     * Max number of attempts to send a message, after which the message's status is set to NOT_SENT.
     */
    @Property("cuba.email.defaultSendingAttemptsCount")
    @DefaultInt(10)
    int getDefaultSendingAttemptsCount();

    /**
     * Timeout in seconds for message in {@link com.haulmont.cuba.core.global.SendingStatus#SENDING} status
     * to be successfully sent or failed. After this time passes, emailer will try to resend email again.
     */
    @Property("cuba.email.sendingTimeoutSec")
    @DefaultInt(240)
    int getSendingTimeoutSec();

    /**
     * All emails go to this address if {@link #getSendAllToAdmin()} is enabled, regardless of actual recipient.
     */
    @Property("cuba.email.adminAddress")
    @Default("admin@localhost")
    String getAdminAddress();

    void setAdminAddress(String adminAddress);

    /**
     * If this parameter is set to true, all email messages go to {@link #getAdminAddress()}.
     */
    @Property("cuba.email.sendAllToAdmin")
    @DefaultBoolean(false)
    boolean getSendAllToAdmin();

    void setSendAllToAdmin(boolean sendAllToAdmin);

    /**
     * When turned on, email body text and attachments will be stored in file storage
     * instead of BLOB columns in database.
     * Should be used if application stores lots of emails and/or email attachments.
     *
     * @see com.haulmont.cuba.core.entity.SendingMessage#contentTextFile
     * @see com.haulmont.cuba.core.entity.SendingAttachment#contentFile
     */
    @Property("cuba.email.useFileStorage")
    @DefaultBoolean(false)
    boolean isFileStorageUsed();

    void setFileStorageUsed(boolean fileStorageUsed);

    /**
     * User login used by asynchronous sending mechanism to be able to store information in the database.
     */
    @Property("cuba.emailerUserLogin")
    @Default("admin")
    String getEmailerUserLogin();
}