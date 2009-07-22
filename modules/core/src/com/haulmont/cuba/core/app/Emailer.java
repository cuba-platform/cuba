/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 11:03:34
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.global.LoginException;

import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.Transport;
import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.codec.net.QCodec;
import org.apache.commons.codec.EncoderException;

public class Emailer extends ManagementBean implements EmailerMBean, EmailerAPI {
    private Log log = LogFactory.getLog(Emailer.class);

    private EmailerConfig config = ConfigProvider.getConfig(EmailerConfig.class);

    public EmailerAPI getAPI() {
        return this;
    }

    public void sendEmail(String addresses, String caption, String body, EmailAttachment... attachment)
            throws EmailException {
        sendEmail(addresses, caption, body, config.getFromAddress(), attachment);
    }

    public void sendEmail(String addresses, String caption, String body, String from, EmailAttachment... attachment)
            throws EmailException {
        String[] addrArr = addresses.split("[,;]");
        Session session = getMailSession();

        List<String> failedAddresses = new ArrayList<String>();
        List<String> errorMessages = new ArrayList<String>();

        for (String addr : addrArr) {
            try {
                addr = addr.trim();
                String fromEmail;
                if (from == null) {
                    fromEmail = config.getFromAddress();
                } else {
                    fromEmail = from;
                }

                MimeMessage message = createMessage(session, addr, caption, body, attachment, fromEmail);
                send(session, addr, message);
                log.info("Email '" + caption + "' to '" + addr + "' sent succesfully");
            } catch (MessagingException e) {
                log.warn("Unable to send email to '" + addr + "'", e);
                failedAddresses.add(addr);
                errorMessages.add(e.getMessage());
            }
        }
        if (!failedAddresses.isEmpty()) {
            throw new EmailException(
                    failedAddresses.toArray(new String[failedAddresses.size()]),
                    errorMessages.toArray(new String[errorMessages.size()])
            );
        }
    }

    private void send(Session session, String addr, MimeMessage message) throws MessagingException {
        InternetAddress internetAddress = new InternetAddress(addr);
        Transport transp = session.getTransport();
        transp.connect();
        transp.sendMessage(message, new InternetAddress[]{internetAddress});
    }

    private Session getMailSession() {
        Context ctx = Locator.getJndiContext();
        Session session;
        try {
            session = (Session) ctx.lookup("java:/Mail");
        } catch (NamingException e) {
            throw new RuntimeException("Unable to get Email session", e);
        }
        return session;
    }

    private MimeMessage createMessage(
            Session session,
            String address,
            String caption,
            String text,
            EmailAttachment[] attachments, String from) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.addRecipients(Message.RecipientType.TO, address);
        msg.setSubject(caption);
        msg.setSentDate(new Date());
        msg.setFrom(new InternetAddress(from));

        MimeMultipart content = new MimeMultipart("related");
        MimeBodyPart contentBodyPart = new MimeBodyPart();
        if (text.startsWith("<html>")) {
            contentBodyPart.setContent(text, "text/html; charset=UTF-8");
        } else {
            contentBodyPart.setContent(text, "text/plain; charset=UTF-8");
        }
        content.addBodyPart(contentBodyPart);

        if (attachments != null) {
            for (EmailAttachment attachment : attachments) {
                MimeBodyPart attachBodyPart = new MimeBodyPart();

                DataSource source = new ByteArrayDataSource(attachment.getData());
                attachBodyPart.setDataHandler(new DataHandler(source));

                String contentType = FileTypesHelper.getMIMEType(attachment.getName());

                String encodedFileName;
                try {
                    QCodec codec = new QCodec();
                    encodedFileName = codec.encode(attachment.getName());
                } catch (EncoderException e) {
                    encodedFileName = attachment.getName();
                }

                attachBodyPart.setHeader("Content-ID", "<" + encodedFileName + ">");
                attachBodyPart.setHeader("Content-Type", contentType + "; charset=utf-8; name=" + encodedFileName);
                attachBodyPart.setFileName(encodedFileName);
                attachBodyPart.setDisposition("inline");

                content.addBodyPart(attachBodyPart);
            }
        }

        msg.setContent(content);
        msg.saveChanges();

        return msg;
    }

    public String getFromAddress() {
        return config.getFromAddress();
    }

    public void setFromAddress(String address) {
        if (address != null) {
            try {
                login();
                config.setFromAddress(address);
            } catch (LoginException e) {
                throw new RuntimeException(e);
            } finally {
                logout();
            }
        }
    }

    public String sendTestEmail(String addresses) {
        try {
            String att = "<html><body><h1>Test attachment</h1></body></html>";
            EmailAttachment emailAtt = new EmailAttachment(att.getBytes(), "test attachment.html");
            sendEmail(addresses, "Test email", "<html><body><h1>Test email</h1></body></html>", emailAtt);
            return "Email to '" + addresses + "' sent succesfully";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    private static class ByteArrayDataSource implements DataSource {
        private byte[] data;

        public ByteArrayDataSource(byte[] data) {
            this.data = data;
        }

        public String getContentType() {
            return "application/octet-stream";
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArray";
        }

        public OutputStream getOutputStream() throws IOException {
            return null;
        }
    }
}
