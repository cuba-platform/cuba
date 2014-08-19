/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.script;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.haulmont.chile.core.model.MetaProperty.Type;
import static com.haulmont.chile.core.model.MetaProperty.Type.*;
import static com.haulmont.chile.core.model.Range.Cardinality.*;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Service(ScriptGenerationService.NAME)
public class ScriptGenerationServiceBean implements ScriptGenerationService {
    @Inject
    protected Persistence persistence;

    @Inject
    protected ViewRepository viewRepository;

    @Override
    public String generateInsertScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        SqlScriptGenerator generator = AppBeans.getPrototype(SqlScriptGenerator.NAME, entity.getClass());
        entity = reload(entity);

        return generator.generateInsertScript(entity);
    }

    @Override
    public String generateUpdateScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        SqlScriptGenerator generator = AppBeans.getPrototype(SqlScriptGenerator.NAME, entity.getClass());
        entity = reload(entity);
        return generator.generateUpdateScript(entity);
    }

    @Override
    public String generateSelectScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        SqlScriptGenerator generator = AppBeans.getPrototype(SqlScriptGenerator.NAME, entity.getClass());
        return generator.generateSelectScript(entity);
    }

    protected Entity reload(Entity entity) {
        Transaction tx = persistence.createTransaction();
        try {
            entity = persistence.getEntityManager().find(entity.getClass(), entity.getId(), createFullView(entity));
            tx.commit();
        } finally {
            tx.end();
        }
        return entity;
    }

    //todo use local fields only for embedded
    protected View createFullView(Entity entity) {
        MetaClass metaClass = entity.getMetaClass();
        View view = new View(entity.getClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (isReferenceField(metaProperty)) {
                view.addProperty(metaProperty.getName(), viewRepository.getView(metaProperty.getRange().asClass(), View.LOCAL));
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
