/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:09:14
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

public interface DataService extends com.haulmont.cuba.core.app.DataService {
    <A extends Entity> A newInstance(MetaClass metaClass);
    <A extends Entity> A reload(A entity, View view);

    <A extends Entity> A commit(A entity, View view);
    void remove(Entity entity);
}
