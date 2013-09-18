/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authentication;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(EmailerAPI.NAME)
public class Emailer implements EmailerAPI {

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
    public void setConfig(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    protected String getEmailerLogin() {
        return AppContext.getProperty("cuba.emailerUserLogin");
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
            SendingMessage sendingMessage = convertToSendingMessage(address, info.getFrom(), info.getCaption(),
                    info.getBody(), info.getAttachments(), attemptsCount, deadline);

            sendingMessageList.add(sendingMessage);
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
            returnToQueue(sendingMessage);
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
            persistMessages(Collections.singletonList(sendingMessage), SendingStatus.SENDING);

            try {
                emailSender.sendEmail(sendingMessage);
                markAsSent(sendingMessage);
            } catch (Exception e) {
                log.warn("Unable to send email to '" + sendingMessage.getAddress() + "'", e);
                failedAddresses.add(sendingMessage.getAddress());
                errorMessages.add(e.getMessage());
                markAsNonSent(sendingMessage);
            }
        }

        if (!failedAddresses.isEmpty()) {
            throw new EmailException(
                    failedAddresses.toArray(new String[failedAddresses.size()]),
                    errorMessages.toArray(new String[errorMessages.size()])
            );
        }
    }

    @Override
    public String processQueuedEmails() {
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
            returnToQueue(msg);
        }
    }

    protected List<SendingMessage> loadEmailsToSend() {
        Date sendTimeoutTime = DateUtils.addSeconds(timeSource.currentTimestamp(), -config.getMaxSendingTimeSec());

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
            return emailsToSend;
        } finally {
            tx.end();
        }
    }

    protected void persistMessages(List<SendingMessage> sendingMessageList, SendingStatus status) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            for (SendingMessage message : sendingMessageList) {
                message.setStatus(status);

                em.persist(message);
                if (message.getAttachments() != null) {
                    for (SendingAttachment attachment : message.getAttachments()) {
                        em.persist(attachment);
                    }
                }
            }
            tx.commit();
        } finally {
            tx.end();
        }
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

    protected SendingMessage convertToSendingMessage(String address, String from, String caption, String body,
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
}
