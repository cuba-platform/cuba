/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.12.2008 10:17:29
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.util.Set;

public interface QueryTransformer
{
    void addWhere(String where);

    void mergeWhere(String query);

    void replaceWithCount();

    void reset();

    String getResult();

    Set<String> getAddedParams();

    void replaceOrderBy(String property, boolean asc);
}
