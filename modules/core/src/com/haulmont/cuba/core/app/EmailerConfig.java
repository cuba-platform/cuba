/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * Configuration parameters interface used for sending emails.
 *
 * @author krivopustov
 * @version $Id$
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

    /**
     * SMTP server port.
     */
    @Property("cuba.email.smtpPort")
    @Default("25")
    int getSmtpPort();

    /**
     * Whether to authenticate on SMTP server.
     */
    @Property("cuba.email.smtpAuthRequired")
    @DefaultBoolean(false)
    boolean getSmtpAuthRequired();

    /**
     * Whether to use STARTTLS command during the SMTP server authentication.
     */
    @Property("cuba.email.smtpStarttlsEnable")
    @DefaultBoolean(false)
    boolean getSmtpStarttlsEnable();

    /**
     * User name for the SMTP server authentication.
     */
    @Property("cuba.email.smtpUser")
    String getSmtpUser();

    /**
     * User password for the SMTP server authentication.
     */
    @Property("cuba.email.smtpPassword")
    String getSmtpPassword();

    /**
     * SMTP connection timeout value in seconds.
     */
    @Property("cuba.email.smtpConnectionTimeout")
    @DefaultInt(20)
    int getSmtpConnectionTimeoutSec();

    /**
     * SMTP I/O timeout value in seconds.
     */
    @Property("cuba.email.smtpTimeout")
    @DefaultInt(60)
    int getSmtpTimeoutSec();

    /**
     * How many scheduler ticks to skip after server startup.
     * Actual sending will start with the next call.
     * <p/> This reduces the server load on startup.
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
}
