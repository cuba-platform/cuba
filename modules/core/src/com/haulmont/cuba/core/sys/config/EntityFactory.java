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

package com.haulmont.cuba.core.sys.config;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.config.type.TypeFactory;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityLoadInfo;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

@Component(TypeFactory.ENTITY_FACTORY_BEAN_NAME)
public class EntityFactory extends TypeFactory {

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Override
    public Object build(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }

        EntityLoadInfo info = EntityLoadInfo.parse(string);
        if (info == null) {
            throw new IllegalArgumentException("Invalid entity info: " + string);
        }

        Entity entity;
        String property = AppContext.getProperty("cuba.useCurrentTxForConfigEntityLoad");
        Transaction tx = Boolean.valueOf(property) ? persistence.getTransaction() : persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            View view = null;
            if (info.getViewName() != null) {
                view = metadata.getViewRepository().getView(info.getMetaClass(), info.getViewName());
            }

            Class javaClass = info.getMetaClass().getJavaClass();
            if (view != null) {
                entity = em.find(javaClass, info.getId(), view);
            } else {
                entity = em.find(javaClass, info.getId());
            }

            tx.commit();
        } finally {
            tx.end();
        }
        return entity;
    }
}