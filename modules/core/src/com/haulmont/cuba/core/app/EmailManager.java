/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author ovchinnikov
 * @version $Id$
 */
@ManagedBean(EmailManagerAPI.NAME)
public class EmailManager extends ManagementBean implements EmailManagerMBean,EmailManagerAPI {

    private Log log = LogFactory.getLog(EmailManager.class);

    private Set<SendingMessage> messageQueue;
    private static int callCount = 0;
    private static final String EMAIL_DELAY_CALL_COUNT_PROPERTY_NAME = "cuba.email.delayCallCount";
    private static final String EMAIL_MESSAGE_QUEUE_CAPACITY_PROPERTY_NAME = "cuba.email.messageQueueCapacity";

    @Inject
    private ThreadPoolTaskExecutor mailSendTaskExecutor;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private TimeSource timeSource;

    @Inject
    private Persistence persistence;

    private EmailerConfig config;

    @Inject
    public void setConfig(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    @Override
    protected Credentials getCredentialsForLogin() {
        return new Credentials(AppContext.getProperty(EmailerAPI.NAME + ".login"),
                AppContext.getProperty(EmailerAPI.NAME + ".password"));
    }

    @Override
    public void queueEmailsToSend() {
        try {
            int delay = getDelayCallCount();
            if (callCount >= delay) {
                log.debug("Queueing Emails");

                loginOnce();
                try {
                    List<SendingMessage> loadedMessages = loadEmailsToSend();
                    List<SendingMessage> updatedMessages = updateSendingMessagesStatus(loadedMessages);

                    if (messageQueue == null)
                        messageQueue = new LinkedHashSet<>();
                    messageQueue.addAll(updatedMessages);

                    List<SendingMessage> processedMessages = new ArrayList<>();
                    List<UUID> notSentMessageIds = new ArrayList<>();
                    for (SendingMessage msg : messageQueue) {
                        if (needToSetStatusNotSent(msg))
                            notSentMessageIds.add(msg.getId());
                        else {
                            sendAsync(msg);
                        }
                        processedMessages.add(msg);
                    }
                    messageQueue.removeAll(processedMessages);
                    if (!notSentMessageIds.isEmpty())
                        updateSendingMessagesStatus(notSentMessageIds, SendingStatus.NOTSENT);

                } finally {
                    AppContext.setSecurityContext(null);
                }

            } else {
                callCount++;
            }
        } catch (Throwable e) {
            log.error(EmailManagerAPI.NAME + " error:" + ExceptionUtils.getStackTrace(e));
        }
    }

    private void updateSendingMessagesStatus(List<UUID> messages, SendingStatus status) {
        StringBuilder updateQueryStr = new StringBuilder();
        updateQueryStr.append("update sys$SendingMessage sm set sm.status= :status, sm.updateTs = :currentTime")
                .append("\t where sm.id in (:list)");
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.createQuery(updateQueryStr.toString())
                    .setParameter("status", status.getId())
                    .setParameter("list", messages)
                    .setParameter("currentTime", timeSource.currentTimestamp())
                    .executeUpdate();
            tx.commit();
        } finally {
            tx.end();
        }
    }

    private boolean needToSetStatusNotSent(SendingMessage sendingMessage) {
        if (sendingMessage.getDeadline() != null && sendingMessage.getDeadline().getTime() < timeSource.currentTimestamp().getTime())
            return true;
        else {
            int attemptsCount = sendingMessage.getAttemptsCount() != null ? sendingMessage.getAttemptsCount() : config.getDefaultSendingAttemptsCount();
            if (sendingMessage.getAttemptsMade() != null && sendingMessage.getAttemptsMade() >= attemptsCount)
                return true;
        }
        return false;
    }

    private void sendAsync(SendingMessage msg) {
        try {
            Runnable mailSendTask = new EmailSendTask(msg);
            mailSendTaskExecutor.execute(mailSendTask);
        } catch (RejectedExecutionException e) {
            updateSendingMessageStatus(msg, SendingStatus.QUEUE);
        } catch (Exception e) {
            log.error("Exception while sending email: " + ExceptionUtils.getStackTrace(e));
            updateSendingMessageStatus(msg, SendingStatus.QUEUE);
        }
    }

