/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.SendingMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>$Id$</p>
 *
 * @author ovchinnikov
 */

public class EmailSendTask implements Runnable {

    private SendingMessage sendingMessage;
    private Log log = LogFactory.getLog(EmailSendTask.class);

    public EmailSendTask(SendingMessage message) {
        sendingMessage = message;
    }

    public void run() {
        try {
            EmailerAPI emailer = Locator.lookup(EmailerAPI.NAME);
            emailer.scheduledSendEmail(sendingMessage);
        } catch (Exception e) {
            log.error("Exception while sending email: " + ExceptionUtils.getStackTrace(e));
        }

    }

}
