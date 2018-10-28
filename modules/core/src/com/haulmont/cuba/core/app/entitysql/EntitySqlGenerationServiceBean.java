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

package com.haulmont.cuba.core.app.entitysql;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.EntitySqlGenerationService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.haulmont.chile.core.model.MetaProperty.Type;
import static com.haulmont.chile.core.model.MetaProperty.Type.*;
import static com.haulmont.chile.core.model.Range.Cardinality.*;

@Service(EntitySqlGenerationService.NAME)
public class EntitySqlGenerationServiceBean implements EntitySqlGenerationService {
    @Inject
    protected Persistence persistence;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected MetadataTools metadataTools;

    @Override
    public String generateInsertScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        EntitySqlGenerator generator = AppBeans.getPrototype(EntitySqlGenerator.NAME, entity.getClass());
        entity = reload(entity);

        return generator.generateInsertScript(entity);
    }

    @Override
    public String generateUpdateScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        EntitySqlGenerator generator = AppBeans.getPrototype(EntitySqlGenerator.NAME, entity.getClass());
        entity = reload(entity);
        return generator.generateUpdateScript(entity);
    }

    @Override
    public String generateSelectScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        EntitySqlGenerator generator = AppBeans.getPrototype(EntitySqlGenerator.NAME, entity.getClass());
        return generator.generateSelectScript(entity);
    }

    protected Entity reload(Entity entity) {
        String storeName = metadataTools.getStoreName(entity.getMetaClass());
        if (storeName == null)
            throw new RuntimeException("Cannot determine data store for " + entity);

        try (Transaction tx = persistence.createTransaction(storeName)) {
            Entity reloaded = persistence.getEntityManager(storeName).find(entity.getClass(), entity.getId(),
                    createFullView(entity.getMetaClass()));
            if (reloaded != null) {
                entity = reloaded;
            }
            tx.commit();
        }
        return entity;
    }

    protected View createFullView(MetaClass metaClass) {
        View view = new View(metaClass.getJavaClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (metadataTools.isEmbedded(metaProperty)) {
                view.addProperty(metaProperty.getName(), createFullView(metaProperty.getRange().asClass()));
            } else if (isReferenceField(metaProperty)) {
                view.addProperty(metaProperty.getName(), viewRepository.getView(metaProperty.getRange().asClass(), View.MINIMAL));
            } else if (isDataField(metaProperty)) {
                view.addProperty(metaProperty.getName());
            }
        }
        return view;
    }

    protected boolean isReferenceField(MetaProperty metaProperty) {
        Type type = metaProperty.getType();
        Range.Cardinality cardinality = metaProperty.getRange().getCardinality();
        return (ASSOCIATION == type || COMPOSITION == type)
                && (MANY_TO_ONE == cardinality || ONE_TO_ONE == cardinality);
    }

    protected boolean isDataField(MetaProperty metaProperty) {
        Type type = metaProperty.getType();
        return (DATATYPE == type || ENUM == type);
    }
}