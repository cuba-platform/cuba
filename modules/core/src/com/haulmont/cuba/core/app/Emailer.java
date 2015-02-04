/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authentication;
import com.sun.mail.smtp.SMTPAddressFailedException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSendException;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(EmailerAPI.NAME)
public class Emailer implements EmailerAPI {

    protected static final String BODY_STORAGE_ENCODING = "UTF-8";
    protected static final String BODY_FILE_EXTENSION = "txt";

    private Log log = LogFactory.getLog(Emailer.class);

    protected EmailerConfig config;

    protected volatile int callCount = 0;

    @Resource(name = "mailSendTaskExecutor")
    protected TaskExecutor mailSendTaskExecutor;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Authentication authentication;

    @Inject
    protected EmailSenderAPI emailSender;

    @Inject
    protected Resources resources;

    @Inject
    protected FileStorageAPI fileStorage;

    @Inject
    public void setConfig(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    protected String getEmailerLogin() {
        return config.getEmailerUserLogin();
    }

    @Override
    public void sendEmail(String addresses, String caption, String body, EmailAttachment... attachments)
            throws EmailException {
        sendEmail(new EmailInfo(addresses, caption, null, body, attachments));
    }

    @Override
    public void sendEmail(EmailInfo info) throws EmailException {
        prepareEmailInfo(info);
        persistAndSendEmail(info);
    }

    @Override
    public List<SendingMessage> sendEmailAsync(EmailInfo info) {
        //noinspection UnnecessaryLocalVariable
        List<SendingMessage> result = sendEmailAsync(info, null, null);
        return result;
    }

    @Override
    public List<SendingMessage> sendEmailAsync(EmailInfo info, Integer attemptsCount, Date deadline) {
        prepareEmailInfo(info);
        List<SendingMessage> messages = splitEmail(info, attemptsCount, deadline);
        persistMessages(messages, SendingStatus.QUEUE);
        return messages;
    }

    protected void prepareEmailInfo(EmailInfo emailInfo) {
        processBodyTemplate(emailInfo);

        if (emailInfo.getFrom() == null) {
            String defaultFromAddress = config.getFromAddress();
            if (defaultFromAddress == null) {
                throw new IllegalStateException("cuba.email.fromAddress not set in the system");
            }
            emailInfo.setFrom(defaultFromAddress);
        }
    }

    protected void processBodyTemplate(EmailInfo info) {
        String templatePath = info.getTemplatePath();
        if (templatePath == null) {
            return;
        }

        Map<String, Serializable> params = info.getTemplateParameters() == null
                ? Collections.<String, Serializable>emptyMap()
                : info.getTemplateParameters();
        String templateContents = resources.getResourceAsString(templatePath);
        if (templateContents == null) {
            throw new IllegalArgumentException("Could not find template by path: " + templatePath);
        }
        String body = TemplateHelper.processTemplate(templateContents, params);
        info.setBody(body);
    }

    protected List<SendingMessage> splitEmail(EmailInfo info, @Nullable Integer attemptsCount, @Nullable Date deadline) {
        List<SendingMessage> sendingMessageList = new ArrayList<>();
        String[] splitAddresses = info.getAddresses().split("[,;]");
        for (String address : splitAddresses) {
            address = address.trim();
            if (StringUtils.isNotBlank(address)) {
                SendingMessage sendingMessage = convertToSendingMessage(address, info.getFrom(), info.getCaption(),
                        info.getBody(), info.getHeaders(), info.getAttachments(), attemptsCount, deadline);

                sendingMessageList.add(sendingMessage);
            }
        }
        return sendingMessageList;
    }

    protected void sendSendingMessage(SendingMessage sendingMessage) {
        Objects.requireNonNull(sendingMessage, "sendingMessage is null");
        Objects.requireNonNull(sendingMessage.getAddress(), "sendingMessage.address is null");
        Objects.requireNonNull(sendingMessage.getCaption(), "sendingMessage.caption is null");
        Objects.requireNonNull(sendingMessage.getContentText(), "sendingMessage.contentText is null");
        Objects.requireNonNull(sendingMessage.getFrom(), "sendingMessage.from is null");
        try {
            emailSender.sendEmail(sendingMessage);
            markAsSent(sendingMessage);
        } catch (Exception e) {
            log.warn("Unable to send email to '" + sendingMessage.getAddress() + "'", e);
            if (isNeedToRetry(e)) {
                returnToQueue(sendingMessage);
            } else {
                markAsNonSent(sendingMessage);
            }
        }
    }

    protected void persistAndSendEmail(EmailInfo emailInfo) throws EmailException {
        Objects.requireNonNull(emailInfo.getAddresses(), "addresses are null");
        Objects.requireNonNull(emailInfo.getCaption(), "caption is null");
        Objects.requireNonNull(emailInfo.getBody(), "body is null");
        Objects.requireNonNull(emailInfo.getFrom(), "from is null");

        List<SendingMessage> messages = splitEmail(emailInfo, null, null);

        List<String> failedAddresses = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (SendingMessage sendingMessage : messages) {
            SendingMessage persistedMessage = persistMessageIfPossible(sendingMessage);

            try {
                emailSender.sendEmail(sendingMessage);
                if (persistedMessage != null) {
                    markAsSent(persistedMessage);
                }
            } catch (Exception e) {
                log.warn("Unable to send email to '" + sendingMessage.getAddress() + "'", e);
                failedAddresses.add(sendingMessage.getAddress());
                errorMessages.add(e.getMessage());
                if (persistedMessage != null) {
                    markAsNonSent(persistedMessage);
                }
            }
        }

        if (!failedAddresses.isEmpty()) {
            throw new EmailException(failedAddresses, errorMessages);
        }
    }

    /*
     * Try to persist message and catch all errors to allow actual delivery
     * in case of database or file storage failure.
     */
    @Nullable
    protected SendingMessage persistMessageIfPossible(SendingMessage sendingMessage) {
        // A copy of sendingMessage is created
        // to avoid additional overhead to load body and attachments back from FS
        try {
            SendingMessage clonedMessage = createClone(sendingMessage);
            persistMessages(Collections.singletonList(clonedMessage), SendingStatus.SENDING);
            return clonedMessage;
        } catch (Exception e) {
            log.error("Failed to persist message " + sendingMessage.getCaption(), e);
            return null;
        }
    }

    protected SendingMessage createClone(SendingMessage srcMessage) {
        SendingMessage clonedMessage = (SendingMessage) InstanceUtils.copy(srcMessage);
        List<SendingAttachment> clonedList = new ArrayList<>();
        for (SendingAttachment srcAttach : srcMessage.getAttachments()) {
            SendingAttachment clonedAttach = (SendingAttachment) InstanceUtils.copy(srcAttach);
            clonedAttach.setMessage(null);
            clonedAttach.setMessage(clonedMessage);
            clonedList.add(clonedAttach);
        }
        clonedMessage.setAttachments(clonedList);
        return clonedMessage;
    }

    @Override
    public String processQueuedEmails() {
        if (applicationNotStartedYet()) {
            return null;
        }

        int callsToSkip = config.getDelayCallCount();
        if (callCount < callsToSkip) {
            callCount++;
            return null;
        }

        String resultMessage;
        try {
            authentication.begin(getEmailerLogin());
            try {
                resultMessage = sendQueuedEmails();
            } finally {
                authentication.end();
            }
        } catch (Throwable e) {
            log.error("Error", e);
            resultMessage = e.getMessage();
        }
        return resultMessage;
    }

    protected boolean applicationNotStartedYet() {
        return !AppContext.isStarted();
    }

    protected String sendQueuedEmails() {
        List<SendingMessage> messagesToSend = loadEmailsToSend();

        for (SendingMessage msg : messagesToSend) {
            submitExecutorTask(msg);
        }

        if (messagesToSend.isEmpty()) {
            return "";
        }

        return String.format("Processed %d emails", messagesToSend.size());
    }

    protected boolean shouldMarkNotSent(SendingMessage sendingMessage) {
        Date deadline = sendingMessage.getDeadline();
        if (deadline != null && deadline.before(timeSource.currentTimestamp())) {
            return true;
        }

        Integer messageAttemptsLimit = sendingMessage.getAttemptsCount();
        int defaultLimit = config.getDefaultSendingAttemptsCount();
        int attemptsLimit = messageAttemptsLimit != null ? messageAttemptsLimit : defaultLimit;
        //noinspection UnnecessaryLocalVariable
        boolean res = sendingMessage.getAttemptsMade() != null && sendingMessage.getAttemptsMade() >= attemptsLimit;
        return res;
    }

    protected void submitExecutorTask(SendingMessage msg) {
        try {
            Runnable mailSendTask = new EmailSendTask(msg);
            mailSendTaskExecutor.execute(mailSendTask);
        } catch (RejectedExecutionException e) {
            returnToQueue(msg);
        } catch (Exception e) {
            log.error("Exception while sending email: ", e);
            if (isNeedToRetry(e)) {
                returnToQueue(msg);
            } else {
                markAsNonSent(msg);
            }
        }
    }

    protected List<SendingMessage> loadEmailsToSend() {
        Date sendTimeoutTime = DateUtils.addSeconds(timeSource.currentTimestamp(), -config.getSendingTimeoutSec());

        List<SendingMessage> emailsToSend = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<SendingMessage> query = em.createQuery(
                    "select sm from sys$SendingMessage sm" +
                            " where sm.status = :statusQueue or (sm.status = :statusSending and sm.updateTs < :time)" +
                            " order by sm.createTs",
                    SendingMessage.class
            );
            query.setParameter("statusQueue", SendingStatus.QUEUE.getId());
            query.setParameter("time", sendTimeoutTime);
            query.setParameter("statusSending", SendingStatus.SENDING.getId());
            query.setViewName("sendingMessage.loadFromQueue");
            query.setMaxResults(config.getMessageQueueCapacity());

            List<SendingMessage> resList = query.getResultList();

            for (SendingMessage msg : resList) {
                if (shouldMarkNotSent(msg)) {
                    msg.setStatus(SendingStatus.NOTSENT);
                } else {
                    msg.setStatus(SendingStatus.SENDING);
                    emailsToSend.add(msg);
                }
            }
            tx.commit();
        } finally {
            tx.end();
        }

        for (SendingMessage message : emailsToSend) {
            loadBodyAndAttachments(message);
        }
        return emailsToSend;
    }

