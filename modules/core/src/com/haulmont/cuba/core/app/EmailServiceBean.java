/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(EmailService.NAME)
public class EmailServiceBean implements EmailService {

    @Inject
    protected EmailerAPI emailer;

    public void sendEmail(String address, String caption, String body, EmailAttachment... attachment) throws EmailException {
        emailer.sendEmail(address, caption, body, attachment);
    }

    public void sendEmail(EmailInfo info) throws EmailException {
        emailer.sendEmail(info);
    }

    @Override
    public void sendEmailAsync(EmailInfo info, Integer attemptsCount, Date deadline) {
        emailer.sendEmailAsync(info, attemptsCount, deadline);
    }

    @Override
    public void sendEmailAsync(EmailInfo info) {
        emailer.sendEmailAsync(info);
    }

    @Override
    public String loadContentText(SendingMessage sendingMessage) {
        return emailer.loadContentText(sendingMessage);
    }
}
