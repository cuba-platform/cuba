/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Service for sending emails.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface EmailService {

    String NAME = "cuba_EmailService";

    /**
     * Send email synchronously.
     *
     * @param address   comma or semicolon separated list of addresses
     * @param caption   email subject
     * @param body      email body
     * @param attachment    email attachments
     * @throws com.haulmont.cuba.core.global.EmailException   in case of any errors
     */
    void sendEmail(String address, String caption, String body, EmailAttachment... attachment)
            throws EmailException;

    /**
     * Send email synchronously.
     *
     * @param info   email details
     * @throws EmailException   in case of any errors
     */
    void sendEmail(EmailInfo info) throws EmailException;

    /**
     * Send email asynchronously, with limited number of attempts.
     *
     * @param info email details
     * @param attemptsCount  count of attempts to send (1 attempt = 1 emailer cron tick)
     * @param deadline Emailer tries to send message till deadline.
     *              If deadline has come and message has not been sent, status of this message is changed to
     *              {@link com.haulmont.cuba.core.global.SendingStatus#NOTSENT}
     */
    void sendEmailAsync(EmailInfo info, @Nullable Integer attemptsCount, @Nullable Date deadline);

    /**
     * Send email asynchronously.
     *
     * @param info email details
     */
    void sendEmailAsync(EmailInfo info);
}