    @Override
    public String loadContentText(SendingMessage sendingMessage) {
        SendingMessage msg;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            msg = em.reload(sendingMessage, "sendingMessage.loadContentText");
            tx.commit();
        } finally {
            tx.end();
        }
        Objects.requireNonNull(msg, "Sending message not found: " + sendingMessage.getId());
        if (msg.getContentTextFile() != null) {
            byte[] bodyContent;
            try {
                bodyContent = fileStorage.loadFile(msg.getContentTextFile());
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
            //noinspection UnnecessaryLocalVariable
            String res = bodyTextFromByteArray(bodyContent);
            return res;
        } else {
            return msg.getContentText();
        }
    }

    protected void loadBodyAndAttachments(SendingMessage message) {
        try {
            if (message.getContentTextFile() != null) {
                byte[] bodyContent = fileStorage.loadFile(message.getContentTextFile());
                String body = bodyTextFromByteArray(bodyContent);
                message.setContentText(body);
            }

            for (SendingAttachment attachment : message.getAttachments()) {
                if (attachment.getContentFile() != null) {
                    byte[] content = fileStorage.loadFile(attachment.getContentFile());
                    attachment.setContent(content);
                }
            }
        } catch (FileStorageException e) {
            log.error("Failed to load body or attachments for " + message);
        }
    }

