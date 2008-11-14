/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:26:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.ejb;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.intf.BasicInvocationContext;
import com.haulmont.cuba.core.intf.BasicService;
import com.haulmont.cuba.core.intf.BasicServiceRemote;
import com.haulmont.cuba.core.Locator;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.List;

@Stateless(name = BasicService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
public class BasicServiceBean implements BasicService, BasicServiceRemote
{
    private BasicWorker getBasicWorker() {
        return Locator.lookupLocal(BasicWorker.JNDI_NAME);
    }

    public <T extends BaseEntity> T create(T entity) {
        return getBasicWorker().create(entity);
    }

    public <T extends BaseEntity> T update(T entity) {
        return getBasicWorker().update(entity);
    }

    public void delete(BasicInvocationContext ctx) {
        getBasicWorker().delete(ctx);
    }

    public <T extends BaseEntity> T get(BasicInvocationContext ctx) {
        return (T) getBasicWorker().get(ctx);
    }

    public <T extends BaseEntity> T load(BasicInvocationContext ctx) {
        return (T) getBasicWorker().load(ctx);
    }

    public <T extends BaseEntity> List<T> loadList(BasicInvocationContext ctx) {
        return getBasicWorker().loadList(ctx);
    }
}
