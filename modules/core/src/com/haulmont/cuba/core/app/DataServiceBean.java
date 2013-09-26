/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.NotDetachedCommitContext;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(DataService.NAME)
public class DataServiceBean implements DataService {

    @Inject
    protected DataWorker dataWorker;

    @Inject
    private Persistence persistence;

    @Override
    public DbDialect getDbDialect() {
        return persistence.getDbDialect();
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        return dataWorker.commit(context);
    }

    @Override
    public Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext context) {
        return dataWorker.commitNotDetached(context);
    }

    @Override
    @Nullable
    public <A extends Entity> A load(LoadContext context) {
        return dataWorker.load(context);
    }

    @Override
    @Nonnull
    public <A extends Entity> List<A> loadList(LoadContext context) {
        return dataWorker.loadList(context);
    }
}