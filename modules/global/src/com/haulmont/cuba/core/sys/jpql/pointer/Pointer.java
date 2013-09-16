/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;

/**
 * Author: Alexander Chevelev
 * Date: 21.10.2010
 * Time: 1:44:24
 */
public interface Pointer {
    Pointer next(DomainModel model, String field);
}