    private List<SendingMessage> loadEmailsToSend() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            View view = new View(SendingMessage.class, true)
                    .addProperty("attachments", new View(SendingAttachment.class, true)
                            .addProperty("content")
                            .addProperty("contentId")
                            .addProperty("name")
                            .addProperty("message"))
                    .addProperty("caption")
                    .addProperty("address")
                    .addProperty("from")
                    .addProperty("contentText")
                    .addProperty("deadline")
                    .addProperty("attemptsCount")
                    .addProperty("attemptsMade")
                    .addProperty("version")
                    .addProperty("status");
            em.setView(view);
            Query query = em.createQuery("select sm from sys$SendingMessage sm " +
                    "where sm.status=:statusQueue \n" +
                    "\t or (sm.status = :statusSending and sm.updateTs<:time)" +
                    "\t order by sm.createTs")
                    .setParameter("statusQueue", SendingStatus.QUEUE.getId())
                    .setParameter("time", DateUtils.addSeconds(timeSource.currentTimestamp(), -config.getMaxSendingTimeSec()))
                    .setParameter("statusSending", SendingStatus.SENDING.getId());
            List<SendingMessage> res = query.setMaxResults(getMessageQueueCapacity()).getResultList();
            tx.commit();
            return res;
        } finally {
            tx.end();
        }
    }

    private List<SendingMessage> updateSendingMessagesStatus(List<SendingMessage> messageList) {
        if (messageList == null || messageList.isEmpty())
            return Collections.emptyList();

        List<SendingMessage> messagesToRemove = new ArrayList<>();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            for (SendingMessage msg : messageList) {
                int recordForUpdateCount = 0;
                String queryStr = "update sys$SendingMessage sm set sm.status= :status, sm.updateTs= :currentTime, sm.updatedBy = :user, sm.version= :version1 " +
                        "\t where sm.id =:id and sm.version=:version";
                Query query = em.createQuery(queryStr);
                query.setParameter("status", SendingStatus.SENDING.getId());
                query.setParameter("currentTime", timeSource.currentTimestamp());
                query.setParameter("user", userSessionSource.getUserSession().getUser().getLogin());
                query.setParameter("version", msg.getVersion());
                query.setParameter("version1", msg.getVersion() + 1);
                query.setParameter("id", msg.getId());
                recordForUpdateCount += query.executeUpdate();
                if (recordForUpdateCount == 0)
                    messagesToRemove.add(msg);
            }
            tx.commit();
        } finally {
            tx.end();
        }
        List<SendingMessage> res = new ArrayList<SendingMessage>();
        res.addAll(messageList);
        res.removeAll(messagesToRemove);
        return res;
    }

    @Override
    public List<SendingMessage> addEmailsToQueue(List<SendingMessage> sendingMessageList) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            for (SendingMessage message : sendingMessageList) {
                em.persist(message);
                if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
                    for (SendingAttachment attachment : message.getAttachments())
                        em.persist(attachment);
                }
            }
            tx.commit();
        } finally {
            tx.end();
        }
        return sendingMessageList;
    }

    private void updateSendingMessageStatus(SendingMessage sendingMessage, SendingStatus status) {
        if (sendingMessage != null) {
            boolean increaseAttemptsMade = !status.equals(SendingStatus.SENDING);
            Date currentTimestamp = timeSource.currentTimestamp();

            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                StringBuilder queryStr = new StringBuilder("update sys$SendingMessage sm set sm.status = :status, sm.updateTs=:updateTs, sm.updatedBy = :updatedBy, sm.version = sm.version + 1 ");
                if (increaseAttemptsMade)
                    queryStr.append(", sm.attemptsMade = sm.attemptsMade + 1 ");
                if (status.equals(SendingStatus.SENT))
                    queryStr.append(", sm.dateSent = :dateSent");
                queryStr.append("\n where sm.id=:id");
                Query query = em.createQuery(queryStr.toString())
                        .setParameter("status", status.getId())
                        .setParameter("id", sendingMessage.getId())
                        .setParameter("updateTs", currentTimestamp)
                        .setParameter("updatedBy", userSessionSource.getUserSession().getUser().getLogin());
                if (status.equals(SendingStatus.SENT))
                    query.setParameter("dateSent", currentTimestamp);
                query.executeUpdate();
                tx.commit();
            } finally {
                tx.end();
            }
        }
    }

    private int getMessageQueueCapacity() {
        String messageQueueCapacity = AppContext.getProperty(EMAIL_MESSAGE_QUEUE_CAPACITY_PROPERTY_NAME);
        int capacity = 0;
        if (messageQueueCapacity != null)
            try {
                capacity = Integer.valueOf(messageQueueCapacity);
            } catch (Exception e) {
                capacity = config.getMessageQueueCapacity();
            }
        capacity = capacity == 0 ? config.getMessageQueueCapacity() : capacity;
        return capacity;
    }

    private int getDelayCallCount() {
        String delayCallCountStr = AppContext.getProperty(EMAIL_DELAY_CALL_COUNT_PROPERTY_NAME);
        int delayCallCount = 0;
        if (delayCallCountStr != null)
            try {
                delayCallCount = Integer.valueOf(delayCallCountStr);
            } catch (Exception e) {
                delayCallCount = config.getDelayCallCount();
            }
        delayCallCount = delayCallCount == 0 ? config.getDelayCallCount() : delayCallCount;
        return delayCallCount;
    }

    @Override
    public String getDelayCallCountAsString() {
        return String.valueOf(getDelayCallCount());
    }

    @Override
    public String getMessageQueueCapacityAsString() {
        return String.valueOf(getMessageQueueCapacity());
    }
}