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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.app.EmailerConfig;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.app.Authenticated;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component("cuba_EmailerMBean")
public class Emailer implements EmailerMBean {

    @Inject
    protected EmailerAPI emailer;

    @Inject
    protected Persistence persistence;

    protected EmailerConfig config;

    protected Logger log = LoggerFactory.getLogger(Emailer.class);

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    @Override
    public String getFromAddress() {
        return config.getFromAddress();
    }

    @Authenticated
    @Override
    public void setFromAddress(String address) {
        if (address != null) {
            config.setFromAddress(address);
        }
    }

    @Override
    public String getSmtpHost() {
        return config.getSmtpHost();
    }

    @Override
    public int getSmtpPort() {
        return config.getSmtpPort();
    }

    @Override
    public String getSmtpUser() {
        return config.getSmtpUser();
    }

    @Override
    public boolean getSmtpAuthRequired() {
        return config.getSmtpAuthRequired();
    }

    @Override
    public boolean getStarttlsEnable() {
        return config.getSmtpStarttlsEnable();
    }

    public boolean getSmtpSslEnabled() {
        return config.getSmtpSslEnabled();
    }

    @Override
    public int getSmtpTimeoutSec() {
        return config.getSmtpTimeoutSec();
    }

    @Override
    public int getSmtpConnectionTimeoutSec() {
        return config.getSmtpConnectionTimeoutSec();
    }

    @Authenticated
    @Override
    public String sendTestEmail(String addresses) {
        try {
            String att = "<html><body><h1>Test attachment</h1></body></html>";
            EmailAttachment emailAtt = EmailAttachment.createTextAttachment(att, StandardCharsets.UTF_8.name(), "test attachment.html");
            emailer.sendEmail(addresses, "Test email", "<html><body><h1>Test email</h1></body></html>", emailAtt);
            return "Email to '" + addresses + "' sent succesfully";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public String migrateEmailsToFileStorage(String password) {
        if (!"do migration".equals(password)) {
            return "Wrong password";
        }

        int processed;
        do {
            try {
                processed = migrateMessagesBatch();
                log.info(String.format("Migrated %d emails", processed));
            } catch (Exception e) {
                throw new RuntimeException("Failed to migrate batch", e);
            }
        } while (processed > 0);
        log.info("Finished migrating emails");
        do {
            try {
                processed = migrateAttachmentsBatch();
                log.info(String.format("Migrated %d attachments", processed));
            } catch (Exception e) {
                throw new RuntimeException("Failed to migrate batch", e);
            }
        } while (processed > 0);
        log.info("Finished migrating attachments");

        return "Finished";
    }

    protected int migrateMessagesBatch() {
        List<SendingMessage> resultList;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            String qstr = "select m from sys$SendingMessage m where m.contentText is not null";
            TypedQuery<SendingMessage> query = em.createQuery(qstr, SendingMessage.class);
            query.setMaxResults(50);
            query.setViewName(View.MINIMAL);

            resultList = query.getResultList();
            tx.commit();
        } finally {
            tx.end();
        }

        if (!resultList.isEmpty()) {
            emailer.migrateEmailsToFileStorage(resultList);
        }

        return resultList.size();
    }

    protected int migrateAttachmentsBatch() {
        List<SendingAttachment> resultList;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            String qstr = "select a from sys$SendingAttachment a where a.content is not null";
            TypedQuery<SendingAttachment> query = em.createQuery(qstr, SendingAttachment.class);
            query.setMaxResults(50);
            query.setViewName(View.MINIMAL);

            resultList = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }

        if (!resultList.isEmpty()) {
            emailer.migrateAttachmentsToFileStorage(resultList);
        }

        return resultList.size();
    }
}