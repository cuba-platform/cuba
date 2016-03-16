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

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Service for sending emails.
 *
 */
public interface EmailService {

    String NAME = "cuba_EmailService";

    /**
     * Send email synchronously.
     *
     * @param address    comma or semicolon separated list of addresses
     * @param caption    email subject
     * @param body       email body
     * @param attachment email attachments
     * @throws com.haulmont.cuba.core.global.EmailException
     *          in case of any errors
     */
    void sendEmail(String address, String caption, String body, EmailAttachment... attachment)
            throws EmailException;

    /**
     * Send email synchronously.
     *
     * @param info email details
     * @throws EmailException in case of any errors
     */
    void sendEmail(EmailInfo info) throws EmailException;

    /**
     * Send email asynchronously, with limited number of attempts.
     * <p>
     * The actual sending is performed by invoking the {@code EmailerAPI.processQueuedEmails()} (e.g. from a scheduled task).
     *
     * @param info          email details
     * @param attemptsCount count of attempts to send (1 attempt = 1 emailer cron tick)
     * @param deadline      Emailer tries to send message till deadline.
     *                      If deadline has come and message has not been sent, status of this message is changed to
     *                      {@link com.haulmont.cuba.core.global.SendingStatus#NOTSENT}
     */
    void sendEmailAsync(EmailInfo info, @Nullable Integer attemptsCount, @Nullable Date deadline);

    /**
     * Send email asynchronously.
     * <p>
     * The actual sending is performed by invoking the {@code EmailerAPI.processQueuedEmails()} (e.g. from a scheduled task).
     *
     * @param info email details
     */
    void sendEmailAsync(EmailInfo info);

    /**
     * Load content text for given message.
     *
     * @return email content text
     */
    String loadContentText(SendingMessage sendingMessage);
}
