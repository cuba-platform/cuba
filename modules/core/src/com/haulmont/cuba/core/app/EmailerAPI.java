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
import com.haulmont.cuba.core.global.EmailDto;

import java.io.IOException;

public interface EmailerAPI
{
    void sendEmail(String address, String caption, String body, EmailAttachment... attachment)
            throws EmailException;

    void sendEmail(EmailDto dto) throws IOException, EmailException;
}
