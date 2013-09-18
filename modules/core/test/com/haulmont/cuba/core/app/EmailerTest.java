/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.CubaMailSender;
import com.haulmont.cuba.testsupport.TestMailSender;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class EmailerTest extends CubaTestCase {

    private EmailerAPI emailer;
    private TestMailSender testMailSender;
    private TimeSource timeSource;

    private EmailerConfig emailerConfig;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        emailer = AppBeans.get(EmailerAPI.NAME);
        testMailSender = AppBeans.get(CubaMailSender.NAME);
        timeSource = AppBeans.get(TimeSource.NAME);

        emailerConfig = AppBeans.get(Configuration.class).getConfig(EmailerConfig.class);
        emailerConfig.setDelayCallCount(0);

        // send pending emails which might be in the queue
        emailer.processQueuedEmails();
        testMailSender.clearBuffer();
    }

    /*
     * Test single recipient, text body, subject.
     */
    public void testSynchronous() throws Exception {
        testMailSender.clearBuffer();

        EmailInfo myInfo = new EmailInfo("testemail@example.com", "Test Email", "Test Body");
        emailer.sendEmail(myInfo);

        assertEquals(1, testMailSender.getBufferSize());
        MimeMessage msg = testMailSender.fetchSentEmail();

        assertEquals(1, msg.getAllRecipients().length);
        assertEquals("testemail@example.com", msg.getAllRecipients()[0].toString());

        assertEquals("Test Email", msg.getSubject());
        assertEquals("Test Body", getBody(msg));
        assertTrue(getBodyContentType(msg).startsWith("text/plain;"));
    }

    /*
     * Test sendEmail() with parameter list.
     */
    public void testSimpleParamList() throws Exception {
        testMailSender.clearBuffer();

        emailer.sendEmail("myemail@example.com", "Test Email", "Test Body 2");

        assertEquals(1, testMailSender.getBufferSize());
        MimeMessage msg = testMailSender.fetchSentEmail();

        assertEquals(1, msg.getAllRecipients().length);
        assertEquals("myemail@example.com", msg.getAllRecipients()[0].toString());

        assertEquals("Test Email", msg.getSubject());
        assertEquals("Test Body 2", getBody(msg));
        assertTrue(getBodyContentType(msg).startsWith("text/plain;"));
    }

    public void testAsynchronous() throws Exception {
        testMailSender.clearBuffer();

        String body = "Test Email Body";
        EmailInfo myInfo = new EmailInfo("recipient@example.com", "Test", body);
        List<SendingMessage> messages = emailer.sendEmailAsync(myInfo);
        assertEquals(1, messages.size());

        // not sent yet
        assertTrue(testMailSender.isEmpty());
        SendingMessage sendingMsg = reload(messages.get(0));
        assertEquals(SendingStatus.QUEUE, sendingMsg.getStatus());

        // run scheduler
        emailer.processQueuedEmails();

        // check
        assertEquals(1, testMailSender.getBufferSize());
        MimeMessage msg = testMailSender.fetchSentEmail();

        assertEquals(1, msg.getAllRecipients().length);
        assertEquals("recipient@example.com", msg.getAllRecipients()[0].toString());

        assertEquals("Test", msg.getSubject());
        assertEquals(body, getBody(msg));
        assertTrue(getBodyContentType(msg).startsWith("text/plain;"));

        sendingMsg = reload(messages.get(0));
        assertEquals(SendingStatus.SENT, sendingMsg.getStatus());
    }

    public void testHtmlContent() throws Exception {
        testMailSender.clearBuffer();

        String body = "<html><body><b>Hi</b></body></html>";
        EmailInfo myInfo = new EmailInfo("recipient@example.com", "Test", body);
        emailer.sendEmail(myInfo);

        assertEquals(1, testMailSender.getBufferSize());
        MimeMessage msg = testMailSender.fetchSentEmail();

        assertTrue(getBodyContentType(msg).startsWith("text/html;"));
    }

    public void testImplicitFromAddress() throws Exception {
        EmailInfo myInfo;

        // synchronous
        emailerConfig.setFromAddress("implicit@example.com");
        myInfo = new EmailInfo("test@example.com", "Test Email", "Test Body");
        emailer.sendEmail(myInfo);
        assertEquals("implicit@example.com", testMailSender.fetchSentEmail().getFrom()[0].toString());

        // asynchronous
        emailerConfig.setFromAddress("implicit2@example.com");
        myInfo = new EmailInfo("test@example.com", "Test Email", "Test Body");
        emailer.sendEmailAsync(myInfo);

        emailer.processQueuedEmails();
        assertEquals("implicit2@example.com", testMailSender.fetchSentEmail().getFrom()[0].toString());
    }

    public void testExplicitFromAddress() throws Exception {
        EmailInfo myInfo;
        MimeMessage msg;

        // synchronous
        myInfo = new EmailInfo("test@example.com", "Test Email", "Test Body");
        myInfo.setFrom("explicit@example.com");
        emailer.sendEmail(myInfo);
        msg = testMailSender.fetchSentEmail();
        assertEquals("explicit@example.com", msg.getFrom()[0].toString());

        // asynchronous
        myInfo = new EmailInfo("test@example.com", "Test Email", "Test Body");
        myInfo.setFrom("explicit2@example.com");
        emailer.sendEmailAsync(myInfo);

        emailer.processQueuedEmails();
        msg = testMailSender.fetchSentEmail();
        assertEquals("explicit2@example.com", msg.getFrom()[0].toString());
    }

    public void testSynchronousFail() throws Exception {
        testMailSender.clearBuffer();

        testMailSender.failPlease();
        try {
            emailer.sendEmail("myemail@example.com", "Test Email", "Test Body 2");
            fail("Must fail with EmailException");
        } catch (EmailException e) {
            assertEquals(1, e.getFailedAddresses().length);
            assertEquals("myemail@example.com", e.getFailedAddresses()[0]);
            assertTrue(testMailSender.isEmpty());
        } finally {
            testMailSender.workNormallyPlease();
        }
    }

    public void testAsynchronousAttemptLimit() throws Exception {
        testMailSender.clearBuffer();

        String body = "Test Email Body";
        EmailInfo myInfo = new EmailInfo("recipient@example.com", "Test", body);
        List<SendingMessage> messages = emailer.sendEmailAsync(myInfo, 2, getDeadlineWhichDoesntMatter());
        assertEquals(1, messages.size());

        // not sent yet
        assertTrue(testMailSender.isEmpty());
        SendingMessage sendingMsg = reload(messages.get(0));
        assertEquals(SendingStatus.QUEUE, sendingMsg.getStatus());

        // will fail
        testMailSender.failPlease();
        try {
            // try once
            emailer.processQueuedEmails();
            sendingMsg = reload(sendingMsg);
            assertEquals(SendingStatus.QUEUE, sendingMsg.getStatus());

            // try second time
            emailer.processQueuedEmails();
            sendingMsg = reload(sendingMsg);
            assertEquals(SendingStatus.QUEUE, sendingMsg.getStatus());

        } finally {
            testMailSender.workNormallyPlease();
        }

        // marks as not-sent in the next tick
        emailer.processQueuedEmails();
        sendingMsg = reload(sendingMsg);
        assertEquals(SendingStatus.NOTSENT, sendingMsg.getStatus());
        assertEquals(2, sendingMsg.getAttemptsCount().intValue());
    }

    public void testSentFromSecondAttempt() throws Exception {
        testMailSender.clearBuffer();

        String body = "Test Email Body";
        EmailInfo myInfo = new EmailInfo("recipient@example.com", "Test", body);
        List<SendingMessage> messages = emailer.sendEmailAsync(myInfo, 2, getDeadlineWhichDoesntMatter());
        assertEquals(1, messages.size());

        // not sent yet
        assertTrue(testMailSender.isEmpty());
        SendingMessage sendingMsg = reload(messages.get(0));
        assertEquals(SendingStatus.QUEUE, sendingMsg.getStatus());

        // will fail
        testMailSender.failPlease();
        try {
            // try once
            emailer.processQueuedEmails();
            sendingMsg = reload(sendingMsg);
            assertEquals(SendingStatus.QUEUE, sendingMsg.getStatus());

        } finally {
            testMailSender.workNormallyPlease();
        }

        // success now
        emailer.processQueuedEmails();
        sendingMsg = reload(sendingMsg);
        assertEquals(SendingStatus.SENT, sendingMsg.getStatus());
        assertEquals(2, sendingMsg.getAttemptsCount().intValue());
    }

    public void testSeveralRecipients() throws Exception {
        testMailSender.clearBuffer();

        String body = "Test Email Body";
        String recipients = "misha@example.com,kolya@example.com;tanya@example.com;"; // 3 recipients
        EmailInfo myInfo = new EmailInfo(recipients, "Test", body);
        List<SendingMessage> messages = emailer.sendEmailAsync(myInfo);
        assertEquals(3, messages.size());

        assertTrue(testMailSender.isEmpty());
        emailer.processQueuedEmails();

        Set<String> recipientSet = new HashSet<>();
        // check
        assertEquals(3, testMailSender.getBufferSize());
        for (int i = 0; i < 3; i++) {
            MimeMessage msg = testMailSender.fetchSentEmail();
            Address[] msgRecipients = msg.getAllRecipients();
            assertEquals(1, msgRecipients.length);
            recipientSet.add(msgRecipients[0].toString());
        }

        assertTrue(recipientSet.contains("misha@example.com"));
        assertTrue(recipientSet.contains("kolya@example.com"));
        assertTrue(recipientSet.contains("tanya@example.com"));
    }

    public void testSendAllToAdmin() throws Exception {
        emailerConfig.setSendAllToAdmin(true);
        emailerConfig.setAdminAddress("admin@example.com");
        try {
            emailer.sendEmail("michael@example.com", "Test Email 5", "Test Body 5");

            emailer.sendEmailAsync(new EmailInfo("nikolay@example.com", "Test Email 6", "Test Body 6"));
            emailer.processQueuedEmails();

            for (int i = 0; i < 2; i++) {
                MimeMessage msg = testMailSender.fetchSentEmail();
                assertEquals(1, msg.getAllRecipients().length);
                assertEquals("admin@example.com", msg.getAllRecipients()[0].toString());
            }

        } finally {
            emailerConfig.setSendAllToAdmin(false);
        }
    }

    public void testEmailTemplate() throws Exception {
        testMailSender.clearBuffer();

        String templateFileName = "/com/haulmont/cuba/core/app/testEmailTemplate.ftl";

        Map<String, Serializable> params = new HashMap<>();
        params.put("userName", "Bob");
        params.put("dateParam", new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2013"));

        EmailInfo info = new EmailInfo("bob@example.com", "Test", null, templateFileName, params);
        emailer.sendEmailAsync(info);
        emailer.processQueuedEmails();

        String body = getBody(testMailSender.fetchSentEmail());
        assertEquals("Greetings, Bob! 01-05-2013", body.trim());
    }

    public void testTextAttachment() throws Exception {
        testMailSender.clearBuffer();

        String attachmentText = "Test Attachment Text";
        EmailAttachment textAttach = EmailAttachment.createTextAttachment(attachmentText, "ISO-8859-1", "test.txt");

        EmailInfo myInfo = new EmailInfo("test@example.com", "Test", null, "Test", textAttach);
        emailer.sendEmailAsync(myInfo);

        emailer.processQueuedEmails();

        MimeMessage msg = testMailSender.fetchSentEmail();
        MimeBodyPart firstAttachment = getFirstAttachment(msg);

        // check content bytes
        Object content = firstAttachment.getContent();
        assertTrue(content instanceof InputStream);
        byte[] data = IOUtils.toByteArray((InputStream) content);
        assertEquals(attachmentText, new String(data, "ISO-8859-1"));

        // disposition
        assertEquals(Part.ATTACHMENT, firstAttachment.getDisposition());

        // charset header
        String contentType = firstAttachment.getContentType();
        assertTrue(contentType.toLowerCase().contains("charset=iso-8859-1"));
    }

    public void testInlineImage() throws Exception {
        testMailSender.clearBuffer();

        byte[] imageBytes = new byte[]{1, 2, 3, 4, 5};
        String fileName = "logo.png";
        EmailAttachment imageAttach = new EmailAttachment(imageBytes, fileName, "logo");

        EmailInfo myInfo = new EmailInfo("test@example.com", "Test", null, "Test", imageAttach);
        emailer.sendEmailAsync(myInfo);

        emailer.processQueuedEmails();

        MimeMessage msg = testMailSender.fetchSentEmail();
        MimeBodyPart attachment = getInlineAttachment(msg);

        // check content bytes
        InputStream content = (InputStream) attachment.getContent();
        byte[] data = IOUtils.toByteArray(content);
        assertByteArrayEquals(imageBytes, data);

        // disposition
        assertEquals(Part.INLINE, attachment.getDisposition());

        // mime type
        String contentType = attachment.getContentType();
        assertTrue(contentType.contains("image/png"));
    }

    public void testPdfAttachment() throws Exception {
        testMailSender.clearBuffer();

        byte[] pdfBytes = new byte[]{1, 2, 3, 4, 6};
        String fileName = "invoice.pdf";
        EmailAttachment imageAttach = new EmailAttachment(pdfBytes, fileName);

        EmailInfo myInfo = new EmailInfo("test@example.com", "Test", null, "Test", imageAttach);
        emailer.sendEmailAsync(myInfo);

        emailer.processQueuedEmails();

        MimeMessage msg = testMailSender.fetchSentEmail();
        MimeBodyPart attachment = getFirstAttachment(msg);

        // check content bytes
        InputStream content = (InputStream) attachment.getContent();
        byte[] data = IOUtils.toByteArray(content);
        assertByteArrayEquals(pdfBytes, data);

        // disposition
        assertEquals(Part.ATTACHMENT, attachment.getDisposition());

        // mime type
        String contentType = attachment.getContentType();
        assertTrue(contentType.contains("application/pdf"));
    }

    /* Utility */
    private Date getDeadlineWhichDoesntMatter() {
        return DateUtils.addHours(timeSource.currentTimestamp(), 2);
    }

    private String getBody(MimeMessage msg) throws Exception {
        MimeBodyPart textPart = getTextPart(msg);
        return (String) textPart.getContent();
    }

    private String getBodyContentType(MimeMessage msg) throws Exception {
        MimeBodyPart textPart = getTextPart(msg);
        return textPart.getContentType();
    }

    private MimeBodyPart getTextPart(MimeMessage msg) throws IOException, MessagingException {
        assertTrue(msg.getContent() instanceof MimeMultipart);
        MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();

        Object content2 = mimeMultipart.getBodyPart(0).getContent();
        assertTrue(content2 instanceof MimeMultipart);
        MimeMultipart textBodyPart = (MimeMultipart) content2;

        return (MimeBodyPart) textBodyPart.getBodyPart(0);
    }

    private MimeBodyPart getFirstAttachment(MimeMessage msg) throws IOException, MessagingException {
        assertTrue(msg.getContent() instanceof MimeMultipart);
        MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
        return (MimeBodyPart) mimeMultipart.getBodyPart(1);
    }

    private MimeBodyPart getInlineAttachment(MimeMessage msg) throws IOException, MessagingException {
        assertTrue(msg.getContent() instanceof MimeMultipart);
        MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();

        Object content2 = mimeMultipart.getBodyPart(0).getContent();
        assertTrue(content2 instanceof MimeMultipart);
        MimeMultipart textBodyPart = (MimeMultipart) content2;

        return (MimeBodyPart) textBodyPart.getBodyPart(1);
    }

    private SendingMessage reload(SendingMessage sendingMessage) {
        Transaction tx = persistence.createTransaction();
        try {
            sendingMessage = persistence.getEntityManager().reload(sendingMessage);
            tx.commit();
        } finally {
            tx.end();
        }
        return sendingMessage;
    }

    private void assertByteArrayEquals(byte[] data1, byte[] data2) {
        assertEquals(data1.length, data2.length);
        for (int i = 0; i < data1.length; i++) {
            assertEquals(data1[i], data2[i]);
        }
    }

}
