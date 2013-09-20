/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;

import javax.mail.MessagingException;

/**
 * Adapter to javax.mail email sending API.
 * <p/>
 * Should not be used from application code, use {@link EmailerAPI}.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public interface EmailSenderAPI {
    String NAME = "cuba_EmailSender";

    /**
     * Sends email with help of {@link org.springframework.mail.javamail.JavaMailSender}.
     * Message body and attachments' content must be loaded from file storage.
     * <p/>
     * Use {@link EmailerAPI} instead if you need email to be delivered reliably and stored to email history.
     *
     * @throws MessagingException if delivery fails
     */
    void sendEmail(SendingMessage sendingMessage) throws MessagingException;
}
