/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:13:20
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;

import javax.ejb.Remote;
import java.util.List;
import java.util.Map;

@Remote
public interface DataServiceRemote
{
    String JNDI_NAME = "cuba/core/DataService";

    DbDialect getDbDialect();

    Map<Entity, Entity> commit(CommitContext<Entity> context);

    <A extends Entity> A load(LoadContext context);
    <A extends Entity> List<A> loadList(LoadContext context);
}
