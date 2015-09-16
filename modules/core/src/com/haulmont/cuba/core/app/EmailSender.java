/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.EmailHeader;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.CubaMailSender;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QCodec;
import org.apache.commons.lang.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
@ManagedBean(EmailSenderAPI.NAME)
public class EmailSender implements EmailSenderAPI {

    private Logger log = LoggerFactory.getLogger(EmailSender.class);

    protected JavaMailSender mailSender;

    @Inject
    protected TimeSource timeSource;

    @Resource(name = CubaMailSender.NAME)
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(SendingMessage sendingMessage) throws MessagingException {
        MimeMessage msg = createMimeMessage(sendingMessage);

        StopWatch sw = new Log4JStopWatch("EmailSender.send");
        mailSender.send(msg);
        sw.stop();

        log.info("Email '" + msg.getSubject() + "' to '" + sendingMessage.getAddress() + "' has been sent successfully");
    }

    protected MimeMessage createMimeMessage(SendingMessage sendingMessage) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        assignRecipient(sendingMessage, msg);
        msg.setSubject(sendingMessage.getCaption(), StandardCharsets.UTF_8.name());
        msg.setSentDate(timeSource.currentTimestamp());

        assignFromAddress(sendingMessage, msg);
        addHeaders(sendingMessage, msg);

        MimeMultipart content = new MimeMultipart("mixed");
        MimeBodyPart textBodyPart = new MimeBodyPart();
        MimeMultipart textPart = new MimeMultipart("related");
        MimeBodyPart contentBodyPart = new MimeBodyPart();
        String bodyContentType = getContentBodyType(sendingMessage);

        contentBodyPart.setContent(sendingMessage.getContentText(), bodyContentType);
        textPart.addBodyPart(contentBodyPart);
        textBodyPart.setContent(textPart);
        content.addBodyPart(textBodyPart);

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

    protected void assignRecipient(SendingMessage sendingMessage, MimeMessage message) throws MessagingException {
        InternetAddress internetAddress = new InternetAddress(sendingMessage.getAddress().trim());
        message.setRecipient(Message.RecipientType.TO, internetAddress);
    }

    protected String getContentBodyType(SendingMessage sendingMessage) {
        String text = sendingMessage.getContentText();
        String bodyContentType;
        if (text.trim().startsWith("<html>")) {
            bodyContentType = "text/html; charset=UTF-8";
        } else {
            bodyContentType = "text/plain; charset=UTF-8";
        }
        return bodyContentType;
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
                log.warn("Can't parse email header: '" + header + "'");
            }
        }
    }

    protected MimeBodyPart createAttachmentPart(SendingAttachment attachment) throws MessagingException {
        DataSource source = new MyByteArrayDataSource(attachment.getContent());

        String mimeType = FileTypesHelper.getMIMEType(attachment.getName());
        String encodedFileName = encodeAttachmentName(attachment);

        String contentId = attachment.getContentId();
        if (contentId == null) {
            contentId = encodedFileName;
        }

        String disposition = attachment.getDisposition() != null ? attachment.getDisposition() : Part.INLINE;
        String charset = MimeUtility.mimeCharset(attachment.getEncoding() != null ?
                attachment.getEncoding() : StandardCharsets.UTF_8.name());
        String contentTypeValue = String.format("%s; charset=%s; name=%s", mimeType, charset, encodedFileName);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setHeader("Content-ID", "<" + contentId + ">");
        attachmentPart.setHeader("Content-Type", contentTypeValue);
        attachmentPart.setFileName(encodedFileName);
        attachmentPart.setDisposition(disposition);

        return attachmentPart;
    }

    protected String encodeAttachmentName(SendingAttachment attachment) {
        String encodedFileName;
        try {
            QCodec qCodec = new QCodec();
            encodedFileName = qCodec.encode(attachment.getName());
        } catch (EncoderException e) {
            encodedFileName = attachment.getName();
        }
        return encodedFileName;
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
