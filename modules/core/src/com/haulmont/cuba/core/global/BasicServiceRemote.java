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

import com.haulmont.cuba.core.entity.BaseEntity;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface BasicServiceRemote
{
    <T extends BaseEntity> T create(T entity);

    <T extends BaseEntity> T update(T entity);

    void delete(BasicInvocationContext ctx);

    <T extends BaseEntity> T get(BasicInvocationContext ctx);

    <T extends BaseEntity> T load(BasicInvocationContext ctx);

    <T extends BaseEntity> List<T> loadList(BasicInvocationContext ctx);
}
