/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.app.EmailerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.EmailAttachment;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_EmailerMBean")
public class Emailer implements EmailerMBean {

    @Inject
    protected EmailerAPI emailer;

    protected EmailerConfig emailerConfig;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.emailerConfig = configuration.getConfig(EmailerConfig.class);
    }

    @Override
    public String getFromAddress() {
        return emailer.getFromAddress();
    }

    @Authenticated
    @Override
    public void setFromAddress(String address) {
        if (address != null) {
            emailerConfig.setFromAddress(address);
        }
    }

    @Override
    public String getSmtpHost() {
        return emailer.getSmtpHost();
    }

    @Override
    public String sendTestEmail(String addresses) {
        try {
            String att = "<html><body><h1>Test attachment</h1></body></html>";
            EmailAttachment emailAtt = new EmailAttachment(att.getBytes(), "test attachment.html");
            emailer.sendEmail(addresses, "Test email", "<html><body><h1>Test email</h1></body></html>", emailAtt);
            return "Email to '" + addresses + "' sent succesfully";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
