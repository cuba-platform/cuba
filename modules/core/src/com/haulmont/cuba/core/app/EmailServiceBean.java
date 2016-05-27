/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;

@Service(EmailService.NAME)
public class EmailServiceBean implements EmailService {

    @Inject
    protected EmailerAPI emailer;

    @Override
    public void sendEmail(String address, String caption, String body, EmailAttachment... attachment) throws EmailException {
        emailer.sendEmail(address, caption, body, attachment);
    }

    @Override
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