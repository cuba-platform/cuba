/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.app.core.sendingmessage.browse.resendmessage;

import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@UiController("ResendMessage")
@UiDescriptor("resend-message.xml")
public class ResendMessage extends Screen {
    protected SendingMessage message;

    @Inject
    protected TextField<String> emailTextField;

    @Inject
    protected FileLoader fileLoader;
    @Inject
    protected EmailService emailService;
    @Inject
    protected Notifications notifications;
    @Inject
    protected MessageBundle messageBundle;
    @Inject
    protected DataManager dataManager;

    public void setMessage(SendingMessage message) {
        this.message = message;
        emailTextField.setValue(message.getAddress());
    }

    @Subscribe("resendEmailBtn")
    protected void onResendEmailBtnClick(Button.ClickEvent event) {
        if (message != null) {
            EmailInfo emailInfo = EmailInfoBuilder.create()
                    .setAddresses(emailTextField.getValue())
                    .setCaption(message.getCaption())
                    .setBody(emailBody(message))
                    .setFrom(message.getFrom())
                    .setBodyContentType(message.getBodyContentType())
                    .setAttachments(getAttachmentsArray(message.getAttachments()))
                    .setBcc(message.getBcc())
                    .setCc(message.getCc())
                    .setHeaders(parseHeadersString(message.getHeaders()))
                    .build();
            try {
                emailService.sendEmail(emailInfo);
            } catch (EmailException e) {
                throw new RuntimeException("Something went wrong during email resending", e);
            }
            notifications.create(Notifications.NotificationType.HUMANIZED)
                    .withCaption(messageBundle.getMessage("resendMessage.notification.caption"))
                    .withDescription(messageBundle.getMessage("resendMessage.notification.description"))
                    .show();
            this.closeWithDefaultAction();
        }
    }

    protected String emailBody(SendingMessage message) {
        if (message.getContentTextFile() != null) {
            try (InputStream inputStream = fileLoader.openStream(message.getContentTextFile());) {
                return IOUtils.toString(inputStream, Charset.defaultCharset());
            } catch (FileStorageException | IOException e) {
                throw new RuntimeException("Can't read message body from the file", e);
            }
        }
        return message.getContentText();
    }

    protected List<EmailHeader> parseHeadersString(String headersString) {
        List<EmailHeader> emailHeadersList = new ArrayList<>();
        if (headersString != null) {
            for (String header : headersString.split("\n")) {
                emailHeadersList.add(EmailHeader.parse(header));
            }
        }
        return emailHeadersList;
    }

    protected EmailAttachment[] getAttachmentsArray(List<SendingAttachment> sendingAttachments) {
        EmailAttachment[] emailAttachments = new EmailAttachment[sendingAttachments.size()];
        for (int i = 0; i < sendingAttachments.size(); i++) {
            SendingAttachment sendingAttachment = sendingAttachments.get(i);
            byte[] content = retrieveContent(sendingAttachment);
            EmailAttachment emailAttachment = new EmailAttachment(
                    content,
                    sendingAttachment.getName(),
                    sendingAttachment.getContentId(),
                    sendingAttachment.getDisposition(),
                    sendingAttachment.getEncoding()
            );
            emailAttachments[i] = emailAttachment;
        }
        return emailAttachments;
    }

    protected byte[] retrieveContent(SendingAttachment sendingAttachment) {
        byte[] content = sendingAttachment.getContent();
        if (content != null) {
            return content;
        }

        FileDescriptor contentFile = dataManager.load(FileDescriptor.class)
                .query("select e.contentFile from sys$SendingAttachment e where e.id = :id")
                .parameter("id", sendingAttachment.getId())
                .one();
        try (InputStream inputStream = fileLoader.openStream(contentFile)) {
            content = IOUtils.toByteArray(inputStream);
        } catch (FileStorageException | IOException e) {
            throw new RuntimeException("Can't read content from message attachment", e);
        }
        return content;
    }
}
