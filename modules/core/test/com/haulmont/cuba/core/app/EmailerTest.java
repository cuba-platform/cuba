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

import com.google.common.collect.Lists;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.CubaMailSender;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestMailSender;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.mail.Address;
import javax.mail.Message;
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

import static org.junit.Assert.*;

public class EmailerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private EmailerAPI emailer;
    private TestMailSender testMailSender;
    private TimeSource timeSource;
    private DataManager dataManager;

    private EmailerConfig emailerConfig;

    @Before
    public void setUp() throws Exception {
        emailer = AppBeans.get(EmailerAPI.NAME);
        testMailSender = AppBeans.get(CubaMailSender.NAME);
        timeSource = AppBeans.get(TimeSource.NAME);
        dataManager = AppBeans.get(DataManager.class);

        emailerConfig = AppBeans.get(Configuration.class).getConfig(EmailerConfig.class);
        emailerConfig.setDelayCallCount(0);

        // send pending emails which might be in the queue
        emailer.processQueuedEmails();
        testMailSender.clearBuffer();
    }

    @Test
    public void testSynchronous() throws Exception {
        doTestSynchronous(false);
    }

    @Test
    public void testSynchronousWithMultipleRecipients() throws Exception {
        doTestWithMultipleRecipientsSynchronous(false);
    }

    @Test
    public void testSynchronousFS() throws Exception {
        doTestSynchronous(true);
    }

    /*
     * Test single recipient, text body, subject.
     */
    private void doTestSynchronous(boolean useFs) throws Exception {
        emailerConfig.setFileStorageUsed(useFs);
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
     * Test single recipient, text body, subject.
     */
    private void doTestWithMultipleRecipientsSynchronous(boolean useFs) throws Exception {
        emailerConfig.setFileStorageUsed(useFs);
        testMailSender.clearBuffer();

        EmailInfo myInfo = new EmailInfo("testemail@example.com,testemail2@example.com", "Test Email", "Test Body");
        myInfo.setSendInOneMessage(true);
        myInfo.setCc("testemail3@example.com,testemail4@example.com");
        myInfo.setBcc("testemail5@example.com,testemail6@example.com");
        emailer.sendEmail(myInfo);

        assertEquals(1, testMailSender.getBufferSize());
        MimeMessage msg = testMailSender.fetchSentEmail();

        assertEquals(2, msg.getRecipients(Message.RecipientType.TO).length);
        assertEquals("testemail@example.com", msg.getRecipients(Message.RecipientType.TO)[0].toString());
        assertEquals("testemail2@example.com", msg.getRecipients(Message.RecipientType.TO)[1].toString());

        assertEquals(2, msg.getRecipients(Message.RecipientType.CC).length);
        assertEquals("testemail3@example.com", msg.getRecipients(Message.RecipientType.CC)[0].toString());
        assertEquals("testemail4@example.com", msg.getRecipients(Message.RecipientType.CC)[1].toString());

        assertEquals(2, msg.getRecipients(Message.RecipientType.BCC).length);
        assertEquals("testemail5@example.com", msg.getRecipients(Message.RecipientType.BCC)[0].toString());
        assertEquals("testemail6@example.com", msg.getRecipients(Message.RecipientType.BCC)[1].toString());

        assertEquals("Test Email", msg.getSubject());
        assertEquals("Test Body", getBody(msg));
        assertTrue(getBodyContentType(msg).startsWith("text/plain;"));
    }

    /*
     * Test sendEmail() with parameter list.
     */
    @Test
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

    @Test
    public void testAsynchronous() throws Exception {
        doTestAsynchronous(false);
    }

    @Test
    public void testAsynchronousFS() throws Exception {
        doTestAsynchronous(true);
    }

    @Test
    public void testFileStorageEmailBodyReturningToDbColumn() {
        emailerConfig.setFileStorageUsed(true);
        testMailSender.clearBuffer();

        String body = "Test Email Body";
        EmailInfo myInfo = new EmailInfo("recipient@example.com", "Test", body);
        List<SendingMessage> messages = emailer.sendEmailAsync(myInfo);
        assertEquals(1, messages.size());

        // not sent yet
        SendingMessage sendingMsg = reload(messages.get(0), "sendingMessage.loadFromQueue");
        assertNotNull(sendingMsg.getContentTextFile());
        assertNull(sendingMsg.getContentText());             // null

        // run scheduler
        emailer.processQueuedEmails();

        sendingMsg = reload(messages.get(0), "sendingMessage.loadFromQueue");
        assertNotNull(sendingMsg.getContentTextFile());
        assertNull(sendingMsg.getContentText());             // null??
    }

    private void doTestAsynchronous(boolean useFs) throws Exception {
        emailerConfig.setFileStorageUsed(useFs);
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

    @Test
    public void testHtmlContent() throws Exception {
        testMailSender.clearBuffer();

        String body = "<html><body><b>Hi</b></body></html>";
        EmailInfo myInfo = new EmailInfo("recipient@example.com", "Test", body);
        emailer.sendEmail(myInfo);

        assertEquals(1, testMailSender.getBufferSize());
        MimeMessage msg = testMailSender.fetchSentEmail();

        assertTrue(getBodyContentType(msg).startsWith("text/html;"));
    }

    @Test
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

    @Test
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

    @Test
    public void testSynchronousFail() throws Exception {
        testMailSender.clearBuffer();

        testMailSender.failPlease();
        try {
            emailer.sendEmail("myemail@example.com", "Test Email", "Test Body 2");
            fail("Must fail with EmailException");
        } catch (EmailException e) {
            assertEquals(1, e.getFailedAddresses().size());
            assertEquals("myemail@example.com", e.getFailedAddresses().get(0));
            assertTrue(testMailSender.isEmpty());
        } finally {
            testMailSender.workNormallyPlease();
        }
    }

    @Test
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

    @Test
    public void testSentFromSecondAttempt() throws Exception {
        doTestSentFromSecondAttempt(false);
    }

    @Test
    public void testSentFromSecondAttemptFS() throws Exception {
        doTestSentFromSecondAttempt(true);
    }

    private void doTestSentFromSecondAttempt(boolean useFs) {
        emailerConfig.setFileStorageUsed(useFs);
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

    @Test
    public void testSeveralRecipients() throws Exception {
        doTestSeveralRecipients(false);
    }

    @Test
    public void testSeveralRecipientsFS() throws Exception {
        doTestSeveralRecipients(true);
    }

    private void doTestSeveralRecipients(boolean useFs) throws MessagingException {
        emailerConfig.setFileStorageUsed(useFs);
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

    @Test
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

    @Test
    public void testEmailTemplate() throws Exception {
        testMailSender.clearBuffer();

        String templateFileName = "/com/haulmont/cuba/core/app/testEmailTemplate.ftl";

        Map<String, Serializable> params = new HashMap<>();
        params.put("userName", "Bob");
        params.put("greeting", "Hello");
        params.put("dateParam", new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2013"));

        EmailInfo info = new EmailInfo("bob@example.com", "${greeting} Test", null, templateFileName, params);
        emailer.sendEmailAsync(info);
        emailer.processQueuedEmails();

        MimeMessage sent = testMailSender.fetchSentEmail();
        String body = getBody(sent);
        assertEquals("Greetings, Bob! 01-05-2013", body.trim());

        String caption = getCaption(sent);
        assertEquals("Hello Test", caption.trim());

        info = new EmailInfo("bob@example.com", "${greeting} Test", null, null, params);
        info.setBody("Greetings, ${userName}!");
        emailer.sendEmailAsync(info);
        emailer.processQueuedEmails();
        sent = testMailSender.fetchSentEmail();
        assertEquals("Greetings, Bob!", getBody(sent).trim());

        info = new EmailInfo("bob@example.com", "${greeting} Test", null, null);
        info.setBody("Greetings, ${userName}!");
        emailer.sendEmailAsync(info);
        emailer.processQueuedEmails();
        sent = testMailSender.fetchSentEmail();

        assertEquals("Greetings, ${userName}!", getBody(sent).trim());
        assertEquals("${greeting} Test", getCaption(sent).trim());
    }


    @Test
    public void testTextAttachment() throws Exception {
        doTestTextAttachment(false);
    }

    @Test
    public void testTextAttachmentFS() throws Exception {
        doTestTextAttachment(true);
    }

    private void doTestTextAttachment(boolean useFs) throws IOException, MessagingException {
        emailerConfig.setFileStorageUsed(useFs);
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

    @Test
    public void testInlineImage() throws Exception {
        doTestInlineImage(false);
    }

    @Test
    public void testInlineImageFS() throws Exception {
        doTestInlineImage(true);
    }

    private void doTestInlineImage(boolean useFs) throws IOException, MessagingException {
        emailerConfig.setFileStorageUsed(useFs);
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

    @Test
    public void testPdfAttachment() throws Exception {
        doTestPdfAttachment(false);
    }

    @Test
    public void testPdfAttachmentFS() throws Exception {
        doTestPdfAttachment(true);
    }

    private void doTestPdfAttachment(boolean useFs) throws IOException, MessagingException {
        emailerConfig.setFileStorageUsed(useFs);
        testMailSender.clearBuffer();

        byte[] pdfBytes = new byte[]{1, 2, 3, 4, 6};
        String fileName = "invoice.pdf";
        EmailAttachment pdfAttach = new EmailAttachment(pdfBytes, fileName);

        EmailInfo myInfo = new EmailInfo("test@example.com", "Test", null, "Test", pdfAttach);
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

    @Test
    public void testLoadBody() throws Exception {
        doTestLoadBody(false);
    }

    @Test
    public void testLoadBodyFS() throws Exception {
        doTestLoadBody(true);
    }

    private void doTestLoadBody(boolean useFs) throws Exception {
        emailerConfig.setFileStorageUsed(useFs);

        String body = "Hi! This is test email. Bye.";
        EmailInfo emailInfo = new EmailInfo("test@example.com", "Test", body);
        List<SendingMessage> messages = emailer.sendEmailAsync(emailInfo);

        SendingMessage msg = reload(messages.get(0));

        String actualBody = emailer.loadContentText(msg);
        assertEquals(body, actualBody);
    }

    @Test
    public void testMigration() throws Exception {
        emailerConfig.setFileStorageUsed(false);

        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 6};
        EmailAttachment fileAttachment = new EmailAttachment(expectedBytes, "invoice.pdf");

        String body = "Hi! This is test email. Bye.";
        EmailInfo emailInfo = new EmailInfo("test@example.com", "Test", body);
        emailInfo.setAttachments(new EmailAttachment[]{fileAttachment});

        List<SendingMessage> messages = emailer.sendEmailAsync(emailInfo);
        SendingMessage msg;
        SendingAttachment attachment;

        // check DB storage
        msg = reload(messages.get(0), "sendingMessage.loadFromQueue");
        attachment = msg.getAttachments().get(0);

        assertNotNull(msg.getContentText());
        assertNull(msg.getContentTextFile());
        assertNotNull(attachment.getContent());
        assertNull(attachment.getContentFile());

        emailer.migrateEmailsToFileStorage(Lists.newArrayList(msg));
        emailer.migrateAttachmentsToFileStorage(Lists.newArrayList(attachment));

        // check file storage
        msg = reload(msg, "sendingMessage.loadFromQueue");
        attachment = msg.getAttachments().get(0);

        assertNull(msg.getContentText());
        assertNotNull(msg.getContentTextFile());
        assertEquals(body, emailer.loadContentText(msg));

        assertNull(attachment.getContent());
        assertNotNull(attachment.getContentFile());
        FileStorageAPI fileStorage = AppBeans.get(FileStorageAPI.NAME);
        byte[] actualBytes = fileStorage.loadFile(attachment.getContentFile());
        assertByteArrayEquals(expectedBytes, actualBytes);
    }

    /* Utility */
    private Date getDeadlineWhichDoesntMatter() {
        return DateUtils.addHours(timeSource.currentTimestamp(), 2);
    }

    private String getBody(MimeMessage msg) throws Exception {
        MimeBodyPart textPart = getTextPart(msg);
        return (String) textPart.getContent();
    }

    private String getCaption(MimeMessage msg) throws Exception {
        return msg.getSubject();
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
        return reload(sendingMessage, null);
    }

    private SendingMessage reload(SendingMessage sendingMessage, String viewName) {
        LoadContext<SendingMessage> loadContext = LoadContext.create(SendingMessage.class).setId(sendingMessage.getId());
        if (viewName != null)
            loadContext.setView(viewName);

        return dataManager.load(loadContext);

//        Transaction tx = cont.persistence().createTransaction();
//        try {
//            sendingMessage = cont.persistence().getEntityManager().reload(sendingMessage, viewNames);
//            tx.commit();
//        } finally {
//            tx.end();
//        }
//        return sendingMessage;
    }

    private void assertByteArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}