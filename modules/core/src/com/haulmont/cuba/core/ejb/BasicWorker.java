/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 15:28:22
 *
 * $Id$
 */
package com.haulmont.cuba.core.ejb;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.intf.BasicInvocationContext;

import javax.ejb.Local;
import java.util.List;

@Local
public interface BasicWorker
{
    String JNDI_NAME = "cuba/core/BasicWorker";

    <T extends BaseEntity> T create(T entity);

    <T extends BaseEntity> T update(T entity);

    void delete(BasicInvocationContext ctx);

    <T extends BaseEntity> T get(BasicInvocationContext ctx);

    <T extends BaseEntity> T load(BasicInvocationContext ctx);

    <T extends BaseEntity> List<T> loadList(BasicInvocationContext ctx);
}
