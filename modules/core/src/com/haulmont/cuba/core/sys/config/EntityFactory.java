/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author krivopustov
 * @version $Id$
 */
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