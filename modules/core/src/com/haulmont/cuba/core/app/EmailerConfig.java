/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * Configuration parameters interface used for sending emails.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Prefix("cuba.email.")
@Source(type = SourceType.DATABASE)
public interface EmailerConfig extends Config
{
    /**
     * @return Default "from" address
     */
    @Default("DoNotReply@haulmont.com")
    String getFromAddress();
    void setFromAddress(String fromAddress);

    /**
     * @return SMTP host name
     */
    @Default("test.host")
    String getSmtpHost();

    /**
     * Used in server startup.
     * Sending emails will be started after delayCallCount cron ticks (used to not overload server in startup)
     */
    @Default("2")
    int getDelayCallCount();

    /**
     * MaxResults query limit for load messages from DB in one tick
     */
    @Default("100")
    int getMessageQueueCapacity();

    /**
     * 
     * @return Quantity of sending attempts after which message's status is set to NOT_SENT
     */
    @DefaultInt(10)
    int getDefaultSendingAttemptsCount();

    /**
     *
     * @return Max time of sending message while it is still considered to be valid
     */
    @DefaultInt(120)
    int getMaxSendingTimeSec();

    /**
     *
     * @return Admin's email address
     */
    @Default("address@company.com")
    String getAdminAddress();

    /**
     * If this parameter is set to true, all email messages will be sent to admin's email address
     * @return
     */
    @DefaultBoolean(false)
    boolean getSendAllToAdmin();
}
