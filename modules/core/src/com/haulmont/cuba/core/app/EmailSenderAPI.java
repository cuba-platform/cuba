/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;

import javax.mail.MessagingException;

/**
 * Adapter to javax.mail email sending API.
 *
 * Should not be used from application code, use {@link EmailerAPI}.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public interface EmailSenderAPI {
    String NAME = "cuba_EmailSender";

    /**
     * Sends email with help of {@link org.springframework.mail.javamail.JavaMailSender}.
     *
     * Use {@link EmailerAPI} instead if you need email to be delivered reliably and stored to email history.
     *
     * @throws MessagingException if delivery fails
     */
    void sendEmail(SendingMessage sendingMessage) throws MessagingException;
}
