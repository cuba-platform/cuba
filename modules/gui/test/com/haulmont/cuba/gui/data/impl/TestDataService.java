/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.DataService;

import java.util.*;

/**
* <p>$Id$</p>
*
* @author krivopustov
*/
public class TestDataService implements DataService {

    public interface CommitValidator {
        void validate(CommitContext<Entity> context);
    }

    int commitCount;

    CommitValidator commitValidator;

    @Override
    public DbDialect getDbDialect() {
        return null;
    }

    @Override
    public Set<Entity> commit(CommitContext<Entity> context) {
        commitCount++;

        if (commitValidator != null)
            commitValidator.validate(context);

        Set<Entity> result = new HashSet<Entity>();
        for (Entity entity : context.getCommitInstances()) {
            result.add(entity);
        }
        return result;
    }

    @Override
    public Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext<Entity> context) {
        return null;
    }

    @Override
    public <A extends Entity> A load(LoadContext context) {
        return null;
    }

    @Override
    public <A extends Entity> List<A> loadList(LoadContext context) {
        return null;
    }

    @Override
    public <A extends Entity> A newInstance(MetaClass metaClass) {
        return null;
    }

    @Override
    public <A extends Entity> A reload(A entity, String viewName) {
        return null;
    }

    @Override
    public <A extends Entity> A reload(A entity, View view) {
        return null;
    }

    @Override
    public <A extends Entity> A reload(A entity, View view, MetaClass metaClass) {
        return null;
    }

    @Override
    public <A extends Entity> A reload(A entity, View view, MetaClass metaClass, boolean useSecurityCostraints) {
        return null;
    }

    @Override
    public <A extends Entity> A commit(A entity, View view) {
        return null;
    }

    @Override
    public void remove(Entity entity) {
    }
}
