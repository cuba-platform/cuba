/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 08.12.2009 9:54:13
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import org.springframework.stereotype.Service;

@Service(EmailService.NAME)
public class EmailServiceBean implements EmailService {

    public void sendEmail(String address, String caption, String body, EmailAttachment... attachment) throws EmailException {
        EmailerAPI emailer = Locator.lookup(EmailerAPI.NAME);
        emailer.sendEmail(address, caption, body, attachment);
    }

    public void sendEmail(EmailInfo info) throws EmailException {
        EmailerAPI emailer = Locator.lookup(EmailerAPI.NAME);
        emailer.sendEmail(info);
    }
}
