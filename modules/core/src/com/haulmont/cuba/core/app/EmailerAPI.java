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

/**
 * API of {@link Emailer} MBean.<br>
 */
public interface EmailerAPI {
    String NAME = "cuba_Emailer";

    /**
     * Sends email
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
     * Sends email
     *
     * @param info object containing information about sending
     * @throws EmailException in case of any errors
     */
    void sendEmail(EmailInfo info) throws EmailException;

    /**
     *
     * @param info
     * @param sync  - synchronous sending if true
     * @throws EmailException
     */
    void sendEmail(EmailInfo info, boolean sync) throws EmailException;


    void sendEmailAsync(EmailInfo info, Integer attemptsCount,Date deadline) throws EmailException;

    /**
     * Don't use
     */
    void scheduledSendEmail(SendingMessage sendingMessage) throws LoginException, EmailException;

}