    protected void persistMessages(List<SendingMessage> sendingMessageList, SendingStatus status) {
        MessagePersistingContext context = new MessagePersistingContext();

        try {
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                for (SendingMessage message : sendingMessageList) {
                    message.setStatus(status);

                    try {
                        persistSendingMessage(em, message, context);
                    } catch (FileStorageException e) {
                        throw new RuntimeException("Failed to store message " + message.getCaption(), e);
                    }
                }
                tx.commit();
            } finally {
                tx.end();
            }
            context.finished();
        } finally {
            removeOrphanFiles(context);
        }
    }

    protected void removeOrphanFiles(MessagePersistingContext context) {
        for (FileDescriptor file : context.files) {
            try {
                fileStorage.removeFile(file);
            } catch (Exception e) {
                log.error("Failed to remove file " + file);
            }
        }
    }

    protected void persistSendingMessage(EntityManager em, SendingMessage message,
                                         MessagePersistingContext context) throws FileStorageException {
        boolean useFileStorage = config.isFileStorageUsed();

        if (useFileStorage) {
            byte[] bodyBytes = bodyTextToBytes(message);

            FileDescriptor contentTextFile = createBodyFileDescriptor(message, bodyBytes);
            fileStorage.saveFile(contentTextFile, bodyBytes);
            context.files.add(contentTextFile);

            em.persist(contentTextFile);
            message.setContentTextFile(contentTextFile);
            message.setContentText(null);
        }

        em.persist(message);

        for (SendingAttachment attachment : message.getAttachments()) {
            if (useFileStorage) {
                FileDescriptor contentFile = createAttachmentFileDescriptor(attachment);

                fileStorage.saveFile(contentFile, attachment.getContent());
                context.files.add(contentFile);
                em.persist(contentFile);

                attachment.setContentFile(contentFile);
                attachment.setContent(null);
            }

            em.persist(attachment);
        }
    }

    protected FileDescriptor createAttachmentFileDescriptor(SendingAttachment attachment) {
        FileDescriptor contentFile = metadata.create(FileDescriptor.class);
        contentFile.setCreateDate(timeSource.currentTimestamp());
        contentFile.setName(attachment.getName());
        contentFile.setExtension(FilenameUtils.getExtension(attachment.getName()));
        contentFile.setSize((long) attachment.getContent().length);
        return contentFile;
    }

    protected FileDescriptor createBodyFileDescriptor(SendingMessage message, byte[] bodyBytes) {
        FileDescriptor contentTextFile = metadata.create(FileDescriptor.class);
        contentTextFile.setCreateDate(timeSource.currentTimestamp());
        contentTextFile.setName("Email_" + message.getId() + "." + BODY_FILE_EXTENSION);
        contentTextFile.setExtension(BODY_FILE_EXTENSION);
        contentTextFile.setSize((long) bodyBytes.length);
        return contentTextFile;
    }

    protected void returnToQueue(SendingMessage sendingMessage) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            SendingMessage msg = em.merge(sendingMessage);

            msg.setAttemptsMade(msg.getAttemptsMade() + 1);
            msg.setStatus(SendingStatus.QUEUE);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void markAsSent(SendingMessage sendingMessage) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            SendingMessage msg = em.merge(sendingMessage);

            msg.setStatus(SendingStatus.SENT);
            msg.setAttemptsMade(msg.getAttemptsMade() + 1);
            msg.setDateSent(timeSource.currentTimestamp());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void markAsNonSent(SendingMessage sendingMessage) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            SendingMessage msg = em.merge(sendingMessage);

            msg.setStatus(SendingStatus.NOTSENT);
            msg.setAttemptsMade(msg.getAttemptsMade() + 1);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected SendingMessage convertToSendingMessage(String address, String from, String caption, String body, @Nullable List<EmailHeader> headers,
                                                     @Nullable EmailAttachment[] attachments,
                                                     @Nullable Integer attemptsCount, @Nullable Date deadline) {
        SendingMessage sendingMessage = metadata.create(SendingMessage.class);

        sendingMessage.setAddress(address);
        sendingMessage.setFrom(from);
        sendingMessage.setContentText(body);
        sendingMessage.setCaption(caption);
        sendingMessage.setAttemptsCount(attemptsCount);
        sendingMessage.setDeadline(deadline);
        sendingMessage.setAttemptsMade(0);

        if (attachments != null && attachments.length > 0) {
            StringBuilder attachmentsName = new StringBuilder();
            List<SendingAttachment> sendingAttachments = new ArrayList<>(attachments.length);
            for (EmailAttachment ea : attachments) {
                attachmentsName.append(ea.getName()).append(";");

                SendingAttachment sendingAttachment = toSendingAttachment(ea);
                sendingAttachment.setMessage(sendingMessage);
                sendingAttachments.add(sendingAttachment);
            }
            sendingMessage.setAttachments(sendingAttachments);
            sendingMessage.setAttachmentsName(attachmentsName.toString());
        } else {
            sendingMessage.setAttachments(Collections.<SendingAttachment>emptyList());
        }

        if (headers != null && !headers.isEmpty()) {
            StringBuilder headersLine = new StringBuilder();
            for (EmailHeader header : headers) {
                headersLine.append(header.toString()).append(SendingMessage.HEADERS_SEPARATOR);
            }
            sendingMessage.setHeaders(headersLine.toString());
        } else {
            sendingMessage.setHeaders(null);
        }

        replaceRecipientIfNecessary(sendingMessage);

        return sendingMessage;
    }

    protected void replaceRecipientIfNecessary(SendingMessage msg) {
        if (config.getSendAllToAdmin()) {
            String adminAddress = config.getAdminAddress();
            log.warn(String.format(
                    "Replacing actual email recipient '%s' by admin address '%s'", msg.getAddress(), adminAddress
            ));
            msg.setAddress(adminAddress);
        }
    }

    protected SendingAttachment toSendingAttachment(EmailAttachment ea) {
        SendingAttachment sendingAttachment = metadata.create(SendingAttachment.class);
        sendingAttachment.setContent(ea.getData());
        sendingAttachment.setContentId(ea.getContentId());
        sendingAttachment.setName(ea.getName());
        sendingAttachment.setEncoding(ea.getEncoding());
        sendingAttachment.setDisposition(ea.getDisposition());
        return sendingAttachment;
    }

    protected byte[] bodyTextToBytes(SendingMessage message) {
        byte[] bodyBytes;
        try {
            bodyBytes = message.getContentText().getBytes(BODY_STORAGE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return bodyBytes;
    }

    protected String bodyTextFromByteArray(byte[] bodyContent) {
        try {
            return new String(bodyContent, BODY_STORAGE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isNeedToRetry(Exception e) {
        if (e instanceof MailSendException) {
            if (e.getCause() instanceof SMTPAddressFailedException) {
                return false;
            }
        } else if (e instanceof AddressException) {
            return false;
        }
        return true;
    }

    @Override
    public void migrateEmailsToFileStorage(List<SendingMessage> messages) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            for (SendingMessage msg : messages) {
                migrateMessage(em, msg);
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public void migrateAttachmentsToFileStorage(List<SendingAttachment> attachments) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            for (SendingAttachment attachment : attachments) {
                migrateAttachment(em, attachment);
            }

            tx.commit();
        } finally {
            tx.end();
        }

    }

    protected void migrateMessage(EntityManager em, SendingMessage msg) {
        msg = em.merge(msg);
        byte[] bodyBytes = bodyTextToBytes(msg);
        FileDescriptor bodyFile = createBodyFileDescriptor(msg, bodyBytes);

        try {
            fileStorage.saveFile(bodyFile, bodyBytes);
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
        em.persist(bodyFile);
        msg.setContentTextFile(bodyFile);
        msg.setContentText(null);
    }

    protected void migrateAttachment(EntityManager em, SendingAttachment attachment) {
        attachment = em.merge(attachment);
        FileDescriptor contentFile = createAttachmentFileDescriptor(attachment);

        try {
            fileStorage.saveFile(contentFile, attachment.getContent());
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
        em.persist(contentFile);
        attachment.setContentFile(contentFile);
        attachment.setContent(null);
    }

    protected static class EmailSendTask implements Runnable {

        private SendingMessage sendingMessage;
        private Log log = LogFactory.getLog(EmailSendTask.class);

        public EmailSendTask(SendingMessage message) {
            sendingMessage = message;
        }

        @Override
        public void run() {
            try {
                Authentication authentication = AppBeans.get(Authentication.NAME);
                Emailer emailer = AppBeans.get(EmailerAPI.NAME);

                authentication.begin(emailer.getEmailerLogin());
                try {
                    emailer.sendSendingMessage(sendingMessage);
                } finally {
                    authentication.end();
                }
            } catch (Exception e) {
                log.error("Exception while sending email: ", e);
            }
        }
    }

    protected static class MessagePersistingContext {
        public final List<FileDescriptor> files = new ArrayList<>();

        public void finished() {
            files.clear();
        }
    }
}
