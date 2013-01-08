/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
public interface EmailerConfig extends Config
{
    /**
     * @return Default "from" address
     */
    @Property("cuba.email.fromAddress")
    @Default("DoNotReply@haulmont.com")
    String getFromAddress();
    void setFromAddress(String fromAddress);

    @Property("cuba.email.smtpHost")
    @Default("test.host")
    String getSmtpHost();

    @Property("cuba.email.smtpPort")
    @Default("25")
    int getSmtpPort();

    @Property("cuba.email.smtpAuthRequired")
    @DefaultBoolean(false)
    boolean getSmtpAuthRequired();

    @Property("cuba.email.smtpStarttlsEnable")
    @DefaultBoolean(false)
    boolean getSmtpStarttlsEnable();

    @Property("cuba.email.smtpUser")
    String getSmtpUser();

    @Property("cuba.email.smtpPassword")
    String getSmtpPassword();

    /**
     * Used in server startup.
     * Sending emails will be started after delayCallCount cron ticks (used to not overload server in startup)
     */
    @Property("cuba.email.delayCallCount")
    @Default("2")
    int getDelayCallCount();

    /**
     * MaxResults query limit for load messages from DB in one tick
     */
    @Property("cuba.email.messageQueueCapacity")
    @Default("100")
    int getMessageQueueCapacity();

    /**
     * 
     * @return Quantity of sending attempts after which message's status is set to NOT_SENT
     */
    @Property("cuba.email.defaultSendingAttemptsCount")
    @DefaultInt(10)
    int getDefaultSendingAttemptsCount();

    /**
     *
     * @return Max time of sending message while it is still considered to be valid
     */
    @Property("cuba.email.maxSendingTimeSec")
    @DefaultInt(120)
    int getMaxSendingTimeSec();

    /**
     *
     * @return Admin's email address
     */
    @Property("cuba.email.adminAddress")
    @Default("address@company.com")
    String getAdminAddress();

    /**
     * If this parameter is set to true, all email messages will be sent to admin's email address
     */
    @Property("cuba.email.sendAllToAdmin")
    @DefaultBoolean(false)
    boolean getSendAllToAdmin();
}
