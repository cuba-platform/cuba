/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.sys.CubaMailSender;
import junit.framework.Assert;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Used by functional tests.
 * Fakes real JavaMailSender.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public class TestMailSender extends CubaMailSender {
    private List<MimeMessage> myMessages = new ArrayList<>();

    private boolean mustFail;

    public void clearBuffer() {
        myMessages.clear();
    }

    public int getBufferSize() {
        return myMessages.size();
    }

    public MimeMessage fetchSentEmail() {
        Assert.assertFalse(myMessages.isEmpty());
        return myMessages.remove(0);
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        if (mustFail) {
            throw new MailSendException("Smtp server not available");
        }
        myMessages.add(mimeMessage);
    }

    public boolean isEmpty() {
        return myMessages.isEmpty();
    }

    public void failPlease() {
        this.mustFail = true;
    }

    public void workNormallyPlease() {
        this.mustFail = false;
    }
}
