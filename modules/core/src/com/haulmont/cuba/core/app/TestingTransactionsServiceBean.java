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

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.security.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Service for integration testing. Don't use it in application code!
 */
@Service(TestingTransactionsService.NAME)
@Transactional
public class TestingTransactionsServiceBean implements TestingTransactionsService {

    private final Logger log = LoggerFactory.getLogger(TestingTransactionsServiceBean.class);

    @Inject
    private Persistence persistence;

    @Override
    public void declarativeTransaction_withoutMethodTxAnnotation() {
        checkTestMode();
        //noinspection ResultOfMethodCallIgnored
        persistence.getEntityManager().find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

        persistence.getEntityManagerContext().setAttribute("test1", "test_value");
    }

    @Override
    @Transactional
    public void declarativeTransaction_withMethodTxAnnotation() {
        checkTestMode();
        //noinspection ResultOfMethodCallIgnored
        persistence.getEntityManager().find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

        persistence.getEntityManagerContext().setAttribute("test2", "test_value");
    }

    private void checkTestMode() {
        if (!Boolean.valueOf(System.getProperty("cuba.unitTestMode")))
            throw new IllegalStateException("Not in test mode");
    }
}