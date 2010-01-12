/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 12.02.2009 12:06:15
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.PropertyVisitor;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Instance;

import java.util.*;

import com.haulmont.cuba.gui.data.DataService;
import org.apache.openjpa.util.Proxy;

public class GenericDataService implements DataService {

    protected com.haulmont.cuba.core.app.DataService service;

    public GenericDataService(DataService service) {
        this.service = service;
    }

    public GenericDataService(boolean remoteCalls) {
        this.service = ServiceLocator.getDataService();
    }

    public <A extends Entity> A newInstance(MetaClass metaClass) {
        try {
            final Class aClass = metaClass.getJavaClass();
            return (A) aClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <A extends Entity> A reload(A entity, View view) {
        final LoadContext context = new LoadContext(entity.getClass());
        context.setId(entity.getId());
        context.setView(view);

        return (A)load(context);
    }

    public <A extends Entity> A commit(A instance, View view) {
        final CommitContext<Entity> context =
                new CommitContext<Entity>(
                        Collections.singleton((Entity) instance),
                        Collections.<Entity>emptyList());
        if (view != null)
            context.getViews().put(instance, view);

        final Map res = commit(context);

        return (A) res.get(instance);
    }

    public void remove(Entity entity) {
        final CommitContext<Entity> context =
                new CommitContext<Entity>(
                        Collections.<Entity>emptyList(),
                        Collections.singleton(entity));
        commit(context);
    }

    public DbDialect getDbDialect() {
        return service.getDbDialect();
    }

    public Map<Entity, Entity> commit(CommitContext<Entity> context) {
        try {
            Map<Entity, Entity> result = service.commit(context);
            return result;
        } catch (RuntimeException e) {
            for (Entity entity : context.getCommitInstances()) {
                MetadataHelper.walkProperties((Instance) entity, new RefiningPropertyVisitor());
            }
            for (Entity entity : context.getRemoveInstances()) {
                MetadataHelper.walkProperties((Instance) entity, new RefiningPropertyVisitor());
            }
            throw e;
        }
    }

    public <A extends Entity> A load(LoadContext context) {
        return service.<A>load(context);
    }

    public <A extends Entity> List<A> loadList(LoadContext context) {
        return service.<A>loadList(context);
    }

    private static class RefiningPropertyVisitor implements PropertyVisitor {

        public void visit(Instance instance, MetaProperty property) {
            Object value = instance.getValue(property.getName());
            if (value != null && value instanceof Proxy) {
                Object newValue;
                if (value instanceof Set) {
                    newValue = new HashSet(((Set) value));
                } else if (value instanceof List) {
                    newValue = new ArrayList(((List) value));
                } else if (value instanceof Date) {
                    newValue = new Date(((Date) value).getTime());
                } else
                    throw new UnsupportedOperationException("Unsupported proxy type: " + value.getClass());
                instance.setValue(property.getName(), newValue);
            }
        }
    }
}
