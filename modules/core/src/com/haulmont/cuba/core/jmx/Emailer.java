/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.app.EmailerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.security.app.Authenticated;
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

    protected EmailerConfig config;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    @Override
    public String getFromAddress() {
        return config.getFromAddress();
    }

    @Authenticated
    @Override
    public void setFromAddress(String address) {
        if (address != null) {
            config.setFromAddress(address);
        }
    }

    @Override
    public String getSmtpHost() {
        return config.getSmtpHost();
    }

    @Override
    public int getSmtpPort() {
        return config.getSmtpPort();
    }

    @Override
    public String getSmtpUser() {
        return config.getSmtpUser();
    }

    @Override
    public boolean getSmtpAuthRequired() {
        return config.getSmtpAuthRequired();
    }

    @Override
    public boolean getStarttlsEnable() {
        return config.getSmtpStarttlsEnable();
    }

    @Authenticated
    @Override
    public String sendTestEmail(String addresses) {
        try {
            String att = "<html><body><h1>Test attachment</h1></body></html>";
            EmailAttachment emailAtt = EmailAttachment.createTextAttachment(att, "UTF-8", "test attachment.html");
            emailer.sendEmail(addresses, "Test email", "<html><body><h1>Test email</h1></body></html>", emailAtt);
            return "Email to '" + addresses + "' sent succesfully";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
