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

package com.haulmont.cuba.core.app;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SecurityState;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(AttributeAccessService.NAME)
public class AttributeAccessServiceBean implements AttributeAccessService {

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected AttributeSecuritySupport support;
    @Inject
    private EntityStates entityStates;

    @Override
    public SecurityState computeSecurityState(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        SecurityState state;
        String storeName = metadataTools.getStoreName(metadata.getClassNN(entity.getClass()));
        Transaction tx = persistence.createTransaction(storeName);
        try {
            Entity e;
            if (entityStates.isNew(entity)) {
                e = entity;
            } else {
                EntityManager em = persistence.getEntityManager(storeName);
                e = em.merge(entity);
            }
            support.setupAttributeAccess(e);
            state = BaseEntityInternalAccess.getSecurityState(e);
            // do not commit the transaction
        } finally {
            tx.end();
        }
        return state;
    }
}
