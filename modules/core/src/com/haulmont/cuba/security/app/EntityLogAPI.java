/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.03.2009 14:55:49
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.entity.BaseEntity;

public interface EntityLogAPI
{
    void registerCreate(BaseEntity entity);

    void registerCreate(BaseEntity entity, boolean auto);

    void registerModify(BaseEntity entity);

    void registerModify(BaseEntity entity, boolean auto);

    void registerDelete(BaseEntity entity);

    void registerDelete(BaseEntity entity, boolean auto);
}
