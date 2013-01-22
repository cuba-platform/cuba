/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 11:06:14
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.security.global.LoginException;

import java.util.Date;
import java.util.List;

/**
 * Provides application with emailing functionality.
 * <br/>
 * Sending email can be synchronous (caller's thread is blocked until email is delivered to SMTP server)
 * or asynchronous (email is persisted in a DB queue and sent later by scheduled task).
 * <br/>
 * In order to send emails asynchronously, you should register a scheduled task that periodically invokes
 * {@link com.haulmont.cuba.core.app.EmailManagerAPI#queueEmailsToSend()} method.
 *
 * @see EmailManagerAPI
 */
public interface EmailerAPI {
    String NAME = "cuba_Emailer";

    /**
     * Send email synchronously.
     *
     * @param address    comma or semicolon separated list of addresses
     * @param caption    email subject
     * @param body       email body
     * @param attachment email attachments
     * @throws EmailException in case of any errors
     */
    void sendEmail(String address, String caption, String body, EmailAttachment... attachment)
            throws EmailException;

    /**
     * Send email synchronously.
     *
     * @param info email details
     * @throws EmailException in case of any errors
     * @see EmailInfo
     */
    void sendEmail(EmailInfo info) throws EmailException;

    /**
     * Send email synchronously.
     *
     * @param info email details
     * @param sync synchronous sending if true
     * @throws EmailException in case of error
     * @see EmailInfo
     */
    void sendEmail(EmailInfo info, boolean sync) throws EmailException;


    /**
     * Send email asynchronously, with limited number of attempts.
     *
     * @param info email details
     * @param attemptsCount  count of attempts to send (1 attempt = 1 emailer cron tick)
     * @param deadline Emailer tries to send message till deadline.
     *              If deadline has come and message has not been sent, status of this message will changed to
     *              {@link com.haulmont.cuba.core.global.SendingStatus#NOTSENT}
     * @see EmailInfo
     */
    void sendEmailAsync(EmailInfo info, Integer attemptsCount, Date deadline);

    /**
     * Send email asynchronously.
     *
     * @param info email details
     * @return List of created SendingMessage
     * @see EmailInfo
     */
    List<SendingMessage> sendMessagesAsync(EmailInfo info);

    /**
     * Used internally, don't invoke it from application code.
     */
    void scheduledSendEmail(SendingMessage sendingMessage) throws LoginException, EmailException;
}
