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

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.CubaMailSender;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component(EmailSenderAPI.NAME)
public class EmailSender implements EmailSenderAPI {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    protected JavaMailSender mailSender;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected GlobalConfig globalConfig;

    @Resource(name = CubaMailSender.NAME)
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(SendingMessage sendingMessage) throws MessagingException {
        MimeMessage msg = createMimeMessage(sendingMessage);

        StopWatch sw = new Slf4JStopWatch("EmailSender.send");
        mailSender.send(msg);
        sw.stop();

        log.info("Email '{}' to '{}' has been sent successfully", msg.getSubject(), sendingMessage.getAddress());
    }

    @Override
    public void updateSession() {
        if (mailSender instanceof CubaMailSender)
            ((CubaMailSender) mailSender).updateSession();
    }

    protected MimeMessage createMimeMessage(SendingMessage sendingMessage) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        assignRecipient(Message.RecipientType.TO, sendingMessage.getAddress(), msg);
        assignRecipient(Message.RecipientType.CC, sendingMessage.getCc(), msg);
        assignRecipient(Message.RecipientType.BCC, sendingMessage.getBcc(), msg);
        msg.setSubject(sendingMessage.getCaption(), StandardCharsets.UTF_8.name());
        msg.setSentDate(timeSource.currentTimestamp());

        assignFromAddress(sendingMessage, msg);
        addHeaders(sendingMessage, msg);

        MimeMultipart content = new MimeMultipart("mixed");
        MimeMultipart textPart = new MimeMultipart("related");

        setMimeMessageContent(sendingMessage, content, textPart);

        for (SendingAttachment attachment : sendingMessage.getAttachments()) {
            MimeBodyPart attachmentPart = createAttachmentPart(attachment);

            if (attachment.getContentId() == null) {
                content.addBodyPart(attachmentPart);
            } else
                textPart.addBodyPart(attachmentPart);
        }

        msg.setContent(content);
        msg.saveChanges();
        return msg;
    }

    protected void setMimeMessageContent(SendingMessage sendingMessage, MimeMultipart content, MimeMultipart textPart)
            throws MessagingException {
        MimeBodyPart textBodyPart = new MimeBodyPart();
        MimeBodyPart contentBodyPart = new MimeBodyPart();

        contentBodyPart.setContent(sendingMessage.getContentText(), sendingMessage.getBodyContentType());
        textPart.addBodyPart(contentBodyPart);
        textBodyPart.setContent(textPart);
        content.addBodyPart(textBodyPart);
    }

    protected void assignRecipient(Message.RecipientType type, String addresses, MimeMessage message) throws MessagingException {
        if (StringUtils.isNotBlank(addresses)) {
            for (String address : splitAddresses(addresses)) {
                message.addRecipient(type, new InternetAddress(address.trim()));
            }
        }
    }

    protected Iterable<String> splitAddresses(String addresses) {
        return Splitter.on(CharMatcher.anyOf(";,")).omitEmptyStrings().trimResults().split(addresses);
    }

    protected void assignFromAddress(SendingMessage sendingMessage, MimeMessage msg) throws MessagingException {
        InternetAddress[] internetAddresses = InternetAddress.parse(sendingMessage.getFrom());
        for (InternetAddress internetAddress : internetAddresses) {
            if (StringUtils.isNotEmpty(internetAddress.getPersonal())) {
                try {
                    internetAddress.setPersonal(internetAddress.getPersonal(), StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    throw new MessagingException("Unsupported encoding type", e);
                }
            }
        }

        if (internetAddresses.length == 1) {
            msg.setFrom(internetAddresses[0]);
        } else {
            msg.addFrom(internetAddresses);
        }
    }

    private void addHeaders(SendingMessage sendingMessage, MimeMessage message) throws MessagingException {
        if (sendingMessage.getHeaders() == null)
            return;
        String[] splitHeaders = sendingMessage.getHeaders().split(SendingMessage.HEADERS_SEPARATOR);
        for (String header : splitHeaders) {
            EmailHeader emailHeader = EmailHeader.parse(header);
            if (emailHeader != null) {
                message.addHeader(emailHeader.getName(), emailHeader.getValue());
            } else {
                log.warn("Can't parse email header: '{}'", header);
            }
        }
    }

    protected MimeBodyPart createAttachmentPart(SendingAttachment attachment) throws MessagingException {
        DataSource source = new MyByteArrayDataSource(attachment.getContent());

        String mimeType = FileTypesHelper.getMIMEType(attachment.getName());

        String contentId = attachment.getContentId();
        if (contentId == null) {
            contentId = generateAttachmentContentId(attachment.getName());
        }

        String disposition = attachment.getDisposition() != null ? attachment.getDisposition() : Part.INLINE;
        String charset = MimeUtility.mimeCharset(attachment.getEncoding() != null ?
                attachment.getEncoding() : StandardCharsets.UTF_8.name());
        String contentTypeValue = String.format("%s; charset=%s; name=\"%s\"", mimeType, charset, attachment.getName());

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setHeader("Content-ID", "<" + contentId + ">");
        attachmentPart.setHeader("Content-Type", contentTypeValue);
        attachmentPart.setFileName(attachment.getName());
        attachmentPart.setDisposition(disposition);

        return attachmentPart;
    }

    protected String generateAttachmentContentId(String attachmentName) {
        if (StringUtils.isEmpty(attachmentName)) {
            return "";
        }

        String contentId;
        try {
            contentId = URLEncoder.encode(attachmentName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            contentId = attachmentName;
        }

        if (StringUtils.isNotEmpty(globalConfig.getWebHostName())) {
            contentId += "@" + globalConfig.getWebHostName();
        }

        return contentId;
    }

    protected static class MyByteArrayDataSource implements DataSource {
        private byte[] data;

        public MyByteArrayDataSource(byte[] data) {
            this.data = data;
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public String getName() {
            return "ByteArray";
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return null;
        }
    }
}