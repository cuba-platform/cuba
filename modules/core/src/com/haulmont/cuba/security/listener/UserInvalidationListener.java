/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.events.UserInvalidationEvent;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("cuba_UserInvalidationListener")
public class UserInvalidationListener {

    private final Logger log = LoggerFactory.getLogger(UserInvalidationListener.class);

    @Inject
    protected UserManagementService userManagementService;

    @Inject
    protected UserSessionsAPI userSessionsAPI;

    @Inject
    protected Persistence persistence;

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 100)
    @EventListener
    public void handleUserInvalidation(UserInvalidationEvent event) {
        User user = event.getSource();

        log.info("Handling user invalidation: {}", user.getLogin());

        try (Transaction tx = persistence.createTransaction()) {
            List<UUID> sessionsIds = userSessionsAPI.getUserSessionsStream()
                    .filter(session -> session != null &&
                            (user.equals(session.getUser()) || user.equals(session.getSubstitutedUser())))
                    .map(UserSession::getId)
                    .collect(Collectors.toList());

            sessionsIds.forEach(userSessionsAPI::killSession);

            userManagementService.resetRememberMeTokens(Collections.singletonList(user.getId()));

            tx.commit();

            log.info("UserSessions & 'Remember me' tokens were invalidated for a user: {}", user.getLogin());
        } catch (Throwable t) {
            log.error("An error occurred while handling user invalidation for user: {}.", user.getLogin(), t);
        }
    }
}
