/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authentication;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author ovchinnikov
 * @version $Id$
 */
@ManagedBean(EmailManagerAPI.NAME)
public class EmailManager implements EmailManagerAPI {

    protected Log log = LogFactory.getLog(getClass());

    protected static int callCount = 0;

    @Resource(name = "mailSendTaskExecutor")
    protected ThreadPoolTaskExecutor mailSendTaskExecutor;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Authentication authentication;

    protected EmailerConfig config;

    @Inject
    public void setConfig(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    protected String getSystemLogin() {
        return AppContext.getProperty("cuba.emailerUserLogin");
    }

    @Override
    public String queueEmailsToSend() {
        String resultMessage =  null;
        try {
            int delay = config.getDelayCallCount();
            if (callCount >= delay) {
                log.debug("Queueing Emails");

                authentication.begin(getSystemLogin());
                try {
                    List<SendingMessage> loadedMessages = loadEmailsToSend();
                    List<SendingMessage> updatedMessages = updateSendingMessagesStatus(loadedMessages);

                    Set<SendingMessage> messageQueue = new LinkedHashSet<>();
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

                    if (!processedMessages.isEmpty()) {
                        resultMessage = String.format("Processed %d emails", processedMessages.size());
                    }
                } finally {
                    authentication.end();
                }

            } else {
                callCount++;
            }
        } catch (Throwable e) {
            log.error(EmailManagerAPI.NAME + " error:" + ExceptionUtils.getStackTrace(e));
            resultMessage = e.getMessage();
        }
        return resultMessage;
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
            Query query = em.createQuery("select sm from sys$SendingMessage sm " +
                    "where sm.status=:statusQueue \n" +
                    "\t or (sm.status = :statusSending and sm.updateTs<:time)" +
                    "\t order by sm.createTs")
                    .setParameter("statusQueue", SendingStatus.QUEUE.getId())
                    .setParameter("time", DateUtils.addSeconds(timeSource.currentTimestamp(), -config.getMaxSendingTimeSec()))
                    .setParameter("statusSending", SendingStatus.SENDING.getId());
            query.setView(view);
            List<SendingMessage> res = query.setMaxResults(config.getMessageQueueCapacity()).getResultList();
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
}