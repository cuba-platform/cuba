/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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
     */
    void sendEmail(EmailInfo info) throws EmailException;

    /**
     * Send email synchronously or asynchronously.
     *
     * @param info email details
     * @param sync synchronous sending if true
     * @throws EmailException in case of error on synchronous sending
     */
    void sendEmail(EmailInfo info, boolean sync) throws EmailException;

    /**
     * Send email asynchronously, with limited number of attempts.
     *
     * @param info email details
     * @param attemptsCount  count of attempts to send (1 attempt = 1 emailer cron tick)
     * @param deadline Emailer tries to send message till deadline.
     *              If deadline has come and message has not been sent, status of this message is changed to
     *              {@link com.haulmont.cuba.core.global.SendingStatus#NOTSENT}
     * @return list of created {@link SendingMessage}s
     */
    List<SendingMessage> sendEmailAsync(EmailInfo info, Integer attemptsCount, Date deadline);

    /**
     * Send email asynchronously.
     *
     * @param info email details
     * @return list of created {@link SendingMessage}s
     */
    List<SendingMessage> sendEmailAsync(EmailInfo info);

    /** For internal use only. Don't call from application code. */
    void scheduledSendEmail(SendingMessage sendingMessage) throws LoginException, EmailException;
}
