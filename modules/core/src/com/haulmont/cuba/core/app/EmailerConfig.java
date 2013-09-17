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
     * How many calls of <code>EmailManager.queueEmailsToSend()</code> to skip after a server startup.
     * Actual sending will start with the next call.
     * <p/> This reduces the server load on startup.
     */
    @Property("cuba.email.delayCallCount")
    @Default("2")
    int getDelayCallCount();

    /**
     * Max number of messages to read from queue in one <code>EmailManager.queueEmailsToSend()</code> call.
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
     * Max estimated time needed to send a message to SMTP server. This is used to optimize the loading
     * messages from queue.
     */
    @Property("cuba.email.maxSendingTimeSec")
    @DefaultInt(120)
    int getMaxSendingTimeSec();

    /**
     * All emails go to this address if <code>cuba.email.sendAllToAdmin=true</code>, regardless of actual recipient.
     */
    @Property("cuba.email.adminAddress")
    @Default("admin@localhost")
    String getAdminAddress();

    /**
     * If this parameter is set to true, all email messages go to <code>cuba.email.adminAddress</code>.
     */
    @Property("cuba.email.sendAllToAdmin")
    @DefaultBoolean(false)
    boolean getSendAllToAdmin();
}
