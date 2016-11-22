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
 */

package com.haulmont.cuba.tx_listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.listener.BeforeCommitTransactionListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("cuba_TestBeforeCommitTxListener2")
public class TestBeforeCommitTxListener2 implements BeforeCommitTransactionListener, Ordered {

    @Override
    public void beforeCommit(EntityManager entityManager, Collection<Entity> managedEntities) {
        System.out.println("TestBeforeCommitTxListener2.beforeCommit");
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
