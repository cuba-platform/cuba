/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:35:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.NotDetachedCommitContext;

import java.util.List;
import java.util.Map;

public interface DataService
{
    String NAME = "cuba_DataService";

    @Deprecated
    String JNDI_NAME = NAME;

    DbDialect getDbDialect();

    Map<Entity, Entity> commit(CommitContext<Entity> context);

    Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext<Entity> context);

    <A extends Entity> A load(LoadContext context);
    <A extends Entity> List<A> loadList(LoadContext context);
}
