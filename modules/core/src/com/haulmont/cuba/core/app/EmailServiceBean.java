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

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.core.sys.ServiceInterceptor;
import com.haulmont.cuba.core.Locator;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

@Stateless(name = EmailService.JNDI_NAME)
@Interceptors(ServiceInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)

public class EmailServiceBean implements EmailService {

    public void sendEmail(String address, String caption, String body, EmailAttachment... attachment) throws EmailException {
        EmailerAPI emailer = Locator.lookupMBean(EmailerMBean.class).getAPI();
        emailer.sendEmail(address, caption, body, attachment);
    }

    public void sendEmail(EmailInfo info) throws EmailException {
        EmailerAPI emailer = Locator.lookupMBean(EmailerMBean.class).getAPI();
        emailer.sendEmail(info);
    }
}
