/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 08.12.2009 9:52:00
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;

public interface EmailService {

    String NAME = "cuba_EmailService";

    /**
     * Sends email
     * @param address   comma or semicolon separated list of addresses
     * @param caption   email subject
     * @param body      email body
     * @param attachment    email attachments
     * @throws com.haulmont.cuba.core.global.EmailException   in case of any errors
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
