/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ovchinnikov
 * @version $Id$
 */
public class EmailSendTask implements Runnable {

    private SendingMessage sendingMessage;
    private Log log = LogFactory.getLog(EmailSendTask.class);

    public EmailSendTask(SendingMessage message) {
        sendingMessage = message;
    }

    @Override
    public void run() {
        try {
            EmailerAPI emailer = AppBeans.get(EmailerAPI.NAME);
            emailer.scheduledSendEmail(sendingMessage);
        } catch (Exception e) {
            log.error("Exception while sending email: " + ExceptionUtils.getStackTrace(e));
        }
    }
}