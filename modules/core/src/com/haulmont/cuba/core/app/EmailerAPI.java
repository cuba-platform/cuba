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

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;

/**
 * API of {@link Emailer} MBean.<br>
 */
public interface EmailerAPI
{
    String NAME = "cuba_Emailer";

    /**
     * Sends email
     * @param address   comma or semicolon separated list of addresses
     * @param caption   email subject
     * @param body      email body
     * @param attachment    email attachments
     * @throws EmailException   in case of any errors
     */
    void sendEmail(String address, String caption, String body, EmailAttachment... attachment)
            throws EmailException;

    /**
     * Sends email
     * @param info   object containing information about sending
     * @throws EmailException   in case of any errors
     */
    void sendEmail(EmailInfo info) throws EmailException;
}
